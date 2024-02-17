package sample.instagram.dto.image.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ImageSearch {

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

}
