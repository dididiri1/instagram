package sample.instagram.domain.image;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reponse.QImageResponse;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.instagram.domain.image.QImage.image;
import static sample.instagram.domain.member.Role.ROLE_USER;
import static sample.instagram.domain.subscribe.QSubscribe.subscribe;

@Transactional
public class ImageQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() throws Exception {
        queryFactory = new JPAQueryFactory(em);
        Member member1 = createMember("testUser1", "test@example.com", "유저1");
        Member member2 = createMember("testUser2", "test@example.com", "유저2");
        em.persist(member1);
        em.persist(member2);

        Subscribe subscribe1 = Subscribe.create(member1, member2);
        Subscribe subscribe2 = Subscribe.create(member2, member1);
        em.persist(subscribe1);
        em.persist(subscribe2);

        Image image1 = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image2 = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);

        em.persist(image1);
        em.persist(image2);

        em.flush();
        em.clear();
    }

    @DisplayName("구독 유저들의 스토리 이미지를 조회한다.")
    @Test
    void getStoryImages() throws Exception {
        //given
        Long memberId = 1L;
        List<Long> ids = getSubscribeMemberId(memberId);

        //when
        List<ImageResponse> imageResponses = queryFactory
                .select(new QImageResponse(image.id, image.caption, image.imageUrl))
                .from(image)
                .where(image.member.id.in(ids))
                .fetch();

        for (ImageResponse images : imageResponses) {
            System.out.println("images.getId() = " + images.getId());
            System.out.println("images.getImageUrl() = " + images.getImageUrl());
            System.out.println("images.getCaption() = " + images.getCaption());
        }

        //then
        assertThat(imageResponses).hasSize(2)
                .extracting("id", "caption", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple(1L, "이미지 캡션", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png"),
                        tuple(2L, "이미지 캡션", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png")
                );
    }

    private List<Long> getSubscribeMemberId(Long memberId) {
        return queryFactory
                .select(subscribe.toMember.id)
                .from(subscribe)
                .where(subscribe.fromMember.id.eq(memberId))
                .orderBy(subscribe.id.desc())
                .fetch();
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

    public Image createImage(String imageUrl, Member member) {
        return Image.builder()
                .caption("이미지 캡션")
                .imageUrl(imageUrl)
                .member(member)
                .build();
    }
}
