package sample.instagram.domain.subscribe;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.dto.subscribe.reponse.QSubscribeMemberResponse;
import sample.instagram.dto.subscribe.reponse.SubscribeMemberResponse;
import sample.instagram.service.member.MemberService;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.instagram.domain.member.QMember.member;
import static sample.instagram.domain.member.Role.ROLE_USER;
import static sample.instagram.domain.subscribe.QSubscribe.subscribe;

@Transactional
public class SubscribeQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private SubscribeRepositoryJpa subscribeRepositoryJpa;

    @BeforeEach
    public void before() throws Exception {
        queryFactory = new JPAQueryFactory(em);
    }

    @DisplayName("구독 정보 리스트를 조회 한다.")
    @Test
    public void getSubscribes() throws Exception {
        //given

        Member member1 = createMember("member1", "name1");
        Member member2 = createMember("member2", "name2");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Subscribe subscribe1 = createSubscribe(member1, member2);
        Subscribe subscribe2 = createSubscribe(member2, member1);
        subscribeRepositoryJpa.saveAll(List.of(subscribe1, subscribe2));

        Long pageMemberId = member1.getId();
        Long memberId = member2.getId();

        //when
        List<SubscribeMemberResponse> result = queryFactory
                .select(new QSubscribeMemberResponse(member.id, member.username,
                        Expressions.cases()
                                .when(JPAExpressions.selectOne()
                                        .from(subscribe)
                                        .where(subscribe.fromMember.id.eq(memberId).and(subscribe.toMember.id.eq(member.id))).exists())
                                .then(1)
                                .otherwise(0).as("subscribeState"),
                        Expressions.cases()
                                .when(member.id.eq(memberId)).then(1)
                                .otherwise(0).as("equalMemberState")
                ))
                .from(member)
                .innerJoin(member.subscribes, subscribe)
                .where(subscribe.fromMember.id.eq(pageMemberId))
                .fetch();

        System.out.println("result = " + result);
    }

    private Member createMember(String username, String name) {
        return Member.builder()
                .username(username)
                .password(new BCryptPasswordEncoder().encode("1234"))
                .email("test@example.com")
                .name(name)
                .role(ROLE_USER)
                .build();
    }

    private Subscribe createSubscribe(Member fromMember, Member toMember) {
        return Subscribe.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();
    }
}
