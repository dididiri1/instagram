package sample.instagram.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.instagram.domain.subscribe.Subscribe;

import java.util.Optional;

public interface LikeRepositoryJpa extends JpaRepository<Like, Long> {

    void deleteByImageIdAndMemberId(Long imageId, Long memberId);

    Like findByImageIdAndMemberId(Long imageId, Long memberId);
}
