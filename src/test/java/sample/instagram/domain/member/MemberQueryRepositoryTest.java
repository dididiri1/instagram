package sample.instagram.domain.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.subscribe.Subscribe;

import javax.persistence.EntityManager;

import static sample.instagram.domain.image.QImage.image;
import static sample.instagram.domain.member.QMember.member;
import static sample.instagram.domain.member.Role.ROLE_USER;

@Transactional
public class MemberQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() throws Exception {
        queryFactory = new JPAQueryFactory(em);

        Member member1 = createMember("member1", "name1");
        Member member2 = createMember("member2", "name2");
        Member member3 = createMember("member3", "name3");
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        Subscribe subscribe1 = Subscribe.create(member1, member2);
        Subscribe subscribe2 = Subscribe.create(member2, member1);
        Subscribe subscribe3 = Subscribe.create(member1, member3);

        em.persist(subscribe1);
        em.persist(subscribe2);
        em.persist(subscribe3);

        Image image1 = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example1.png", "caption1", member1);
        Image image2 = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example2.png", "caption2", member1);

        em.persist(image1);
        em.persist(image2);

        em.flush();
        em.clear();
    }

    @DisplayName("회원 프로필 정보를 조회한다.")
    @Test
    void findMemberProfile() throws Exception {
        //given
        Long memberId = 1L;

        //when

        //then

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
}
