package sample.instagram.api.controller.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import sample.instagram.ControllerTestSupport;
import sample.instagram.dto.comment.request.CommentRequest;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentApiControllerTest extends ControllerTestSupport {

    @DisplayName("댓글을 등록한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createComment() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .memberId(1L)
                .imageId(1L)
                .content("댓글 내용입니다.")
                .build();

        // when // then
        mockMvc.perform(post("/api/v1/comment")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
