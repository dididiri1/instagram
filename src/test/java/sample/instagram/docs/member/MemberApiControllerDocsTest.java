package sample.instagram.docs.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import sample.instagram.controller.api.member.MemberApiController;
import sample.instagram.domain.member.Member;
import sample.instagram.dto.member.response.MemberProfileResponse;
import sample.instagram.service.member.MemberService;
import sample.instagram.docs.RestDocsSupport;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.dto.member.response.MemberResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberApiControllerDocsTest extends RestDocsSupport {

    private final MemberService memberService = mock(MemberService.class);

    @Override
    protected Object initController() {
        return new MemberApiController(memberService);
    }

    @Test
    @DisplayName("신규 회원 등록")
    void createMember() throws Exception {

        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .username("kangmin")
                .password("1234")
                .name("김강민")
                .email("kmkim6368@gmail.com")
                .build();

        given(memberService.createMember(any(MemberCreateRequest.class)))
                .willReturn(MemberResponse.builder()
                        .id(1L)
                        .username("kangmin")
                        .email("kmkim6368@gmail.com")
                        .name("김강민")
                        .build());

        // expected
        this.mockMvc.perform(post("/api/v1/members")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("member-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("사용자명"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("비밀번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("이름")
                        ),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터")
                        ).andWithPrefix("data.",
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .description("사용자명"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름")
                        )
                ));
    }

    @Test
    @DisplayName("유저명 중복 체크")
    void checkUsername() throws Exception {

        // given
        String username = "kangmin";

        // expected
        this.mockMvc.perform(get("/api/v1/members/checkUsername/{username}", username)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-check-username",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("username").description("사용자명")
                        ),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("데이터")
                        )
                ));
    }

    @Test
    @DisplayName("회원 단건 조회")
    void getMember() throws Exception {

        // given
        long memberId = 1L;

        given(memberService.getMember(memberId))
                .willReturn(MemberResponse.builder()
                        .id(1L)
                        .username("kangmin")
                        .email("kmkim6368@gmail.com")
                        .name("김강민")
                        .build());

        // expected
        this.mockMvc.perform(get("/api/v1/members/{id}", memberId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-one",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("회원 ID")
                        ),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터")
                        ).andWithPrefix("data.",
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .description("사용자명"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름")
                        )
                ));
    }

    @Test
    @DisplayName("회원 수정")
    void updateMember() throws Exception {

        // given
        long memberId = 1L;

        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .password("1234")
                .email("test@gmail.com")
                .name("김구라")
                .build();

        given(memberService.updateMember(any(Long.class), any(MemberUpdateRequest.class)))
                .willReturn(MemberResponse.builder()
                        .id(memberId)
                        .username("testUser")
                        .email("test@gmail.com")
                        .name("김구라")
                        .build());

        // expected
        this.mockMvc.perform(patch("/api/v1/members/{id}", memberId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("회원 ID")
                        ),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("이름")
                        ),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터")
                        ).andWithPrefix("data.",
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .description("유저명"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름")
                        )
                ));
    }

   /* @Test
    @DisplayName("회원 프로필 정보를 조회 한다.")
    void getMemberProfile() throws Exception {

        // given
        long pageMemberId = 1L;
        long memberId = 1L;

        MemberProfileResponse response = new MemberProfileResponse();

        when(memberService.getMemberProfile(pageMemberId, memberId)).thenReturn(response);

        // expected
        this.mockMvc.perform(get("/api/v1/members/{pageMemberId}/profile/{memberId}", pageMemberId, memberId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-get-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("pageMemberId").description("페이지 회원 ID"),
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.pageOwnerState").description("페이지 주인 여부"),
                                fieldWithPath("data.imageCount").description("이미지 갯수"),
                                fieldWithPath("data.subscribeState").description("구독 상태"),
                                fieldWithPath("data.subscribeCount").description("구독 갯수"),
                                fieldWithPath("data.member").description("멤버 정보").optional(), // Optional로 표시
                                fieldWithPath("data.member.createDateTime").description("멤버 생성일시"),
                                fieldWithPath("data.member.modifiedDateTime").description("멤버 수정일시"),
                                fieldWithPath("data.member.id").description("멤버 ID"),
                                fieldWithPath("data.member.username").description("멤버 사용자명"),
                                fieldWithPath("data.member.email").description("멤버 이메일"),
                                fieldWithPath("data.member.name").description("멤버 이름"),
                                fieldWithPath("data.member.images[0].createDateTime").description("이미지 생성일시"),
                                fieldWithPath("data.member.images[0].modifiedDateTime").description("이미지 수정일시"),
                                fieldWithPath("data.member.images[0].id").description("이미지 ID"),
                                fieldWithPath("data.member.images[0].caption").description("이미지 캡션"),
                                fieldWithPath("data.member.images[0].imageUrl").description("이미지 URL")
                        )
                ));
    }*/
}


