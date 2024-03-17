package sample.instagram.controller.api.notice;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.instagram.dto.notice.reponse.NoticeCreateRequest;
import sample.instagram.service.notice.NoticeService;

@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public String createNotice(@RequestBody NoticeCreateRequest request) {


        return null;
    }
}
