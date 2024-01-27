package sample.instagram.domain.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static sample.instagram.domain.member.QMember.member;
import static sample.instagram.domain.subscribe.QSubscribe.subscribe;

@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Member> findAllWithSubscribe(Long memberId) {
        return queryFactory
                .select(member).distinct()
                .from(member)
                .join(member.subscribes, subscribe).fetchJoin()
                .where(subscribe.fromMember.id.eq(memberId))
                .fetch();
    }

    public Member findSubscribeById(Long memberId) {
        return queryFactory
                .select(member).distinct()
                .from(member)
                .join(member.subscribes, subscribe).fetchJoin()
                .where(member.id.eq(memberId))
                .fetchOne();
    }
}
