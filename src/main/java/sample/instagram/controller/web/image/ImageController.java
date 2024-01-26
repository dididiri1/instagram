package sample.instagram.controller.web.image;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sample.instagram.config.auth.PrincipalDetails;

@Controller
public class ImageController {

    @GetMapping("/")
    public String story() {

        return "index";
    }

    @GetMapping("/image/popular")
    public String popular(Model model) {

        return "image/popular";
    }

    @GetMapping("/image/upload")
    public String upload(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        model.addAttribute("memberId", principalDetails.getMember().getId());

        return "image/upload";
    }
}
