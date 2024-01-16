package sample.instagram.controller.api.image;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sample.instagram.dto.ResponseDto;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reponse.ImageStoryResponse;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;
import sample.instagram.dto.image.reqeust.ImageSearch;
import sample.instagram.service.image.ImageService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ImageApiController {

    private final ImageService imageService;

    @PostMapping("/api/v1/images")
    public ResponseEntity<?> createImage(@Valid @ModelAttribute ImageCreateRequest imageCreateRequest
                                                , BindingResult bindingResult) {
        if(imageCreateRequest.getFile().isEmpty()) {
            throw new ValidationException("이미지가 첨부되지 않았습니다.");
        }
        ImageResponse imageResponse = imageService.createImage(imageCreateRequest);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.CREATED.value(), "이미지 등록 성공", imageResponse), HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/images/{id}")
    public ResponseEntity<?> getStoryImages(@PathVariable("id") Long memberId, @PageableDefault(size = 3) Pageable pageable) {
        List<ImageStoryResponse> imageResponses = imageService.getStoryImages(memberId, pageable);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK.value(), "스토리 조회 성공", imageResponses), HttpStatus.OK);
    }
}
