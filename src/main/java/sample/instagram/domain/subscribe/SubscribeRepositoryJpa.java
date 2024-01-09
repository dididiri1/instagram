package sample.instagram.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepositoryJpa extends JpaRepository<Subscribe, Long> {


    void deleteByFromMemberIdAndToMemberId(Long fromUserId, Long toUserId);
}
