package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.productgroupimage.ProductGroupImageApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.dto.command.RegisterProductGroupImagesApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.dto.command.UpdateProductGroupImagesApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.mapper.ProductGroupImageCommandApiMapper;
import com.ryuqq.setof.application.productgroupimage.port.in.command.RegisterProductGroupImagesUseCase;
import com.ryuqq.setof.application.productgroupimage.port.in.command.UpdateProductGroupImagesUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * ProductGroupImageCommandController REST Docs 테스트.
 *
 * <p>상품 그룹 이미지 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductGroupImageCommandController REST Docs 테스트")
@WebMvcTest(ProductGroupImageCommandController.class)
@WithMockUser
class ProductGroupImageCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterProductGroupImagesUseCase registerUseCase;

    @MockBean private UpdateProductGroupImagesUseCase updateUseCase;

    @MockBean private ProductGroupImageCommandApiMapper mapper;

    private static final Long PRODUCT_GROUP_ID =
            ProductGroupImageApiFixtures.DEFAULT_PRODUCT_GROUP_ID;

    @Nested
    @DisplayName("이미지 등록 API")
    class RegisterProductGroupImagesTest {

        @Test
        @DisplayName("이미지 등록 성공")
        void registerProductGroupImages_Success() throws Exception {
            // given
            RegisterProductGroupImagesApiRequest request =
                    ProductGroupImageApiFixtures.registerRequest();
            willDoNothing().given(registerUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            post(
                                            "/api/v2/admin/product-groups/{productGroupId}/images",
                                            PRODUCT_GROUP_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("productGroupId")
                                                    .description("상품 그룹 ID")),
                                    requestFields(
                                            fieldWithPath("images")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("등록할 이미지 목록 [필수]"),
                                            fieldWithPath("images[].imageType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "이미지 유형 [필수] +\n"
                                                                    + "THUMBNAIL: 썸네일, DETAIL: 상세"),
                                            fieldWithPath("images[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL [필수]"),
                                            fieldWithPath("images[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NULL)
                                                    .description("빈 응답")
                                                    .optional(),
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 추적 ID")
                                                    .optional())));
        }
    }

    @Nested
    @DisplayName("이미지 수정 API")
    class UpdateProductGroupImagesTest {

        @Test
        @DisplayName("이미지 전체 교체 성공")
        void updateProductGroupImages_Success() throws Exception {
            // given
            UpdateProductGroupImagesApiRequest request =
                    ProductGroupImageApiFixtures.updateRequest();
            willDoNothing().given(updateUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put(
                                            "/api/v2/admin/product-groups/{productGroupId}/images",
                                            PRODUCT_GROUP_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("productGroupId")
                                                    .description("상품 그룹 ID")),
                                    requestFields(
                                            fieldWithPath("images")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("수정할 이미지 목록 [필수]"),
                                            fieldWithPath("images[].imageType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "이미지 유형 [필수] +\n"
                                                                    + "THUMBNAIL: 썸네일, DETAIL: 상세"),
                                            fieldWithPath("images[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL [필수]"),
                                            fieldWithPath("images[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]"))));
        }
    }
}
