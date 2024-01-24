package sample.instagram.dto.image.reponse;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.like.Like;
import sample.instagram.dto.comment.response.CommentResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ImageStoryResponse {

    private Long id;

    private String caption;

    private String imageUrl;

    private String username;

    private boolean likeState;

    private int likeCount;

    private List<CommentResponse> comments;

    @Builder
    private ImageStoryResponse(Long id, String caption, String imageUrl, String username, boolean likeState, int likeCount, List<CommentResponse> comments) {
        this.id = id;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.username = username;
        this.likeState = likeState;
        this.likeCount = likeCount;
        this.comments = comments;
    }

    public static ImageStoryResponse of(Image image, Long memberId) {
        return ImageStoryResponse.builder()
                .id(image.getId())
                .caption(image.getCaption())
                .imageUrl(image.getImageUrl())
                .username(image.getMember().getUsername())
                .likeState(isLikeState(image.getLikes(), memberId))
                .likeCount(image.getLikes().size())
                .comments(image.getComments().stream()
                        .map(comment -> CommentResponse.of(comment))
                        .sorted(Comparator.comparing(CommentResponse::getId).reversed())
                        .collect(Collectors.toList()))
                .build();
    }

    private static boolean isLikeState(List<Like> likes, Long memberId) {
        return likes.stream()
                .anyMatch(like -> like.getMember().getId().equals(memberId));
    }
}
