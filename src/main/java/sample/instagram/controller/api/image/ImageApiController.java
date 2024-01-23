package sample.instagram.controller.api.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sample.instagram.dto.ApiResponse;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;
import sample.instagram.service.image.ImageService;
import sample.instagram.dto.image.reponse.ImagePopularResponse;
import sample.instagram.service.like.LikeService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ImageApiController {

    private final ImageService imageService;

    private final LikeService likeService;

    @PostMapping("/api/v1/images")
    public ResponseEntity<?> createImage(@Valid @ModelAttribute ImageCreateRequest imageCreateRequest, BindingResult bindingResult) {
        if(imageCreateRequest.getFile().isEmpty()) {
            throw new ValidationException("이미지가 첨부되지 않았습니다.");
        }
        ImageResponse imageResponse = imageService.createImage(imageCreateRequest);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "이미지 등록 성공", imageResponse), HttpStatus.CREATED);
    }

    @PostMapping("/api/v1/images/{imageId}/likes/{memberId}")
    public ResponseEntity<?> likes(@PathVariable Long imageId, @PathVariable Long memberId) {
        likeService.createLike(imageId, memberId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "좋아요 성공", null), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/v1/images/{imageId}/likes/{memberId}")
    public ResponseEntity<?> unlikes(@PathVariable Long imageId, @PathVariable Long memberId) {
        likeService.deleteLike(imageId, memberId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "좋아요 취소 성공", null), HttpStatus.OK);
    }

    /**
     * @Method: getPopularImages
     * @Description: 인기 이미지 정보 조회
     */
    @GetMapping("/api/v1/images/popular")
    public ResponseEntity<?> getPopularImages() {
        List<ImagePopularResponse> imagePopularResponses = imageService.getPopularImages();
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "인기 이미지 정보 조회", imagePopularResponses), HttpStatus.OK);
    }
}
