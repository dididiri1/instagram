package sample.instagram.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepositoryJpa extends JpaRepository<Like, Long> {

    void deleteByImageIdAndMemberId(Long imageId, Long memberId);
}
