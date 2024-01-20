package sample.instagram.api.controller.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import sample.instagram.ControllerTestSupport;
import sample.instagram.dto.DataResponse;
import sample.instagram.dto.ResultStatus;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;
import sample.instagram.service.image.reponse.ImagePopularResponse;
import sample.instagram.service.like.LikeService;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @DisplayName("좋아요를 등록한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createLike() throws Exception {
        // given
        Long imageId = 1L;
        Long memberId = 1L;

        // when // then
        mockMvc.perform(post("/api/v1/images/{imageId}/likes/{memberId}", imageId, memberId)
                        .contentType(APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());;
    }

    @DisplayName("좋아요를 취소한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void deleteSubscribe() throws Exception {
        //given
        Long imageId = 1L;
        Long memberId = 1L;

        // when // then
        mockMvc.perform(delete("/api/v1/images/{imageId}/likes/{memberId}", imageId, memberId)
                        .contentType(APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists());
    }


    @DisplayName("인기 이미지를 조회 한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getPopularImages() throws Exception {
        // given
        List<ImagePopularResponse> result = List.of();
        when(imageService.getPopularImages()).thenReturn(result);

        // when // then
        mockMvc.perform(get("/api/v1/images/popular")
                        .contentType(APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isArray());;
    }

    private MockMultipartFile createMockMultipartFile(String fileName) throws IOException {
        return new MockMultipartFile("file", fileName, MediaType.IMAGE_JPEG_VALUE, "test".getBytes());
    }

}
