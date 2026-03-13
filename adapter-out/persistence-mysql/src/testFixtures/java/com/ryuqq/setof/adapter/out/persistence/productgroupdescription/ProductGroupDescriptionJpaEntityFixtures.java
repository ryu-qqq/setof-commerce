package com.ryuqq.setof.adapter.out.persistence.productgroupdescription;

import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.DescriptionImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.ProductGroupDescriptionJpaEntity;
import java.time.Instant;
import java.util.List;

/**
 * ProductGroupDescriptionJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 ProductGroupDescription 관련 JPA 엔티티 객체들을 생성합니다.
 */
public final class ProductGroupDescriptionJpaEntityFixtures {

    private ProductGroupDescriptionJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 100L;
    public static final String DEFAULT_CONTENT = "<p>상품 상세설명입니다</p>";
    public static final String DEFAULT_CDN_PATH = "https://cdn.example.com/descriptions/100";

    public static final Long DEFAULT_IMAGE_ID = 1L;
    public static final String DEFAULT_IMAGE_URL = "https://cdn.example.com/desc/image1.jpg";

    // ===== ProductGroupDescriptionJpaEntity Fixtures =====

    /** 기본 상세설명 Entity 생성. */
    public static ProductGroupDescriptionJpaEntity activeEntity() {
        Instant now = Instant.now();
        return ProductGroupDescriptionJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_CONTENT,
                DEFAULT_CDN_PATH,
                now,
                now,
                null);
    }

    /** 특정 productGroupId를 가진 상세설명 Entity 생성. */
    public static ProductGroupDescriptionJpaEntity activeEntity(Long productGroupId) {
        Instant now = Instant.now();
        return ProductGroupDescriptionJpaEntity.create(
                DEFAULT_ID, productGroupId, DEFAULT_CONTENT, DEFAULT_CDN_PATH, now, now, null);
    }

    /** content가 null인 상세설명 Entity 생성. */
    public static ProductGroupDescriptionJpaEntity entityWithNullContent() {
        Instant now = Instant.now();
        return ProductGroupDescriptionJpaEntity.create(
                2L, DEFAULT_PRODUCT_GROUP_ID, null, null, now, now, null);
    }

    /** 소프트 삭제된 상세설명 Entity 생성. */
    public static ProductGroupDescriptionJpaEntity deletedEntity() {
        Instant now = Instant.now();
        return ProductGroupDescriptionJpaEntity.create(
                3L, DEFAULT_PRODUCT_GROUP_ID, DEFAULT_CONTENT, DEFAULT_CDN_PATH, now, now, now);
    }

    /** 신규 생성될 Entity (ID가 null). */
    public static ProductGroupDescriptionJpaEntity newEntity() {
        Instant now = Instant.now();
        return ProductGroupDescriptionJpaEntity.create(
                null, DEFAULT_PRODUCT_GROUP_ID, DEFAULT_CONTENT, DEFAULT_CDN_PATH, now, now, null);
    }

    // ===== DescriptionImageJpaEntity Fixtures =====

    /** 기본 설명 이미지 Entity 생성. */
    public static DescriptionImageJpaEntity descriptionImageEntity() {
        return DescriptionImageJpaEntity.create(DEFAULT_IMAGE_ID, DEFAULT_ID, DEFAULT_IMAGE_URL, 0);
    }

    /** 특정 descriptionId를 가진 설명 이미지 Entity 생성. */
    public static DescriptionImageJpaEntity descriptionImageEntity(Long descriptionId) {
        return DescriptionImageJpaEntity.create(
                DEFAULT_IMAGE_ID, descriptionId, DEFAULT_IMAGE_URL, 0);
    }

    /** 기본 이미지 목록 생성 (3개). */
    public static List<DescriptionImageJpaEntity> defaultImageEntities() {
        return List.of(
                DescriptionImageJpaEntity.create(
                        1L, DEFAULT_ID, "https://cdn.example.com/desc/image1.jpg", 0),
                DescriptionImageJpaEntity.create(
                        2L, DEFAULT_ID, "https://cdn.example.com/desc/image2.jpg", 1),
                DescriptionImageJpaEntity.create(
                        3L, DEFAULT_ID, "https://cdn.example.com/desc/image3.jpg", 2));
    }

    /** 빈 이미지 목록 생성. */
    public static List<DescriptionImageJpaEntity> emptyImageEntities() {
        return List.of();
    }
}
