package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
import com.ryuqq.setof.adapter.in.rest.admin.productgroup.ProductGroupApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupBasicInfoApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupFullApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.mapper.ProductGroupCommandApiMapper;
import com.ryuqq.setof.application.productgroup.port.in.command.RegisterProductGroupFullUseCase;
import com.ryuqq.setof.application.productgroup.port.in.command.UpdateProductGroupBasicInfoUseCase;
import com.ryuqq.setof.application.productgroup.port.in.command.UpdateProductGroupFullUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * ProductGroupCommandController REST Docs 테스트.
 *
 * <p>상품 그룹 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductGroupCommandController REST Docs 테스트")
@WebMvcTest(ProductGroupCommandController.class)
@WithMockUser
class ProductGroupCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterProductGroupFullUseCase registerUseCase;

    @MockBean private UpdateProductGroupFullUseCase updateFullUseCase;

    @MockBean private UpdateProductGroupBasicInfoUseCase updateBasicInfoUseCase;

    @MockBean private ProductGroupCommandApiMapper mapper;

    private static final Long PRODUCT_GROUP_ID = 1L;

    @Nested
    @DisplayName("상품 그룹 등록 API")
    class RegisterProductGroupTest {

        @Test
        @DisplayName("상품 그룹 등록 성공")
        void registerProductGroup_Success() throws Exception {
            // given
            RegisterProductGroupApiRequest request = ProductGroupApiFixtures.registerRequest();
            given(registerUseCase.execute(any())).willReturn(PRODUCT_GROUP_ID);

            // when & then
            mockMvc.perform(
                            post("/api/v2/admin/product-groups")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.productGroupId").value(PRODUCT_GROUP_ID))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("productGroupId")
                                                    .type(JsonFieldType.NULL)
                                                    .description(
                                                            "상품 그룹 ID [선택, 마이그레이션 시 레거시 ID 동기화용]")
                                                    .optional(),
                                            fieldWithPath("sellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("셀러 ID [필수, 1 이상]"),
                                            fieldWithPath("brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID [필수, 1 이상]"),
                                            fieldWithPath("categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 ID [필수, 1 이상]"),
                                            fieldWithPath("shippingPolicyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("배송 정책 ID [필수, 1 이상]"),
                                            fieldWithPath("refundPolicyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("환불 정책 ID [필수, 1 이상]"),
                                            fieldWithPath("productGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상품 그룹명 [필수, 최대 200자]"),
                                            fieldWithPath("optionType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "옵션 타입 [선택, COMBINATION/SINGLE/NONE."
                                                                + " 미입력 시 optionGroups 수로 자동 결정]")
                                                    .optional(),
                                            fieldWithPath("regularPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정가 [필수, 0 이상]"),
                                            fieldWithPath("currentPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("판매가 [필수, 0 이상]"),
                                            fieldWithPath("images")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("이미지 목록 [필수, 1개 이상]"),
                                            fieldWithPath("images[].imageType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 유형 [필수, THUMBNAIL/DETAIL]"),
                                            fieldWithPath("images[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL [필수]"),
                                            fieldWithPath("images[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]"),
                                            fieldWithPath("optionGroups")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("옵션 그룹 목록 [선택]")
                                                    .optional(),
                                            fieldWithPath("optionGroups[].optionGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 그룹명 [필수]")
                                                    .optional(),
                                            fieldWithPath("optionGroups[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]")
                                                    .optional(),
                                            fieldWithPath("optionGroups[].optionValues")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("옵션 값 목록 [필수, 1개 이상]")
                                                    .optional(),
                                            fieldWithPath(
                                                            "optionGroups[].optionValues[].optionValueName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 값명 [필수]")
                                                    .optional(),
                                            fieldWithPath("optionGroups[].optionValues[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]")
                                                    .optional(),
                                            fieldWithPath("products")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상품 목록 [필수, 1개 이상]"),
                                            fieldWithPath("products[].productId")
                                                    .type(JsonFieldType.NULL)
                                                    .description("상품 ID [선택, 마이그레이션 시 레거시 ID 동기화용]")
                                                    .optional(),
                                            fieldWithPath("products[].skuCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("SKU 코드 [선택]")
                                                    .optional(),
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
                                                    .description("선택된 옵션 목록 [필수]"),
                                            fieldWithPath(
                                                            "products[].selectedOptions[].optionGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 그룹명 [필수]")
                                                    .optional(),
                                            fieldWithPath(
                                                            "products[].selectedOptions[].optionValueName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 값명 [필수]")
                                                    .optional(),
                                            fieldWithPath("description")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("상세 설명 [선택]")
                                                    .optional(),
                                            fieldWithPath("description.content")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세 설명 HTML 컨텐츠 [필수]")
                                                    .optional(),
                                            fieldWithPath("description.descriptionImages")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상세설명 이미지 목록 [선택]")
                                                    .optional(),
                                            fieldWithPath(
                                                            "description.descriptionImages[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL [필수]")
                                                    .optional(),
                                            fieldWithPath(
                                                            "description.descriptionImages[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]")
                                                    .optional(),
                                            fieldWithPath("notice")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("고시정보 [선택]")
                                                    .optional(),
                                            fieldWithPath("notice.entries")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("고시 항목 목록 [필수, 1개 이상]")
                                                    .optional(),
                                            fieldWithPath("notice.entries[].noticeFieldId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("고시 필드 ID [필수, 1 이상]")
                                                    .optional(),
                                            fieldWithPath("notice.entries[].fieldName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("필드명 [필수]")
                                                    .optional(),
                                            fieldWithPath("notice.entries[].fieldValue")
                                                    .type(JsonFieldType.STRING)
                                                    .description("필드 값 [필수]")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.productGroupId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("생성된 상품 그룹 ID"),
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
    @DisplayName("상품 그룹 전체 수정 API")
    class UpdateProductGroupFullTest {

        @Test
        @DisplayName("상품 그룹 전체 수정 성공")
        void updateProductGroupFull_Success() throws Exception {
            // given
            UpdateProductGroupFullApiRequest request = ProductGroupApiFixtures.updateFullRequest();
            willDoNothing().given(updateFullUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put("/api/v2/admin/product-groups/{productGroupId}", PRODUCT_GROUP_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("productGroupId")
                                                    .description("수정할 상품 그룹 ID")),
                                    requestFields(
                                            fieldWithPath("productGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상품 그룹명 [필수, 최대 200자]"),
                                            fieldWithPath("brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID [필수, 1 이상]"),
                                            fieldWithPath("categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 ID [필수, 1 이상]"),
                                            fieldWithPath("shippingPolicyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("배송 정책 ID [필수, 1 이상]"),
                                            fieldWithPath("refundPolicyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("환불 정책 ID [필수, 1 이상]"),
                                            fieldWithPath("optionType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "옵션 타입 [선택, COMBINATION/SINGLE/NONE."
                                                                + " 미입력 시 optionGroups 수로 자동 결정]")
                                                    .optional(),
                                            fieldWithPath("regularPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정가 [필수, 0 이상]"),
                                            fieldWithPath("currentPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("판매가 [필수, 0 이상]"),
                                            fieldWithPath("images")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("이미지 목록 [필수, 1개 이상]"),
                                            fieldWithPath("images[].imageType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 유형 [필수, THUMBNAIL/DETAIL]"),
                                            fieldWithPath("images[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL [필수]"),
                                            fieldWithPath("images[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]"),
                                            fieldWithPath("optionGroups")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("옵션 그룹 목록 [선택]")
                                                    .optional(),
                                            fieldWithPath("optionGroups[].sellerOptionGroupId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("셀러 옵션 그룹 ID [선택, 수정 시 식별용]")
                                                    .optional(),
                                            fieldWithPath("optionGroups[].optionGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 그룹명 [필수]")
                                                    .optional(),
                                            fieldWithPath("optionGroups[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]")
                                                    .optional(),
                                            fieldWithPath("optionGroups[].optionValues")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("옵션 값 목록 [필수, 1개 이상]")
                                                    .optional(),
                                            fieldWithPath(
                                                            "optionGroups[].optionValues[].sellerOptionValueId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("셀러 옵션 값 ID [선택, 수정 시 식별용]")
                                                    .optional(),
                                            fieldWithPath(
                                                            "optionGroups[].optionValues[].optionValueName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 값명 [필수]")
                                                    .optional(),
                                            fieldWithPath("optionGroups[].optionValues[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]")
                                                    .optional(),
                                            fieldWithPath("products")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상품 목록 [필수, 1개 이상]"),
                                            fieldWithPath("products[].productId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("상품 ID [선택, 수정 시 식별용]")
                                                    .optional(),
                                            fieldWithPath("products[].skuCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("SKU 코드 [선택]")
                                                    .optional(),
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
                                                    .description("선택된 옵션 목록 [필수]"),
                                            fieldWithPath(
                                                            "products[].selectedOptions[].optionGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 그룹명 [필수]")
                                                    .optional(),
                                            fieldWithPath(
                                                            "products[].selectedOptions[].optionValueName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 값명 [필수]")
                                                    .optional(),
                                            fieldWithPath("description")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("상세 설명 [선택]")
                                                    .optional(),
                                            fieldWithPath("description.content")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세 설명 HTML 컨텐츠 [필수]")
                                                    .optional(),
                                            fieldWithPath("description.descriptionImages")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상세설명 이미지 목록 [선택]")
                                                    .optional(),
                                            fieldWithPath(
                                                            "description.descriptionImages[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL [필수]")
                                                    .optional(),
                                            fieldWithPath(
                                                            "description.descriptionImages[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서 [필수, 0 이상]")
                                                    .optional(),
                                            fieldWithPath("notice")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("고시정보 [선택]")
                                                    .optional(),
                                            fieldWithPath("notice.entries")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("고시 항목 목록 [필수, 1개 이상]")
                                                    .optional(),
                                            fieldWithPath("notice.entries[].noticeFieldId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("고시 필드 ID [필수, 1 이상]")
                                                    .optional(),
                                            fieldWithPath("notice.entries[].fieldName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("필드명 [필수]")
                                                    .optional(),
                                            fieldWithPath("notice.entries[].fieldValue")
                                                    .type(JsonFieldType.STRING)
                                                    .description("필드 값 [필수]")
                                                    .optional())));
        }
    }

    @Nested
    @DisplayName("상품 그룹 기본 정보 수정 API")
    class UpdateProductGroupBasicInfoTest {

        @Test
        @DisplayName("상품 그룹 기본 정보 수정 성공")
        void updateBasicInfo_Success() throws Exception {
            // given
            UpdateProductGroupBasicInfoApiRequest request =
                    ProductGroupApiFixtures.updateBasicInfoRequest();
            willDoNothing().given(updateBasicInfoUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch(
                                            "/api/v2/admin/product-groups/{productGroupId}/basic-info",
                                            PRODUCT_GROUP_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("productGroupId")
                                                    .description("수정할 상품 그룹 ID")),
                                    requestFields(
                                            fieldWithPath("productGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상품 그룹명 [필수, 최대 200자]"),
                                            fieldWithPath("brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID [필수, 1 이상]"),
                                            fieldWithPath("categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 ID [필수, 1 이상]"),
                                            fieldWithPath("shippingPolicyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("배송 정책 ID [필수, 1 이상]"),
                                            fieldWithPath("refundPolicyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("환불 정책 ID [필수, 1 이상]"))));
        }
    }
}
