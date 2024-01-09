package sample.instagram.domain.subscribe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;
import sample.instagram.domain.member.MemberRepositoryJpa;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.instagram.domain.member.Role.ROLE_USER;

@Transactional
public class SubscribeRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private SubscribeRepositoryJpa subscribeRepositoryJpa;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("구독을 등록한다.")
    @Test
    void createSubscribe() throws Exception {
        //given
        Member member1 = createMember("testUser1", "1234","test2@naver.com", "김구라");
        Member member2 = createMember("testUser2", "1234","test2@naver.com", "홍길동");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Member fromMember = memberRepository.findOne(member1.getId());
        Member toMember = memberRepository.findOne(member2.getId());


        //when
        Subscribe subscribe = Subscribe.create(fromMember, toMember);
        Subscribe saveSubscribe = subscribeRepositoryJpa.save(subscribe);

        //then
        assertThat(saveSubscribe).isNotNull();
        assertThat(saveSubscribe.getFromMember().getId()).isEqualTo(member1.getId());
        assertThat(saveSubscribe.getToMember().getId()).isEqualTo(member2.getId());
    }

    private Member createMember(String username, String password, String email, String name) {
        return Member.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .email(email)
                .name(name)
                .role(ROLE_USER)
                .build();
    }
}
