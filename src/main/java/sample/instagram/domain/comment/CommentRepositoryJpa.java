package sample.instagram.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepositoryJpa extends JpaRepository<Comment, Long> {
}
