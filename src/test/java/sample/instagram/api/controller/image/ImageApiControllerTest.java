package sample.instagram.api.controller.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import sample.instagram.ControllerTestSupport;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;

import java.io.IOException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageApiControllerTest extends ControllerTestSupport {

    @DisplayName("이미지를 등록한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createImage() throws Exception {
        // given
        ImageCreateRequest request = ImageCreateRequest.builder()
                .memberId(1L)
                .caption("사진설명")
                .file(createMockMultipartFile("test.jpg"))
                .build();

        // when // then
        mockMvc.perform(multipart("/api/v1/images")
                        .file("file", request.getFile().getBytes())
                        .param("memberId", request.getMemberId().toString())
                        .param("caption", request.getCaption())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());

    }

    private MockMultipartFile createMockMultipartFile(String fileName) throws IOException {
        return new MockMultipartFile("file", fileName, MediaType.IMAGE_JPEG_VALUE, "test".getBytes());
    }

}
