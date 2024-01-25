package sample.instagram.service.like;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.image.ImageRepositoryJpa;
import sample.instagram.domain.like.Like;
import sample.instagram.domain.like.LikeRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.instagram.domain.member.Role.ROLE_USER;

public class LikeServiceTest extends IntegrationTestSupport {

    @Autowired
    private LikeService likeService;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private ImageRepositoryJpa imageRepositoryJpa;

    @Autowired
    private LikeRepositoryJpa likeRepositoryJpa;

    @AfterEach
    void tearDown() {
        likeRepositoryJpa.deleteAllInBatch();
        imageRepositoryJpa.deleteAllInBatch();
        memberRepositoryJpa.deleteAllInBatch();
    }

    @DisplayName("좋아요를 등록한다.")
    @Test
    void createLike() throws Exception {
        //given
        Member member = createMember("member1", "name1");
        Member saveMember = memberRepositoryJpa.save(member);

        Image image = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", saveMember);
        Image saveImage = imageRepositoryJpa.save(image);

        //when
        Like saveLike = likeService.createLike(saveImage.getId(), saveMember.getId());

        //then
        assertThat(saveLike).isNotNull();
        assertThat(saveLike.getImage().getId()).isEqualTo(saveImage.getId());
        assertThat(saveLike.getMember().getId()).isEqualTo(saveMember.getId());

    }

    @DisplayName("좋아요를 취소 한다.")
    @Test
    void deleteLike() throws Exception {
        //given
        Member member = createMember("member1", "name1");
        memberRepositoryJpa.save(member);

        Image image = createImage("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member);
        imageRepositoryJpa.save(image);

        Like like = createLike(image, member);
        likeRepositoryJpa.save(like);

        //when
        likeService.deleteLike(image.getId(), member.getId());

        //then
        Optional<Like> findLike = likeRepositoryJpa.findById(like.getId());
        assertThat(findLike.isPresent()).isFalse();
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
