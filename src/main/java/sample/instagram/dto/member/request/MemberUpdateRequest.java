package sample.instagram.dto.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
public class MemberUpdateRequest {

    private String password;

    @NotBlank(message = "이메일 타입은 필수입니다.")
    private String email;

    @NotBlank(message = "이름 타입은 필수입니다.")
    private String name;

    private String bio;

    @Builder
    public MemberUpdateRequest(String password, String email, String name, String bio) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.bio = bio;
    }

    public Member toEntity(String password) {
        return Member.builder()
                .password(password)
                .email(email)
                .name(name)
                .bio(bio)
                .build();
    }
}
