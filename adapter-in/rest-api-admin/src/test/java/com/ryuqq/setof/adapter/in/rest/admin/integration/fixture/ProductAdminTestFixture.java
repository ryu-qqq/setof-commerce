package com.ryuqq.setof.adapter.in.rest.admin.integration.fixture;

import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreatePriceV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.CreateProductStatusV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.DeleteProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateCategoryV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateDisplayYnV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command.UpdateProductGroupV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productimage.dto.command.CreateProductImageV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productnotice.dto.command.CreateProductNoticeV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productstock.dto.command.CreateOptionV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productstock.dto.command.UpdateProductStockV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.dto.command.UpdateProductDescriptionV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupStatusV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.command.UpdateProductImageV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.UpdateProductNoticeV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.command.SetStockV2ApiRequest;
import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.dto.response.ProductResponse;
import com.ryuqq.setof.application.productdescription.dto.response.DescriptionImageResponse;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productnotice.dto.response.NoticeItemResponse;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Product Admin 통합 테스트 Fixture
 *
 * <p>Admin API 통합 테스트에서 사용하는 상수 및 Request/Response 빌더를 제공합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public final class ProductAdminTestFixture {

    private ProductAdminTestFixture() {
        // Utility class
    }

    // ============================================================
    // Admin Constants
    // ============================================================

    public static final String DEFAULT_ADMIN_ID = "admin-001";
    public static final Long DEFAULT_SELLER_ID = 1L;
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 100L;
    public static final Long DEFAULT_PRODUCT_ID = 1000L;
    public static final Long DEFAULT_CATEGORY_ID = 10L;
    public static final Long DEFAULT_BRAND_ID = 5L;

    // ============================================================
    // Product Group Constants
    // ============================================================

    public static final String DEFAULT_PRODUCT_GROUP_NAME = "테스트 상품 그룹";
    public static final String DEFAULT_OPTION_TYPE = "SIZE_COLOR";
    public static final String DEFAULT_MANAGEMENT_TYPE = "SELF";
    public static final String DEFAULT_STATUS = "ACTIVE";
    public static final String INACTIVE_STATUS = "INACTIVE";

    // ============================================================
    // Price Constants
    // ============================================================

    public static final BigDecimal DEFAULT_REGULAR_PRICE = new BigDecimal("50000");
    public static final BigDecimal DEFAULT_CURRENT_PRICE = new BigDecimal("40000");
    public static final BigDecimal UPDATED_REGULAR_PRICE = new BigDecimal("60000");
    public static final BigDecimal UPDATED_CURRENT_PRICE = new BigDecimal("45000");
    public static final int DEFAULT_DISCOUNT_RATE = 20;

    // ============================================================
    // Stock Constants
    // ============================================================

    public static final int DEFAULT_STOCK_QUANTITY = 100;
    public static final int UPDATED_STOCK_QUANTITY = 150;
    public static final int LOW_STOCK_QUANTITY = 5;
    public static final int ZERO_STOCK_QUANTITY = 0;

    // ============================================================
    // Image Constants
    // ============================================================

    public static final String DEFAULT_IMAGE_URL = "https://cdn.example.com/products/image1.jpg";
    public static final String DEFAULT_THUMBNAIL_URL =
            "https://cdn.example.com/products/thumb1.jpg";
    public static final int DEFAULT_IMAGE_ORDER = 1;

    // ============================================================
    // Description Constants
    // ============================================================

    public static final String DEFAULT_DETAIL_DESCRIPTION = "<p>상품 상세 설명입니다.</p>";
    public static final String UPDATED_DETAIL_DESCRIPTION = "<p>수정된 상품 상세 설명입니다.</p>";

    // ============================================================
    // Notice Constants
    // ============================================================

    public static final String DEFAULT_MATERIAL = "면 100%";
    public static final String DEFAULT_COLOR = "블랙, 화이트";
    public static final String DEFAULT_SIZE = "S, M, L, XL";
    public static final String DEFAULT_MANUFACTURER = "테스트 제조사";
    public static final String DEFAULT_COUNTRY_OF_ORIGIN = "대한민국";

    // ============================================================
    // Option Constants
    // ============================================================

    public static final String DEFAULT_OPTION1_NAME = "사이즈";
    public static final String DEFAULT_OPTION1_VALUE = "M";
    public static final String DEFAULT_OPTION2_NAME = "색상";
    public static final String DEFAULT_OPTION2_VALUE = "블랙";
    public static final BigDecimal DEFAULT_ADDITIONAL_PRICE = BigDecimal.ZERO;

    // ============================================================
    // Non-existent IDs for negative tests
    // ============================================================

    public static final Long NON_EXISTENT_PRODUCT_GROUP_ID = 999999L;
    public static final Long NON_EXISTENT_PRODUCT_ID = 999999L;
    public static final Long NON_EXISTENT_CATEGORY_ID = 999999L;

    // ============================================================
    // V1 Request Builders
    // ============================================================

    /** 기본 V1 상품 그룹 생성 요청을 생성합니다. */
    public static CreateProductGroupV1ApiRequest createProductGroupV1Request() {
        return createProductGroupV1Request(DEFAULT_SELLER_ID, DEFAULT_PRODUCT_GROUP_NAME);
    }

    /** 커스텀 V1 상품 그룹 생성 요청을 생성합니다. */
    public static CreateProductGroupV1ApiRequest createProductGroupV1Request(
            Long sellerId, String productGroupName) {
        return new CreateProductGroupV1ApiRequest(
                null,
                productGroupName,
                sellerId,
                DEFAULT_OPTION_TYPE,
                DEFAULT_MANAGEMENT_TYPE,
                DEFAULT_CATEGORY_ID,
                DEFAULT_BRAND_ID,
                createProductStatusV1Request(),
                createPriceV1Request(),
                createProductNoticeV1Request(),
                null,
                null,
                null,
                List.of(createProductImageV1Request()),
                DEFAULT_DETAIL_DESCRIPTION,
                List.of(createOptionV1Request()));
    }

    /** V1 상품 상태 요청을 생성합니다. (soldOutYn, displayYn) */
    public static CreateProductStatusV1ApiRequest createProductStatusV1Request() {
        return new CreateProductStatusV1ApiRequest("N", "Y");
    }

    /** V1 가격 요청을 생성합니다. */
    public static CreatePriceV1ApiRequest createPriceV1Request() {
        return new CreatePriceV1ApiRequest(DEFAULT_REGULAR_PRICE, DEFAULT_CURRENT_PRICE);
    }

    /** V1 가격 수정 요청을 생성합니다. */
    public static CreatePriceV1ApiRequest createUpdatedPriceV1Request() {
        return new CreatePriceV1ApiRequest(UPDATED_REGULAR_PRICE, UPDATED_CURRENT_PRICE);
    }

    /** V1 상품 고시 요청을 생성합니다. */
    public static CreateProductNoticeV1ApiRequest createProductNoticeV1Request() {
        return new CreateProductNoticeV1ApiRequest(
                DEFAULT_MATERIAL,
                DEFAULT_COLOR,
                DEFAULT_SIZE,
                DEFAULT_MANUFACTURER,
                DEFAULT_COUNTRY_OF_ORIGIN,
                "적정 세탁 방법",
                "제조년월",
                "A/S 책임자",
                "품질보증기준");
    }

    /** V1 상품 이미지 요청을 생성합니다. (productImageType, imageUrl, originUrl) */
    public static CreateProductImageV1ApiRequest createProductImageV1Request() {
        return new CreateProductImageV1ApiRequest("MAIN", DEFAULT_IMAGE_URL, DEFAULT_THUMBNAIL_URL);
    }

    /** V1 옵션 요청을 생성합니다. (productId, quantity, additionalPrice, options) */
    public static CreateOptionV1ApiRequest createOptionV1Request() {
        return new CreateOptionV1ApiRequest(
                null,
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_ADDITIONAL_PRICE,
                List.of(
                        new CreateOptionV1ApiRequest.CreateOptionDetailV1ApiRequest(
                                null, null, DEFAULT_OPTION1_NAME, DEFAULT_OPTION1_VALUE),
                        new CreateOptionV1ApiRequest.CreateOptionDetailV1ApiRequest(
                                null, null, DEFAULT_OPTION2_NAME, DEFAULT_OPTION2_VALUE)));
    }

    /** V1 카테고리 수정 요청을 생성합니다. */
    public static UpdateCategoryV1ApiRequest createUpdateCategoryV1Request(Long categoryId) {
        return new UpdateCategoryV1ApiRequest(categoryId);
    }

    /** V1 전시 여부 수정 요청을 생성합니다. */
    public static UpdateDisplayYnV1ApiRequest createUpdateDisplayYnV1Request(String displayYn) {
        return new UpdateDisplayYnV1ApiRequest(displayYn);
    }

    /** V1 상품 그룹 수정 요청을 생성합니다. */
    public static UpdateProductGroupV1ApiRequest createUpdateProductGroupV1Request() {
        return new UpdateProductGroupV1ApiRequest(
                null, // deliveryNotice
                null, // refundNotice
                createProductNoticeV1Request(),
                List.of(createProductImageV1Request()),
                null, // detailDescription
                List.of(createOptionV1Request()),
                null, // productGroupDetails
                null // updateStatus
                );
    }

    /** V1 상품 그룹 삭제 요청을 생성합니다. */
    public static DeleteProductGroupV1ApiRequest createDeleteProductGroupV1Request(
            List<Long> productGroupIds) {
        return new DeleteProductGroupV1ApiRequest(productGroupIds);
    }

    /** V1 재고 수정 요청을 생성합니다. (productId, productStockQuantity) */
    public static UpdateProductStockV1ApiRequest createUpdateStockV1Request(int quantity) {
        return new UpdateProductStockV1ApiRequest(DEFAULT_PRODUCT_ID, quantity);
    }

    // ============================================================
    // V2 Request Builders
    // ============================================================

    /** V2 상품 그룹 등록 요청을 생성합니다. */
    public static RegisterProductGroupV2ApiRequest createRegisterProductGroupV2Request() {
        return new RegisterProductGroupV2ApiRequest(
                DEFAULT_SELLER_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_BRAND_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                null, // shippingPolicyId
                null, // refundPolicyId
                List.of(createProductSkuV2Request()),
                List.of(createProductImageV2Request()),
                createProductDescriptionV2Request(),
                createProductNoticeV2Request());
    }

    /** V2 상품(SKU) 요청을 생성합니다. */
    public static RegisterProductGroupV2ApiRequest.ProductSkuV2ApiRequest
            createProductSkuV2Request() {
        return new RegisterProductGroupV2ApiRequest.ProductSkuV2ApiRequest(
                DEFAULT_OPTION1_NAME,
                DEFAULT_OPTION1_VALUE,
                DEFAULT_OPTION2_NAME,
                DEFAULT_OPTION2_VALUE,
                DEFAULT_ADDITIONAL_PRICE,
                DEFAULT_STOCK_QUANTITY);
    }

    /** V2 상품 이미지 요청을 생성합니다 (for RegisterProductGroup). */
    public static RegisterProductGroupV2ApiRequest.ProductImageV2ApiRequest
            createProductImageV2Request() {
        return new RegisterProductGroupV2ApiRequest.ProductImageV2ApiRequest(
                "MAIN", DEFAULT_IMAGE_URL, DEFAULT_THUMBNAIL_URL, DEFAULT_IMAGE_ORDER);
    }

    /** V2 상품 설명 요청을 생성합니다 (for RegisterProductGroup). */
    public static RegisterProductGroupV2ApiRequest.ProductDescriptionV2ApiRequest
            createProductDescriptionV2Request() {
        return new RegisterProductGroupV2ApiRequest.ProductDescriptionV2ApiRequest(
                DEFAULT_DETAIL_DESCRIPTION, List.of());
    }

    /** V2 상품 고시 요청을 생성합니다 (for RegisterProductGroup). */
    public static RegisterProductGroupV2ApiRequest.ProductNoticeV2ApiRequest
            createProductNoticeV2Request() {
        return new RegisterProductGroupV2ApiRequest.ProductNoticeV2ApiRequest(
                1L,
                List.of(
                        new RegisterProductGroupV2ApiRequest.ProductNoticeV2ApiRequest
                                .NoticeItemV2ApiRequest("material", DEFAULT_MATERIAL, 1),
                        new RegisterProductGroupV2ApiRequest.ProductNoticeV2ApiRequest
                                .NoticeItemV2ApiRequest("color", DEFAULT_COLOR, 2),
                        new RegisterProductGroupV2ApiRequest.ProductNoticeV2ApiRequest
                                .NoticeItemV2ApiRequest("size", DEFAULT_SIZE, 3)));
    }

    /** V2 재고 설정 요청을 생성합니다. (quantity only) */
    public static SetStockV2ApiRequest createSetStockV2Request() {
        return new SetStockV2ApiRequest(DEFAULT_STOCK_QUANTITY);
    }

    /** V2 상품 그룹 수정 요청을 생성합니다. */
    public static UpdateProductGroupV2ApiRequest createUpdateProductGroupV2Request() {
        return new UpdateProductGroupV2ApiRequest(
                DEFAULT_CATEGORY_ID,
                DEFAULT_BRAND_ID,
                "수정된 상품 그룹명",
                DEFAULT_OPTION_TYPE,
                UPDATED_REGULAR_PRICE,
                UPDATED_CURRENT_PRICE,
                DEFAULT_STATUS,
                null, // shippingPolicyId
                null, // refundPolicyId
                List.of(createUpdateSkuV2Request()),
                List.of(createUpdateImageV2RequestForGroup()),
                createUpdateDescriptionV2RequestForGroup(),
                createUpdateNoticeV2RequestForGroup());
    }

    /** V2 SKU 수정 요청을 생성합니다 (for UpdateProductGroup). */
    private static UpdateProductGroupV2ApiRequest.UpdateProductSkuV2ApiRequest
            createUpdateSkuV2Request() {
        return new UpdateProductGroupV2ApiRequest.UpdateProductSkuV2ApiRequest(
                DEFAULT_OPTION1_NAME,
                DEFAULT_OPTION1_VALUE,
                DEFAULT_OPTION2_NAME,
                DEFAULT_OPTION2_VALUE,
                DEFAULT_ADDITIONAL_PRICE,
                UPDATED_STOCK_QUANTITY);
    }

    /** V2 이미지 수정 요청을 생성합니다 (for UpdateProductGroup). */
    private static UpdateProductGroupV2ApiRequest.UpdateProductImageV2ApiRequest
            createUpdateImageV2RequestForGroup() {
        return new UpdateProductGroupV2ApiRequest.UpdateProductImageV2ApiRequest(
                null, "MAIN", DEFAULT_IMAGE_URL, DEFAULT_THUMBNAIL_URL, DEFAULT_IMAGE_ORDER);
    }

    /** V2 설명 수정 요청을 생성합니다 (for UpdateProductGroup). */
    private static UpdateProductGroupV2ApiRequest.UpdateProductDescriptionV2ApiRequest
            createUpdateDescriptionV2RequestForGroup() {
        return new UpdateProductGroupV2ApiRequest.UpdateProductDescriptionV2ApiRequest(
                null, UPDATED_DETAIL_DESCRIPTION, List.of());
    }

    /** V2 고시 수정 요청을 생성합니다 (for UpdateProductGroup). */
    private static UpdateProductGroupV2ApiRequest.UpdateProductNoticeV2ApiRequest
            createUpdateNoticeV2RequestForGroup() {
        return new UpdateProductGroupV2ApiRequest.UpdateProductNoticeV2ApiRequest(
                null,
                1L,
                List.of(
                        new RegisterProductGroupV2ApiRequest.ProductNoticeV2ApiRequest
                                .NoticeItemV2ApiRequest("material", "수정된 소재", 1)));
    }

    /** V2 상품 그룹 상태 수정 요청을 생성합니다. */
    public static UpdateProductGroupStatusV2ApiRequest createUpdateStatusV2Request(String status) {
        return new UpdateProductGroupStatusV2ApiRequest(status);
    }

    /** V2 상품 설명 수정 요청을 생성합니다 (standalone). */
    public static UpdateProductDescriptionV2ApiRequest createUpdateDescriptionV2Request() {
        return new UpdateProductDescriptionV2ApiRequest(1L, UPDATED_DETAIL_DESCRIPTION, List.of());
    }

    /** V2 단일 이미지 수정 요청을 생성합니다. */
    public static UpdateProductImageV2ApiRequest createUpdateImageV2Request() {
        return new UpdateProductImageV2ApiRequest("MAIN", DEFAULT_IMAGE_URL, DEFAULT_IMAGE_ORDER);
    }

    /** V2 상품 고시 수정 요청을 생성합니다 (standalone). */
    public static UpdateProductNoticeV2ApiRequest createUpdateNoticeV2Request() {
        return new UpdateProductNoticeV2ApiRequest(
                1L,
                List.of(
                        new UpdateProductNoticeV2ApiRequest.NoticeItemV2ApiRequest(
                                "material", "수정된 소재", 1),
                        new UpdateProductNoticeV2ApiRequest.NoticeItemV2ApiRequest(
                                "color", "수정된 색상", 2),
                        new UpdateProductNoticeV2ApiRequest.NoticeItemV2ApiRequest(
                                "size", "수정된 사이즈", 3)));
    }

    // ============================================================
    // Response Builders for Mocking
    // ============================================================

    /** FullProductResponse Mock 데이터를 생성합니다. */
    public static FullProductResponse createFullProductResponse() {
        return createFullProductResponse(DEFAULT_PRODUCT_GROUP_ID);
    }

    /** FullProductResponse Mock 데이터를 생성합니다. */
    public static FullProductResponse createFullProductResponse(Long productGroupId) {
        return new FullProductResponse(
                createProductGroupDetailResponse(productGroupId),
                List.of(createProductResponse()),
                List.of(createProductImageResponse()),
                createProductDescriptionResponse(),
                createProductNoticeResponse(),
                List.of(createProductStockResponse()));
    }

    /** ProductGroupResponse (상세) Mock 데이터를 생성합니다. */
    public static ProductGroupResponse createProductGroupDetailResponse(Long productGroupId) {
        return new ProductGroupResponse(
                productGroupId,
                DEFAULT_SELLER_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_BRAND_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_STATUS,
                null, // shippingPolicyId
                null, // refundPolicyId
                List.of(createProductResponse()));
    }

    /** ProductGroupSummaryResponse Mock 데이터를 생성합니다. */
    public static ProductGroupSummaryResponse createProductGroupSummaryResponse() {
        return createProductGroupSummaryResponse(DEFAULT_PRODUCT_GROUP_ID);
    }

    /** ProductGroupSummaryResponse Mock 데이터를 생성합니다. */
    public static ProductGroupSummaryResponse createProductGroupSummaryResponse(
            Long productGroupId) {
        return new ProductGroupSummaryResponse(
                productGroupId,
                DEFAULT_SELLER_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_STATUS,
                1);
    }

    /** ProductResponse Mock 데이터를 생성합니다. */
    public static ProductResponse createProductResponse() {
        return createProductResponse(DEFAULT_PRODUCT_ID);
    }

    /** ProductResponse Mock 데이터를 생성합니다. (10 params) */
    public static ProductResponse createProductResponse(Long productId) {
        return new ProductResponse(
                productId,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_OPTION_TYPE,
                DEFAULT_OPTION1_NAME,
                DEFAULT_OPTION1_VALUE,
                DEFAULT_OPTION2_NAME,
                DEFAULT_OPTION2_VALUE,
                DEFAULT_ADDITIONAL_PRICE,
                false,
                true);
    }

    /** ProductImageResponse Mock 데이터를 생성합니다. */
    public static ProductImageResponse createProductImageResponse() {
        return createProductImageResponse(1L);
    }

    /** ProductImageResponse Mock 데이터를 생성합니다. (7 params) */
    public static ProductImageResponse createProductImageResponse(Long imageId) {
        return new ProductImageResponse(
                imageId,
                DEFAULT_PRODUCT_GROUP_ID,
                "MAIN",
                DEFAULT_IMAGE_URL,
                DEFAULT_THUMBNAIL_URL,
                DEFAULT_IMAGE_ORDER,
                Instant.now());
    }

    /** ProductDescriptionResponse Mock 데이터를 생성합니다. (8 params) */
    public static ProductDescriptionResponse createProductDescriptionResponse() {
        return new ProductDescriptionResponse(
                1L,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_DETAIL_DESCRIPTION,
                List.of(
                        new DescriptionImageResponse(
                                1, DEFAULT_IMAGE_URL, DEFAULT_THUMBNAIL_URL, Instant.now(), true)),
                true,
                true,
                Instant.now(),
                Instant.now());
    }

    /** ProductNoticeResponse Mock 데이터를 생성합니다. (7 params) */
    public static ProductNoticeResponse createProductNoticeResponse() {
        return new ProductNoticeResponse(
                1L,
                DEFAULT_PRODUCT_GROUP_ID,
                1L,
                List.of(
                        new NoticeItemResponse("material", DEFAULT_MATERIAL, 1, true),
                        new NoticeItemResponse("color", DEFAULT_COLOR, 2, true),
                        new NoticeItemResponse("size", DEFAULT_SIZE, 3, true)),
                3,
                Instant.now(),
                Instant.now());
    }

    /** ProductStockResponse Mock 데이터를 생성합니다. */
    public static ProductStockResponse createProductStockResponse() {
        return createProductStockResponse(DEFAULT_PRODUCT_ID);
    }

    /** ProductStockResponse Mock 데이터를 생성합니다. */
    public static ProductStockResponse createProductStockResponse(Long productId) {
        return new ProductStockResponse(1L, productId, DEFAULT_STOCK_QUANTITY, Instant.now());
    }

    // ============================================================
    // Helper Methods
    // ============================================================

    /** 페이지 크기 기본값 */
    public static int getDefaultPageSize() {
        return 20;
    }

    /** 최대 페이지 크기 */
    public static int getMaxPageSize() {
        return 100;
    }

    /** 여러 ProductGroupSummaryResponse를 생성합니다. */
    public static List<ProductGroupSummaryResponse> createProductGroupSummaryResponses(int count) {
        return java.util.stream.IntStream.rangeClosed(1, count)
                .mapToObj(i -> createProductGroupSummaryResponse((long) i))
                .toList();
    }

    /** 여러 ProductImageResponse를 생성합니다. */
    public static List<ProductImageResponse> createProductImageResponses(int count) {
        return java.util.stream.IntStream.rangeClosed(1, count)
                .mapToObj(i -> createProductImageResponse((long) i))
                .toList();
    }
}
