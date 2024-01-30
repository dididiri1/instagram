package sample.instagram.docs.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import sample.instagram.controller.api.image.ImageApiController;
import sample.instagram.docs.RestDocsSupport;
import sample.instagram.dto.image.reponse.ImageCreateResponse;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;
import sample.instagram.service.image.ImageService;
import sample.instagram.dto.image.reponse.ImagePopularResponse;
import sample.instagram.service.like.LikeService;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageApiControllerDocsTest extends RestDocsSupport {

    private final ImageService imageService = mock(ImageService.class);

    private final LikeService likeService = mock(LikeService.class);

    @Override
    protected Object initController() {
        return new ImageApiController(imageService, likeService);
    }

    @Test
    @DisplayName("이미지를 등록하는 API")
    void createImage() throws Exception {

        // given
        ImageCreateRequest request = ImageCreateRequest.builder()
                .memberId(1L)
                .caption("이미지 설명")
                .file(createMockMultipartFile("test.jpg"))
                .build();

        given(imageService.createImage(any(ImageCreateRequest.class)))
                .willReturn(ImageCreateResponse.builder()
                        .id(1L)
                        .caption("이미지 소개")
                        .imageUrl("https://kangmin-s3-bucket.s3.ap-northeast-2.amazonaws.com/storage/test/sample.jpg")
                        .build());

        // expected
        this.mockMvc.perform(multipart("/api/v1/images")
                        .file("file", request.getFile().getBytes())
                        .param("memberId", request.getMemberId().toString())
                        .param("caption", request.getCaption())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("image-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts (
                                partWithName("file").description("업로드할 이미지")
                        ),
                        requestParameters (
                                parameterWithName("memberId").description("회원 ID"),
                                parameterWithName("caption").description("이미지 설명")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터")
                        ).andWithPrefix("data.",
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("이미지 ID"),
                                fieldWithPath("caption").type(JsonFieldType.STRING)
                                        .description("이미지 설명"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING)
                                        .description("이미지 주소")
                        )
                ));
    }

    @Test
    @DisplayName("좋아요를 등록하는 API")
    void createSubscribe() throws Exception {

        // given
        Long imageId = 1L;
        Long memberId = 2L;

        // expected
        this.mockMvc.perform(post("/api/v1/images/{imageId}/likes/{memberId}", imageId, memberId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("image-like-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("imageId").description("이미지 ID"),
                                parameterWithName("memberId").description("회원 ID")
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
    @DisplayName("좋아요를 취소하는 API")
    void deleteSubscribe() throws Exception {

        // given
        Long imageId = 1L;
        Long memberId = 1L;

        // expected
        this.mockMvc.perform(delete("/api/v1/images/{imageId}/likes/{memberId}", imageId, memberId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("image-like-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("imageId").description("이미지 ID"),
                                parameterWithName("memberId").description("회원 ID")
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
    @DisplayName("인기 이미지들을 조회하는 API")
    void getPopularImages() throws Exception {
        // given
        given(imageService.getPopularImages())
                .willReturn(List.of(
                        ImagePopularResponse.builder()
                                .memberId(1L)
                                .imageUrl("https://kangmin-s3-bucket.s3.ap-northeast-2.amazonaws.com/storage/test/sample.jpg")
                                .likeCount(3)
                                .build(),
                        ImagePopularResponse.builder()
                                .memberId(2L)
                                .imageUrl("https://kangmin-s3-bucket.s3.ap-northeast-2.amazonaws.com/storage/test/sample.jpg")
                                .likeCount(0)
                                .build()
                ));

        // expected
        this.mockMvc.perform(get("/api/v1/images/popular")
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("image-all-popular",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("데이터")
                        ).andWithPrefix("data[].",
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING)
                                        .description("이미지 주소"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER)
                                        .description("좋아요 갯수")
                        )
                ));
    }

    private MockMultipartFile createMockMultipartFile(String fileName) throws IOException {
        return new MockMultipartFile("file", fileName, MediaType.IMAGE_JPEG_VALUE, "test".getBytes());
    }
}
