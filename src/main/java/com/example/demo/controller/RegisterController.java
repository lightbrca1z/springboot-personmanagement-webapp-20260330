package com.example.demo.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.RegisterForm;
import com.example.demo.entry.AppUser;
import com.example.demo.repository.AppUserRepository;

@Controller
public class RegisterController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @Transactional
    public String register(
            @ModelAttribute("registerForm") @Validated RegisterForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!form.isPasswordConfirmed()) {
            bindingResult.reject("global", "パスワードと確認用パスワードが一致しません。");
        }
        if (appUserRepository.existsByUsername(form.getUsername())) {
            bindingResult.rejectValue("username", "duplicate", "このユーザー名は既に使われています。");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("registerError", true);
            return "loginRegister";
        }

        AppUser user = new AppUser();
        user.setUsername(form.getUsername().trim());
        user.setPassword(passwordEncoder.encode(form.getPassword()));

        try {
            appUserRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("username", "duplicate", "このユーザー名は既に使われています。");
            model.addAttribute("registerError", true);
            return "loginRegister";
        }

        redirectAttributes.addFlashAttribute("registeredMessage", "登録が完了しました。ログインしてください。");
        return "redirect:/login";
    }
}
