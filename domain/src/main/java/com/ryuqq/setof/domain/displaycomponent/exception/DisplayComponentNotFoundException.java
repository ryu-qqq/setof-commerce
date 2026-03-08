package com.ryuqq.setof.domain.displaycomponent.exception;

/**
 * DisplayComponentNotFoundException - 전시 컴포넌트 미존재 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class DisplayComponentNotFoundException extends DisplayComponentException {

    private static final DisplayComponentErrorCode ERROR_CODE =
            DisplayComponentErrorCode.DISPLAY_COMPONENT_NOT_FOUND;

    public DisplayComponentNotFoundException() {
        super(ERROR_CODE);
    }

    public DisplayComponentNotFoundException(String detail) {
        super(ERROR_CODE, String.format("전시 컴포넌트를 찾을 수 없습니다: %s", detail));
    }
}
