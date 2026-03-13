package com.ryuqq.setof.adapter.out.persistence.productnotice;

import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeEntryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
import java.time.Instant;
import java.util.List;

/**
 * ProductNoticeJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 ProductNotice 관련 JPA 엔티티 객체들을 생성합니다.
 */
public final class ProductNoticeJpaEntityFixtures {

    private ProductNoticeJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 100L;

    public static final Long DEFAULT_ENTRY_ID = 10L;
    public static final String DEFAULT_FIELD_NAME = "소재";
    public static final String DEFAULT_FIELD_VALUE = "면 100%";

    // ===== ProductNoticeJpaEntity Fixtures =====

    /** 기본 상품고시 Entity 생성. */
    public static ProductNoticeJpaEntity activeEntity() {
        Instant now = Instant.now();
        return ProductNoticeJpaEntity.create(DEFAULT_ID, DEFAULT_PRODUCT_GROUP_ID, now, now);
    }

    /** 특정 productGroupId를 가진 상품고시 Entity 생성. */
    public static ProductNoticeJpaEntity activeEntity(Long productGroupId) {
        Instant now = Instant.now();
        return ProductNoticeJpaEntity.create(DEFAULT_ID, productGroupId, now, now);
    }

    /** 신규 생성될 Entity (ID가 null). */
    public static ProductNoticeJpaEntity newEntity() {
        Instant now = Instant.now();
        return ProductNoticeJpaEntity.create(null, DEFAULT_PRODUCT_GROUP_ID, now, now);
    }

    // ===== ProductNoticeEntryJpaEntity Fixtures =====

    /** 기본 고시 항목 Entity 생성. */
    public static ProductNoticeEntryJpaEntity defaultEntryEntity() {
        Instant now = Instant.now();
        return ProductNoticeEntryJpaEntity.create(
                DEFAULT_ENTRY_ID,
                DEFAULT_ID,
                DEFAULT_FIELD_NAME,
                DEFAULT_FIELD_VALUE,
                1,
                now,
                now,
                null);
    }

    /** 특정 noticeId를 가진 고시 항목 Entity 생성. */
    public static ProductNoticeEntryJpaEntity entryEntity(
            Long noticeId, String fieldName, String fieldValue, int sortOrder) {
        Instant now = Instant.now();
        return ProductNoticeEntryJpaEntity.create(
                DEFAULT_ENTRY_ID, noticeId, fieldName, fieldValue, sortOrder, now, now, null);
    }

    /** 삭제된 고시 항목 Entity 생성. */
    public static ProductNoticeEntryJpaEntity deletedEntryEntity() {
        Instant now = Instant.now();
        return ProductNoticeEntryJpaEntity.create(
                DEFAULT_ENTRY_ID + 1,
                DEFAULT_ID,
                DEFAULT_FIELD_NAME,
                DEFAULT_FIELD_VALUE,
                1,
                now,
                now,
                now);
    }

    /** 기본 고시 항목 목록 생성 (4개). */
    public static List<ProductNoticeEntryJpaEntity> defaultEntryEntities() {
        Instant now = Instant.now();
        return List.of(
                ProductNoticeEntryJpaEntity.create(
                        10L, DEFAULT_ID, "소재", "면 100%", 1, now, now, null),
                ProductNoticeEntryJpaEntity.create(
                        11L, DEFAULT_ID, "세탁방법", "손세탁", 2, now, now, null),
                ProductNoticeEntryJpaEntity.create(
                        12L, DEFAULT_ID, "제조국", "대한민국", 3, now, now, null),
                ProductNoticeEntryJpaEntity.create(
                        13L, DEFAULT_ID, "제조자", "테스트제조사", 4, now, now, null));
    }

    /** 빈 고시 항목 목록 생성. */
    public static List<ProductNoticeEntryJpaEntity> emptyEntryEntities() {
        return List.of();
    }
}
