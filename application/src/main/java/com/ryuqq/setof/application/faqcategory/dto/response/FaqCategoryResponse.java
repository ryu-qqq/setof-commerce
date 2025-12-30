package com.ryuqq.setof.application.faqcategory.dto.response;

import java.time.Instant;

/**
 * FAQ 카테고리 Response DTO
 *
 * @author development-team
 * @since 1.0.0
 */
public record FaqCategoryResponse(
        Long id,
        String code,
        String name,
        String description,
        int displayOrder,
        String status,
        Instant createdAt,
        Instant updatedAt) {

    public static FaqCategoryResponse of(
            Long id,
            String code,
            String name,
            String description,
            int displayOrder,
            String status,
            Instant createdAt,
            Instant updatedAt) {
        return new FaqCategoryResponse(
                id, code, name, description, displayOrder, status, createdAt, updatedAt);
    }
}
