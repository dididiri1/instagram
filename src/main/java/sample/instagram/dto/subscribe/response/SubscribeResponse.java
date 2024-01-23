package sample.instagram.dto.subscribe.response;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.subscribe.Subscribe;

@Getter
public class SubscribeResponse {

    private Long id;
    private Long fromMemberId;
    private Long toMemberId;

    @Builder
    private SubscribeResponse(Long id, Long fromMemberId, Long toMemberId) {
        this.id = id;
        this.fromMemberId = fromMemberId;
        this.toMemberId = toMemberId;
    }

    public static SubscribeResponse of(Subscribe subscribe) {
        return SubscribeResponse.builder()
                .id(subscribe.getId())
                .fromMemberId(subscribe.getFromMember().getId())
                .toMemberId(subscribe.getToMember().getId())
                .build();
    }
}
