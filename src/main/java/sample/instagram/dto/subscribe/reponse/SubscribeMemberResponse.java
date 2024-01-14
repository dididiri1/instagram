package sample.instagram.dto.subscribe.reponse;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

@Data
public class SubscribeMemberResponse {

    private Long memberId;
    private String username;
    private Long fromMemberId;
    private Long toMemberId;
    private Integer subscribeState;
    private Integer equalMemberState;

    @QueryProjection
    public SubscribeMemberResponse(Long memberId, String username, Long fromMemberId, Long toMemberId, Integer subscribeState, Integer equalMemberState) {
        this.memberId = memberId;
        this.username = username;
        this.fromMemberId = fromMemberId;
        this.toMemberId = toMemberId;
        this.subscribeState = subscribeState;
        this.equalMemberState = equalMemberState;
    }
}
