package com.ryuqq.setof.domain.productgroup;

import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import com.ryuqq.setof.domain.productgroup.vo.OptionGroupName;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.OptionValueName;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupName;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import com.ryuqq.setof.domain.productgroupimage.id.ProductGroupImageId;
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

    // ===== SellerOptionGroup/Value 상수 =====
    public static final String DEFAULT_OPTION_GROUP_NAME = "색상";
    public static final String DEFAULT_OPTION_GROUP_NAME_2 = "사이즈";
    public static final String DEFAULT_OPTION_VALUE_NAME = "검정";
    public static final String DEFAULT_OPTION_VALUE_NAME_2 = "흰색";

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

    /** 신규 상품그룹 생성 (ACTIVE 상태) */
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
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** SOLD_OUT 상태로 복원된 상품그룹 */
    public static ProductGroup soldOutProductGroup() {
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
                ProductGroupStatus.SOLD_OUT,
                defaultImages(),
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** DELETED 상태로 복원된 상품그룹 */
    public static ProductGroup deletedProductGroup() {
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
                ProductGroupStatus.DELETED,
                defaultImages(),
                List.of(),
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

    // ===== SellerOptionGroupId Fixtures =====

    /** 기본 SellerOptionGroupId (non-null) */
    public static SellerOptionGroupId defaultSellerOptionGroupId() {
        return SellerOptionGroupId.of(10L);
    }

    /** 신규 SellerOptionGroupId (null value) */
    public static SellerOptionGroupId newSellerOptionGroupId() {
        return SellerOptionGroupId.forNew();
    }

    // ===== SellerOptionValueId Fixtures =====

    /** 기본 SellerOptionValueId (non-null) */
    public static SellerOptionValueId defaultSellerOptionValueId() {
        return SellerOptionValueId.of(100L);
    }

    /** 신규 SellerOptionValueId (null value) */
    public static SellerOptionValueId newSellerOptionValueId() {
        return SellerOptionValueId.forNew();
    }

    // ===== OptionGroupName / OptionValueName Fixtures =====

    /** 기본 OptionGroupName */
    public static OptionGroupName defaultOptionGroupName() {
        return OptionGroupName.of(DEFAULT_OPTION_GROUP_NAME);
    }

    /** 기본 OptionValueName */
    public static OptionValueName defaultOptionValueName() {
        return OptionValueName.of(DEFAULT_OPTION_VALUE_NAME);
    }

    // ===== SellerOptionValue Fixtures =====

    /** 신규 SellerOptionValue (forNew) */
    public static SellerOptionValue newSellerOptionValue() {
        return SellerOptionValue.forNew(defaultSellerOptionGroupId(), defaultOptionValueName(), 1);
    }

    /** 영속성에서 복원된 활성 SellerOptionValue */
    public static SellerOptionValue activeSellerOptionValue() {
        return SellerOptionValue.reconstitute(
                defaultSellerOptionValueId(),
                defaultSellerOptionGroupId(),
                defaultOptionValueName(),
                1,
                DeletionStatus.active());
    }

    /** 영속성에서 복원된 활성 SellerOptionValue (특정 ID, 이름) */
    public static SellerOptionValue activeSellerOptionValue(Long id, String name) {
        return SellerOptionValue.reconstitute(
                SellerOptionValueId.of(id),
                defaultSellerOptionGroupId(),
                OptionValueName.of(name),
                1,
                DeletionStatus.active());
    }

    /** 삭제된 SellerOptionValue */
    public static SellerOptionValue deletedSellerOptionValue() {
        return SellerOptionValue.reconstitute(
                defaultSellerOptionValueId(),
                defaultSellerOptionGroupId(),
                defaultOptionValueName(),
                1,
                DeletionStatus.deletedAt(CommonVoFixtures.yesterday()));
    }

    // ===== SellerOptionGroup Fixtures =====

    /** 신규 SellerOptionGroup (forNew) */
    public static SellerOptionGroup newSellerOptionGroup() {
        return SellerOptionGroup.forNew(
                ProductGroupId.of(1L),
                defaultOptionGroupName(),
                1,
                List.of(newSellerOptionValue()));
    }

    /** 영속성에서 복원된 활성 SellerOptionGroup */
    public static SellerOptionGroup activeSellerOptionGroup() {
        return SellerOptionGroup.reconstitute(
                defaultSellerOptionGroupId(),
                ProductGroupId.of(1L),
                defaultOptionGroupName(),
                1,
                List.of(activeSellerOptionValue()),
                DeletionStatus.active());
    }

    /** 영속성에서 복원된 활성 SellerOptionGroup (특정 ID, 이름) */
    public static SellerOptionGroup activeSellerOptionGroup(Long id, String name) {
        return SellerOptionGroup.reconstitute(
                SellerOptionGroupId.of(id),
                ProductGroupId.of(1L),
                OptionGroupName.of(name),
                1,
                List.of(activeSellerOptionValue()),
                DeletionStatus.active());
    }

    /** 빈 옵션 값을 가진 SellerOptionGroup */
    public static SellerOptionGroup sellerOptionGroupWithNoValues() {
        return SellerOptionGroup.reconstitute(
                defaultSellerOptionGroupId(),
                ProductGroupId.of(1L),
                defaultOptionGroupName(),
                1,
                List.of(),
                DeletionStatus.active());
    }

    /** 삭제된 SellerOptionGroup */
    public static SellerOptionGroup deletedSellerOptionGroup() {
        return SellerOptionGroup.reconstitute(
                defaultSellerOptionGroupId(),
                ProductGroupId.of(1L),
                defaultOptionGroupName(),
                1,
                List.of(deletedSellerOptionValue()),
                DeletionStatus.deletedAt(CommonVoFixtures.yesterday()));
    }

    /** 기본 SellerOptionGroup 목록 (단일 그룹) */
    public static List<SellerOptionGroup> defaultSellerOptionGroups() {
        return List.of(activeSellerOptionGroup());
    }

    /** 두 개의 SellerOptionGroup 목록 (COMBINATION 옵션용) */
    public static List<SellerOptionGroup> combinationSellerOptionGroups() {
        return List.of(
                activeSellerOptionGroup(10L, DEFAULT_OPTION_GROUP_NAME),
                activeSellerOptionGroup(11L, DEFAULT_OPTION_GROUP_NAME_2));
    }
}
