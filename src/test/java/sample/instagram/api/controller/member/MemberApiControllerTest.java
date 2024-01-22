package sample.instagram.api.controller.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import sample.instagram.ControllerTestSupport;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.service.member.MemberSubscribeResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
        mockMvc.perform(post("/api/v1/members/new")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("신규 회원을 등록시 유저명은 필수값이다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createMemberWithoutUsername() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .password("1234")
                .email("test@naver.com")
                .name("홍길동")
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/members/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @DisplayName("신규 회원을 등록시 이메일은 필수값이다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createMemberWithoutEmail() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .password("1234")
                .username("testUser")
                .name("홍길동")
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/members/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @DisplayName("신규 회원을 등록시 이름은 필수값이다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createMemberWithoutName() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .password("1234")
                .username("testUser")
                .email("test@naver.com")
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/members/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @DisplayName("회원 정보를 조회 한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getMember() throws Exception {
        //given
        long memberId = 1L;

        //when //then
        mockMvc.perform(get("/api/v1/members/{id}", memberId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());
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
        mockMvc.perform(patch("/api/v1/members/{id}", memberId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());;
    }

    @DisplayName("회원 프로필 정보를 조회 한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getMemberProfile() throws Exception {
        //given
        Long pageMemberId = 1L;
        Long memberId = 1L;

        //when //then
        mockMvc.perform(get("/api/v1/members/{pageMemberId}/profile/{memberId}", pageMemberId, memberId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("구독을 조회 한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getSubscribes() throws Exception {
        //given
        List<MemberSubscribeResponse> result = List.of();
        when(memberService.getMemberSubscribes(any(Long.class), any(Long.class))).thenReturn(result);

        //when //then
        mockMvc.perform(get("/api/v1/members/{pageMemberId}/subscribe/{memberId}", 1L, 1L)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("스토리 정보를 조회한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getStories() throws Exception {
        // given
        Long memberId = 1L;
        int page = 0;
        int size = 3;

        // when // then
        mockMvc.perform(get("/api/v1/members/{id}/story", memberId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isArray());

    }

}
