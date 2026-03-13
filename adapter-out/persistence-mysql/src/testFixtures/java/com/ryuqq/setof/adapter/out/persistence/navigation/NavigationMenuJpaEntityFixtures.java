package com.ryuqq.setof.adapter.out.persistence.navigation;

import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import java.time.Instant;

/**
 * NavigationMenuJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 NavigationMenuJpaEntity 관련 객체들을 생성합니다.
 */
public final class NavigationMenuJpaEntityFixtures {

    private NavigationMenuJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final String DEFAULT_TITLE = "홈";
    public static final String DEFAULT_LINK_URL = "https://example.com/home";
    public static final int DEFAULT_DISPLAY_ORDER = 1;
    public static final Instant DEFAULT_DISPLAY_START_AT = Instant.now().minusSeconds(3600);
    public static final Instant DEFAULT_DISPLAY_END_AT = Instant.now().plusSeconds(86400);

    // ===== Entity Fixtures =====

    /** 활성 상태의 네비게이션 메뉴 Entity 생성. */
    public static NavigationMenuJpaEntity activeEntity() {
        Instant now = Instant.now();
        return NavigationMenuJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }

    /** ID를 지정한 활성 상태 네비게이션 메뉴 Entity 생성. */
    public static NavigationMenuJpaEntity activeEntity(Long id) {
        Instant now = Instant.now();
        return NavigationMenuJpaEntity.create(
                id,
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }

    /** 커스텀 제목과 순서를 가진 활성 메뉴 Entity 생성. */
    public static NavigationMenuJpaEntity activeEntityWithTitle(String title, int displayOrder) {
        Instant now = Instant.now();
        return NavigationMenuJpaEntity.create(
                null,
                title,
                DEFAULT_LINK_URL,
                displayOrder,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }

    /** 비활성 상태 네비게이션 메뉴 Entity 생성. */
    public static NavigationMenuJpaEntity inactiveEntity() {
        Instant now = Instant.now();
        return NavigationMenuJpaEntity.create(
                2L,
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                null);
    }

    /** 삭제된 상태 네비게이션 메뉴 Entity 생성. */
    public static NavigationMenuJpaEntity deletedEntity() {
        Instant now = Instant.now();
        return NavigationMenuJpaEntity.create(
                3L,
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                now);
    }

    /** 새로 생성될 Entity (ID가 null). */
    public static NavigationMenuJpaEntity newEntity() {
        Instant now = Instant.now();
        return NavigationMenuJpaEntity.create(
                null,
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }
}
