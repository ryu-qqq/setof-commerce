package com.ryuqq.setof.domain.review.event;

import com.ryuqq.setof.domain.review.aggregate.Review;
import java.time.Instant;
import java.util.UUID;

public record ReviewUpdatedEvent(
        Long reviewId,
        UUID memberId,
        Long productGroupId,
        int oldRating,
        int newRating,
        Instant updatedAt) {

    public static ReviewUpdatedEvent from(Review review, int oldRating) {
        return new ReviewUpdatedEvent(
                review.getId() != null ? review.getId().getValue() : null,
                review.getMemberId(),
                review.getProductGroupId(),
                oldRating,
                review.getRatingValue(),
                review.getUpdatedAt());
    }
}
