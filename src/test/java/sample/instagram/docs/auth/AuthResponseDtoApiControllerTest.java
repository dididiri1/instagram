package sample.instagram.docs.auth;

import org.junit.jupiter.api.Test;
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
                                fieldWithPath("username").description("The username for authentication"),
                                fieldWithPath("password").description("The password for authentication")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("JWT access token")
                        )
                ));
    }
}
