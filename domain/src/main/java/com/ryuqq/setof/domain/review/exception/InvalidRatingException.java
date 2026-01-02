package com.ryuqq.setof.domain.review.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class InvalidRatingException extends DomainException {

    public InvalidRatingException() {
        super(ReviewErrorCode.INVALID_RATING);
    }

    public InvalidRatingException(int rating) {
        super(
                ReviewErrorCode.INVALID_RATING,
                "Invalid rating value: " + rating + ". Must be between 1 and 5.");
    }
}
