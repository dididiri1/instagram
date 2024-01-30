package sample.instagram.dto.member.response;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.member.Member;

@Getter
public class MemberUpdateResponse {

    private Long id;
    private String username;
    private String email;
    private String name;
    private String bio;


    @Builder
    private MemberUpdateResponse(Long id, String username, String email, String name, String bio) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.bio = bio;
    }

    public static MemberUpdateResponse of(Member member) {
        return MemberUpdateResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .bio(member.getBio())
                .build();
    }
}
