package sample.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sample.instagram.service.auth.AuthService;
import sample.instagram.domain.member.Member;
import sample.instagram.dto.member.request.MemberCreateRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginForm(Model model, HttpServletRequest request, HttpServletResponse response) {
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        try {
            request.getSession().setAttribute("prevPage", savedRequest.getRedirectUrl());
        } catch(NullPointerException e) {
            request.getSession().setAttribute("prevPage", "/index");
        }

        return "auth/signin";
    }

    @GetMapping("/auth/signup")
    public String signupForm() {

        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(@ModelAttribute @Valid MemberCreateRequest request, BindingResult bindingResult) {
        Member member = authService.createMember(request);

        return "auth/signin";
    }

}
