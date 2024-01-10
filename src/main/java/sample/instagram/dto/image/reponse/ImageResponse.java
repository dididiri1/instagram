package sample.instagram.dto.image.reponse;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.image.Image;

@Getter
public class ImageResponse {

    private Long id;

    private String caption;

    private String imageUrl;

    @Builder
    public ImageResponse(Long id, String caption, String imageUrl) {
        this.id = id;
        this.caption = caption;
        this.imageUrl = imageUrl;
    }

    public static ImageResponse of(Image image) {
        return ImageResponse.builder()
                .id(image.getId())
                .caption(image.getCaption())
                .imageUrl(image.getImageUrl())
                .build();

    }
}
