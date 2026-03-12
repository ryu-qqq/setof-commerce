package com.ryuqq.setof.adapter.in.rest.admin.productgroup;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest.DescriptionApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest.DescriptionImageApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest.ImageApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest.NoticeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest.NoticeEntryApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest.OptionGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest.OptionValueApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest.ProductApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest.SelectedOptionApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupBasicInfoApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupFullApiRequest;
import java.util.List;

/**
 * ProductGroup API 테스트 Fixtures.
 *
 * <p>상품 그룹 API 테스트에서 사용되는 Request 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupApiFixtures {

    private ProductGroupApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 1L;
    public static final Long DEFAULT_SELLER_ID = 10L;
    public static final Long DEFAULT_BRAND_ID = 2L;
    public static final Long DEFAULT_CATEGORY_ID = 100L;
    public static final Long DEFAULT_SHIPPING_POLICY_ID = 1L;
    public static final Long DEFAULT_REFUND_POLICY_ID = 1L;
    public static final String DEFAULT_PRODUCT_GROUP_NAME = "나이키 에어맥스 90";
    public static final String DEFAULT_OPTION_TYPE = "SINGLE";
    public static final int DEFAULT_REGULAR_PRICE = 100_000;
    public static final int DEFAULT_CURRENT_PRICE = 89_000;

    // ===== Register Request Fixtures =====

    public static RegisterProductGroupApiRequest registerRequest() {
        return new RegisterProductGroupApiRequest(
                null,
                DEFAULT_SELLER_ID,
                DEFAULT_BRAND_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                List.of(thumbnailImageRequest()),
                List.of(singleOptionGroupRequest()),
                List.of(productRequest()),
                descriptionRequest(),
                noticeRequest());
    }

    public static RegisterProductGroupApiRequest registerRequestWithoutOptions() {
        return new RegisterProductGroupApiRequest(
                null,
                DEFAULT_SELLER_ID,
                DEFAULT_BRAND_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                "NONE",
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                List.of(thumbnailImageRequest()),
                null,
                List.of(productRequestWithNoOptions()),
                null,
                null);
    }

    public static RegisterProductGroupApiRequest registerRequestWithCombinationOptions() {
        return new RegisterProductGroupApiRequest(
                null,
                DEFAULT_SELLER_ID,
                DEFAULT_BRAND_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                "COMBINATION",
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                List.of(thumbnailImageRequest()),
                List.of(colorOptionGroupRequest(), sizeOptionGroupRequest()),
                List.of(productRequest()),
                null,
                null);
    }

    // ===== UpdateFull Request Fixtures =====

    public static UpdateProductGroupFullApiRequest updateFullRequest() {
        return new UpdateProductGroupFullApiRequest(
                DEFAULT_PRODUCT_GROUP_NAME + " (수정)",
                DEFAULT_BRAND_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID,
                DEFAULT_OPTION_TYPE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                List.of(updateFullImageRequest()),
                List.of(updateFullOptionGroupRequest()),
                List.of(updateFullProductRequest()),
                updateFullDescriptionRequest(),
                updateFullNoticeRequest());
    }

    public static UpdateProductGroupFullApiRequest updateFullRequestWithoutOptions() {
        return new UpdateProductGroupFullApiRequest(
                DEFAULT_PRODUCT_GROUP_NAME + " (수정)",
                DEFAULT_BRAND_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID,
                "NONE",
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                List.of(updateFullImageRequest()),
                null,
                List.of(updateFullProductRequestWithNoOptions()),
                null,
                null);
    }

    // ===== UpdateBasicInfo Request Fixtures =====

    public static UpdateProductGroupBasicInfoApiRequest updateBasicInfoRequest() {
        return new UpdateProductGroupBasicInfoApiRequest(
                DEFAULT_PRODUCT_GROUP_NAME + " (기본정보 수정)",
                DEFAULT_BRAND_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID);
    }

    public static UpdateProductGroupBasicInfoApiRequest updateBasicInfoRequest(
            String productGroupName, Long brandId, Long categoryId) {
        return new UpdateProductGroupBasicInfoApiRequest(
                productGroupName,
                brandId,
                categoryId,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID);
    }

    // ===== Inner Request Fixtures =====

    public static ImageApiRequest thumbnailImageRequest() {
        return new ImageApiRequest("THUMBNAIL", "https://example.com/thumbnail.jpg", 0);
    }

    public static ImageApiRequest detailImageRequest() {
        return new ImageApiRequest("DETAIL", "https://example.com/detail.jpg", 1);
    }

    public static OptionGroupApiRequest singleOptionGroupRequest() {
        return new OptionGroupApiRequest(
                "색상",
                0,
                List.of(new OptionValueApiRequest("블랙", 0), new OptionValueApiRequest("화이트", 1)));
    }

    public static OptionGroupApiRequest colorOptionGroupRequest() {
        return new OptionGroupApiRequest(
                "색상",
                0,
                List.of(new OptionValueApiRequest("블랙", 0), new OptionValueApiRequest("화이트", 1)));
    }

    public static OptionGroupApiRequest sizeOptionGroupRequest() {
        return new OptionGroupApiRequest(
                "사이즈",
                1,
                List.of(
                        new OptionValueApiRequest("S", 0),
                        new OptionValueApiRequest("M", 1),
                        new OptionValueApiRequest("L", 2)));
    }

    public static ProductApiRequest productRequest() {
        return new ProductApiRequest(
                null,
                "SKU-001",
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                100,
                0,
                List.of(new SelectedOptionApiRequest("색상", "블랙")));
    }

    public static ProductApiRequest productRequestWithNoOptions() {
        return new ProductApiRequest(
                null, "SKU-001", DEFAULT_REGULAR_PRICE, DEFAULT_CURRENT_PRICE, 100, 0, List.of());
    }

    public static DescriptionApiRequest descriptionRequest() {
        return new DescriptionApiRequest(
                "<p>상품 상세 설명입니다.</p>",
                List.of(
                        new DescriptionImageApiRequest("https://example.com/desc1.jpg", 0),
                        new DescriptionImageApiRequest("https://example.com/desc2.jpg", 1)));
    }

    public static NoticeApiRequest noticeRequest() {
        return new NoticeApiRequest(
                List.of(
                        new NoticeEntryApiRequest(1L, "소재", "면 100%"),
                        new NoticeEntryApiRequest(2L, "색상", "블랙")));
    }

    // ===== UpdateFull Inner Request Fixtures =====

    public static UpdateProductGroupFullApiRequest.ImageApiRequest updateFullImageRequest() {
        return new UpdateProductGroupFullApiRequest.ImageApiRequest(
                "THUMBNAIL", "https://example.com/thumbnail-updated.jpg", 0);
    }

    public static UpdateProductGroupFullApiRequest.OptionGroupApiRequest
            updateFullOptionGroupRequest() {
        return new UpdateProductGroupFullApiRequest.OptionGroupApiRequest(
                1L,
                "색상",
                0,
                List.of(
                        new UpdateProductGroupFullApiRequest.OptionValueApiRequest(1L, "블랙", 0),
                        new UpdateProductGroupFullApiRequest.OptionValueApiRequest(null, "레드", 1)));
    }

    public static UpdateProductGroupFullApiRequest.ProductApiRequest updateFullProductRequest() {
        return new UpdateProductGroupFullApiRequest.ProductApiRequest(
                1L,
                "SKU-001-UPD",
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                50,
                0,
                List.of(new UpdateProductGroupFullApiRequest.SelectedOptionApiRequest("색상", "블랙")));
    }

    public static UpdateProductGroupFullApiRequest.ProductApiRequest
            updateFullProductRequestWithNoOptions() {
        return new UpdateProductGroupFullApiRequest.ProductApiRequest(
                1L, "SKU-001-UPD", DEFAULT_REGULAR_PRICE, DEFAULT_CURRENT_PRICE, 50, 0, List.of());
    }

    public static UpdateProductGroupFullApiRequest.DescriptionApiRequest
            updateFullDescriptionRequest() {
        return new UpdateProductGroupFullApiRequest.DescriptionApiRequest(
                "<p>수정된 상품 상세 설명입니다.</p>",
                List.of(
                        new UpdateProductGroupFullApiRequest.DescriptionImageApiRequest(
                                "https://example.com/desc-updated.jpg", 0)));
    }

    public static UpdateProductGroupFullApiRequest.NoticeApiRequest updateFullNoticeRequest() {
        return new UpdateProductGroupFullApiRequest.NoticeApiRequest(
                List.of(
                        new UpdateProductGroupFullApiRequest.NoticeEntryApiRequest(
                                1L, "소재", "폴리에스터 100%"),
                        new UpdateProductGroupFullApiRequest.NoticeEntryApiRequest(
                                2L, "색상", "블랙/레드")));
    }
}
