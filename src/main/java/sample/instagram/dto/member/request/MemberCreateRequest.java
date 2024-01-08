package sample.instagram.dto.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
public class MemberCreateRequest {

    @Size(min = 2, max = 20)
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
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
