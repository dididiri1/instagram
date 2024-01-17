package sample.instagram.domain.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.image.ImageRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.Subscribe;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.instagram.domain.member.Role.ROLE_USER;

@Transactional
public class LikeRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private LikeRepositoryJpa likeRepositoryJpa;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private ImageRepositoryJpa imageRepositoryJpa;


    @DisplayName("좋아요를 등록한다.")
    @Test
    void createLike() throws Exception {
        //given
        Member member = createMember("testUser","test@naver.com", "유저");
        Member saveMember = memberRepositoryJpa.save(member);

        Image image = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", saveMember);
        imageRepositoryJpa.save(image);
        Like like = createLike(image, member);

        //when
        Like saveLike = likeRepositoryJpa.save(like);

        //then
        assertThat(saveLike).isNotNull();
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

    private Image createImage(String imageUrl, Member member) {
        return Image.builder()
                .caption("이미지 캡션")
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
