package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Page;

import com.example.demo.entry.AppUser;
import com.example.demo.entry.Person;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.security.AppUserPrincipal;
import com.example.demo.service.PersonSearchService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HelloController {

    //ページサイズの設定。
    private static final int PAGE_SIZE = 10;

    @Autowired
    private PersonRepository repository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PersonSearchService personSearchService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, "name", new StringTrimmerEditor(true));
        binder.registerCustomEditor(String.class, "email", new StringTrimmerEditor(true));
        binder.registerCustomEditor(String.class, "phone", new StringTrimmerEditor(true));
        binder.registerCustomEditor(String.class, "memo", new StringTrimmerEditor(true));
    }

    /**
     * {@code @AuthenticationPrincipal} は匿名ユーザー等で null になるため、
     * {@link Authentication} から {@link AppUserPrincipal} を明示的に解決する。
     */
    private static AppUserPrincipal resolveAppUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        Object p = authentication.getPrincipal();
        if (p instanceof AppUserPrincipal aup) {
            return aup;
        }
        return null;
    }

    private static ModelAndView redirectToLogin() {
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/")
    public ModelAndView index(
            ModelAndView mav,
            Authentication authentication,
            @RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "searchEmail", required = false) String searchEmail,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        AppUserPrincipal principal = resolveAppUser(authentication);
        if (principal == null) {
            return redirectToLogin();
        }
        Long uid = principal.getUserId();
        int safePage = Math.max(0, page);
        Pageable pageable = PageRequest.of(safePage, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "id"));
        Page<Person> result = personSearchService.search(uid, searchName, searchEmail, pageable);
        mav.setViewName("index");
        mav.addObject("title", "Person 一覧");
        mav.addObject("msg", "あなたの登録データのみ表示されます。");
        mav.addObject("data", result.getContent());
        mav.addObject("page", result);
        mav.addObject("searchName", searchName != null ? searchName : "");
        mav.addObject("searchEmail", searchEmail != null ? searchEmail : "");
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView createForm(ModelAndView mav, Authentication authentication) {
        if (resolveAppUser(authentication) == null) {
            return redirectToLogin();
        }
        mav.setViewName("create");
        mav.addObject("title", "Person 登録");
        mav.addObject("formModel", new Person());
        return mav;
    }

    @PostMapping("/create")
    @Transactional
    public ModelAndView create(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("formModel") @Validated Person person,
            BindingResult result,
            ModelAndView mav,
            RedirectAttributes redirectAttributes) {
        AppUserPrincipal principal = resolveAppUser(authentication);
        if (principal == null) {
            return redirectToLogin();
        }
        if (!result.hasErrors()) {
            AppUser owner = appUserRepository.findById(principal.getUserId()).orElse(null);
            if (owner == null) {
                // DB がリセットされたのにセッションだけ残っていると FK 違反になるため、ログアウトして再ログインさせる
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                return new ModelAndView("redirect:/login?session=invalid");
            }
            person.setOwner(owner);
            repository.saveAndFlush(person);
            redirectAttributes.addFlashAttribute("successMessage", "登録しました");
            return new ModelAndView("redirect:/");
        }
        mav.setViewName("create");
        mav.addObject("title", "Person 登録");
        mav.addObject("msg", "入力に誤りがあります。修正してください。");
        mav.addObject("formModel", person);
        return mav;
    }

    @PostMapping("/delete")
    @Transactional
    public ModelAndView delete(
            Authentication authentication,
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes) {
        AppUserPrincipal principal = resolveAppUser(authentication);
        if (principal == null) {
            return redirectToLogin();
        }
        Person p = repository.findByIdAndOwner_Id(id, principal.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        repository.delete(p);
        redirectAttributes.addFlashAttribute("successMessage", "削除しました");
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editForm(
            Authentication authentication,
            @PathVariable("id") Long id,
            ModelAndView mav) {
        AppUserPrincipal principal = resolveAppUser(authentication);
        if (principal == null) {
            return redirectToLogin();
        }
        Person data = repository.findByIdAndOwner_Id(id, principal.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mav.setViewName("edit");
        mav.addObject("title", "Person 編集");
        mav.addObject("formModel", data);
        return mav;
    }

    @PostMapping("/edit")
    @Transactional
    public ModelAndView update(
            Authentication authentication,
            @ModelAttribute("formModel") @Validated Person person,
            BindingResult result,
            ModelAndView mav,
            RedirectAttributes redirectAttributes) {
        AppUserPrincipal principal = resolveAppUser(authentication);
        if (principal == null) {
            return redirectToLogin();
        }
        Person existing = repository.findByIdAndOwner_Id(person.getId(), principal.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!result.hasErrors()) {
            person.setOwner(existing.getOwner());
            person.setCreatedAt(existing.getCreatedAt());
            repository.save(person);
            redirectAttributes.addFlashAttribute("successMessage", "更新しました");
            return new ModelAndView("redirect:/");
        }
        person.setOwner(existing.getOwner());
        person.setCreatedAt(existing.getCreatedAt());
        person.setUpdatedAt(existing.getUpdatedAt());
        mav.setViewName("edit");
        mav.addObject("title", "Person 編集");
        mav.addObject("msg", "入力に誤りがあります。修正してください。");
        mav.addObject("formModel", person);
        return mav;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteForm(
            Authentication authentication,
            @PathVariable("id") Long id,
            ModelAndView mav) {
        AppUserPrincipal principal = resolveAppUser(authentication);
        if (principal == null) {
            return redirectToLogin();
        }
        Person data = repository.findByIdAndOwner_Id(id, principal.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mav.setViewName("delete");
        mav.addObject("title", "Person 削除");
        mav.addObject("msg", "本当に削除しますか？");
        mav.addObject("formModel", data);
        return mav;
    }

    @PostMapping("/reset")
    @Transactional
    public ModelAndView reset(Authentication authentication) {
        AppUserPrincipal principal = resolveAppUser(authentication);
        if (principal == null) {
            return redirectToLogin();
        }
        repository.deleteAllByOwner_Id(principal.getUserId());
        return new ModelAndView("redirect:/");
    }
}
