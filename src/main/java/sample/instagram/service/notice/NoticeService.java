package sample.instagram.service.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.domain.notice.Notice;
import sample.instagram.domain.notice.NoticeRepositoryJpa;
import sample.instagram.dto.notice.reponse.NoticeCreateRequest;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepositoryJpa noticeRepositoryJpa;

    @Transactional
    public void createNotice(NoticeCreateRequest request) {
        Notice noticeEntity = request.toEntity(request);
        noticeRepositoryJpa.save(noticeEntity);
    }
}
