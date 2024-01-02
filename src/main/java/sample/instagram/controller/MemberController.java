package sample.instagram.controller;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class MemberController {

    @GetMapping("/member/{pageUserId}")
    public String profile(@PathVariable int pageUserId, Model model) {

        return "member/profile";
    }

    @GetMapping("/member/{id}/update")
    public String update(@PathVariable int id, Model model) {

        return "member/update";
    }
}
