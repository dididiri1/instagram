package sample.instagram.controller.web.image;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sample.instagram.config.auth.PrincipalDetails;
import sample.instagram.dto.image.reponse.ImagePopularResponse;
import sample.instagram.service.image.ImageService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ImageController {

    private final ImageService imageService;

    @GetMapping({"/", "/index"})
    public String story() {

        return "index";
    }

    @GetMapping("/image/popular")
    public String popular(Model model) {
        List<ImagePopularResponse> images= imageService.getPopularImages();
        model.addAttribute("images",images);

        return "image/popular";
    }

    @GetMapping("/image/upload")
    public String upload(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        model.addAttribute("memberId", principalDetails.getMember().getId());

        return "image/upload";
    }
}
