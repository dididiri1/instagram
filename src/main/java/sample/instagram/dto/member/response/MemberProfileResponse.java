package sample.instagram.dto.member.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.dto.comment.response.CommentResponse;
import sample.instagram.dto.image.reponse.ImageCreateResponse;
import sample.instagram.dto.image.reponse.ImageResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MemberProfileResponse {

    private boolean pageOwnerState;
    private int imageCount;
    private boolean subscribeState;
    private int subscribeCount;
    private String username;
    private String name;
    private String profileImageUrl;
    private List<ImageResponse> images;

    @Builder
    public MemberProfileResponse(boolean pageOwnerState, int imageCount, boolean subscribeState, int subscribeCount, String username, String name, String profileImageUrl, List<ImageResponse> images) {
        this.pageOwnerState = pageOwnerState;
        this.imageCount = imageCount;
        this.subscribeState = subscribeState;
        this.subscribeCount = subscribeCount;
        this.username = username;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.images = images;
    }

    public static MemberProfileResponse of(Member member, Long memberId, int subscribeCount) {
        return MemberProfileResponse.builder()
                .pageOwnerState(isPageOwnerState(member.getId(), memberId))
                .imageCount(member.getImages().size())
                .subscribeState(isSubscribeState(member.getSubscribes(), memberId, member.getId()))
                .subscribeCount(subscribeCount)
                .username(member.getUsername())
                .name(member.getName())
                .profileImageUrl(member.getProfileImageUrl())
                .images(member.getImages().stream()
                        .map(image -> ImageResponse.of(image))
                        .sorted(Comparator.comparing(ImageResponse::getId).reversed())
                        .collect(Collectors.toList())
                )
                .build();
    }

    private static boolean isSubscribeState(List<Subscribe> subscribes, Long fromMemberId , Long toMemberId) {
        return subscribes.stream()
                .anyMatch(s -> isSubscriptionMatch(s, fromMemberId, toMemberId));
    }

    private static boolean isSubscriptionMatch(Subscribe subscribe, Long fromMemberId, Long toMemberId) {
        return subscribe.getFromMember().getId().equals(fromMemberId) && subscribe.getToMember().getId().equals(toMemberId);
    }

    private static boolean isPageOwnerState(Long fromMemberId, Long toMemberId) {
        return (fromMemberId == toMemberId);
    }

}
