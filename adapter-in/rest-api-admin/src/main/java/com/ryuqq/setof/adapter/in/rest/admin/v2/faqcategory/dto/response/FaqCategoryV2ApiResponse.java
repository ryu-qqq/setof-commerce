package com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.dto.response;

import com.ryuqq.setof.application.faqcategory.dto.response.FaqCategoryResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * FAQ 카테고리 응답 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "FAQ 카테고리 응답")
public record FaqCategoryV2ApiResponse(
        @Schema(description = "카테고리 ID", example = "1") Long id,
        @Schema(description = "카테고리 코드", example = "ORDER") String code,
        @Schema(description = "카테고리명", example = "주문/결제") String name,
        @Schema(description = "설명", example = "주문 및 결제 관련 FAQ") String description,
        @Schema(description = "표시 순서", example = "1") int displayOrder,
        @Schema(description = "상태", example = "ACTIVE") String status,
        @Schema(description = "생성일시", example = "2024-01-01T00:00:00Z") Instant createdAt,
        @Schema(description = "수정일시", example = "2024-01-01T00:00:00Z") Instant updatedAt) {

    /**
     * Application FaqCategoryResponse → API Response 변환
     *
     * @param response Application Response
     * @return API Response
     */
    public static FaqCategoryV2ApiResponse from(FaqCategoryResponse response) {
        return new FaqCategoryV2ApiResponse(
                response.id(),
                response.code(),
                response.name(),
                response.description(),
                response.displayOrder(),
                response.status(),
                response.createdAt(),
                response.updatedAt());
    }
}
