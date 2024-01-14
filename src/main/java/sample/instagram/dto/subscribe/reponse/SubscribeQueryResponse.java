package sample.instagram.dto.subscribe.reponse;

import lombok.Builder;
import lombok.Data;

@Data
public class SubscribeQueryResponse {

    private Long memberId;
    private String username;
    private Long fromMemberId;
    private Long toMemberId;
    private boolean subscribeStatus;
    private Integer equalMemberState;

    @Builder
    public SubscribeQueryResponse(Long memberId, String username, Long fromMemberId, Long toMemberId, boolean subscribeStatus, Integer equalMemberState) {
        this.memberId = memberId;
        this.username = username;
        this.fromMemberId = fromMemberId;
        this.toMemberId = toMemberId;
        this.subscribeStatus = subscribeStatus;
        this.equalMemberState = equalMemberState;
    }
}
