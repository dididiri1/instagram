package sample.instagram.api.controller.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import sample.instagram.ControllerTestSupport;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberApiControllerTest extends ControllerTestSupport {

    @DisplayName("신규 회원을 등록한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createMember() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .username("testUser")
                .password("1234")
                .email("test@naver.com")
                .name("홍길동")
                .build();

        //when //then
        mockMvc.perform(post("/api/members")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("신규 회원을 등록시 유저명은 필수값이다.")
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void createMemberWithoutUsername() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .password("1234")
                .email("test@naver.com")
                .name("홍길동")
                .build();

        //when //then
        mockMvc.perform(post("/api/members")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));
    }

    @DisplayName("회원 정보를 조회 한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getMember() throws Exception {
        //given
        long memberId = 1L;

        //when //then
        mockMvc.perform(get("/api/members/{id}", memberId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원 정보를 수정 한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void updateMember() throws Exception {
        //given
        long memberId = 1L;

        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .password("1234")
                .email("test@gmail.com")
                .name("김구라")
                .build();

        //when //then
        mockMvc.perform(patch("/api/members/{id}", memberId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
