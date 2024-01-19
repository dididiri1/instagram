package sample.instagram.service.image.reponse;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.image.Image;

@Getter
public class ImageStoryResponse {

    private Long id;

    private String caption;

    private String imageUrl;

    private String username;

    private boolean likeState;

    private int likeCount;

    @Builder
    private ImageStoryResponse(Long id, String caption, String imageUrl, String username, int likeCount) {
        this.id = id;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.username = username;
        this.likeCount = likeCount;
    }

    public static ImageStoryResponse of(Image image) {
        return ImageStoryResponse.builder()
                .id(image.getId())
                .caption(image.getCaption())
                .imageUrl(image.getImageUrl())
                .username(image.getMember().getUsername())
                .likeCount(image.getLikes().size())
                .build();

    }
}
