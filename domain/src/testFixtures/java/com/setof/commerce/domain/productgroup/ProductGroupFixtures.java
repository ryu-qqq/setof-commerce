package com.ryuqq.setof.domain.productgroup;

import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroupImage;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupImageId;
import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupName;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.refundpolicy.id.RefundPolicyId;
import com.ryuqq.setof.domain.shippingpolicy.id.ShippingPolicyId;
import java.util.List;

/**
 * ProductGroup 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 ProductGroup 관련 객체들을 생성합니다.
 */
public final class ProductGroupFixtures {

    private ProductGroupFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_PRODUCT_GROUP_NAME = "테스트 상품그룹";
    public static final String DEFAULT_IMAGE_URL = "https://example.com/product-image.png";
    public static final String DEFAULT_DETAIL_IMAGE_URL = "https://example.com/product-detail.png";
    public static final int DEFAULT_REGULAR_PRICE = 50000;
    public static final int DEFAULT_CURRENT_PRICE = 45000;
    public static final int DEFAULT_SALE_PRICE = 40000;
    public static final long DEFAULT_SELLER_ID = 1L;
    public static final long DEFAULT_BRAND_ID = 1L;
    public static final long DEFAULT_CATEGORY_ID = 1L;
    public static final long DEFAULT_SHIPPING_POLICY_ID = 1L;
    public static final long DEFAULT_REFUND_POLICY_ID = 1L;

    // ===== ProductGroup Aggregate Fixtures =====

    /** 신규 상품그룹 생성 (DRAFT 상태) */
    public static ProductGroup newProductGroup() {
        return ProductGroup.forNew(
                CommonVoFixtures.defaultSellerId(),
                BrandId.of(DEFAULT_BRAND_ID),
                CategoryId.of(DEFAULT_CATEGORY_ID),
                ShippingPolicyId.of(DEFAULT_SHIPPING_POLICY_ID),
                RefundPolicyId.of(DEFAULT_REFUND_POLICY_ID),
                defaultProductGroupName(),
                OptionType.SINGLE,
                CommonVoFixtures.money(DEFAULT_REGULAR_PRICE),
                CommonVoFixtures.money(DEFAULT_CURRENT_PRICE),
                CommonVoFixtures.money(DEFAULT_SALE_PRICE),
                CommonVoFixtures.now());
    }

    /** ACTIVE 상태로 복원된 상품그룹 */
    public static ProductGroup activeProductGroup() {
        return activeProductGroup(1L);
    }

    /** ACTIVE 상태로 복원된 상품그룹 (특정 ID) */
    public static ProductGroup activeProductGroup(Long id) {
        return ProductGroup.reconstitute(
                ProductGroupId.of(id),
                CommonVoFixtures.defaultSellerId(),
                BrandId.of(DEFAULT_BRAND_ID),
                CategoryId.of(DEFAULT_CATEGORY_ID),
                ShippingPolicyId.of(DEFAULT_SHIPPING_POLICY_ID),
                RefundPolicyId.of(DEFAULT_REFUND_POLICY_ID),
                defaultProductGroupName(),
                OptionType.SINGLE,
                CommonVoFixtures.money(DEFAULT_REGULAR_PRICE),
                CommonVoFixtures.money(DEFAULT_CURRENT_PRICE),
                CommonVoFixtures.money(DEFAULT_SALE_PRICE),
                ProductGroupStatus.ACTIVE,
                defaultImages(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** INACTIVE 상태로 복원된 상품그룹 */
    public static ProductGroup inactiveProductGroup() {
        return ProductGroup.reconstitute(
                ProductGroupId.of(2L),
                CommonVoFixtures.defaultSellerId(),
                BrandId.of(DEFAULT_BRAND_ID),
                CategoryId.of(DEFAULT_CATEGORY_ID),
                ShippingPolicyId.of(DEFAULT_SHIPPING_POLICY_ID),
                RefundPolicyId.of(DEFAULT_REFUND_POLICY_ID),
                defaultProductGroupName(),
                OptionType.SINGLE,
                CommonVoFixtures.money(DEFAULT_REGULAR_PRICE),
                CommonVoFixtures.money(DEFAULT_CURRENT_PRICE),
                CommonVoFixtures.money(DEFAULT_SALE_PRICE),
                ProductGroupStatus.INACTIVE,
                defaultImages(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** SOLDOUT 상태로 복원된 상품그룹 */
    public static ProductGroup soldOutProductGroup() {
        return ProductGroup.reconstitute(
                ProductGroupId.of(3L),
                CommonVoFixtures.defaultSellerId(),
                BrandId.of(DEFAULT_BRAND_ID),
                CategoryId.of(DEFAULT_CATEGORY_ID),
                ShippingPolicyId.of(DEFAULT_SHIPPING_POLICY_ID),
                RefundPolicyId.of(DEFAULT_REFUND_POLICY_ID),
                defaultProductGroupName(),
                OptionType.SINGLE,
                CommonVoFixtures.money(DEFAULT_REGULAR_PRICE),
                CommonVoFixtures.money(DEFAULT_CURRENT_PRICE),
                CommonVoFixtures.money(DEFAULT_SALE_PRICE),
                ProductGroupStatus.SOLDOUT,
                defaultImages(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** DELETED 상태로 복원된 상품그룹 */
    public static ProductGroup deletedProductGroup() {
        return ProductGroup.reconstitute(
                ProductGroupId.of(4L),
                CommonVoFixtures.defaultSellerId(),
                BrandId.of(DEFAULT_BRAND_ID),
                CategoryId.of(DEFAULT_CATEGORY_ID),
                ShippingPolicyId.of(DEFAULT_SHIPPING_POLICY_ID),
                RefundPolicyId.of(DEFAULT_REFUND_POLICY_ID),
                defaultProductGroupName(),
                OptionType.SINGLE,
                CommonVoFixtures.money(DEFAULT_REGULAR_PRICE),
                CommonVoFixtures.money(DEFAULT_CURRENT_PRICE),
                CommonVoFixtures.money(DEFAULT_SALE_PRICE),
                ProductGroupStatus.DELETED,
                defaultImages(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** DRAFT 상태로 복원된 상품그룹 */
    public static ProductGroup draftProductGroup() {
        return ProductGroup.reconstitute(
                ProductGroupId.of(5L),
                CommonVoFixtures.defaultSellerId(),
                BrandId.of(DEFAULT_BRAND_ID),
                CategoryId.of(DEFAULT_CATEGORY_ID),
                ShippingPolicyId.of(DEFAULT_SHIPPING_POLICY_ID),
                RefundPolicyId.of(DEFAULT_REFUND_POLICY_ID),
                defaultProductGroupName(),
                OptionType.SINGLE,
                CommonVoFixtures.money(DEFAULT_REGULAR_PRICE),
                CommonVoFixtures.money(DEFAULT_CURRENT_PRICE),
                CommonVoFixtures.money(DEFAULT_SALE_PRICE),
                ProductGroupStatus.DRAFT,
                defaultImages(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== VO Fixtures =====

    /** 기본 상품그룹명 VO */
    public static ProductGroupName defaultProductGroupName() {
        return ProductGroupName.of(DEFAULT_PRODUCT_GROUP_NAME);
    }

    /** 기본 이미지 URL VO */
    public static ImageUrl defaultImageUrl() {
        return ImageUrl.of(DEFAULT_IMAGE_URL);
    }

    // ===== ProductGroupImage Fixtures =====

    /** 썸네일 이미지 */
    public static ProductGroupImage thumbnailImage() {
        return ProductGroupImage.reconstitute(
                ProductGroupImageId.of(1L), ImageType.THUMBNAIL, defaultImageUrl(), 1);
    }

    /** 상세 이미지 */
    public static ProductGroupImage detailImage() {
        return ProductGroupImage.reconstitute(
                ProductGroupImageId.of(2L),
                ImageType.DETAIL,
                ImageUrl.of(DEFAULT_DETAIL_IMAGE_URL),
                2);
    }

    /** 기본 이미지 목록 (썸네일 + 상세) */
    public static List<ProductGroupImage> defaultImages() {
        return List.of(thumbnailImage(), detailImage());
    }
}
