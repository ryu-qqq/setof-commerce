package com.ryuqq.setof.adapter.out.persistence.brand;

import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import java.time.Instant;

/**
 * BrandJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 BrandJpaEntity 관련 객체들을 생성합니다.
 */
public final class BrandJpaEntityFixtures {

    private BrandJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final String DEFAULT_BRAND_NAME = "테스트브랜드";
    public static final String DEFAULT_ICON_URL = "https://example.com/brand-icon.png";
    public static final String DEFAULT_DISPLAY_KOREAN_NAME = "테스트 브랜드";
    public static final String DEFAULT_DISPLAY_ENGLISH_NAME = "Test Brand";
    public static final int DEFAULT_DISPLAY_ORDER = 1;

    // ===== Entity Fixtures =====

    /** 활성 상태의 브랜드 Entity 생성. */
    public static BrandJpaEntity activeEntity() {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_ICON_URL,
                DEFAULT_DISPLAY_KOREAN_NAME,
                DEFAULT_DISPLAY_ENGLISH_NAME,
                DEFAULT_DISPLAY_ORDER,
                true,
                now,
                now,
                null);
    }

    /** ID를 지정한 활성 상태 브랜드 Entity 생성. */
    public static BrandJpaEntity activeEntity(Long id) {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                id,
                DEFAULT_BRAND_NAME,
                DEFAULT_ICON_URL,
                DEFAULT_DISPLAY_KOREAN_NAME,
                DEFAULT_DISPLAY_ENGLISH_NAME,
                DEFAULT_DISPLAY_ORDER,
                true,
                now,
                now,
                null);
    }

    /** 커스텀 브랜드명을 가진 활성 상태 브랜드 Entity 생성. */
    public static BrandJpaEntity activeEntityWithName(String brandName, String displayKoreanName) {
        return activeEntityWithName(brandName, displayKoreanName, brandName);
    }

    /** 커스텀 브랜드명을 가진 활성 상태 브랜드 Entity 생성 (한글/영문 표시명 개별 지정). */
    public static BrandJpaEntity activeEntityWithName(
            String brandName, String displayKoreanName, String displayEnglishName) {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                null,
                brandName,
                DEFAULT_ICON_URL,
                displayKoreanName,
                displayEnglishName,
                DEFAULT_DISPLAY_ORDER,
                true,
                now,
                now,
                null);
    }

    /** 비활성 상태 브랜드 Entity 생성. */
    public static BrandJpaEntity inactiveEntity() {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                2L,
                DEFAULT_BRAND_NAME,
                DEFAULT_ICON_URL,
                DEFAULT_DISPLAY_KOREAN_NAME,
                DEFAULT_DISPLAY_ENGLISH_NAME,
                DEFAULT_DISPLAY_ORDER,
                false,
                now,
                now,
                null);
    }

    /** 삭제된 상태 브랜드 Entity 생성. */
    public static BrandJpaEntity deletedEntity() {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                3L,
                DEFAULT_BRAND_NAME,
                DEFAULT_ICON_URL,
                DEFAULT_DISPLAY_KOREAN_NAME,
                DEFAULT_DISPLAY_ENGLISH_NAME,
                DEFAULT_DISPLAY_ORDER,
                false,
                now,
                now,
                now);
    }

    /** 새로 생성될 Entity (ID가 null). */
    public static BrandJpaEntity newEntity() {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                null,
                DEFAULT_BRAND_NAME,
                DEFAULT_ICON_URL,
                DEFAULT_DISPLAY_KOREAN_NAME,
                DEFAULT_DISPLAY_ENGLISH_NAME,
                DEFAULT_DISPLAY_ORDER,
                true,
                now,
                now,
                null);
    }

    /** 표시 순서가 다른 Entity 생성. */
    public static BrandJpaEntity entityWithDisplayOrder(int displayOrder) {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_BRAND_NAME,
                DEFAULT_ICON_URL,
                DEFAULT_DISPLAY_KOREAN_NAME,
                DEFAULT_DISPLAY_ENGLISH_NAME,
                displayOrder,
                true,
                now,
                now,
                null);
    }

    /** 비활성 상태의 새 Entity 생성 (ID는 null). */
    public static BrandJpaEntity newInactiveEntity() {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                null,
                DEFAULT_BRAND_NAME,
                DEFAULT_ICON_URL,
                DEFAULT_DISPLAY_KOREAN_NAME,
                DEFAULT_DISPLAY_ENGLISH_NAME,
                DEFAULT_DISPLAY_ORDER,
                false,
                now,
                now,
                null);
    }

    /** 삭제된 상태의 새 Entity 생성 (ID는 null). */
    public static BrandJpaEntity newDeletedEntity() {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                null,
                DEFAULT_BRAND_NAME,
                DEFAULT_ICON_URL,
                DEFAULT_DISPLAY_KOREAN_NAME,
                DEFAULT_DISPLAY_ENGLISH_NAME,
                DEFAULT_DISPLAY_ORDER,
                false,
                now,
                now,
                now);
    }
}
