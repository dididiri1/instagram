package sample.instagram.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.instagram.dto.Role.ROLE_USER;

@Transactional
public class MemberQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private MemberQueryRepository memberQueryRepository;

    @Autowired
    private SubscribeRepositoryJpa subscribeRepositoryJpa;

    @DisplayName("유저의 구독자 리스트를 조회 한다.")
    @Test
    public void findMemberSubscribes() throws Exception {
        //given
        Member member1 = createMember("member1", "name1");
        Member member2 = createMember("member2", "name2");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Subscribe subscribe1 = createSubscribe(member1, member2);
        Subscribe subscribe2 = createSubscribe(member2, member1);
        subscribeRepositoryJpa.saveAll(List.of(subscribe1, subscribe2));

        em.flush();
        em.clear();

        //when
        List<Member> members = memberQueryRepository.findAllWithSubscribe(member1.getId());

        //then
        assertThat(members).hasSize(1);
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
