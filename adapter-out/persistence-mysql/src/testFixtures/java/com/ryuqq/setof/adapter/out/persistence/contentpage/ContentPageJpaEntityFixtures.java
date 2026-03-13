package com.ryuqq.setof.adapter.out.persistence.contentpage;

import com.ryuqq.setof.adapter.out.persistence.contentpage.entity.ContentPageJpaEntity;
import java.time.Instant;

/**
 * ContentPageJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 ContentPageJpaEntity 관련 객체들을 생성합니다.
 */
public final class ContentPageJpaEntityFixtures {

    private ContentPageJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final String DEFAULT_TITLE = "테스트 콘텐츠 페이지";
    public static final String DEFAULT_MEMO = "테스트 메모";
    public static final String DEFAULT_IMAGE_URL = "https://example.com/image.jpg";
    public static final Instant DEFAULT_DISPLAY_START_AT = Instant.parse("2000-01-01T00:00:00Z");
    public static final Instant DEFAULT_DISPLAY_END_AT = Instant.parse("2099-12-31T23:59:59Z");

    // ===== Entity Fixtures =====

    /** 활성 상태의 콘텐츠 페이지 Entity 생성. */
    public static ContentPageJpaEntity activeEntity() {
        Instant now = Instant.now();
        return ContentPageJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_TITLE,
                DEFAULT_MEMO,
                DEFAULT_IMAGE_URL,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }

    /** ID를 지정한 활성 상태 콘텐츠 페이지 Entity 생성. */
    public static ContentPageJpaEntity activeEntity(Long id) {
        Instant now = Instant.now();
        return ContentPageJpaEntity.create(
                id,
                DEFAULT_TITLE,
                DEFAULT_MEMO,
                DEFAULT_IMAGE_URL,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }

    /** 비활성 상태 콘텐츠 페이지 Entity 생성. */
    public static ContentPageJpaEntity inactiveEntity() {
        Instant now = Instant.now();
        return ContentPageJpaEntity.create(
                2L,
                DEFAULT_TITLE,
                DEFAULT_MEMO,
                DEFAULT_IMAGE_URL,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                null);
    }

    /** 삭제된 상태 콘텐츠 페이지 Entity 생성. */
    public static ContentPageJpaEntity deletedEntity() {
        Instant now = Instant.now();
        return ContentPageJpaEntity.create(
                3L,
                DEFAULT_TITLE,
                DEFAULT_MEMO,
                DEFAULT_IMAGE_URL,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                now);
    }

    /** 새로 생성될 Entity (ID가 null). */
    public static ContentPageJpaEntity newEntity() {
        Instant now = Instant.now();
        return ContentPageJpaEntity.create(
                null,
                DEFAULT_TITLE,
                DEFAULT_MEMO,
                DEFAULT_IMAGE_URL,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }

    /** memo가 null인 Entity 생성. */
    public static ContentPageJpaEntity entityWithoutMemo() {
        Instant now = Instant.now();
        return ContentPageJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_TITLE,
                null,
                DEFAULT_IMAGE_URL,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }
}
