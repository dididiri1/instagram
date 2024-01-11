package sample.instagram.api.service.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.api.service.aws.S3UploaderService;
import sample.instagram.domain.member.Member;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static sample.instagram.domain.member.Role.ROLE_USER;

public class ImageServiceTest extends IntegrationTestSupport {

    @MockBean
    private S3UploaderService s3UploaderService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @DisplayName("이미지를 업로드 한다.")
    @Test
    void createImage() throws Exception {
        //given
        ImageCreateRequest request = ImageCreateRequest.builder()
                .memberId(1L)
                .caption("자기소개")
                .file(mock(MultipartFile.class))
                .build();

        // stubbing
        Mockito.when(s3UploaderService.uploadFileS3(any(), any(String.class)))
                .thenReturn("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png");

        //when
        ImageResponse imageResponse = imageService.createImage(request);

        //then
        assertThat(imageResponse).isNotNull();
        assertThat(imageResponse.getImageUrl()).isEqualTo("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/storage/story/test.jpg");
        assertThat(imageResponse.getCaption()).isEqualTo("자기소개");
    }

    private Member createMember(String username, String password, String email, String name) {
        return Member.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .email(email)
                .name(name)
                .role(ROLE_USER)
                .build();
    }
}
