package com.ryuqq.setof.adapter.in.rest.v1.navigation;

import com.ryuqq.setof.adapter.in.rest.v1.navigation.dto.response.NavigationMenuV1ApiResponse;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
import java.time.Instant;
import java.util.List;

/**
 * Navigation V1 API 테스트 Fixtures.
 *
 * <p>Navigation 관련 Domain 객체 및 API Response 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class NavigationApiFixtures {

    private NavigationApiFixtures() {}

    // ===== NavigationMenu Domain Fixtures =====

    public static NavigationMenu navigationMenu(long id) {
        return NavigationMenu.reconstitute(
                NavigationMenuId.of(id),
                "신상품",
                "/new-arrivals",
                1,
                DisplayPeriod.of(
                        Instant.parse("2000-01-01T00:00:00Z"),
                        Instant.parse("2099-12-31T23:59:59Z")),
                true,
                DeletionStatus.active(),
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-01-01T00:00:00Z"));
    }

    public static NavigationMenu navigationMenu(long id, String title, String linkUrl) {
        return NavigationMenu.reconstitute(
                NavigationMenuId.of(id),
                title,
                linkUrl,
                1,
                DisplayPeriod.of(
                        Instant.parse("2000-01-01T00:00:00Z"),
                        Instant.parse("2099-12-31T23:59:59Z")),
                true,
                DeletionStatus.active(),
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-01-01T00:00:00Z"));
    }

    public static List<NavigationMenu> navigationMenuList() {
        return List.of(
                navigationMenu(1L),
                navigationMenu(2L, "베스트", "/best"),
                navigationMenu(3L, "세일", "/sale"));
    }

    // ===== NavigationMenuV1ApiResponse Fixtures =====

    public static NavigationMenuV1ApiResponse navigationMenuResponse(long id) {
        return new NavigationMenuV1ApiResponse(id, "신상품", "/new-arrivals");
    }

    public static NavigationMenuV1ApiResponse navigationMenuResponse(
            long id, String title, String linkUrl) {
        return new NavigationMenuV1ApiResponse(id, title, linkUrl);
    }

    public static List<NavigationMenuV1ApiResponse> navigationMenuResponseList() {
        return List.of(
                navigationMenuResponse(1L),
                navigationMenuResponse(2L, "베스트", "/best"),
                navigationMenuResponse(3L, "세일", "/sale"));
    }
}
