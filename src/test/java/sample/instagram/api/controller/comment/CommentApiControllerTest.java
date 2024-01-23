package sample.instagram.api.controller.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import sample.instagram.ControllerTestSupport;
import sample.instagram.dto.comment.request.CommentRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @DisplayName("댓글 등록시 회원 ID는 필수값이다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createCommentWithoutMemberId() throws Exception {
        //given
        CommentRequest request = CommentRequest.builder()
                .imageId(1L)
                .content("댓글 내용입니다.")
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/comment")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @DisplayName("댓글 등록시 이미지 ID는 필수값이다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createCommentWithoutImageId() throws Exception {
        //given
        CommentRequest request = CommentRequest.builder()
                .memberId(1L)
                .content("댓글 내용입니다.")
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/comment")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @DisplayName("댓글 등록시 내용은 필수값이다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createCommentWithoutContent() throws Exception {
        //given
        CommentRequest request = CommentRequest.builder()
                .memberId(1L)
                .imageId(1L)
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/comment")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }
}
