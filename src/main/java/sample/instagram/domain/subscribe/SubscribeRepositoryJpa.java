package sample.instagram.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscribeRepositoryJpa extends JpaRepository<Subscribe, Long> {

    void deleteByFromMemberIdAndToMemberId(Long fromUserId, Long toUserId);

    Optional<Subscribe> findByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);

    boolean existsByFromMemberIdAndToMemberId(Long pageMemberId, Long memberId);

    int countByFromMemberId(Long pageMemberId);

}

