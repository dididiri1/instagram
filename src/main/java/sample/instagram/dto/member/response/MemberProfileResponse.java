package sample.instagram.dto.member.response;

import lombok.Getter;
import lombok.Setter;
import sample.instagram.domain.member.Member;

@Getter
@Setter
public class MemberProfileResponse {

    private boolean pageOwnerState;
    private int imageCount;
    private boolean subscribeState;
    private int subscribeCount;
    private Member member;

}
