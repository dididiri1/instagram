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
public class MemberCreateRequest {

    @NotNull(message = "유저명 타입은 필수입니다.")
    private String username;

    @NotNull(message = "패스워드 타입은 필수입니다.")
    private String password;

    @NotNull(message = "이메일 타입은 필수입니다.")
    private String email;

    @NotNull(message = "이름 타입은 필수입니다.")
    private String name;

    @Builder
    public MemberCreateRequest(String username, String password, String email, String name) {
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
