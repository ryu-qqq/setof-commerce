package com.ryuqq.setof.adapter.in.rest.admin.navigation;

import com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.dto.command.RegisterNavigationMenuApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.dto.command.UpdateNavigationMenuApiRequest;
import java.time.Instant;

/**
 * Navigation Menu v2 Command API 테스트 Fixtures.
 *
 * <p>NavigationMenuCommandApiMapper 및 Controller 테스트에서 공통으로 사용하는 API 요청 객체 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class NavigationMenuCommandApiFixtures {

    private NavigationMenuCommandApiFixtures() {}

    // ===== 공통 상수 =====
    public static final long DEFAULT_NAVIGATION_MENU_ID = 1L;
    public static final String DEFAULT_TITLE = "이벤트";
    public static final String DEFAULT_LINK_URL = "/event";
    public static final int DEFAULT_DISPLAY_ORDER = 1;
    public static final Instant DEFAULT_DISPLAY_START_AT = Instant.parse("2025-01-01T00:00:00Z");
    public static final Instant DEFAULT_DISPLAY_END_AT = Instant.parse("2025-12-31T23:59:59Z");

    // ===== RegisterNavigationMenuApiRequest =====

    public static RegisterNavigationMenuApiRequest registerRequest() {
        return new RegisterNavigationMenuApiRequest(
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true);
    }

    public static RegisterNavigationMenuApiRequest registerRequestInactive() {
        return new RegisterNavigationMenuApiRequest(
                DEFAULT_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false);
    }

    public static RegisterNavigationMenuApiRequest registerRequest(String title, String linkUrl) {
        return new RegisterNavigationMenuApiRequest(
                title,
                linkUrl,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true);
    }

    // ===== UpdateNavigationMenuApiRequest =====

    public static UpdateNavigationMenuApiRequest updateRequest() {
        return new UpdateNavigationMenuApiRequest(
                "수정된 메뉴",
                "/updated-link",
                2,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false);
    }

    public static UpdateNavigationMenuApiRequest updateRequestActive() {
        return new UpdateNavigationMenuApiRequest(
                "수정된 메뉴",
                "/updated-link",
                2,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true);
    }
}
