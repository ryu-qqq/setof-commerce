package com.ryuqq.setof.adapter.out.persistence.displaycomponent;

import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.DisplayComponentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.DisplayTabJpaEntity;
import java.time.Instant;

/**
 * DisplayComponentJpaEntity / DisplayTabJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 DisplayComponent 관련 객체들을 생성합니다.
 */
public final class DisplayComponentJpaEntityFixtures {

    private DisplayComponentJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final long DEFAULT_CONTENT_PAGE_ID = 1L;
    public static final String DEFAULT_NAME = "테스트 컴포넌트";
    public static final String DEFAULT_COMPONENT_TYPE = "PRODUCT";
    public static final int DEFAULT_DISPLAY_ORDER = 1;
    public static final String DEFAULT_LIST_TYPE = "TWO_STEP";
    public static final String DEFAULT_ORDER_TYPE = "NONE";
    public static final String DEFAULT_BADGE_TYPE = "NONE";
    public static final int DEFAULT_EXPOSED_PRODUCTS = 10;
    public static final Instant DEFAULT_DISPLAY_START_AT = Instant.parse("2000-01-01T00:00:00Z");
    public static final Instant DEFAULT_DISPLAY_END_AT = Instant.parse("2099-12-31T23:59:59Z");

    // ===== DisplayComponentJpaEntity Fixtures =====

    /** 활성 상태의 PRODUCT 타입 컴포넌트 Entity 생성. */
    public static DisplayComponentJpaEntity activeProductEntity() {
        Instant now = Instant.now();
        return DisplayComponentJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_CONTENT_PAGE_ID,
                DEFAULT_NAME,
                "PRODUCT",
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_LIST_TYPE,
                DEFAULT_ORDER_TYPE,
                DEFAULT_BADGE_TYPE,
                false,
                DEFAULT_EXPOSED_PRODUCTS,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                now,
                now,
                null);
    }

    /** TEXT 타입 컴포넌트 Entity 생성 (specData 포함). */
    public static DisplayComponentJpaEntity textEntity() {
        Instant now = Instant.now();
        return DisplayComponentJpaEntity.create(
                2L,
                DEFAULT_CONTENT_PAGE_ID,
                "텍스트 컴포넌트",
                "TEXT",
                2,
                "NONE",
                DEFAULT_ORDER_TYPE,
                DEFAULT_BADGE_TYPE,
                false,
                0,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "{\"content\":\"텍스트 내용\"}",
                now,
                now,
                null);
    }

    /** TAB 타입 컴포넌트 Entity 생성. */
    public static DisplayComponentJpaEntity tabEntity() {
        Instant now = Instant.now();
        return DisplayComponentJpaEntity.create(
                3L,
                DEFAULT_CONTENT_PAGE_ID,
                "탭 컴포넌트",
                "TAB",
                3,
                DEFAULT_LIST_TYPE,
                DEFAULT_ORDER_TYPE,
                DEFAULT_BADGE_TYPE,
                false,
                20,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "{\"sticky\":false,\"movingType\":\"SCROLL\"}",
                now,
                now,
                null);
    }

    /** ID를 지정한 활성 컴포넌트 Entity 생성. */
    public static DisplayComponentJpaEntity activeEntity(Long id) {
        Instant now = Instant.now();
        return DisplayComponentJpaEntity.create(
                id,
                DEFAULT_CONTENT_PAGE_ID,
                DEFAULT_NAME,
                DEFAULT_COMPONENT_TYPE,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_LIST_TYPE,
                DEFAULT_ORDER_TYPE,
                DEFAULT_BADGE_TYPE,
                false,
                DEFAULT_EXPOSED_PRODUCTS,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                now,
                now,
                null);
    }

    /** 비활성 상태 컴포넌트 Entity 생성. */
    public static DisplayComponentJpaEntity inactiveEntity() {
        Instant now = Instant.now();
        return DisplayComponentJpaEntity.create(
                4L,
                DEFAULT_CONTENT_PAGE_ID,
                DEFAULT_NAME,
                DEFAULT_COMPONENT_TYPE,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_LIST_TYPE,
                DEFAULT_ORDER_TYPE,
                DEFAULT_BADGE_TYPE,
                false,
                DEFAULT_EXPOSED_PRODUCTS,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                now,
                now,
                null);
    }

    /** 삭제된 컴포넌트 Entity 생성. */
    public static DisplayComponentJpaEntity deletedEntity() {
        Instant now = Instant.now();
        return DisplayComponentJpaEntity.create(
                5L,
                DEFAULT_CONTENT_PAGE_ID,
                DEFAULT_NAME,
                DEFAULT_COMPONENT_TYPE,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_LIST_TYPE,
                DEFAULT_ORDER_TYPE,
                DEFAULT_BADGE_TYPE,
                false,
                DEFAULT_EXPOSED_PRODUCTS,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                now,
                now,
                now);
    }

    // ===== DisplayTabJpaEntity Fixtures =====

    /** 기본 탭 Entity 생성. */
    public static DisplayTabJpaEntity activeTabEntity(
            Long id, long componentId, String tabName, int displayOrder) {
        Instant now = Instant.now();
        return DisplayTabJpaEntity.create(id, componentId, tabName, displayOrder, now, null);
    }

    /** 삭제된 탭 Entity 생성. */
    public static DisplayTabJpaEntity deletedTabEntity(Long id, long componentId) {
        Instant now = Instant.now();
        return DisplayTabJpaEntity.create(id, componentId, "삭제된 탭", 1, now, now);
    }
}
