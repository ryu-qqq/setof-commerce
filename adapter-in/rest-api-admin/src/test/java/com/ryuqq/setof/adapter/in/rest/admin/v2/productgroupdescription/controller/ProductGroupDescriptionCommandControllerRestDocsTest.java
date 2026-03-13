package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.productgroupdescription.ProductGroupDescriptionApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.dto.command.RegisterProductGroupDescriptionApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.dto.command.UpdateProductGroupDescriptionApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.mapper.ProductGroupDescriptionCommandApiMapper;
import com.ryuqq.setof.application.productdescription.port.in.command.RegisterProductGroupDescriptionUseCase;
import com.ryuqq.setof.application.productdescription.port.in.command.UpdateProductGroupDescriptionUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * ProductGroupDescriptionCommandController REST Docs 테스트.
 *
 * <p>상품 그룹 상세 설명 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductGroupDescriptionCommandController REST Docs 테스트")
@WebMvcTest(ProductGroupDescriptionCommandController.class)
@WithMockUser
class ProductGroupDescriptionCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterProductGroupDescriptionUseCase registerUseCase;

    @MockBean private UpdateProductGroupDescriptionUseCase updateUseCase;

    @MockBean private ProductGroupDescriptionCommandApiMapper mapper;

    private static final Long PRODUCT_GROUP_ID =
            ProductGroupDescriptionApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
    private static final Long CREATED_DESCRIPTION_ID = 200L;

    @Nested
    @DisplayName("상세 설명 등록 API")
    class RegisterProductGroupDescriptionTest {

        @Test
        @DisplayName("상세 설명 등록 성공")
        void registerProductGroupDescription_Success() throws Exception {
            // given
            RegisterProductGroupDescriptionApiRequest request =
                    ProductGroupDescriptionApiFixtures.registerRequest();
            given(registerUseCase.execute(any())).willReturn(CREATED_DESCRIPTION_ID);

            // when & then
            mockMvc.perform(
                            post(
                                            "/api/v2/admin/product-groups/{productGroupId}/description",
                                            PRODUCT_GROUP_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data").value(CREATED_DESCRIPTION_ID))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("productGroupId")
                                                    .description("상품 그룹 ID")),
                                    requestFields(
                                            fieldWithPath("content")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세 설명 내용 (HTML) [필수]"),
                                            fieldWithPath("descriptionImages")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상세 설명 이미지 목록 [선택]")
                                                    .optional(),
                                            fieldWithPath("descriptionImages[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL [필수]")
                                                    .optional(),
                                            fieldWithPath("descriptionImages[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("생성된 상세 설명 ID"),
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 추적 ID")
                                                    .optional())));
        }

        @Test
        @DisplayName("이미지 없이 상세 설명 등록 성공")
        void registerProductGroupDescription_WithoutImages_Success() throws Exception {
            // given
            RegisterProductGroupDescriptionApiRequest request =
                    ProductGroupDescriptionApiFixtures.registerRequestWithoutImages();
            given(registerUseCase.execute(any())).willReturn(CREATED_DESCRIPTION_ID);

            // when & then
            mockMvc.perform(
                            post(
                                            "/api/v2/admin/product-groups/{productGroupId}/description",
                                            PRODUCT_GROUP_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data").value(CREATED_DESCRIPTION_ID));
        }
    }

    @Nested
    @DisplayName("상세 설명 수정 API")
    class UpdateProductGroupDescriptionTest {

        @Test
        @DisplayName("상세 설명 수정 성공")
        void updateProductGroupDescription_Success() throws Exception {
            // given
            UpdateProductGroupDescriptionApiRequest request =
                    ProductGroupDescriptionApiFixtures.updateRequest();
            willDoNothing().given(updateUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put(
                                            "/api/v2/admin/product-groups/{productGroupId}/description",
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
                                            fieldWithPath("content")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세 설명 내용 (HTML) [필수]"),
                                            fieldWithPath("descriptionImages")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상세 설명 이미지 목록 [선택]")
                                                    .optional(),
                                            fieldWithPath("descriptionImages[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL [필수]")
                                                    .optional(),
                                            fieldWithPath("descriptionImages[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]")
                                                    .optional())));
        }
    }
}
