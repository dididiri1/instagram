package sample.instagram.controller.api.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.instagram.service.subscribe.SubscribeService;
import sample.instagram.dto.ApiResponse;
import sample.instagram.dto.subscribe.response.SubscribeResponse;

@RequiredArgsConstructor
@RestController
public class SubscribeApiController {

    private final SubscribeService subscribeService;

    /**
     * @Method: inSubscribe
     * @Description: 구독 등록
     */
    @PostMapping("/api/v1/subscribe/{fromMemberId}/{toMemberId}")
    public ResponseEntity<?> inSubscribe(@PathVariable Long fromMemberId, @PathVariable Long toMemberId) {
        SubscribeResponse subscribeResponse = subscribeService.createSubscribe(fromMemberId, toMemberId);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "구독 성공", subscribeResponse), HttpStatus.CREATED);
    }

    /**
     * @Method: unSubscribe
     * @Description: 구독 취소
     */
    @DeleteMapping("/api/v1/subscribe/{fromMemberId}/{toMemberId}")
    public ResponseEntity<?> unSubscribe(@PathVariable Long fromMemberId, @PathVariable Long toMemberId) {
        subscribeService.deleteSubscribe(fromMemberId, toMemberId);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "구독 취소 성공", null), HttpStatus.OK);
    }
}
