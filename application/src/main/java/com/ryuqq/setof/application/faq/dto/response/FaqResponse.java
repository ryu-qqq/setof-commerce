package com.ryuqq.setof.application.faq.dto.response;

import java.time.Instant;

/**
 * FAQ Response DTO
 *
 * @author development-team
 * @since 1.0.0
 */
public record FaqResponse(
        Long id,
        String categoryCode,
        String question,
        String answer,
        boolean isTop,
        int topOrder,
        int displayOrder,
        String status,
        int viewCount,
        Instant createdAt,
        Instant updatedAt) {

    public static FaqResponse of(
            Long id,
            String categoryCode,
            String question,
            String answer,
            boolean isTop,
            int topOrder,
            int displayOrder,
            String status,
            int viewCount,
            Instant createdAt,
            Instant updatedAt) {
        return new FaqResponse(
                id,
                categoryCode,
                question,
                answer,
                isTop,
                topOrder,
                displayOrder,
                status,
                viewCount,
                createdAt,
                updatedAt);
    }
}
