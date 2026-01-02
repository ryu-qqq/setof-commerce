package com.ryuqq.setof.domain.review.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.UUID;

public class DuplicateReviewException extends DomainException {

    public DuplicateReviewException() {
        super(ReviewErrorCode.DUPLICATE_REVIEW);
    }

    public DuplicateReviewException(UUID memberId, long productGroupId) {
        super(
                ReviewErrorCode.DUPLICATE_REVIEW,
                String.format(
                        "Member %s already wrote a review for product group %d",
                        memberId, productGroupId));
    }

    public DuplicateReviewException(UUID memberId, long orderId, long productGroupId) {
        super(
                ReviewErrorCode.DUPLICATE_REVIEW,
                String.format(
                        "Member %s already wrote a review for order %d, product group %d",
                        memberId, orderId, productGroupId));
    }
}
