package com.example.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;

/**
 * MVC 向けの例外を専用ビューへ振り分ける。
 */
@ControllerAdvice
public class GlobalMvcExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalMvcExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ModelAndView handleResponseStatus(ResponseStatusException ex, HttpServletResponse response) {
        int status = ex.getStatusCode().value();
        response.setStatus(status);

        if (status == HttpStatus.NOT_FOUND.value()) {
            log.debug("Not found: {}", ex.getReason());
            ModelAndView mv = new ModelAndView("error/not-found");
            mv.addObject("statusCode", "404");
            mv.addObject("title", "データが見つかりません");
            mv.addObject(
                    "message",
                    "指定したデータは存在しないか、閲覧・編集する権限がありません。");
            return mv;
        }

        log.warn("HTTP {} : {}", status, ex.getReason());
        ModelAndView mv = new ModelAndView("error/not-found");
        mv.addObject("statusCode", String.valueOf(status));
        mv.addObject("title", "エラー");
        mv.addObject("message", ex.getReason() != null ? ex.getReason() : "要求を処理できませんでした。");
        return mv;
    }

    @ExceptionHandler({
        CannotGetJdbcConnectionException.class,
        DataAccessResourceFailureException.class,
        TransientDataAccessResourceException.class,
        CannotCreateTransactionException.class
    })
    public ModelAndView handleDatabaseConnection(Exception ex, HttpServletResponse response) {
        log.warn("Database connection failure", ex);
        response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        ModelAndView mv = new ModelAndView("error/database");
        mv.addObject("title", "データベース接続エラー");
        mv.addObject("message", "データベースに接続できませんでした。時間をおいて再度お試しください。");
        return mv;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDataIntegrity(DataIntegrityViolationException ex, HttpServletResponse response) {
        log.warn("Data integrity violation on save", ex);
        response.setStatus(HttpStatus.CONFLICT.value());
        ModelAndView mv = new ModelAndView("error/save-failed");
        mv.addObject("title", "保存エラー");
        mv.addObject("message", "データの保存中にエラーが発生しました。入力内容や他ユーザーの更新の有無をご確認ください。");
        return mv;
    }
}
