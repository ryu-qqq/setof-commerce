package com.ryuqq.setof.adapter.out.persistence.product;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductOptionMappingJpaEntity;
import java.time.Instant;
import java.util.List;

/**
 * ProductJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 Product 관련 JPA 엔티티 객체들을 생성합니다.
 */
public final class ProductJpaEntityFixtures {

    private ProductJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 1L;
    public static final String DEFAULT_SKU_CODE = "SKU-001";
    public static final int DEFAULT_REGULAR_PRICE = 10000;
    public static final int DEFAULT_CURRENT_PRICE = 10000;
    public static final Integer DEFAULT_SALE_PRICE = 9000;
    public static final int DEFAULT_DISCOUNT_RATE = 10;
    public static final int DEFAULT_STOCK_QUANTITY = 100;
    public static final String DEFAULT_STATUS = "ACTIVE";
    public static final int DEFAULT_SORT_ORDER = 0;

    public static final Long DEFAULT_MAPPING_ID = 1L;
    public static final Long DEFAULT_SELLER_OPTION_VALUE_ID = 1L;

    // ===== ProductJpaEntity Fixtures =====

    /** ACTIVE 상태 상품 Entity 생성. */
    public static ProductJpaEntity activeEntity() {
        Instant now = Instant.now();
        return ProductJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_SKU_CODE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_SALE_PRICE,
                DEFAULT_DISCOUNT_RATE,
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_STATUS,
                DEFAULT_SORT_ORDER,
                now,
                now);
    }

    /** 특정 ID를 가진 ACTIVE 상태 상품 Entity 생성. */
    public static ProductJpaEntity activeEntity(Long id) {
        Instant now = Instant.now();
        return ProductJpaEntity.create(
                id,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_SKU_CODE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_SALE_PRICE,
                DEFAULT_DISCOUNT_RATE,
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_STATUS,
                DEFAULT_SORT_ORDER,
                now,
                now);
    }

    /** 특정 productGroupId를 가진 ACTIVE 상태 상품 Entity 생성. */
    public static ProductJpaEntity activeEntityWithProductGroupId(Long productGroupId) {
        Instant now = Instant.now();
        return ProductJpaEntity.create(
                DEFAULT_ID,
                productGroupId,
                DEFAULT_SKU_CODE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_SALE_PRICE,
                DEFAULT_DISCOUNT_RATE,
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_STATUS,
                DEFAULT_SORT_ORDER,
                now,
                now);
    }

    /** INACTIVE 상태 상품 Entity 생성. */
    public static ProductJpaEntity inactiveEntity() {
        Instant now = Instant.now();
        return ProductJpaEntity.create(
                2L,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_SKU_CODE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_SALE_PRICE,
                DEFAULT_DISCOUNT_RATE,
                DEFAULT_STOCK_QUANTITY,
                "INACTIVE",
                DEFAULT_SORT_ORDER,
                now,
                now);
    }

    /** DELETED 상태 상품 Entity 생성. */
    public static ProductJpaEntity deletedEntity() {
        Instant now = Instant.now();
        return ProductJpaEntity.create(
                3L,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_SKU_CODE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_SALE_PRICE,
                DEFAULT_DISCOUNT_RATE,
                DEFAULT_STOCK_QUANTITY,
                "DELETED",
                DEFAULT_SORT_ORDER,
                now,
                now);
    }

    /** SOLD_OUT 상태 상품 Entity 생성. */
    public static ProductJpaEntity soldOutEntity() {
        Instant now = Instant.now();
        return ProductJpaEntity.create(
                4L,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_SKU_CODE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_SALE_PRICE,
                DEFAULT_DISCOUNT_RATE,
                0,
                "SOLD_OUT",
                DEFAULT_SORT_ORDER,
                now,
                now);
    }

    /** salePrice가 null인 상품 Entity 생성. */
    public static ProductJpaEntity entityWithoutSalePrice() {
        Instant now = Instant.now();
        return ProductJpaEntity.create(
                5L,
                DEFAULT_PRODUCT_GROUP_ID,
                null,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                null,
                0,
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_STATUS,
                DEFAULT_SORT_ORDER,
                now,
                now);
    }

    /** 신규 생성될 Entity (ID가 null). */
    public static ProductJpaEntity newEntity() {
        Instant now = Instant.now();
        return ProductJpaEntity.create(
                null,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_SKU_CODE,
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_SALE_PRICE,
                DEFAULT_DISCOUNT_RATE,
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_STATUS,
                DEFAULT_SORT_ORDER,
                now,
                now);
    }

    // ===== ProductOptionMappingJpaEntity Fixtures =====

    /** 활성 상태 옵션 매핑 Entity 생성. */
    public static ProductOptionMappingJpaEntity activeMappingEntity() {
        return ProductOptionMappingJpaEntity.create(
                DEFAULT_MAPPING_ID, DEFAULT_ID, DEFAULT_SELLER_OPTION_VALUE_ID, false, null);
    }

    /** 활성 상태 옵션 매핑 Entity 생성 (특정 productId). */
    public static ProductOptionMappingJpaEntity activeMappingEntity(Long productId) {
        return ProductOptionMappingJpaEntity.create(
                DEFAULT_MAPPING_ID, productId, DEFAULT_SELLER_OPTION_VALUE_ID, false, null);
    }

    /** 삭제된 옵션 매핑 Entity 생성. */
    public static ProductOptionMappingJpaEntity deletedMappingEntity() {
        Instant now = Instant.now();
        return ProductOptionMappingJpaEntity.create(
                2L, DEFAULT_ID, DEFAULT_SELLER_OPTION_VALUE_ID, true, now);
    }

    /** 기본 옵션 매핑 목록 생성 (활성 2개). */
    public static List<ProductOptionMappingJpaEntity> defaultMappingEntities() {
        return List.of(
                ProductOptionMappingJpaEntity.create(1L, DEFAULT_ID, 1L, false, null),
                ProductOptionMappingJpaEntity.create(2L, DEFAULT_ID, 2L, false, null));
    }

    /** 빈 옵션 매핑 목록 생성. */
    public static List<ProductOptionMappingJpaEntity> emptyMappingEntities() {
        return List.of();
    }
}
