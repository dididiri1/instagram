package sample.instagram.service.image;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.image.ImageRepositoryJpa;
import sample.instagram.domain.like.Like;
import sample.instagram.domain.like.LikeRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reponse.ImageStoryResponse;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;
import sample.instagram.service.aws.S3UploaderService;
import sample.instagram.service.image.reponse.ImagePopularResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static sample.instagram.domain.image.QImage.image;
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

    @Autowired
    private LikeRepositoryJpa likeRepositoryJpa;

    @MockBean
    private S3UploaderService s3UploaderService;

    @AfterEach
    void tearDown() {
        likeRepositoryJpa.deleteAllInBatch();
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

    @DisplayName("스토리 정보 첫번째 페이지를 조회한다.")
    @Test
    void getStoryWithPage0AndSize3() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3);

        Member member1 = createMember("member1", "name1");
        Member member2 = createMember("member2", "name2");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Subscribe subscribe1 = Subscribe.create(member1, member2);
        Subscribe subscribe2 = Subscribe.create(member2, member1);
        subscribeRepositoryJpa.saveAll(List.of(subscribe1, subscribe2));

        Image image1 = createImage("caption1", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image2 = createImage("caption2", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image3 = createImage("caption3", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image4 = createImage("caption4", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        imageRepositoryJpa.saveAll(List.of(image1, image2, image3, image4));

        Like like1 = createLike(image3, member1);
        Like like2 = createLike(image4, member1);
        likeRepositoryJpa.saveAll(List.of(like1, like2));

        //when
        List<ImageStoryResponse> images = imageService.getStory(member1.getId(), pageRequest);

        //then
        assertThat(images).hasSize(3)
                .extracting("caption", "imageUrl", "username", "likeState", "likeCount")
                .containsExactlyInAnyOrder(
                        tuple("caption4", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", "member2", true, 1),
                        tuple("caption3", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", "member2", true, 1),
                        tuple("caption2", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", "member2",  false, 0)
                );
    }

    @DisplayName("스토리 정보 두번째 페이지를 조회한다.")
    @Test
    void getStoryWithPage1AndSize3() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(1, 3);

        Member member1 = createMember("member1", "name1");
        Member member2 = createMember("member2", "name2");
        memberRepositoryJpa.saveAll(List.of(member1, member2));

        Subscribe subscribe1 = Subscribe.create(member1, member2);
        Subscribe subscribe2 = Subscribe.create(member2, member1);
        subscribeRepositoryJpa.saveAll(List.of(subscribe1, subscribe2));

        Image image1 = createImage("caption1", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image2 = createImage("caption2", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image3 = createImage("caption3", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        Image image4 = createImage("caption4", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        imageRepositoryJpa.saveAll(List.of(image1, image2, image3, image4));

        Like like1 = createLike(image3, member1);
        Like like2 = createLike(image4, member1);
        likeRepositoryJpa.saveAll(List.of(like1, like2));

        //when
        List<ImageStoryResponse> images = imageService.getStory(member1.getId(), pageRequest);

        //then
        assertThat(images).hasSize(1)
                .extracting("caption", "imageUrl", "username", "likeState", "likeCount")
                .containsExactlyInAnyOrder(
                        tuple("caption1", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", "member2", false , 0)
                );
    }

    @DisplayName("인기 이미지 조회한다. 리스트는 좋아요 순으로 내림차순 정렬되어야 한다.")
    @Test
    void getPoplar() throws Exception {
        //given
        Member member1 = createMember("member1", "name1");
        Member member2 = createMember("member2", "name2");
        Member member3 = createMember("member3", "name3");
        Member member4 = createMember("member4", "name4");
        memberRepositoryJpa.saveAll(List.of(member1, member2, member3, member4));

        Subscribe subscribe1 = Subscribe.create(member1, member2);
        Subscribe subscribe2 = Subscribe.create(member2, member1);
        subscribeRepositoryJpa.saveAll(List.of(subscribe1, subscribe2));

        Image image1 = createImage("caption1", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member1);
        Image image2 = createImage("caption2", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", member2);
        imageRepositoryJpa.saveAll(List.of(image1, image2));

        Like like1 = createLike(image1, member2);
        Like like2 = createLike(image2, member1);
        Like like3 = createLike(image2, member3);
        Like like4 = createLike(image2, member4);
        likeRepositoryJpa.saveAll(List.of(like1, like2, like3, like4));

        //when
        List<ImagePopularResponse> images = imageService.getPopularImages();

        //then
        assertThat(images).hasSize(2)
                .extracting("caption", "imageUrl", "likeCount")
                .containsExactly(
                        tuple("caption2", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", 3),
                        tuple("caption1", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png", 1)
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

    private Image createImage(String caption, String imageUrl, Member member) {
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
