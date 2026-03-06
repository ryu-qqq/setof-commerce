package com.ryuqq.setof.domain.banner.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 배너 도메인 예외. */
public class BannerException extends DomainException {

    public BannerException(BannerErrorCode errorCode) {
        super(errorCode);
    }

    public BannerException(BannerErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public BannerException(BannerErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
