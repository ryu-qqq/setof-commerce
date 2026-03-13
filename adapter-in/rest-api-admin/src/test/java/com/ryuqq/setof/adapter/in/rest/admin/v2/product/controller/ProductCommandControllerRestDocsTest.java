package com.ryuqq.setof.adapter.in.rest.admin.v2.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.product.ProductApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.product.dto.command.UpdateProductPriceApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.product.dto.command.UpdateProductStockApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.product.dto.command.UpdateProductsApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.product.mapper.ProductCommandApiMapper;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductPriceUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductStockUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductsUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * ProductCommandController REST Docs 테스트.
 *
 * <p>상품(SKU) Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductCommandController REST Docs 테스트")
@WebMvcTest(ProductCommandController.class)
@WithMockUser
class ProductCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private UpdateProductPriceUseCase updatePriceUseCase;

    @MockBean private UpdateProductStockUseCase updateStockUseCase;

    @MockBean private UpdateProductsUseCase updateProductsUseCase;

    @MockBean private ProductCommandApiMapper mapper;

    private static final Long PRODUCT_ID = ProductApiFixtures.DEFAULT_PRODUCT_ID;
    private static final Long PRODUCT_GROUP_ID = ProductApiFixtures.DEFAULT_PRODUCT_GROUP_ID;

    @Nested
    @DisplayName("상품 가격 수정 API")
    class UpdatePriceTest {

        @Test
        @DisplayName("상품 가격 수정 성공")
        void updatePrice_Success() throws Exception {
            // given
            UpdateProductPriceApiRequest request = ProductApiFixtures.updatePriceRequest();
            willDoNothing().given(updatePriceUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch("/api/v2/admin/products/{productId}/price", PRODUCT_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("productId").description("상품 ID")),
                                    requestFields(
                                            fieldWithPath("regularPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정가 [필수, 0 이상]"),
                                            fieldWithPath("currentPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("판매가 [필수, 0 이상]"))));
        }
    }

    @Nested
    @DisplayName("상품 재고 수정 API")
    class UpdateStockTest {

        @Test
        @DisplayName("상품 재고 수정 성공")
        void updateStock_Success() throws Exception {
            // given
            UpdateProductStockApiRequest request = ProductApiFixtures.updateStockRequest();
            willDoNothing().given(updateStockUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch("/api/v2/admin/products/{productId}/stock", PRODUCT_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("productId").description("상품 ID")),
                                    requestFields(
                                            fieldWithPath("stockQuantity")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("재고 수량 [필수, 0 이상]"))));
        }
    }

    @Nested
    @DisplayName("상품 일괄 수정 API")
    class UpdateProductsTest {

        @Test
        @DisplayName("상품 그룹 하위 상품 일괄 수정 성공")
        void updateProducts_Success() throws Exception {
            // given
            UpdateProductsApiRequest request = ProductApiFixtures.updateProductsRequest();
            willDoNothing().given(updateProductsUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch(
                                            "/api/v2/admin/products/product-groups/{productGroupId}",
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
                                            fieldWithPath("optionGroups")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("옵션 그룹 목록 [필수]"),
                                            fieldWithPath("optionGroups[].sellerOptionGroupId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("기존 옵션 그룹 ID (신규이면 null)")
                                                    .optional(),
                                            fieldWithPath("optionGroups[].optionGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 그룹명 [필수]"),
                                            fieldWithPath("optionGroups[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]"),
                                            fieldWithPath("optionGroups[].optionValues")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("옵션 값 목록"),
                                            fieldWithPath(
                                                            "optionGroups[].optionValues[].sellerOptionValueId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("기존 옵션 값 ID (신규이면 null)")
                                                    .optional(),
                                            fieldWithPath(
                                                            "optionGroups[].optionValues[].optionValueName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 값명 [필수]"),
                                            fieldWithPath("optionGroups[].optionValues[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]"),
                                            fieldWithPath("products")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("수정할 상품 목록 [필수, 1개 이상]"),
                                            fieldWithPath("products[].productId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("상품 ID (신규이면 null)")
                                                    .optional(),
                                            fieldWithPath("products[].skuCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("SKU 코드 [필수]"),
                                            fieldWithPath("products[].regularPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정가 [필수, 0 이상]"),
                                            fieldWithPath("products[].currentPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("판매가 [필수, 0 이상]"),
                                            fieldWithPath("products[].stockQuantity")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("재고 수량 [필수, 0 이상]"),
                                            fieldWithPath("products[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]"),
                                            fieldWithPath("products[].selectedOptions")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("이름 기반 옵션 선택 목록 [필수]"),
                                            fieldWithPath(
                                                            "products[].selectedOptions[].optionGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 그룹명 [필수]"),
                                            fieldWithPath(
                                                            "products[].selectedOptions[].optionValueName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 값명 [필수]"))));
        }
    }
}
