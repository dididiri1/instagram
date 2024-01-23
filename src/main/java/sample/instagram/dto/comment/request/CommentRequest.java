package sample.instagram.dto.comment.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.instagram.domain.comment.Comment;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.member.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CommentRequest {

    @NotNull(message = "회원 ID은 필수입니다.")
    private Long memberId;

    @NotNull(message = "이미지 ID은 필수입니다.")
    private Long imageId;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @Builder
    private CommentRequest(Long memberId, Long imageId, String content) {
        this.memberId = memberId;
        this.imageId = imageId;
        this.content = content;
    }

    public Comment createComment(Member member, Image image) {
        return Comment.builder()
                .member(member)
                .image(image)
                .content(content)
                .build();
    }
}

