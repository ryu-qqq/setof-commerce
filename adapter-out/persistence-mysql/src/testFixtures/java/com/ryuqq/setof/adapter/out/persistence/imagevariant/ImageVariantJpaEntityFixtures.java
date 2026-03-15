package com.ryuqq.setof.adapter.out.persistence.imagevariant;

import com.ryuqq.setof.adapter.out.persistence.imagevariant.entity.ImageVariantJpaEntity;
import java.time.Instant;

/**
 * ImageVariantJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 ImageVariantJpaEntity 관련 객체들을 생성합니다.
 */
public final class ImageVariantJpaEntityFixtures {

    private ImageVariantJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final Long DEFAULT_SOURCE_IMAGE_ID = 100L;
    public static final String DEFAULT_SOURCE_TYPE = "PRODUCT_GROUP_IMAGE";
    public static final String DEFAULT_VARIANT_TYPE = "MEDIUM_WEBP";
    public static final String DEFAULT_RESULT_ASSET_ID = "asset-uuid-001";
    public static final String DEFAULT_VARIANT_URL =
            "https://cdn.example.com/images/variant/600x600.webp";
    public static final Integer DEFAULT_WIDTH = 600;
    public static final Integer DEFAULT_HEIGHT = 600;

    // ===== Entity Fixtures =====

    /** 활성 상태의 이미지 Variant Entity 생성. */
    public static ImageVariantJpaEntity activeEntity() {
        Instant now = Instant.now();
        return ImageVariantJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_SOURCE_IMAGE_ID,
                DEFAULT_SOURCE_TYPE,
                DEFAULT_VARIANT_TYPE,
                DEFAULT_RESULT_ASSET_ID,
                DEFAULT_VARIANT_URL,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                now,
                now,
                null);
    }

    /** ID를 지정한 활성 상태 이미지 Variant Entity 생성. */
    public static ImageVariantJpaEntity activeEntity(Long id) {
        Instant now = Instant.now();
        return ImageVariantJpaEntity.create(
                id,
                DEFAULT_SOURCE_IMAGE_ID,
                DEFAULT_SOURCE_TYPE,
                DEFAULT_VARIANT_TYPE,
                DEFAULT_RESULT_ASSET_ID,
                DEFAULT_VARIANT_URL,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                now,
                now,
                null);
    }

    /** sourceImageId를 지정한 활성 상태 이미지 Variant Entity 생성. */
    public static ImageVariantJpaEntity activeEntityWithSourceImageId(Long sourceImageId) {
        Instant now = Instant.now();
        return ImageVariantJpaEntity.create(
                null,
                sourceImageId,
                DEFAULT_SOURCE_TYPE,
                DEFAULT_VARIANT_TYPE,
                DEFAULT_RESULT_ASSET_ID,
                DEFAULT_VARIANT_URL,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                now,
                now,
                null);
    }

    /** variantType을 지정한 활성 상태 이미지 Variant Entity 생성 (ID는 null로 새 엔티티 생성). */
    public static ImageVariantJpaEntity activeEntityWithVariantType(
            String variantType, Long sourceImageId) {
        Instant now = Instant.now();
        return ImageVariantJpaEntity.create(
                null,
                sourceImageId,
                DEFAULT_SOURCE_TYPE,
                variantType,
                "asset-" + variantType.toLowerCase(),
                "https://cdn.example.com/images/" + variantType.toLowerCase() + ".webp",
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                now,
                now,
                null);
    }

    /** 삭제된 상태 이미지 Variant Entity 생성. */
    public static ImageVariantJpaEntity deletedEntity() {
        Instant now = Instant.now();
        return ImageVariantJpaEntity.create(
                3L,
                DEFAULT_SOURCE_IMAGE_ID,
                DEFAULT_SOURCE_TYPE,
                DEFAULT_VARIANT_TYPE,
                DEFAULT_RESULT_ASSET_ID,
                DEFAULT_VARIANT_URL,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                now,
                now,
                now);
    }

    /** 새로 생성될 Entity (ID가 null). */
    public static ImageVariantJpaEntity newEntity() {
        Instant now = Instant.now();
        return ImageVariantJpaEntity.create(
                null,
                DEFAULT_SOURCE_IMAGE_ID,
                DEFAULT_SOURCE_TYPE,
                DEFAULT_VARIANT_TYPE,
                DEFAULT_RESULT_ASSET_ID,
                DEFAULT_VARIANT_URL,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                now,
                now,
                null);
    }

    /** 삭제된 상태의 새 Entity 생성 (ID는 null). */
    public static ImageVariantJpaEntity newDeletedEntity() {
        Instant now = Instant.now();
        return ImageVariantJpaEntity.create(
                null,
                DEFAULT_SOURCE_IMAGE_ID,
                DEFAULT_SOURCE_TYPE,
                DEFAULT_VARIANT_TYPE,
                DEFAULT_RESULT_ASSET_ID,
                DEFAULT_VARIANT_URL,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                now,
                now,
                now);
    }

    /** width/height가 null인 Entity 생성 (원본 크기 변환 등). ID를 지정하여 toDomain 변환에 사용 가능. */
    public static ImageVariantJpaEntity newEntityWithNullDimension() {
        Instant now = Instant.now();
        return ImageVariantJpaEntity.create(
                5L,
                DEFAULT_SOURCE_IMAGE_ID,
                DEFAULT_SOURCE_TYPE,
                "ORIGINAL_WEBP",
                "asset-original-001",
                "https://cdn.example.com/images/original.webp",
                null,
                null,
                now,
                now,
                null);
    }

    /** sourceImageId와 variantType을 지정한 새 Entity 생성. */
    public static ImageVariantJpaEntity newEntityWith(Long sourceImageId, String variantType) {
        Instant now = Instant.now();
        return ImageVariantJpaEntity.create(
                null,
                sourceImageId,
                DEFAULT_SOURCE_TYPE,
                variantType,
                "asset-" + variantType.toLowerCase() + "-" + sourceImageId,
                "https://cdn.example.com/images/"
                        + sourceImageId
                        + "/"
                        + variantType.toLowerCase()
                        + ".webp",
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                now,
                now,
                null);
    }
}
