package sample.instagram.controller.api.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sample.instagram.dto.ApiResponse;
import sample.instagram.dto.image.reponse.ImageCreateResponse;
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

    /**
     * @Method: createImage
     * @Description: 이미지 등록
     */
    @PostMapping("/api/v1/images")
    public ResponseEntity<?> createImage(@Valid @ModelAttribute ImageCreateRequest imageCreateRequest, BindingResult bindingResult) {
        if(imageCreateRequest.getFile().isEmpty()) {
            throw new ValidationException("이미지가 첨부되지 않았습니다.");
        }
        ImageCreateResponse imageCreateResponse = imageService.createImage(imageCreateRequest);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "이미지 등록 성공", imageCreateResponse), HttpStatus.CREATED);
    }

    /**
     * @Method: createLike
     * @Description: 좋아요 등록
     */
    @PostMapping("/api/v1/images/{imageId}/likes/{memberId}")
    public ResponseEntity<?> createLike(@PathVariable Long imageId, @PathVariable Long memberId) {
        likeService.createLike(imageId, memberId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "좋아요 성공", null), HttpStatus.CREATED);
    }

    /**
     * @Method: deleteLike
     * @Description: 좋아요 취소
     */
    @DeleteMapping("/api/v1/images/{imageId}/likes/{memberId}")
    public ResponseEntity<?> deleteLike(@PathVariable Long imageId, @PathVariable Long memberId) {
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
