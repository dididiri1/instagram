package sample.instagram.dto.image.reqeust;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.member.Member;

@Getter
@Setter
public class ImageCreateRequest {

    private Long memberId;
    private MultipartFile file;
    private String caption;

    public Image create(String imageUrl, Member member) {
        return Image.builder()
                .caption(caption)
                .imageUrl(imageUrl)
                .member(member)
                .build();
    }
}
