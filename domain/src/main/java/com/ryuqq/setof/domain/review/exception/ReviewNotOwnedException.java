package com.ryuqq.setof.domain.review.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.UUID;

public class ReviewNotOwnedException extends DomainException {

    public ReviewNotOwnedException() {
        super(ReviewErrorCode.REVIEW_NOT_OWNED);
    }

    public ReviewNotOwnedException(long reviewId, UUID memberId) {
        super(
                ReviewErrorCode.REVIEW_NOT_OWNED,
                String.format("Review %d is not owned by member %s", reviewId, memberId));
    }
}
