package sample.instagram.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final EntityManager em;

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
}
