package sample.instagram.domain.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class ImageRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ImageRepositoryJpa imageRepositoryJpa;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이미지 업로드 한다.")
    @Test
    void createImage() throws Exception {
        //given
        Member member = memberRepository.findOne(1L);
        Image image = createImage("이미지 소개", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member);
        imageRepositoryJpa.save(image);

        //when
        Image saveImage = imageRepositoryJpa.save(image);

        //then
        assertThat(saveImage).isNotNull();
        assertThat(saveImage.getCaption()).isEqualTo("이미지 소개");
        assertThat(saveImage.getImageUrl()).isEqualTo("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png");

    }

    public Image createImage(String caption, String imageUrl, Member member) {
        return Image.builder()
                .caption(caption)
                .imageUrl(imageUrl)
                .member(member)
                .build();
    }
}
