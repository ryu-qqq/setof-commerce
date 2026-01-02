package com.ryuqq.setof.domain.review.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class ReviewAlreadyDeletedException extends DomainException {

    public ReviewAlreadyDeletedException() {
        super(ReviewErrorCode.REVIEW_ALREADY_DELETED);
    }

    public ReviewAlreadyDeletedException(long reviewId) {
        super(
                ReviewErrorCode.REVIEW_ALREADY_DELETED,
                "Review ID: " + reviewId + " is already deleted.");
    }
}
