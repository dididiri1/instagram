package sample.instagram.api.controller.subscribe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import sample.instagram.ControllerTestSupport;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubscribeApiControllerTest extends ControllerTestSupport {

    @DisplayName("구독을 등록한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createSubscribe() throws Exception {
        //given
        Long fromMemberId = 1L;
        Long toMemberId = 2L;

        //when //then
        mockMvc.perform(post("/api/subscribe/{fromMemberId}/{toMemberId}", fromMemberId, toMemberId)
                        .contentType(APPLICATION_JSON)
                        .with(csrf())

                )
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
