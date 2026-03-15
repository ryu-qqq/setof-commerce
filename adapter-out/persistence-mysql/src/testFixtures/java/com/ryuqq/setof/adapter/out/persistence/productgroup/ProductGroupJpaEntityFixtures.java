package com.ryuqq.setof.adapter.out.persistence.productgroup;

import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionValueJpaEntity;
import java.time.Instant;
import java.util.List;

/**
 * ProductGroupJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 ProductGroup 관련 JPA 엔티티 객체들을 생성합니다.
 */
public final class ProductGroupJpaEntityFixtures {

    private ProductGroupJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final Long DEFAULT_SELLER_ID = 1L;
    public static final Long DEFAULT_BRAND_ID = 1L;
    public static final Long DEFAULT_CATEGORY_ID = 1L;
    public static final Long DEFAULT_SHIPPING_POLICY_ID = 1L;
    public static final Long DEFAULT_REFUND_POLICY_ID = 1L;
    public static final String DEFAULT_PRODUCT_GROUP_NAME = "테스트 상품그룹";
    public static final String DEFAULT_OPTION_TYPE = "SINGLE";
    public static final int DEFAULT_REGULAR_PRICE = 50000;
    public static final int DEFAULT_CURRENT_PRICE = 45000;
    public static final String DEFAULT_STATUS = "ACTIVE";

    public static final Long DEFAULT_OPTION_GROUP_ID = 10L;
    public static final Long DEFAULT_OPTION_VALUE_ID = 100L;
    public static final String DEFAULT_OPTION_GROUP_NAME = "색상";
    public static final String DEFAULT_OPTION_VALUE_NAME = "검정";

    // ===== ProductGroupJpaEntity Fixtures =====

    /** ACTIVE 상태 상품그룹 Entity 생성. */
    public static ProductGroupJpaEntity activeEntity() {
        Instant now = Instant.now();
        return ProductGroupJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_SELLER_ID,
                DEFAULT_BRAND_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_STATUS,
                now,
                now,
                null);
    }

    /** 특정 ID를 가진 ACTIVE 상태 상품그룹 Entity 생성. */
    public static ProductGroupJpaEntity activeEntity(Long id) {
        Instant now = Instant.now();
        return ProductGroupJpaEntity.create(
                id,
                DEFAULT_SELLER_ID,
                DEFAULT_BRAND_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_STATUS,
                now,
                now,
                null);
    }

    /** 특정 sellerId를 가진 ACTIVE 상태 상품그룹 Entity 생성. */
    public static ProductGroupJpaEntity activeEntityWithSellerId(Long sellerId) {
        Instant now = Instant.now();
        return ProductGroupJpaEntity.create(
                DEFAULT_ID,
                sellerId,
                DEFAULT_BRAND_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_STATUS,
                now,
                now,
                null);
    }

    /** SOLD_OUT 상태 상품그룹 Entity 생성. */
    public static ProductGroupJpaEntity soldOutEntity() {
        Instant now = Instant.now();
        return ProductGroupJpaEntity.create(
                2L,
                DEFAULT_SELLER_ID,
                DEFAULT_BRAND_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                "SOLD_OUT",
                now,
                now,
                null);
    }

    /** DELETED 상태 상품그룹 Entity 생성 (소프트 삭제). */
    public static ProductGroupJpaEntity deletedEntity() {
        Instant now = Instant.now();
        return ProductGroupJpaEntity.create(
                3L,
                DEFAULT_SELLER_ID,
                DEFAULT_BRAND_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                DEFAULT_OPTION_TYPE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                "DELETED",
                now,
                now,
                now);
    }

    /** COMBINATION 옵션 타입의 상품그룹 Entity 생성. */
    public static ProductGroupJpaEntity combinationOptionEntity() {
        Instant now = Instant.now();
        return ProductGroupJpaEntity.create(
                4L,
                DEFAULT_SELLER_ID,
                DEFAULT_BRAND_ID,
                DEFAULT_CATEGORY_ID,
                DEFAULT_SHIPPING_POLICY_ID,
                DEFAULT_REFUND_POLICY_ID,
                DEFAULT_PRODUCT_GROUP_NAME,
                "COMBINATION",
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_STATUS,
                now,
                now,
                null);
    }

    /** 신규 생성될 Entity (ID가 null). */
    public static ProductGroupJpaEntity newEntity() {
        Instant now = Instant.now();
        return ProductGroupJpaEntity.create(
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
                DEFAULT_STATUS,
                now,
                now,
                null);
    }

    // ===== SellerOptionGroupJpaEntity Fixtures =====

    /** 활성 상태 옵션 그룹 Entity 생성. */
    public static SellerOptionGroupJpaEntity activeOptionGroupEntity() {
        Instant now = Instant.now();
        return SellerOptionGroupJpaEntity.create(
                DEFAULT_OPTION_GROUP_ID,
                DEFAULT_ID,
                DEFAULT_OPTION_GROUP_NAME,
                1,
                false,
                null,
                now,
                now);
    }

    /** 특정 productGroupId를 가진 활성 옵션 그룹 Entity 생성. */
    public static SellerOptionGroupJpaEntity activeOptionGroupEntity(Long productGroupId) {
        Instant now = Instant.now();
        return SellerOptionGroupJpaEntity.create(
                DEFAULT_OPTION_GROUP_ID,
                productGroupId,
                DEFAULT_OPTION_GROUP_NAME,
                1,
                false,
                null,
                now,
                now);
    }

    /** 삭제된 옵션 그룹 Entity 생성. */
    public static SellerOptionGroupJpaEntity deletedOptionGroupEntity() {
        Instant now = Instant.now();
        return SellerOptionGroupJpaEntity.create(
                DEFAULT_OPTION_GROUP_ID + 1,
                DEFAULT_ID,
                DEFAULT_OPTION_GROUP_NAME,
                1,
                true,
                now,
                now,
                now);
    }

    // ===== SellerOptionValueJpaEntity Fixtures =====

    /** 활성 상태 옵션 값 Entity 생성. */
    public static SellerOptionValueJpaEntity activeOptionValueEntity() {
        Instant now = Instant.now();
        return SellerOptionValueJpaEntity.create(
                DEFAULT_OPTION_VALUE_ID,
                DEFAULT_OPTION_GROUP_ID,
                DEFAULT_OPTION_VALUE_NAME,
                1,
                false,
                null,
                now,
                now);
    }

    /** 특정 optionGroupId를 가진 활성 옵션 값 Entity 생성. */
    public static SellerOptionValueJpaEntity activeOptionValueEntity(Long optionGroupId) {
        Instant now = Instant.now();
        return SellerOptionValueJpaEntity.create(
                DEFAULT_OPTION_VALUE_ID,
                optionGroupId,
                DEFAULT_OPTION_VALUE_NAME,
                1,
                false,
                null,
                now,
                now);
    }

    /** 삭제된 옵션 값 Entity 생성. */
    public static SellerOptionValueJpaEntity deletedOptionValueEntity() {
        Instant now = Instant.now();
        return SellerOptionValueJpaEntity.create(
                DEFAULT_OPTION_VALUE_ID + 1,
                DEFAULT_OPTION_GROUP_ID,
                DEFAULT_OPTION_VALUE_NAME,
                1,
                true,
                now,
                now,
                now);
    }

    /** 기본 옵션 그룹 목록 생성 (단일). */
    public static List<SellerOptionGroupJpaEntity> defaultOptionGroupEntities() {
        return List.of(activeOptionGroupEntity());
    }

    /** 기본 옵션 값 목록 생성 (단일). */
    public static List<SellerOptionValueJpaEntity> defaultOptionValueEntities() {
        return List.of(activeOptionValueEntity());
    }

    /** 빈 옵션 그룹 목록 생성. */
    public static List<SellerOptionGroupJpaEntity> emptyOptionGroupEntities() {
        return List.of();
    }

    /** 빈 옵션 값 목록 생성. */
    public static List<SellerOptionValueJpaEntity> emptyOptionValueEntities() {
        return List.of();
    }
}
