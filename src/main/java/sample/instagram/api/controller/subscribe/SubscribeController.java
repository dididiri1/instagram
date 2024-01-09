package sample.instagram.api.controller.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.instagram.api.service.subscribe.SubscribeService;
import sample.instagram.dto.ResponseDto;

@RequiredArgsConstructor
@RestController
public class SubscribeController {

    private final SubscribeService subscribeService;

    @PostMapping("/api/subscribe/{fromMemberId}/{toMemberId}")
    public ResponseEntity<?> subscribe(@PathVariable Long fromMemberId, @PathVariable Long toMemberId) {
        subscribeService.createSubscribe(fromMemberId, toMemberId);

        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.CREATED.value(), "구독 성공", null), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/subscribe/{fromMemberId}/{toMemberId}")
    public ResponseEntity<?> unSubscribe(@PathVariable Long fromMemberId, @PathVariable Long toMemberId) {
        subscribeService.deleteSubscribe(fromMemberId, toMemberId);

        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.CREATED.value(), "구독 취소 성공", null), HttpStatus.OK);
    }
}
