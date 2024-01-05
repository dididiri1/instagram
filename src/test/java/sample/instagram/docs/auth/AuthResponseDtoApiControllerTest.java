package sample.instagram.docs.auth;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sample.instagram.api.controller.MemberApiController;
import sample.instagram.api.service.MemberService;
import sample.instagram.docs.RestDocsSupport;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class AuthResponseDtoApiControllerTest extends RestDocsSupport {

    private final MemberService memberService = mock(MemberService.class);

    @Override
    protected Object initController() {
        return new MemberApiController(memberService);
    }

    @Test
    public void testLoginApi() throws Exception {
        String username = "kangmin";
        String password = "12345";

        // MockMvc를 사용하여 API 호출
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty())
                .andDo(document("login",
                        requestFields(
                                fieldWithPath("username").description("유저명"),
                                fieldWithPath("password").description("패스워드")
                        ),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터")
                        ).andWithPrefix("data.",
                                fieldWithPath("returnUrl").type(JsonFieldType.STRING)
                                        .description("리다이렉트 URL")
                        )
                ));
    }
}
