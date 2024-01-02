package sample.instagram.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ImageController {

    @GetMapping("/image/popular")
    public String popular(Model model) {

        return "image/popular";
    }

    @GetMapping("/image/upload")
    public String upload() {

        return "image/upload";
    }
}
