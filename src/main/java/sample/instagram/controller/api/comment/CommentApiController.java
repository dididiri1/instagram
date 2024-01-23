package sample.instagram.controller.api.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.instagram.dto.ApiResponse;
import sample.instagram.dto.comment.request.CommentRequest;
import sample.instagram.dto.comment.response.CommentResponse;
import sample.instagram.service.comment.CommentService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/api/v1/comment")
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentRequest request) {
        CommentResponse commentResponse = commentService.createComment(request);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "댓글 등록 성공", commentResponse), HttpStatus.CREATED);
    }
}
