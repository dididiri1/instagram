package sample.instagram.docs.subscribe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import sample.instagram.controller.api.subscribe.SubscribeApiController;
import sample.instagram.docs.RestDocsSupport;
import sample.instagram.dto.subscribe.response.SubscribeResponse;
import sample.instagram.service.subscribe.SubscribeService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubscribeApiControllerDocsTest extends RestDocsSupport {

    private final SubscribeService subscribeService = mock(SubscribeService.class);

    @Override
    protected Object initController() {
        return new SubscribeApiController(subscribeService);
    }

    @Test
    @DisplayName("구독을 등록하는 API")
    void createSubscribe() throws Exception {

        // given
        Long fromMemberId = 1L;
        Long toMemberId = 2L;

        given(subscribeService.createSubscribe(any(Long.class),any(Long.class)))
                .willReturn(SubscribeResponse.builder()
                        .id(1L)
                        .fromMemberId(1L)
                        .toMemberId(2L)
                        .build());

        // expected
        this.mockMvc.perform(post("/api/v1/subscribe/{fromMemberId}/{toMemberId}", fromMemberId, toMemberId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("subscribe-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("fromMemberId").description("구독자 ID"),
                                parameterWithName("toMemberId").description("발행자 ID")
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
                                        .description("구독 ID"),
                                fieldWithPath("fromMemberId").type(JsonFieldType.NUMBER)
                                        .description("구독자 ID"),
                                fieldWithPath("toMemberId").type(JsonFieldType.NUMBER)
                                        .description("발행자 ID")
                        )
                ));
    }

    @Test
    @DisplayName("구독을 취소하는 API")
    void deleteSubscribe() throws Exception {
        // given
        Long fromMemberId = 1L;
        Long toMemberId = 2L;

        // when then
        this.mockMvc.perform(delete("/api/v1/subscribe/{fromMemberId}/{toMemberId}", fromMemberId, toMemberId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("subscribe-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("fromMemberId").description("구독자 ID"),
                                parameterWithName("toMemberId").description("발행자 ID")
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
}
