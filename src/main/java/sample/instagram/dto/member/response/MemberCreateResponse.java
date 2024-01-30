package sample.instagram.dto.member.response;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.member.Member;

@Getter
public class MemberCreateResponse {

    private Long id;
    private String username;
    private String email;
    private String name;

    @Builder
    private MemberCreateResponse(Long id, String username, String email, String name) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
    }

    public static MemberCreateResponse of(Member member) {
        return MemberCreateResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}
