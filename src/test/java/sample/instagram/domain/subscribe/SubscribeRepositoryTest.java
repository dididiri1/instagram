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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        Member member1 = createMember("fromUser","test1@naver.com", "유저1");
        Member member2 = createMember("toUser","test2@naver.com", "유저2");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Member fromMember = memberRepository.findOne(member1.getId());
        Member toMember = memberRepository.findOne(member2.getId());

        //when
        Subscribe subscribe = Subscribe.create(fromMember, toMember);
        Subscribe saveSubscribe = subscribeRepositoryJpa.save(subscribe);

        //then
        assertThat(saveSubscribe).isNotNull();
    }

    @DisplayName("구독을 취소한다.")
    @Test
    void deleteSubscribe() throws Exception {
        //given
        Long fromMemberId = 1L;
        Long toMemberId = 2L;

        //when
        subscribeRepositoryJpa.deleteByFromMemberIdAndToMemberId(fromMemberId, toMemberId);

        //then
        Optional<Subscribe> deletedSubscribe = subscribeRepositoryJpa.findByFromMemberIdAndToMemberId(fromMemberId, toMemberId);
        assertThat(deletedSubscribe.isPresent()).isFalse();
    }

    @DisplayName("두 유저가 구독일 경우 구독 상태는 TRUE 이다.")
    @Test
    void existsByFromMemberIdAndToMemberId() throws Exception {
        //given
        Member member1 = createMember("fromUser","test1@naver.com", "유저1");
        Member member2 = createMember("toUser","test2@naver.com", "유저2");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Member fromMember = memberRepository.findOne(member1.getId());
        Member toMember = memberRepository.findOne(member2.getId());

        Subscribe subscribe = Subscribe.create(fromMember, toMember);
        subscribeRepositoryJpa.save(subscribe);

        //when
        boolean subscribeState = subscribeRepositoryJpa.existsByFromMemberIdAndToMemberId(member1.getId(), toMember.getId());

        //then
        assertThat(subscribeState).isTrue();
    }

    @DisplayName("특정 유저의 구독 갯수를 조회한다.")
    @Test
    void countByFromMemberId() throws Exception {
        //given
        Member member1 = createMember("fromUser","test1@naver.com", "유저1");
        Member member2 = createMember("toUser","test2@naver.com", "유저2");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Member fromMember = memberRepository.findOne(member1.getId());
        Member toMember = memberRepository.findOne(member2.getId());

        //when
        Subscribe subscribe = Subscribe.create(fromMember, toMember);
        subscribeRepositoryJpa.save(subscribe);

        //when
        int subscribeCount = subscribeRepositoryJpa.countByFromMemberId(member1.getId());

        //then
        assertThat(subscribeCount).isEqualTo(1);
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
