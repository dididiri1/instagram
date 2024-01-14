package sample.instagram.domain.subscribe;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import sample.instagram.dto.subscribe.reponse.QSubscribeMemberResponse;
import sample.instagram.dto.subscribe.reponse.SubscribeMemberResponse;

import javax.persistence.EntityManager;
import java.util.List;

import static sample.instagram.domain.member.QMember.member;
import static sample.instagram.domain.subscribe.QSubscribe.subscribe;

@Repository
public class SubscribeQueryRepository {

    private final JPAQueryFactory queryFactory;

    public SubscribeQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<SubscribeMemberResponse> findSubscribes(Long fromMemberId, Long toMemberId) {
        return queryFactory
                .select(new QSubscribeMemberResponse(member.id, member.username, subscribe.fromMember.id, subscribe.toMember.id,
                        Expressions.cases()
                                .when(JPAExpressions.selectOne()
                                        .from(subscribe)
                                        .where(subscribe.fromMember.id.eq(fromMemberId).and(subscribe.toMember.id.eq(member.id))).exists())
                                .then(1)
                                .otherwise(0).as("subscribeState"),
                        Expressions.cases()
                                .when(member.id.eq(fromMemberId)).then(1)
                                .otherwise(0).as("equalMemberState")
                ))
                .from(member)
                .innerJoin(member.subscribes, subscribe)
                .where(subscribe.fromMember.id.eq(toMemberId))
                .fetch();
    }
}
