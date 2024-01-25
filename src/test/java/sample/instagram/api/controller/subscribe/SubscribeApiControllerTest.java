package sample.instagram.api.controller.subscribe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import sample.instagram.ControllerTestSupport;
import sample.instagram.dto.DataResponse;
import sample.instagram.dto.ResultStatus;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        mockMvc.perform(post("/api/v1/subscribe/{fromMemberId}/{toMemberId}", fromMemberId, toMemberId)
                        .contentType(APPLICATION_JSON)
                        .with(csrf())

                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());;
    }

    @DisplayName("구독을 취소한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void deleteSubscribe() throws Exception {
        //given
        Long fromMemberId = 1L;
        Long toMemberId = 2L;

        //when then
        mockMvc.perform(delete("/api/v1/subscribe/{fromMemberId}/{toMemberId}", fromMemberId, toMemberId)
                        .contentType(APPLICATION_JSON)
                        .with(csrf())

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
