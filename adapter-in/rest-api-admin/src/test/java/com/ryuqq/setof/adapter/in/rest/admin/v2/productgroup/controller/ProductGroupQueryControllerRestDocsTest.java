package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.productgroup.ProductGroupQueryApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupDetailApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.mapper.ProductGroupQueryApiMapper;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.port.in.query.GetAdminProductGroupUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * ProductGroupQueryController REST Docs 테스트.
 *
 * <p>상품 그룹 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductGroupQueryController REST Docs 테스트")
@WebMvcTest(ProductGroupQueryController.class)
@WithMockUser
class ProductGroupQueryControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetAdminProductGroupUseCase getAdminProductGroupUseCase;

    @MockBean private ProductGroupQueryApiMapper mapper;

    private static final Long PRODUCT_GROUP_ID =
            ProductGroupQueryApiFixtures.DEFAULT_PRODUCT_GROUP_ID;

    @Nested
    @DisplayName("상품 그룹 상세 조회 API")
    class GetProductGroupByIdTest {

        @Test
        @DisplayName("상품 그룹 상세 조회 성공")
        void getById_Success() throws Exception {
            // given
            ProductGroupDetailCompositeResult compositeResult =
                    ProductGroupQueryApiFixtures.compositeResult();
            ProductGroupDetailApiResponse detailResponse =
                    ProductGroupQueryApiFixtures.productGroupDetailApiResponse();

            given(getAdminProductGroupUseCase.execute(any())).willReturn(compositeResult);
            given(mapper.toDetailResponse(any())).willReturn(detailResponse);

            // when & then
            mockMvc.perform(get("/api/v2/admin/product-groups/{productGroupId}", PRODUCT_GROUP_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(PRODUCT_GROUP_ID))
                    .andExpect(
                            jsonPath("$.data.productGroupName")
                                    .value(ProductGroupQueryApiFixtures.DEFAULT_PRODUCT_GROUP_NAME))
                    .andExpect(
                            jsonPath("$.data.sellerId")
                                    .value(ProductGroupQueryApiFixtures.DEFAULT_SELLER_ID))
                    .andExpect(
                            jsonPath("$.data.status")
                                    .value(ProductGroupQueryApiFixtures.DEFAULT_STATUS))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("productGroupId")
                                                    .description("상품 그룹 ID")),
                                    responseFields(
                                            fieldWithPath("data.id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("상품 그룹 ID"),
                                            fieldWithPath("data.sellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("셀러 ID"),
                                            fieldWithPath("data.sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러명"),
                                            fieldWithPath("data.brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID"),
                                            fieldWithPath("data.brandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명"),
                                            fieldWithPath("data.categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 ID"),
                                            fieldWithPath("data.categoryName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카테고리명"),
                                            fieldWithPath("data.categoryPath")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카테고리 ID 경로 (예: 1/5/100)"),
                                            fieldWithPath("data.productGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상품 그룹명"),
                                            fieldWithPath("data.optionType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "옵션 유형 (NONE, SINGLE, COMBINATION)"),
                                            fieldWithPath("data.status")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "상태 (DRAFT, ACTIVE, INACTIVE, SOLD_OUT,"
                                                                    + " DELETED)"),
                                            fieldWithPath("data.createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성일시 (ISO 8601)"),
                                            fieldWithPath("data.updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시 (ISO 8601)"),
                                            // images
                                            fieldWithPath("data.images[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상품 그룹 이미지 목록"),
                                            fieldWithPath("data.images[].id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("이미지 ID"),
                                            fieldWithPath("data.images[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL"),
                                            fieldWithPath("data.images[].imageType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 유형 (THUMBNAIL, DETAIL)"),
                                            fieldWithPath("data.images[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서"),
                                            // optionProductMatrix
                                            fieldWithPath("data.optionProductMatrix")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("옵션-상품 매트릭스"),
                                            fieldWithPath("data.optionProductMatrix.optionGroups[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("옵션 그룹 목록"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.optionGroups[].id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("옵션 그룹 ID"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.optionGroups[].optionGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 그룹명"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.optionGroups[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("옵션 그룹 정렬 순서"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.optionGroups[].optionValues[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("옵션 값 목록"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.optionGroups[].optionValues[].id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("옵션 값 ID"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.optionGroups[].optionValues[].sellerOptionGroupId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("소속 옵션 그룹 ID"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.optionGroups[].optionValues[].optionValueName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 값명"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.optionGroups[].optionValues[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("옵션 값 정렬 순서"),
                                            fieldWithPath("data.optionProductMatrix.products[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상품(SKU) 목록"),
                                            fieldWithPath("data.optionProductMatrix.products[].id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("상품 ID"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].skuCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("SKU 코드"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].regularPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정가"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].currentPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재가"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].discountRate")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인율"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].stockQuantity")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("재고수량"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].status")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상품 상태"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].options[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("resolved 옵션 매핑 목록"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].options[].sellerOptionGroupId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("옵션 그룹 ID"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].options[].optionGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 그룹명"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].options[].sellerOptionValueId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("옵션 값 ID"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].options[].optionValueName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 값명"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상품 생성일시 (ISO 8601)"),
                                            fieldWithPath(
                                                            "data.optionProductMatrix.products[].updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상품 수정일시 (ISO 8601)"),
                                            // shippingPolicy
                                            fieldWithPath("data.shippingPolicy")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("배송 정책 (없으면 null)")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.policyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("배송 정책 ID")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.sellerId")
                                                    .type(JsonFieldType.NULL)
                                                    .description("셀러 ID")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.policyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책명")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.defaultPolicy")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("기본 정책 여부")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성 여부")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.shippingFeeType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송비 유형")
                                                    .optional(),
                                            fieldWithPath(
                                                            "data.shippingPolicy.shippingFeeTypeDisplayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송비 유형 표시명")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.baseFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("기본 배송비")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.freeThreshold")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("무료 배송 기준 금액")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.jejuExtraFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("제주 추가 배송비")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.islandExtraFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("도서산간 추가 배송비")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.returnFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("반품 배송비")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.exchangeFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("교환 배송비")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.leadTimeMinDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최소 배송일")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.leadTimeMaxDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최대 배송일")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송 정책 생성일시 (ISO 8601)")
                                                    .optional(),
                                            fieldWithPath("data.shippingPolicy.updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송 정책 수정일시 (ISO 8601)")
                                                    .optional(),
                                            // refundPolicy
                                            fieldWithPath("data.refundPolicy")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("환불 정책 (없으면 null)")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.policyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("환불 정책 ID")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.sellerId")
                                                    .type(JsonFieldType.NULL)
                                                    .description("셀러 ID")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.policyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책명")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.defaultPolicy")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("기본 정책 여부")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성 여부")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.returnPeriodDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("반품 기간 (일)")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.exchangePeriodDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("교환 기간 (일)")
                                                    .optional(),
                                            fieldWithPath(
                                                            "data.refundPolicy.nonReturnableConditions[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("반품 불가 사유 목록")
                                                    .optional(),
                                            fieldWithPath(
                                                            "data.refundPolicy.nonReturnableConditions[].code")
                                                    .type(JsonFieldType.STRING)
                                                    .description("반품 불가 사유 코드")
                                                    .optional(),
                                            fieldWithPath(
                                                            "data.refundPolicy.nonReturnableConditions[].displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("반품 불가 사유 표시명")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.partialRefundEnabled")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("부분 환불 가능 여부")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.inspectionRequired")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("검수 필요 여부")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.inspectionPeriodDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("검수 기간 (일)")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.additionalInfo")
                                                    .type(JsonFieldType.NULL)
                                                    .description("추가 정보")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("환불 정책 생성일시 (ISO 8601)")
                                                    .optional(),
                                            fieldWithPath("data.refundPolicy.updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("환불 정책 수정일시 (ISO 8601)")
                                                    .optional(),
                                            // description
                                            fieldWithPath("data.description")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("상품 상세설명 (없으면 null)")
                                                    .optional(),
                                            fieldWithPath("data.description.id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("상세설명 ID")
                                                    .optional(),
                                            fieldWithPath("data.description.content")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세설명 내용")
                                                    .optional(),
                                            fieldWithPath("data.description.cdnPath")
                                                    .type(JsonFieldType.STRING)
                                                    .description("CDN 경로")
                                                    .optional(),
                                            fieldWithPath("data.description.images[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상세설명 이미지 목록")
                                                    .optional(),
                                            fieldWithPath("data.description.images[].id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("이미지 ID")
                                                    .optional(),
                                            fieldWithPath("data.description.images[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL")
                                                    .optional(),
                                            fieldWithPath("data.description.images[].sortOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정렬 순서")
                                                    .optional(),
                                            // productNotice
                                            fieldWithPath("data.productNotice")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("상품 고시정보 (없으면 null)")
                                                    .optional(),
                                            fieldWithPath("data.productNotice.id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("고시정보 ID")
                                                    .optional(),
                                            fieldWithPath("data.productNotice.entries[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("고시정보 항목 목록")
                                                    .optional(),
                                            fieldWithPath("data.productNotice.entries[].id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("항목 ID")
                                                    .optional(),
                                            fieldWithPath(
                                                            "data.productNotice.entries[].noticeFieldId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("고시 필드 ID")
                                                    .optional(),
                                            fieldWithPath("data.productNotice.entries[].fieldValue")
                                                    .type(JsonFieldType.STRING)
                                                    .description("필드값")
                                                    .optional(),
                                            fieldWithPath("data.productNotice.createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("고시정보 생성일시 (ISO 8601)")
                                                    .optional(),
                                            fieldWithPath("data.productNotice.updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("고시정보 수정일시 (ISO 8601)")
                                                    .optional(),
                                            // common wrapper fields
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
}
