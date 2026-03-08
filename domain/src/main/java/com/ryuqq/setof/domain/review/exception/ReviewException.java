package com.ryuqq.setof.domain.review.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 리뷰 도메인 예외 기본 클래스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ReviewException extends DomainException {

    public ReviewException(ReviewErrorCode errorCode) {
        super(errorCode);
    }

    public ReviewException(ReviewErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ReviewException(ReviewErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
