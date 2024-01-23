package sample.instagram.dto.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProfileImageRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long memberId;

    private MultipartFile file;

    @Builder
    private ProfileImageRequest(Long memberId, MultipartFile file) {
        this.memberId = memberId;
        this.file = file;
    }

}
