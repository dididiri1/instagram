package sample.instagram.api.service.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.service.aws.S3UploaderService;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;
import sample.instagram.service.image.ImageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class ImageServiceTest extends IntegrationTestSupport {

    @MockBean
    private S3UploaderService s3UploaderService;

    @Autowired
    private ImageService imageService;


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
}
