package sample.instagram.dto.member.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.member.Member;
import sample.instagram.dto.image.reponse.ImageResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MemberProfileResponse {

    private boolean pageOwnerState;
    private int imageCount;
    private boolean subscribeState;
    private int subscribeCount;

    private String name;
    private List<ImageResponse> images;

    @Builder
    public MemberProfileResponse(boolean pageOwnerState, int imageCount, boolean subscribeState, int subscribeCount, String name, List<ImageResponse> images) {
        this.pageOwnerState = pageOwnerState;
        this.imageCount = imageCount;
        this.subscribeState = subscribeState;
        this.subscribeCount = subscribeCount;
        this.name = name;
        this.images = images;
    }

    public static MemberProfileResponse of(Member member, boolean pageOwnerState, boolean subscribeState, int subscribeCount) {
        return MemberProfileResponse.builder()
                .name(member.getName())
                .images(member.getImages().stream()
                        .map(image -> ImageResponse.of(image))
                        .collect(Collectors.toList())
                )
                .pageOwnerState(pageOwnerState)
                .imageCount(member.getImages().size())
                .subscribeState(subscribeState)
                .subscribeCount(subscribeCount)
                .build();
    }
}
