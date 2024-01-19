package sample.instagram.dto.image.reponse;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.like.Like;

import java.util.List;

@Getter
public class JsonResultDto {

    private Long id;

    private String caption;

    private String imageUrl;

    private String username;

    private int likeCount;

    @Builder
    public JsonResultDto(Long id, String caption, String imageUrl, String username, int likeCount) {
        this.id = id;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.username = username;
        this.likeCount = likeCount;
    }

    public static JsonResultDto of(Image image) {
        return JsonResultDto.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .caption(image.getCaption())
                .username(image.getMember().getUsername())
                .likeCount(image.getLikes().size())
                .build();

    }
}
