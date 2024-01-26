package sample.instagram.dto.member.response;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.subscribe.Subscribe;

@Getter
public class MemberSubscribeResponse {

    private Long memberId;
    private String profileImageUrl;
    private String username;
    private int subscribeState;
    private int equalMemberState;

    @Builder
    private MemberSubscribeResponse(Long memberId, String profileImageUrl, String username, int subscribeState, int equalMemberState) {
        this.memberId = memberId;
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.subscribeState = subscribeState;
        this.equalMemberState = equalMemberState;
    }

    public static MemberSubscribeResponse of(Member member, Long memberId) {
        return MemberSubscribeResponse.builder()
                .memberId(member.getId())
                .profileImageUrl(member.getProfileImageUrl())
                .username(member.getUsername())
                .subscribeState(isSubscribeState(member, memberId, member.getId()) ? 1 : 0)
                .equalMemberState(isEqualMemberState(member.getId(), memberId))
                .build();
    }

    private static boolean isSubscribeState(Member member, Long fromMemberId , Long toMemberId) {
        return member.getSubscribes().stream()
                .anyMatch(s -> isSubscriptionMatch(s, fromMemberId, toMemberId));
    }

    private static boolean isSubscriptionMatch(Subscribe subscribe, Long fromMemberId, Long toMemberId) {
        return subscribe.getFromMember().getId().equals(fromMemberId) && subscribe.getToMember().getId().equals(toMemberId);
    }

    private static int isEqualMemberState(Long memberId, Long toMemberId) {
        return memberId.equals(toMemberId) ? 1 : 0;
    }
}
