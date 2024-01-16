package sample.instagram.service.image;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.image.ImageRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;
import sample.instagram.service.aws.S3UploaderService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static sample.instagram.domain.member.Role.ROLE_USER;

public class ImageServiceTest extends IntegrationTestSupport {

    @Autowired
    private ImageService imageService;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private SubscribeRepositoryJpa subscribeRepositoryJpa;

    @Autowired
    private ImageRepositoryJpa imageRepositoryJpa;

    @MockBean
    private S3UploaderService s3UploaderService;

    @AfterEach
    void tearDown() {
        imageRepositoryJpa.deleteAllInBatch();
        subscribeRepositoryJpa.deleteAllInBatch();
        memberRepositoryJpa.deleteAllInBatch();
    }

    @DisplayName("이미지를 업로드 한다.")
    @Test
    void createImage() throws Exception {
        //given
        String imageUrl = "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png";

        ImageCreateRequest request = ImageCreateRequest.builder()
                .memberId(1L)
                .caption("자기소개")
                .file(mock(MultipartFile.class))
                .build();

        // stubbing
        Mockito.when(s3UploaderService.uploadFileS3(any(), any(String.class)))
                .thenReturn(imageUrl);

        //when
        ImageResponse imageResponse = imageService.createImage(request);

        //then
        assertThat(imageResponse).isNotNull();
        assertThat(imageResponse.getImageUrl()).isEqualTo(imageUrl);
        assertThat(imageResponse.getCaption()).isEqualTo("자기소개");
    }

    @DisplayName("구독자들의 스토리 이미지 첫번째 페이지 리스트를 조회 한다. (page=0, size=3)")
    @Test
    void getStoryImagesWithPage0() throws Exception {
        //given
        Long memberId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 3);

        Member member1 = createMember("testUser1", "유저1");
        Member member2 = createMember("testUser2", "유저2");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Subscribe subscribe1 = Subscribe.create(member1, member2);
        Subscribe subscribe2 = Subscribe.create(member2, member1);
        subscribeRepositoryJpa.saveAll(List.of(subscribe1, subscribe2));

        Image image1 = createImage("사진 소개1", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image2 = createImage("사진 소개2", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image3 = createImage("사진 소개3", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image4 = createImage("사진 소개4", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        imageRepositoryJpa.saveAll(List.of(image1, image2, image3, image4));

        //when
        List<ImageResponse> images = imageService.getStoryImages(memberId, pageRequest);

        //then
        assertThat(images).hasSize(3)
                .extracting("caption", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple("사진 소개1", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png"),
                        tuple("사진 소개2", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png"),
                        tuple("사진 소개3", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png")
                );
    }

    @DisplayName("구독자들의 스토리 이미지 두번째 페이지 리스트를 조회 한다. (page=1, size=3)")
    @Test
    void getStoryImagesWithPage1() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(1, 3);

        Member member1 = createMember("testUser1", "유저1");
        Member member2 = createMember("testUser2", "유저2");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Subscribe subscribe1 = Subscribe.create(member1, member2);
        Subscribe subscribe2 = Subscribe.create(member2, member1);
        subscribeRepositoryJpa.saveAll(List.of(subscribe1, subscribe2));

        Image image1 = createImage("사진 소개1", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image2 = createImage("사진 소개2", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image3 = createImage("사진 소개3", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image4 = createImage("사진 소개4", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        imageRepositoryJpa.saveAll(List.of(image1, image2, image3, image4));

        //when
        List<ImageResponse> images = imageService.getStoryImages(member1.getId(), pageRequest);

        //then
        assertThat(images).hasSize(1)
                .extracting("caption", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple("사진 소개4", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png")
                );
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

    public Image createImage(String caption, String imageUrl,Member member) {
        return Image.builder()
                .caption(caption)
                .imageUrl(imageUrl)
                .member(member)
                .build();
    }
}
