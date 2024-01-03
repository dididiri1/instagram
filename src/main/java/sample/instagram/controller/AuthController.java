package sample.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sample.instagram.api.service.AuthService;
import sample.instagram.domain.member.Member;
import sample.instagram.dto.request.MemberCreateRequest;
import sample.instagram.dto.request.RequestTest;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String signinForm() {
        return "auth/signin";
    }

    @GetMapping("/auth/signup")
    public String signupForm() {

        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(@ModelAttribute @Valid MemberCreateRequest request, BindingResult bindingResult) {
        Member member = authService.save(request);

        return "auth/signin";
    }

}
