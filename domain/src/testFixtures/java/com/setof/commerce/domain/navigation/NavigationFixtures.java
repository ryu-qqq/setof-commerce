package com.setof.commerce.domain.navigation;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenuUpdateData;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
import java.time.Instant;

/**
 * Navigation 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Navigation 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class NavigationFixtures {

    private NavigationFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_TITLE = "홈";
    public static final String DEFAULT_LINK_URL = "https://example.com/home";
    public static final int DEFAULT_DISPLAY_ORDER = 1;

    // ===== NavigationMenuId Fixtures =====
    public static NavigationMenuId defaultNavigationMenuId() {
        return NavigationMenuId.of(1L);
    }

    public static NavigationMenuId navigationMenuId(Long value) {
        return NavigationMenuId.of(value);
    }

    public static NavigationMenuId newNavigationMenuId() {
        return NavigationMenuId.forNew();
    }

    // ===== DisplayPeriod Fixtures =====
    public static DisplayPeriod defaultDisplayPeriod() {
        Instant now = CommonVoFixtures.now();
        return DisplayPeriod.of(now.minusSeconds(3600), now.plusSeconds(86400));
    }

    public static DisplayPeriod expiredDisplayPeriod() {
        Instant yesterday = CommonVoFixtures.yesterday();
        return DisplayPeriod.of(yesterday.minusSeconds(86400), yesterday);
    }

    public static DisplayPeriod futureDisplayPeriod() {
        Instant tomorrow = CommonVoFixtures.tomorrow();
        return DisplayPeriod.of(tomorrow, tomorrow.plusSeconds(86400));
    }

    // ===== NavigationMenu Aggregate Fixtures =====
    public static NavigationMenu newNavigationMenu() {
        return NavigationMenu.forNew(
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                defaultDisplayPeriod(),
                true,
                CommonVoFixtures.now());
    }

    public static NavigationMenu newNavigationMenu(String title, String linkUrl) {
        return NavigationMenu.forNew(
                title,
                linkUrl,
                DEFAULT_DISPLAY_ORDER,
                defaultDisplayPeriod(),
                true,
                CommonVoFixtures.now());
    }

    public static NavigationMenu activeNavigationMenu() {
        return NavigationMenu.reconstitute(
                NavigationMenuId.of(1L),
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                defaultDisplayPeriod(),
                true,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static NavigationMenu activeNavigationMenu(Long id) {
        return NavigationMenu.reconstitute(
                NavigationMenuId.of(id),
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                defaultDisplayPeriod(),
                true,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static NavigationMenu inactiveNavigationMenu() {
        return NavigationMenu.reconstitute(
                NavigationMenuId.of(2L),
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                defaultDisplayPeriod(),
                false,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static NavigationMenu deletedNavigationMenu() {
        Instant deletedAt = CommonVoFixtures.yesterday();
        return NavigationMenu.reconstitute(
                NavigationMenuId.of(3L),
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                defaultDisplayPeriod(),
                false,
                DeletionStatus.deletedAt(deletedAt),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static NavigationMenu expiredNavigationMenu() {
        return NavigationMenu.reconstitute(
                NavigationMenuId.of(4L),
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                expiredDisplayPeriod(),
                true,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== NavigationMenuUpdateData Fixtures =====
    public static NavigationMenuUpdateData defaultNavigationMenuUpdateData() {
        return new NavigationMenuUpdateData(
                "남성",
                "https://example.com/men",
                2,
                defaultDisplayPeriod(),
                false,
                CommonVoFixtures.now());
    }

    public static NavigationMenuUpdateData navigationMenuUpdateData(
            String title, String linkUrl, int displayOrder, boolean active) {
        return new NavigationMenuUpdateData(
                title,
                linkUrl,
                displayOrder,
                defaultDisplayPeriod(),
                active,
                CommonVoFixtures.now());
    }
}
