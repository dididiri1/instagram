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

import javax.persistence.EntityManager;

import static sample.instagram.domain.member.Role.ROLE_USER;

@Transactional
public class MemberRepositoryJpaTest extends IntegrationTestSupport {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @BeforeEach
    public void before() throws Exception {
        queryFactory = new JPAQueryFactory(em);
        Member member1 = createMember("testUser1", "test1@example.com", "유저1");
        em.persist(member1);

        Image image1 = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example1.png", "캡션1", member1);
        Image image2 = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example2.png", "캡션2", member1);
        Image image3 = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example2.png", "캡션3", member1);

        em.persist(image1);
        em.persist(image2);
        em.persist(image3);

        em.flush();
        em.clear();
    }

    @DisplayName("회원 조회 이미지 양방향 쿼리 테스트")
    @Test
    void getMemberProfileImagesQueryTest() throws Exception {
        Member memberEntity = memberRepositoryJpa.findById(1L).orElseThrow();
        int size = memberEntity.getImages().size();
        System.out.println("size = " + size);
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

    private Image createImage(String imageUrl, String caption, Member member) {
        return Image.builder()
                .caption(caption)
                .imageUrl(imageUrl)
                .member(member)
                .build();
    }
}
