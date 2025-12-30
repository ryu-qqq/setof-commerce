package com.ryuqq.setof.domain.review.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class InvalidReviewContentException extends DomainException {

    public InvalidReviewContentException() {
        super(ReviewErrorCode.INVALID_REVIEW_CONTENT);
    }

    public InvalidReviewContentException(String message) {
        super(ReviewErrorCode.INVALID_REVIEW_CONTENT, message);
    }
}
