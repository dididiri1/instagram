package sample.instagram.dto.comment.response;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.comment.Comment;

@Getter
public class CommentResponse {

    private Long id;
    private String content;

    @Builder
    private CommentResponse(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }
}
