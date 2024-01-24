package sample.instagram.dto.comment.response;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.comment.Comment;

@Getter
public class CommentResponse {

    private Long id;
    private String content;
    private String username;

    @Builder
    private CommentResponse(Long id, String content, String username) {
        this.id = id;
        this.content = content;
        this.username = username;
    }

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getMember().getUsername())
                .build();
    }
}
