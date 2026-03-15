package com.ryuqq.setof.adapter.in.rest.admin.v2.navigation;

/**
 * NavigationMenuAdminEndpoints - 네비게이션 메뉴 Admin API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class
 *
 * <p>API-END-002: static final 상수
 *
 * <p>API-END-003: Path Variable 상수
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class NavigationMenuAdminEndpoints {

    private NavigationMenuAdminEndpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** 네비게이션 메뉴 기본 경로 */
    public static final String NAVIGATION_MENUS = "/api/v2/admin/navigation-menus";

    /** Navigation Menu ID Path Variable */
    public static final String ID = "/{navigationMenuId}";

    /** Navigation Menu ID Path Variable 이름 */
    public static final String PATH_NAVIGATION_MENU_ID = "navigationMenuId";

    /** 소프트 삭제 경로 */
    public static final String REMOVE = "/remove";
}
