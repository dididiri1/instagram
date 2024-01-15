package sample.instagram.dto.subscribe.reponse;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SubscribeMemberResponse {

    private Long memberId;
    private String username;
    private Integer subscribeState;
    private Integer equalMemberState;

    @QueryProjection
    @Builder
    public SubscribeMemberResponse(Long memberId, String username, Integer subscribeState, Integer equalMemberState) {
        this.memberId = memberId;
        this.username = username;
        this.subscribeState = subscribeState;
        this.equalMemberState = equalMemberState;
    }
}
