package sample.instagram.dto.image.reqeust;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.member.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ImageCreateRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long memberId;

    //@NotNull(message = "이미지 첨부는 필수입니다.")
    private MultipartFile file;

    @NotBlank(message = "사진 설명은 필수입니다.")
    private String caption;

    @Builder
    private ImageCreateRequest(Long memberId, MultipartFile file, String caption) {
        this.memberId = memberId;
        this.file = file;
        this.caption = caption;
    }

    public Image toEntity(String imageUrl, Member member) {
        return Image.builder()
                .caption(caption)
                .imageUrl(imageUrl)
                .member(member)
                .build();
    }
}
