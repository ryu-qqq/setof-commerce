package com.ryuqq.setof.domain.reviewimage.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 리뷰 이미지 도메인 예외 기본 클래스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ReviewImageException extends DomainException {

    public ReviewImageException(ReviewImageErrorCode errorCode) {
        super(errorCode);
    }

    public ReviewImageException(ReviewImageErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ReviewImageException(ReviewImageErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
