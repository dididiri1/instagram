package sample.instagram.docs.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import sample.instagram.controller.api.member.MemberApiController;
import sample.instagram.docs.RestDocsSupport;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reponse.ImageStoryResponse;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.dto.member.response.MemberProfileResponse;
import sample.instagram.dto.member.response.MemberResponse;
import sample.instagram.dto.subscribe.reponse.SubscribeMemberResponse;
import sample.instagram.service.image.ImageService;
import sample.instagram.service.member.MemberService;
import sample.instagram.service.subscribe.SubscribeService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberApiControllerDocsTest extends RestDocsSupport {

    private final MemberService memberService = mock(MemberService.class);

    private final SubscribeService subscribeService = mock(SubscribeService.class);

    private final ImageService imageService = mock(ImageService.class);

    @Override
    protected Object initController() {
        return new MemberApiController(memberService, subscribeService, imageService);
    }

    @Test
    @DisplayName("신규 유저를 등록하는 API")
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
        this.mockMvc.perform(post("/api/v1/members/new")
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
    @DisplayName("사용자명을 중복 체크하는 API")
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
    @DisplayName("유저 단건 조회 API")
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
    @DisplayName("유저 정보를 수정하는 API")
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

    @Test
    @DisplayName("유저 프로필 정보를 조회 API")
    void getMemberProfile() throws Exception {

        // given
        long pageMemberId = 1L;
        long memberId = 1L;

        List<ImageResponse> images = List.of(
                ImageResponse.builder()
                        .id(1L)
                        .caption("이미지 소개1")
                        .imageUrl("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png")
                        .build(),
                ImageResponse.builder()
                        .id(2L)
                        .caption("이미지 소개2")
                        .imageUrl("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png")
                        .build()
        );

        given(memberService.getMemberProfile(any(Long.class), any(Long.class)))
                .willReturn(MemberProfileResponse.builder()
                        .pageOwnerState(true)
                        .imageCount(2)
                        .subscribeState(false)
                        .subscribeCount(0)
                        .name("홍길동")
                        .images(images)
                        .build());

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

                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터")
                        ).andWithPrefix("data.",
                                fieldWithPath("pageOwnerState").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 주인 여부"),
                                fieldWithPath("imageCount").type(JsonFieldType.NUMBER)
                                        .description("이미지 갯수"),
                                fieldWithPath("subscribeState").type(JsonFieldType.BOOLEAN)
                                        .description("구독 상태"),
                                fieldWithPath("subscribeCount").type(JsonFieldType.NUMBER)
                                        .description("구독 갯수"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("회원 이름"),
                                fieldWithPath("images[].id").type(JsonFieldType.NUMBER)
                                        .description("이미지 ID"),
                                fieldWithPath("images[].caption").type(JsonFieldType.STRING)
                                        .description("이미지 소개"),
                                fieldWithPath("images[].imageUrl").type(JsonFieldType.STRING)
                                        .description("이미지 URL")

                        )
                ));
    }

    @Test
    @DisplayName("구독을 조회 API")
    void getSubscribes() throws Exception {

        // given
        long pageMemberId = 1L;
        long memberId = 1L;

        List<SubscribeMemberResponse> result = List.of(
                SubscribeMemberResponse.builder()
                        .memberId(1L)
                        .username("member1")
                        .subscribeState(0)
                        .equalMemberState(1)
                        .build(),
                SubscribeMemberResponse.builder()
                        .memberId(2L)
                        .username("member2")
                        .subscribeState(1)
                        .equalMemberState(0)
                        .build()
        );

        given(subscribeService.getSubscribes(any(Long.class), any(Long.class)))
                .willReturn(result);

        // expected
        this.mockMvc.perform(get("/api/v1/members/{pageMemberId}/subscribe/{memberId}", pageMemberId, memberId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("member-subscribe",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("pageMemberId").description("페이지 회원 ID"),
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("데이터")
                        ).andWithPrefix("data",
                                fieldWithPath("[].memberId").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("[].username").type(JsonFieldType.STRING)
                                        .description("회원 이름"),
                                fieldWithPath("[].subscribeState").type(JsonFieldType.NUMBER)
                                        .description("구독 상태"),
                                fieldWithPath("[].equalMemberState").type(JsonFieldType.NUMBER)
                                        .description("동일 유저 상태")

                        )
                ));
    }

    @DisplayName("스토리를 조회 API")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getStory() throws Exception {
        // given
        Long memberId = 1L;
        int page = 0;
        int size = 3;

        List<ImageStoryResponse> result = List.of(
                ImageStoryResponse.builder()
                        .id(1L)
                        .caption("이미지 소개1")
                        .imageUrl("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png")
                        .username("member1")
                        .likeState(true)
                        .likeCount(1)
                        .build(),
                ImageStoryResponse.builder()
                        .id(2L)
                        .caption("이미지 소개2")
                        .imageUrl("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png")
                        .username("member2")
                        .likeState(false)
                        .likeCount(0)
                        .build()
        );

        given(imageService.getStory(any(Long.class), any(Pageable.class)))
                .willReturn(result);

        // when // then
        this.mockMvc.perform(get("/api/v1/members/{id}/story", memberId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-all-story",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("회원 ID")
                        ),
                        requestParameters (
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("데이터")
                        ).andWithPrefix("data[].",
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("이미지 ID"),
                                fieldWithPath("caption").type(JsonFieldType.STRING)
                                        .description("이미지 설명"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING)
                                        .description("이미지 주소"),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .description("회원 이름"),
                                fieldWithPath("likeState").type(JsonFieldType.BOOLEAN)
                                        .description("좋아요 상태"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER)
                                        .description("좋아요 갯수")
                        )
                ));
    }

}


