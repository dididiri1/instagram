package sample.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sample.instagram.config.auth.PrincipalDetails;

@RequiredArgsConstructor
@Controller
public class MemberController {

    @GetMapping("/member/{pageMemberId}")
    public String profile(@PathVariable int pageMemberId, Model model
                            , @AuthenticationPrincipal PrincipalDetails principalDetails) {

        model.addAttribute("pageMemberId", pageMemberId);
        model.addAttribute("memberId", principalDetails.getMember().getId());

        return "member/profile";
    }

    @GetMapping("/member/{memberId}/update")
    public String update(@PathVariable int memberId, Model model) {
        model.addAttribute("memberId", memberId);

        return "member/update";
    }

}
