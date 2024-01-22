package sample.instagram.service.member.reponse;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.member.Member;

@Getter
public class MemberResponse {

    private Long id;
    private String username;
    private String email;
    private String name;

    @Builder
    private MemberResponse(Long id, String username, String email, String name) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
    }

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}
