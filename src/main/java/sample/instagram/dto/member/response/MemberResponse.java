package sample.instagram.dto.member.response;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.member.Member;

@Getter
public class MemberResponse {

    private Long id;
    private String username;
    private String email;
    private String name;
    private String profileImageUrl;;


    @Builder
    private MemberResponse(Long id, String username, String email, String name, String profileImageUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}
