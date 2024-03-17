package sample.instagram.domain.notice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepositoryJpa extends JpaRepository<Notice, Long> {
}
