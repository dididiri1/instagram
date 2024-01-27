package sample.instagram.controller.web.story;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class StoryController {

    @GetMapping("/story/{memberId}")
    public String story(@PathVariable int memberId, Model model) {
        model.addAttribute("memberId", memberId);

        return "image/story";
    }
}
