package sample.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class MemberController {

    @GetMapping("/member/{memberId}")
    public String profile(@PathVariable int memberId, Model model) {

        return "member/profile";
    }

    @GetMapping("/member/{memberId}/update")
    public String update(@PathVariable int memberId, Model model) {
        model.addAttribute("memberId", memberId);

        return "member/update";
    }

}
