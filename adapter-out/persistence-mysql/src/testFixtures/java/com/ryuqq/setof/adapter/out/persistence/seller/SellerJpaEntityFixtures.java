package com.ryuqq.setof.adapter.out.persistence.seller;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import java.time.Instant;

/**
 * SellerJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 SellerJpaEntity 관련 객체들을 생성합니다.
 */
public final class SellerJpaEntityFixtures {

    private SellerJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final String DEFAULT_SELLER_NAME = "테스트 셀러";
    public static final String DEFAULT_DISPLAY_NAME = "테스트 셀러 스토어";
    public static final String DEFAULT_LOGO_URL = "https://example.com/logo.png";
    public static final String DEFAULT_DESCRIPTION = "테스트 셀러 설명입니다.";

    // ===== Entity Fixtures =====

    /** 활성 상태의 셀러 Entity 생성. */
    public static SellerJpaEntity activeEntity() {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_DESCRIPTION,
                true,
                now,
                now,
                null);
    }

    /** ID를 지정한 활성 상태 셀러 Entity 생성. */
    public static SellerJpaEntity activeEntity(Long id) {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                id,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_DESCRIPTION,
                true,
                now,
                now,
                null);
    }

    /** 커스텀 셀러명을 가진 활성 상태 셀러 Entity 생성. ID는 null로 새 엔티티 생성. */
    public static SellerJpaEntity activeEntityWithName(String sellerName, String displayName) {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                null,
                sellerName,
                displayName,
                DEFAULT_LOGO_URL,
                DEFAULT_DESCRIPTION,
                true,
                now,
                now,
                null);
    }

    /** 비활성 상태 셀러 Entity 생성. */
    public static SellerJpaEntity inactiveEntity() {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                2L,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_DESCRIPTION,
                false,
                now,
                now,
                null);
    }

    /** 삭제된 상태 셀러 Entity 생성. */
    public static SellerJpaEntity deletedEntity() {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                3L,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_DESCRIPTION,
                false,
                now,
                now,
                now);
    }

    /** 새로 생성될 Entity (ID가 null). */
    public static SellerJpaEntity newEntity() {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                null,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_DESCRIPTION,
                true,
                now,
                now,
                null);
    }

    /** 로고 URL이 없는 Entity 생성. */
    public static SellerJpaEntity entityWithoutLogoUrl() {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                null,
                DEFAULT_DESCRIPTION,
                true,
                now,
                now,
                null);
    }

    /** 설명이 없는 Entity 생성. */
    public static SellerJpaEntity entityWithoutDescription() {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_LOGO_URL,
                null,
                true,
                now,
                now,
                null);
    }

    /** 로고 URL이 없는 새 Entity 생성 (ID는 null). */
    public static SellerJpaEntity newEntityWithoutLogoUrl() {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                null,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                null,
                DEFAULT_DESCRIPTION,
                true,
                now,
                now,
                null);
    }

    /** 설명이 없는 새 Entity 생성 (ID는 null). */
    public static SellerJpaEntity newEntityWithoutDescription() {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                null,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_LOGO_URL,
                null,
                true,
                now,
                now,
                null);
    }

    /** 비활성 상태의 새 Entity 생성 (ID는 null). */
    public static SellerJpaEntity newInactiveEntity() {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                null,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_DESCRIPTION,
                false,
                now,
                now,
                null);
    }

    /** 삭제된 상태의 새 Entity 생성 (ID는 null). */
    public static SellerJpaEntity newDeletedEntity() {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                null,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_DESCRIPTION,
                false,
                now,
                now,
                now);
    }

    /** 커스텀 셀러명을 가진 비활성 상태 셀러 Entity 생성. ID는 null로 새 엔티티 생성. */
    public static SellerJpaEntity inactiveEntityWithName(String sellerName, String displayName) {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                null,
                sellerName,
                displayName,
                DEFAULT_LOGO_URL,
                DEFAULT_DESCRIPTION,
                false,
                now,
                now,
                null);
    }

    /** 커스텀 셀러명을 가진 삭제 상태 셀러 Entity 생성. ID는 null로 새 엔티티 생성. */
    public static SellerJpaEntity deletedEntityWithName(String sellerName, String displayName) {
        Instant now = Instant.now();
        return SellerJpaEntity.create(
                null,
                sellerName,
                displayName,
                DEFAULT_LOGO_URL,
                DEFAULT_DESCRIPTION,
                false,
                now,
                now,
                now);
    }
}
