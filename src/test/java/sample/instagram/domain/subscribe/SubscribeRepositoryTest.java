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
import static sample.instagram.dto.Role.ROLE_USER;

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
