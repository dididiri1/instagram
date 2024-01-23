package sample.instagram.dto.member.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileImageResponse {

    private String profileImageUrl;

    @Builder
    public ProfileImageResponse(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public static ProfileImageResponse of(String profileImageUrl) {
        return ProfileImageResponse.builder()
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
