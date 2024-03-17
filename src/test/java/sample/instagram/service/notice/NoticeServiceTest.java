package sample.instagram.service.notice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.notice.NoticeRepositoryJpa;

public class NoticeServiceTest extends IntegrationTestSupport {

    @Autowired
    private NoticeRepositoryJpa noticeRepositoryJpa;

    @AfterEach
    void tearDown() {
        noticeRepositoryJpa.deleteAllInBatch();
    }

    @DisplayName("공지사항 등록")
    @Test
    void createNotice() throws Exception {
        //given
        

        //when

        //then

    }
}
