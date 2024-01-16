package sample.instagram.dto.image.reponse;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sample.instagram.domain.image.Image;

@Getter
public class ImageStoryResponse {

    private Long id;

    private String caption;

    private String imageUrl;

    private String username;

    @QueryProjection
    @Builder
    public ImageStoryResponse(Long id, String caption, String imageUrl, String username) {
        this.id = id;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.username = username;
    }

    public static ImageStoryResponse of(Image image, String username) {
        return ImageStoryResponse.builder()
                .id(image.getId())
                .caption(image.getCaption())
                .imageUrl(image.getImageUrl())
                .username(username)
                .build();

    }
}
