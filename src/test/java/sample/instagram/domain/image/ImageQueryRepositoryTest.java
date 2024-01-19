package sample.instagram.domain.image;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.like.Like;
import sample.instagram.domain.like.QLike;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.dto.image.reponse.ImageStoryResponse;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.instagram.domain.image.QImage.image;
import static sample.instagram.domain.member.QMember.member;
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
        Member member1 = createMember("member1", "name1");
        Member member2 = createMember("member2", "name2");
        Member member3 = createMember("member3", "name3");
        Member member4 = createMember("member4", "name4");
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        Subscribe subscribe1 = Subscribe.create(member1, member2);
        Subscribe subscribe2 = Subscribe.create(member2, member1);
        Subscribe subscribe3 = Subscribe.create(member1, member3);
        Subscribe subscribe4 = Subscribe.create(member3, member1);
        Subscribe subscribe5 = Subscribe.create(member1, member4);
        Subscribe subscribe6 = Subscribe.create(member4, member1);
        em.persist(subscribe1);
        em.persist(subscribe2);
        em.persist(subscribe3);
        em.persist(subscribe4);
        em.persist(subscribe5);
        em.persist(subscribe6);

        Image image1 = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example1.png", "caption1", member1);
        Image image2 = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example2.png", "caption2", member1);

        em.persist(image1);
        em.persist(image2);

        Like like1 = createLike(image1, member2);
        Like like2 = createLike(image1, member3);
        Like like3 = createLike(image1, member4);
        Like like4 = createLike(image2, member2);
        Like like5 = createLike(image2, member3);
        Like like6 = createLike(image2, member4);
        em.persist(like1);
        em.persist(like2);
        em.persist(like3);
        em.persist(like4);
        em.persist(like5);


        em.flush();
        em.clear();
    }

    @DisplayName("스토리 정보를 조회한다.")
    @Test
    void findStoryWithImageMember() throws Exception {
        //given
        Long memberId = 2L;
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<Long> ids = queryFactory
                .select(subscribe.toMember.id)
                .from(subscribe)
                .where(subscribe.fromMember.id.eq(memberId))
                .fetch();

        //when
        List<Image> images = queryFactory
                .select(image)
                .from(image)
                .join(image.member, member).fetchJoin()
                .where(image.member.id.in(ids))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

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
