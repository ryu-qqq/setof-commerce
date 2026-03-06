package com.ryuqq.setof.domain.contentpage.exception;

/**
 * 디스플레이 컴포넌트를 찾을 수 없는 경우 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class DisplayComponentNotFoundException extends ContentPageException {

    private static final ContentPageErrorCode ERROR_CODE =
            ContentPageErrorCode.DISPLAY_COMPONENT_NOT_FOUND;

    public DisplayComponentNotFoundException() {
        super(ERROR_CODE);
    }

    public DisplayComponentNotFoundException(String detail) {
        super(ERROR_CODE, String.format("디스플레이 컴포넌트를 찾을 수 없습니다: %s", detail));
    }
}
