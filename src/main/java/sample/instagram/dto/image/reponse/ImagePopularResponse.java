package sample.instagram.dto.image.reponse;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.image.Image;

@Getter
public class ImagePopularResponse {

    private Long memberId;

    private String imageUrl;

    private int likeCount;

    @Builder
    public ImagePopularResponse(Long memberId, String imageUrl, int likeCount) {
        this.memberId = memberId;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
    }

    public static ImagePopularResponse of(Image image) {
        return ImagePopularResponse.builder()
                .memberId(image.getMember().getId())
                .imageUrl(image.getImageUrl())
                .likeCount(image.getLikes().size())
                .build();

    }
}
