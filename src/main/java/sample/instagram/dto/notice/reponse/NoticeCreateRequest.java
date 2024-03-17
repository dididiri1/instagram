package sample.instagram.dto.notice.reponse;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.notice.Notice;

@Getter
public class NoticeCreateRequest {

    private String subject;

    private String content;

    @Builder
    private NoticeCreateRequest(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public static Notice toEntity(NoticeCreateRequest request) {
        return Notice.builder()
                .subject(request.subject)
                .content(request.content)
                .build();
    }
}
