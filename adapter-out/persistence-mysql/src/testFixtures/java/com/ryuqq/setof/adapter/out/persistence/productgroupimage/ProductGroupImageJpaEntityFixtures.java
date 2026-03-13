package com.ryuqq.setof.adapter.out.persistence.productgroupimage;

import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.ProductGroupImageJpaEntity;
import java.time.Instant;
import java.util.List;

/**
 * ProductGroupImageJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 ProductGroupImage 관련 JPA 엔티티 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupImageJpaEntityFixtures {

    private ProductGroupImageJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final Long DEFAULT_DETAIL_ID = 2L;
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 1L;
    public static final String DEFAULT_IMAGE_TYPE_THUMBNAIL = "THUMBNAIL";
    public static final String DEFAULT_IMAGE_TYPE_DETAIL = "DETAIL";
    public static final String DEFAULT_THUMBNAIL_URL = "https://example.com/thumbnail.png";
    public static final String DEFAULT_DETAIL_URL = "https://example.com/detail.png";
    public static final int DEFAULT_SORT_ORDER = 0;

    // ===== ProductGroupImageJpaEntity Fixtures =====

    /** 썸네일 이미지 Entity 생성. */
    public static ProductGroupImageJpaEntity thumbnailEntity() {
        Instant now = Instant.now();
        return ProductGroupImageJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_IMAGE_TYPE_THUMBNAIL,
                DEFAULT_THUMBNAIL_URL,
                0,
                now,
                now,
                null);
    }

    /** 특정 productGroupId를 가진 썸네일 이미지 Entity 생성. */
    public static ProductGroupImageJpaEntity thumbnailEntity(Long productGroupId) {
        Instant now = Instant.now();
        return ProductGroupImageJpaEntity.create(
                DEFAULT_ID,
                productGroupId,
                DEFAULT_IMAGE_TYPE_THUMBNAIL,
                DEFAULT_THUMBNAIL_URL,
                0,
                now,
                now,
                null);
    }

    /** 상세 이미지 Entity 생성. */
    public static ProductGroupImageJpaEntity detailEntity() {
        Instant now = Instant.now();
        return ProductGroupImageJpaEntity.create(
                DEFAULT_DETAIL_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_IMAGE_TYPE_DETAIL,
                DEFAULT_DETAIL_URL,
                1,
                now,
                now,
                null);
    }

    /** 소프트 삭제된 썸네일 이미지 Entity 생성. */
    public static ProductGroupImageJpaEntity deletedThumbnailEntity() {
        Instant now = Instant.now();
        return ProductGroupImageJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_IMAGE_TYPE_THUMBNAIL,
                DEFAULT_THUMBNAIL_URL,
                0,
                now,
                now,
                now);
    }

    /** 신규 생성될 Entity (ID가 null). */
    public static ProductGroupImageJpaEntity newThumbnailEntity() {
        Instant now = Instant.now();
        return ProductGroupImageJpaEntity.create(
                null,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_IMAGE_TYPE_THUMBNAIL,
                DEFAULT_THUMBNAIL_URL,
                0,
                now,
                now,
                null);
    }

    /** 기본 이미지 목록 생성 (썸네일 + 상세). */
    public static List<ProductGroupImageJpaEntity> defaultEntities() {
        return List.of(thumbnailEntity(), detailEntity());
    }

    /** 썸네일만 있는 이미지 목록 생성. */
    public static List<ProductGroupImageJpaEntity> thumbnailOnlyEntities() {
        return List.of(thumbnailEntity());
    }

    /** 빈 이미지 목록 생성. */
    public static List<ProductGroupImageJpaEntity> emptyEntities() {
        return List.of();
    }
}
