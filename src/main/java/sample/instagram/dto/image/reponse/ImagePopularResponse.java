package sample.instagram.dto.image.reponse;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.image.Image;

@Getter
public class ImagePopularResponse {

    private Long id;

    private String caption;

    private String imageUrl;

    private int likeCount;

    @Builder
    public ImagePopularResponse(Long id, String caption, String imageUrl, int likeCount) {
        this.id = id;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
    }

    public static ImagePopularResponse of(Image image) {
        return ImagePopularResponse.builder()
                .id(image.getId())
                .caption(image.getCaption())
                .imageUrl(image.getImageUrl())
                .likeCount(image.getLikes().size())
                .build();

    }
}
