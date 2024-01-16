package sample.instagram.docs.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import sample.instagram.controller.api.image.ImageApiController;
import sample.instagram.docs.RestDocsSupport;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reponse.ImageStoryResponse;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;
import sample.instagram.service.image.ImageService;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageApiControllerDocsTest extends RestDocsSupport {

    private final ImageService imageService = mock(ImageService.class);

    @Override
    protected Object initController() {
        return new ImageApiController(imageService);
    }

    @Test
    @DisplayName("이미지 등록")
    void createImage() throws Exception {

        // given
        ImageCreateRequest request = ImageCreateRequest.builder()
                .memberId(1L)
                .caption("이미지 설명")
                .file(createMockMultipartFile("test.jpg"))
                .build();

        given(imageService.createImage(any(ImageCreateRequest.class)))
                .willReturn(ImageResponse.builder()
                        .id(1L)
                        .caption("사진설명")
                        .imageUrl("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png")
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

    @DisplayName("스토리 이미지 조회한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getStoryImages() throws Exception {
        // given
        Long memberId = 1L;
        int page = 0;
        int size = 3;

        List<ImageStoryResponse> result = getImages();

        given(imageService.getStoryImages(any(Long.class), any(Pageable.class)))
                .willReturn(result);

        // when // then
        this.mockMvc.perform(get("/api/v1/images/{id}", memberId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("image-get-story",
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
                                        .description("회원 이름")
                        )
                ));


    }

    private static List<ImageStoryResponse> getImages() {
        return List.of(
                ImageStoryResponse.builder()
                        .id(1L)
                        .caption("이미지 소개1")
                        .imageUrl("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png")
                        .username("testUser1")
                        .build(),
                ImageStoryResponse.builder()
                        .id(2L)
                        .caption("이미지 소개2")
                        .imageUrl("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png")
                        .username("testUser2")
                        .build()
        );
    }

    private MockMultipartFile createMockMultipartFile(String fileName) throws IOException {
        return new MockMultipartFile("file", fileName, MediaType.IMAGE_JPEG_VALUE, "test".getBytes());
    }
}
