package sample.instagram.api.controller.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.instagram.api.service.image.ImageService;
import sample.instagram.dto.ResponseDto;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;

import javax.validation.Valid;
import javax.validation.ValidationException;

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
}
