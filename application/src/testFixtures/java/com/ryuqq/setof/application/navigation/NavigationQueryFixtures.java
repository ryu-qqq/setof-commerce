package com.ryuqq.setof.application.navigation;

import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.setof.commerce.domain.navigation.NavigationFixtures;
import java.util.List;

/**
 * Navigation Application Query 테스트 Fixtures.
 *
 * <p>NavigationMenuQueryService, NavigationMenuQueryManager 테스트에서 공통으로 사용하는 객체 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class NavigationQueryFixtures {

    private NavigationQueryFixtures() {}

    // ===== NavigationMenu List =====

    public static List<NavigationMenu> activeNavigationMenus() {
        return List.of(
                NavigationFixtures.activeNavigationMenu(1L),
                NavigationFixtures.activeNavigationMenu(2L));
    }

    public static List<NavigationMenu> emptyNavigationMenus() {
        return List.of();
    }
}
