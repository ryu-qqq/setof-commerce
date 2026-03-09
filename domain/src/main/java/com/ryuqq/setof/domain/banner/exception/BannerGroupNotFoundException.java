package com.ryuqq.setof.domain.banner.exception;

/**
 * 배너 그룹을 찾을 수 없는 경우 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class BannerGroupNotFoundException extends BannerException {

    private static final BannerErrorCode ERROR_CODE = BannerErrorCode.BANNER_GROUP_NOT_FOUND;

    public BannerGroupNotFoundException() {
        super(ERROR_CODE);
    }

    public BannerGroupNotFoundException(String detail) {
        super(ERROR_CODE, String.format("해당 배너가 존재하지 않습니다%s", detail));
    }
}
