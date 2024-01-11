package sample.instagram.dto.image.reqeust;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.member.Member;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ImageCreateRequest {

    @NotBlank(message = "회원ID 타입은 필수입니다.")
    private Long memberId;

    private MultipartFile file;

    @NotBlank(message = "머리말 타입은 필수입니다.")
    private String caption;

    @Builder
    public ImageCreateRequest(Long memberId, MultipartFile file, String caption) {
        this.memberId = memberId;
        this.file = file;
        this.caption = caption;
    }

    public Image create(String imageUrl, Member member) {
        return Image.builder()
                .caption(caption)
                .imageUrl(imageUrl)
                .member(member)
                .build();
    }
}
