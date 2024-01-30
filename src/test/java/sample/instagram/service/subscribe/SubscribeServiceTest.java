package sample.instagram.service.subscribe;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;
import sample.instagram.dto.subscribe.response.SubscribeResponse;
import sample.instagram.handler.ex.CustomApiException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sample.instagram.dto.Role.ROLE_USER;

public class SubscribeServiceTest extends IntegrationTestSupport {

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private SubscribeRepositoryJpa subscribeRepositoryJpa;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @AfterEach
    void tearDown() {
        subscribeRepositoryJpa.deleteAllInBatch();
        memberRepositoryJpa.deleteAllInBatch();
    }

    @DisplayName("구독을 등록 한다.")
    @Test
    void createSubscribe() throws Exception {
        //given
        Member fromMember = createMember("member1", "test1@example.com", "name1");
        Member toMember = createMember("member2", "test2@example.com", "name2");
        memberRepositoryJpa.saveAll(List.of(fromMember, toMember));

        //when
        SubscribeResponse subscribeResponse = subscribeService.createSubscribe(fromMember.getId(), toMember.getId());

        //then
        assertThat(subscribeResponse.getId()).isNotNull();
    }

    @DisplayName("이미 구독한 구독자에게 구독할 경우 예외가 발생한다.")
    @Test
    void createSubscribeWithNoMember() throws Exception {
        //given
        Member fromMember = createMember("member1", "test1@example.com", "name1");
        Member toMember = createMember("member2", "test2@example.com", "name2");
        memberRepositoryJpa.saveAll(List.of(fromMember, toMember));

        Subscribe subscribe = createSubscribe(fromMember, toMember);
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
        Member fromMember = createMember("member1", "test1@example.com", "name1");
        Member toMember = createMember("member2", "test2@example.com", "name2");
        memberRepositoryJpa.saveAll(List.of(fromMember, toMember));

        Subscribe subscribe = createSubscribe(fromMember, toMember);
        subscribeRepositoryJpa.save(subscribe);

        //when
        subscribeService.deleteSubscribe(fromMember.getId(), toMember.getId());

        // then
        Optional<Subscribe> findSubscribe = subscribeRepositoryJpa.findById(subscribe.getId());
        assertThat(findSubscribe.isPresent()).isFalse();
    }

    private Member createMember(String username, String email, String name) {
        return Member.builder()
                .username(username)
                .password(new BCryptPasswordEncoder().encode("1234"))
                .email(email)
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
