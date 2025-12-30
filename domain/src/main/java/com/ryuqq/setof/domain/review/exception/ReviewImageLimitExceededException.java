package com.ryuqq.setof.domain.review.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class ReviewImageLimitExceededException extends DomainException {

    private static final int MAX_IMAGE_COUNT = 3;

    public ReviewImageLimitExceededException() {
        super(ReviewErrorCode.REVIEW_IMAGE_LIMIT_EXCEEDED);
    }

    public ReviewImageLimitExceededException(int actualCount) {
        super(
                ReviewErrorCode.REVIEW_IMAGE_LIMIT_EXCEEDED,
                String.format(
                        "Review image count %d exceeds maximum limit %d",
                        actualCount, MAX_IMAGE_COUNT));
    }
}
