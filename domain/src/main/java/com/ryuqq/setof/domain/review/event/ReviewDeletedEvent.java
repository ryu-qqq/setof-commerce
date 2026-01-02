package com.ryuqq.setof.domain.review.event;

import com.ryuqq.setof.domain.review.aggregate.Review;
import java.time.Instant;
import java.util.UUID;

public record ReviewDeletedEvent(
        Long reviewId,
        UUID memberId,
        Long orderId,
        Long productGroupId,
        int rating,
        Instant deletedAt) {

    public static ReviewDeletedEvent from(Review review) {
        return new ReviewDeletedEvent(
                review.getId() != null ? review.getId().getValue() : null,
                review.getMemberId(),
                review.getOrderId(),
                review.getProductGroupId(),
                review.getRatingValue(),
                review.getDeletedAt());
    }
}
