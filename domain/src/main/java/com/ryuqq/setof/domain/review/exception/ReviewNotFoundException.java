package com.ryuqq.setof.domain.review.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class ReviewNotFoundException extends DomainException {

    public ReviewNotFoundException() {
        super(ReviewErrorCode.REVIEW_NOT_FOUND);
    }

    public ReviewNotFoundException(long reviewId) {
        super(ReviewErrorCode.REVIEW_NOT_FOUND, "Review ID: " + reviewId);
    }
}
