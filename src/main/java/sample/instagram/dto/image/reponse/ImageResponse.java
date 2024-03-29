package sample.instagram.dto.image.reponse;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.image.Image;

@Getter
public class ImageResponse {

    private Long id;

    private String caption;

    private String imageUrl;

    private int likeCount;

    @QueryProjection
    @Builder
    public ImageResponse(Long id, String caption, String imageUrl, int likeCount) {
        this.id = id;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
    }

    public static ImageResponse of(Image image) {
        return ImageResponse.builder()
                .id(image.getId())
                .caption(image.getCaption())
                .imageUrl(image.getImageUrl())
                .likeCount(image.getLikes().size())
                .build();

    }
}
