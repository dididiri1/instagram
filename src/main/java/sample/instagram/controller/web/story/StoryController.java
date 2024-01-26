package sample.instagram.controller.web.story;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;

@RequiredArgsConstructor
@Controller
public class StoryController {

    private final MemberRepositoryJpa memberRepositoryJpa;

    @GetMapping("/story/{username}")
    public String story(@PathVariable("username") String username, Model model) {
        Member findMember = memberRepositoryJpa.findByUsername(username);
        model.addAttribute("memberId", findMember.getId());

        return "image/story";
    }
}
