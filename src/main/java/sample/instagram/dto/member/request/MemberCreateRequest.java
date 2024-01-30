package sample.instagram.dto.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sample.instagram.domain.member.Member;
import sample.instagram.dto.Role;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
public class MemberCreateRequest {

    @NotBlank(message = "유저명 타입은 필수입니다.")
    private String username;

    @NotBlank(message = "패스워드 타입은 필수입니다.")
    private String password;

    @NotBlank(message = "이메일 타입은 필수입니다.")
    private String email;

    @NotBlank(message = "이름 타입은 필수입니다.")
    private String name;

    @Builder
    private MemberCreateRequest(String username, String password, String email, String name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    public Member toEntity(String password) {
        return Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .name(name)
                .role(Role.ROLE_USER)
                .build();
    }
}
