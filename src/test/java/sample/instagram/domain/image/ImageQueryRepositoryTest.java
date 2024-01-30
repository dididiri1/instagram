package sample.instagram.domain.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.like.Like;
import sample.instagram.domain.like.LikeRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.instagram.dto.Role.ROLE_USER;

@Transactional
public class ImageQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    EntityManager em;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private SubscribeRepositoryJpa subscribeRepositoryJpa;

    @Autowired
    private ImageRepositoryJpa imageRepositoryJpa;

    @Autowired
    private ImageQueryRepository imageQueryRepository;

    @Autowired
    private LikeRepositoryJpa likeRepositoryJpa;

    @DisplayName("스토리 정보를 조회한다.")
    @Test
    void findAllWithMemberByIdQueryDsl() throws Exception {
        //given
        Member member1 = createMember("member1", "name1");
        Member member2 = createMember("member2", "name2");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Subscribe subscribe1 = createSubscribe(member1, member2);
        Subscribe subscribe2 = createSubscribe(member2, member1);
        subscribeRepositoryJpa.saveAll(List.of(subscribe1, subscribe2));

        Image image1 = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example1.png", "caption1", member1);
        Image image2 = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example2.png", "caption2", member1);
        imageRepositoryJpa.saveAll(List.of(image1, image2));

        Like like = createLike(image2, member2);
        likeRepositoryJpa.save(like);

        Long memberId = member2.getId();
        PageRequest pageRequest = PageRequest.of(0, 3);

        em.flush();
        em.clear();

        //when
        List<Image> images = imageQueryRepository.findMySubscriptionStory(memberId, pageRequest);

        //then
        assertThat(images.size()).isEqualTo(2);
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

    private Image createImage(String imageUrl, String caption, Member member) {
        return Image.builder()
                .caption(caption)
                .imageUrl(imageUrl)
                .member(member)
                .build();
    }

    private Like createLike(Image image, Member member) {
        return Like.builder()
                .image(image)
                .member(member)
                .build();
    }
}
