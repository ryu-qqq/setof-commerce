package com.ryuqq.setof.domain.navigation.exception;

/**
 * 네비게이션 메뉴를 찾을 수 없는 경우 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class NavigationMenuNotFoundException extends NavigationException {

    private static final NavigationErrorCode ERROR_CODE =
            NavigationErrorCode.NAVIGATION_MENU_NOT_FOUND;

    public NavigationMenuNotFoundException() {
        super(ERROR_CODE);
    }

    public NavigationMenuNotFoundException(String detail) {
        super(ERROR_CODE, String.format("네비게이션 메뉴를 찾을 수 없습니다: %s", detail));
    }
}
