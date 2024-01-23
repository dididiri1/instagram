package sample.instagram.dto.member.request;

import lombok.Getter;
import sample.instagram.domain.member.Member;

@Getter
public class MemberProfileRequest {

    private boolean pageOwnerState;
    private int imageCount;
    private boolean subscribeState;
    private int subscribeCount;
    private Member member;
}
