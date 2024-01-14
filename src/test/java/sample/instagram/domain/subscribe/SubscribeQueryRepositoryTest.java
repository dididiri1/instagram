package sample.instagram.domain.subscribe;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.domain.member.Member;
import sample.instagram.dto.subscribe.reponse.QSubscribeMemberResponse;
import sample.instagram.dto.subscribe.reponse.SubscribeMemberResponse;
import sample.instagram.dto.subscribe.reponse.SubscribeQueryResponse;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.types.ExpressionUtils.count;
import static org.assertj.core.api.Assertions.assertThat;
import static sample.instagram.domain.member.QMember.member;
import static sample.instagram.domain.member.Role.ROLE_USER;
import static sample.instagram.domain.subscribe.QSubscribe.subscribe;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@Rollback(value = false)
public class SubscribeQueryRepositoryTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void before() throws Exception {
        queryFactory = new JPAQueryFactory(em);
        Member member1 = createMember("testUser1", "test@example.com", "유저1");
        Member member2 = createMember("testUser2", "test@example.com", "유저2");
        Member member3 = createMember("testUser3", "test@example.com", "유저3");
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        Subscribe subscribe1 = Subscribe.create(member1, member2);
        Subscribe subscribe2 = Subscribe.create(member1, member3);
        Subscribe subscribe3 = Subscribe.create(member2, member1);
        Subscribe subscribe4 = Subscribe.create(member2, member3);
        em.persist(subscribe1);
        em.persist(subscribe2);
        em.persist(subscribe3);
        em.persist(subscribe4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member.getId() = " + member.getId());
        }

        List<Subscribe> subscribes = em.createQuery("select s from Subscribe s", Subscribe.class).getResultList();

        for (Subscribe subscribe : subscribes) {
            System.out.println("subscribe = " + subscribe);
        }
    }

    @Test
    public void subQuery() throws Exception {

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("testUser1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("testUser1");
    }

    @DisplayName("구독 정보 리스트를 조회 한다.")
    @Test
    public void getSubscribes() throws Exception {
        QSubscribe subscribeSub = new QSubscribe("subscribeSub");

        Long fromMemberId = 1L;
        Long toMemberId = 2L;

        List<SubscribeMemberResponse> result = queryFactory
                .select(new QSubscribeMemberResponse(member.id, member.username,
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

        for (SubscribeMemberResponse response : result) {
            System.out.println("response = " + response);
        }

    }

    private Member createMember(String username, String email, String name) {
        return Member.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode("1234"))
                .email(email)
                .name(name)
                .role(ROLE_USER)
                .build();
    }
}
