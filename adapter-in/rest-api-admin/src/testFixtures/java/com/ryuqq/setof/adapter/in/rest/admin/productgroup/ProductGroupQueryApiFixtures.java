package com.ryuqq.setof.adapter.in.rest.admin.productgroup;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.DescriptionImageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.NonReturnableConditionApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductDetailApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupDescriptionApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupDetailApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupImageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductNoticeApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductNoticeEntryApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductOptionMatrixApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.RefundPolicyApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ResolvedProductOptionApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.SellerOptionGroupApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.SellerOptionValueApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ShippingPolicyApiResponse;
import com.ryuqq.setof.application.product.dto.response.ProductDetailResult;
import com.ryuqq.setof.application.product.dto.response.ResolvedProductOptionResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductOptionMatrixResult;
import com.ryuqq.setof.application.productgroup.dto.response.SellerOptionGroupResult;
import com.ryuqq.setof.application.productgroup.dto.response.SellerOptionValueResult;
import com.ryuqq.setof.application.productgroupdescription.dto.response.DescriptionImageResult;
import com.ryuqq.setof.application.productgroupdescription.dto.response.ProductGroupDescriptionResult;
import com.ryuqq.setof.application.productgroupimage.dto.response.ProductGroupImageResult;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeEntryResult;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResult;
import com.ryuqq.setof.application.refundpolicy.dto.response.NonReturnableConditionResult;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResult;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResult;
import java.time.Instant;
import java.util.List;

/**
 * ProductGroup Query API 테스트 Fixtures.
 *
 * <p>상품 그룹 Query API 테스트에서 사용되는 Application Result / API Response 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupQueryApiFixtures {

    private ProductGroupQueryApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 1L;
    public static final Long DEFAULT_SELLER_ID = 10L;
    public static final String DEFAULT_SELLER_NAME = "테스트셀러";
    public static final Long DEFAULT_BRAND_ID = 2L;
    public static final String DEFAULT_BRAND_NAME = "나이키";
    public static final Long DEFAULT_CATEGORY_ID = 100L;
    public static final String DEFAULT_CATEGORY_NAME = "운동화";
    public static final String DEFAULT_CATEGORY_PATH = "1/5/100";
    public static final String DEFAULT_PRODUCT_GROUP_NAME = "나이키 에어맥스 90";
    public static final String DEFAULT_OPTION_TYPE = "SINGLE";
    public static final String DEFAULT_STATUS = "ACTIVE";
    public static final Instant DEFAULT_CREATED_AT = Instant.parse("2025-01-26T01:30:00Z");
    public static final Instant DEFAULT_UPDATED_AT = Instant.parse("2025-01-26T01:30:00Z");
    public static final String DEFAULT_CREATED_AT_STRING = "2025-01-26T10:30:00+09:00";
    public static final String DEFAULT_UPDATED_AT_STRING = "2025-01-26T10:30:00+09:00";

    // ===== Application Result Fixtures =====

    public static ProductGroupDetailCompositeResult compositeResult() {
        return compositeResult(DEFAULT_PRODUCT_GROUP_ID);
    }

    public static ProductGroupDetailCompositeResult compositeResult(Long productGroupId) {
        return new ProductGroupDetailCompositeResult(
                productGroupId,
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_BRAND_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_CATEGORY_PATH,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_STATUS,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                List.of(productGroupImageResult()),
                productOptionMatrixResult(),
                shippingPolicyResult(),
                refundPolicyResult(),
                descriptionResult(),
                productNoticeResult());
    }

    public static ProductGroupDetailCompositeResult compositeResultWithNullShippingPolicy() {
        return new ProductGroupDetailCompositeResult(
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_BRAND_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_CATEGORY_PATH,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_STATUS,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                List.of(productGroupImageResult()),
                productOptionMatrixResult(),
                null,
                refundPolicyResult(),
                descriptionResult(),
                productNoticeResult());
    }

    public static ProductGroupDetailCompositeResult compositeResultWithNullRefundPolicy() {
        return new ProductGroupDetailCompositeResult(
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_BRAND_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_CATEGORY_PATH,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_STATUS,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                List.of(productGroupImageResult()),
                productOptionMatrixResult(),
                shippingPolicyResult(),
                null,
                descriptionResult(),
                productNoticeResult());
    }

    public static ProductGroupDetailCompositeResult compositeResultWithNullDescription() {
        return new ProductGroupDetailCompositeResult(
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_BRAND_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_CATEGORY_PATH,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_STATUS,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                List.of(productGroupImageResult()),
                productOptionMatrixResult(),
                shippingPolicyResult(),
                refundPolicyResult(),
                null,
                productNoticeResult());
    }

    public static ProductGroupDetailCompositeResult compositeResultWithNullProductNotice() {
        return new ProductGroupDetailCompositeResult(
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_BRAND_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_CATEGORY_PATH,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_STATUS,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                List.of(productGroupImageResult()),
                productOptionMatrixResult(),
                shippingPolicyResult(),
                refundPolicyResult(),
                descriptionResult(),
                null);
    }

    public static ProductGroupDetailCompositeResult compositeResultWithEmptyImages() {
        return new ProductGroupDetailCompositeResult(
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_BRAND_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_CATEGORY_PATH,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_STATUS,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                List.of(),
                productOptionMatrixResult(),
                shippingPolicyResult(),
                refundPolicyResult(),
                descriptionResult(),
                productNoticeResult());
    }

    // ===== Sub Result Fixtures =====

    public static ProductGroupImageResult productGroupImageResult() {
        return new ProductGroupImageResult(
                100L, "https://example.com/thumbnail.jpg", "THUMBNAIL", 0);
    }

    public static ProductOptionMatrixResult productOptionMatrixResult() {
        return new ProductOptionMatrixResult(
                List.of(sellerOptionGroupResult()), List.of(productDetailResult()));
    }

    public static SellerOptionGroupResult sellerOptionGroupResult() {
        return new SellerOptionGroupResult(1L, "색상", 0, List.of(sellerOptionValueResult()));
    }

    public static SellerOptionValueResult sellerOptionValueResult() {
        return new SellerOptionValueResult(10L, 1L, "블랙", 0);
    }

    public static ProductDetailResult productDetailResult() {
        return new ProductDetailResult(
                200L,
                "SKU-BLK-001",
                100_000,
                89_000,
                null,
                11,
                100,
                "ACTIVE",
                0,
                List.of(resolvedProductOptionResult()),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    public static ResolvedProductOptionResult resolvedProductOptionResult() {
        return new ResolvedProductOptionResult(1L, "색상", 10L, "블랙");
    }

    public static ShippingPolicyResult shippingPolicyResult() {
        return new ShippingPolicyResult(
                1L,
                "기본 배송정책",
                true,
                true,
                "CONDITIONAL_FREE",
                "조건부 무료배송",
                3000L,
                50000L,
                DEFAULT_CREATED_AT);
    }

    public static RefundPolicyResult refundPolicyResult() {
        return new RefundPolicyResult(
                1L,
                "기본 환불정책",
                true,
                true,
                7,
                14,
                List.of(new NonReturnableConditionResult("OPENED_PACKAGING", "포장 개봉")),
                DEFAULT_CREATED_AT);
    }

    public static ProductGroupDescriptionResult descriptionResult() {
        return new ProductGroupDescriptionResult(
                300L,
                "<p>상품 상세 설명입니다.</p>",
                "https://cdn.example.com/desc",
                List.of(new DescriptionImageResult(400L, "https://example.com/desc1.jpg", 0)));
    }

    public static ProductNoticeResult productNoticeResult() {
        return new ProductNoticeResult(
                500L,
                List.of(
                        new ProductNoticeEntryResult(600L, 10L, "면 100%"),
                        new ProductNoticeEntryResult(601L, 11L, "블랙")),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    // ===== API Response Fixtures =====

    public static ProductGroupDetailApiResponse productGroupDetailApiResponse() {
        return productGroupDetailApiResponse(DEFAULT_PRODUCT_GROUP_ID);
    }

    public static ProductGroupDetailApiResponse productGroupDetailApiResponse(Long productGroupId) {
        return new ProductGroupDetailApiResponse(
                productGroupId,
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_BRAND_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_CATEGORY_PATH,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_STATUS,
                DEFAULT_CREATED_AT_STRING,
                DEFAULT_UPDATED_AT_STRING,
                List.of(productGroupImageApiResponse()),
                productOptionMatrixApiResponse(),
                shippingPolicyApiResponse(),
                refundPolicyApiResponse(),
                descriptionApiResponse(),
                productNoticeApiResponse());
    }

    public static ProductGroupImageApiResponse productGroupImageApiResponse() {
        return new ProductGroupImageApiResponse(
                100L, "https://example.com/thumbnail.jpg", "THUMBNAIL", 0);
    }

    public static ProductOptionMatrixApiResponse productOptionMatrixApiResponse() {
        return new ProductOptionMatrixApiResponse(
                List.of(sellerOptionGroupApiResponse()), List.of(productDetailApiResponse()));
    }

    public static SellerOptionGroupApiResponse sellerOptionGroupApiResponse() {
        return new SellerOptionGroupApiResponse(
                1L, "색상", 0, List.of(sellerOptionValueApiResponse()));
    }

    public static SellerOptionValueApiResponse sellerOptionValueApiResponse() {
        return new SellerOptionValueApiResponse(10L, 1L, "블랙", 0);
    }

    public static ProductDetailApiResponse productDetailApiResponse() {
        return new ProductDetailApiResponse(
                200L,
                "SKU-BLK-001",
                100_000,
                89_000,
                11,
                100,
                "ACTIVE",
                0,
                List.of(resolvedProductOptionApiResponse()),
                DEFAULT_CREATED_AT_STRING,
                DEFAULT_UPDATED_AT_STRING);
    }

    public static ResolvedProductOptionApiResponse resolvedProductOptionApiResponse() {
        return new ResolvedProductOptionApiResponse(1L, "색상", 10L, "블랙");
    }

    public static ShippingPolicyApiResponse shippingPolicyApiResponse() {
        return new ShippingPolicyApiResponse(
                1L,
                null,
                "기본 배송정책",
                true,
                true,
                "CONDITIONAL_FREE",
                "조건부 무료배송",
                3000,
                50000,
                0,
                0,
                0,
                0,
                0,
                0,
                DEFAULT_CREATED_AT_STRING,
                DEFAULT_CREATED_AT_STRING);
    }

    public static RefundPolicyApiResponse refundPolicyApiResponse() {
        return new RefundPolicyApiResponse(
                1L,
                null,
                "기본 환불정책",
                true,
                true,
                7,
                14,
                List.of(new NonReturnableConditionApiResponse("OPENED_PACKAGING", "포장 개봉")),
                false,
                false,
                0,
                null,
                DEFAULT_CREATED_AT_STRING,
                DEFAULT_CREATED_AT_STRING);
    }

    public static ProductGroupDescriptionApiResponse descriptionApiResponse() {
        return new ProductGroupDescriptionApiResponse(
                300L,
                "<p>상품 상세 설명입니다.</p>",
                "https://cdn.example.com/desc",
                List.of(new DescriptionImageApiResponse(400L, "https://example.com/desc1.jpg", 0)));
    }

    public static ProductNoticeApiResponse productNoticeApiResponse() {
        return new ProductNoticeApiResponse(
                500L,
                List.of(
                        new ProductNoticeEntryApiResponse(600L, 10L, "면 100%"),
                        new ProductNoticeEntryApiResponse(601L, 11L, "블랙")),
                DEFAULT_CREATED_AT_STRING,
                DEFAULT_UPDATED_AT_STRING);
    }
}
