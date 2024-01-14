package sample.instagram.service.subscribe;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.domain.subscribe.SubscribeQueryRepository;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;
import sample.instagram.dto.DataResponse;
import sample.instagram.dto.ResultStatus;
import sample.instagram.dto.subscribe.reponse.SubscribeMemberResponse;
import sample.instagram.dto.subscribe.reponse.SubscribeResponse;
import sample.instagram.handler.ex.CustomApiException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sample.instagram.domain.member.Role.ROLE_USER;

public class SubscribeServiceTest extends IntegrationTestSupport {

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private SubscribeRepositoryJpa subscribeRepositoryJpa;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubscribeQueryRepository subscribeQueryRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @AfterEach
    void tearDown() {
        subscribeRepositoryJpa.deleteAllInBatch();
        memberRepositoryJpa.deleteAllInBatch();
    }

    @DisplayName("구독을 등록 한다.")
    @Test
    void createSubscribe() throws Exception {

        //given
        Member member1 = createMember("testUser1", "1234","test1@naver.com", "김구라");
        Member member2 = createMember("testUser2", "1234","test2@naver.com", "홍길동");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Member fromMember = memberRepository.findOne(member1.getId());
        Member toMember = memberRepository.findOne(member2.getId());

        //when
        SubscribeResponse subscribeResponse = subscribeService.createSubscribe(fromMember.getId(), toMember.getId());

        //then
        assertThat(subscribeResponse.getId()).isNotNull();
    }

    @DisplayName("이미 구독한 구독자에게 구독할 경우 예외가 발생한다.")
    @Test
    void createSubscribeWithNoMember() throws Exception {
        //given
        Member member1 = createMember("testUser1", "1234","test1@naver.com", "김구라");
        Member member2 = createMember("testUser2", "1234","test2@naver.com", "홍길동");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Member fromMember = memberRepository.findOne(member1.getId());
        Member toMember = memberRepository.findOne(member2.getId());

        Subscribe subscribe = Subscribe.create(fromMember, toMember);
        subscribeRepositoryJpa.save(subscribe);

        //when  //then
        assertThatThrownBy(() -> subscribeService.createSubscribe(fromMember.getId(), toMember.getId()))
                .isInstanceOf(CustomApiException.class)
                .hasMessage("이미 구독을 하였습니다.");
    }

    @DisplayName("구독을 취소 한다.")
    @Test
    void deleteSubscribe() throws Exception {
        //given
        Long fromMemberId = 1L;
        Long toMemberId = 2L;

        //when
        DataResponse dataResponse = subscribeService.deleteSubscribe(fromMemberId, toMemberId);

        //then
        assertThat(dataResponse.getResult()).isEqualTo(ResultStatus.SUCCESS);
    }

    @DisplayName("구독한 구독자들의 리스트를 조회한다.")
    @Test
    void getSubscribes() throws Exception {
        //given
        Long fromMemberId = 1L;
        Long toMemberId = 2L;

        Member member1 = createMember("testUser1", "test@example.com", "유저1");
        Member member2 = createMember("testUser2", "test@example.com", "유저2");
        Member member3 = createMember("testUser3", "test@example.com", "유저3");
        memberRepositoryJpa.saveAll(List.of(member1, member2, member3));

        Subscribe subscribe1 = Subscribe.create(member1, member2);
        Subscribe subscribe2 = Subscribe.create(member1, member3);
        Subscribe subscribe3 = Subscribe.create(member2, member1);
        Subscribe subscribe4 = Subscribe.create(member2, member3);
        subscribeRepositoryJpa.saveAll(List.of(subscribe1, subscribe2, subscribe3, subscribe4));

        //when
        List<SubscribeMemberResponse> responses = subscribeQueryRepository.findSubscribes(fromMemberId, toMemberId);

        //then
        assertThat(responses).hasSize(2)
                .extracting("memberId", "username", "subscribeState", "equalMemberState")
                .containsExactlyInAnyOrder(
                        tuple(1L, "testUser1", 0, 1),
                        tuple(3L, "testUser3", 1, 0)
                );
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
