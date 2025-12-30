package com.ryuqq.setof.domain.review.event;

import com.ryuqq.setof.domain.review.aggregate.Review;
import java.time.Instant;
import java.util.UUID;

public record ReviewCreatedEvent(
        Long reviewId,
        UUID memberId,
        Long orderId,
        Long productGroupId,
        int rating,
        Instant createdAt) {

    public static ReviewCreatedEvent from(Review review) {
        return new ReviewCreatedEvent(
                review.getId() != null ? review.getId().getValue() : null,
                review.getMemberId(),
                review.getOrderId(),
                review.getProductGroupId(),
                review.getRatingValue(),
                review.getCreatedAt());
    }
}
