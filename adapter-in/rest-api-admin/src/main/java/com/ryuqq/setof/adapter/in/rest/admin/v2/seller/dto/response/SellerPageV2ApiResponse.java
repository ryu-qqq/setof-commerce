package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 셀러 페이징 API 응답 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "셀러 페이지 응답")
public record SellerPageV2ApiResponse(
        @Schema(description = "셀러 목록") List<SellerSummaryV2ApiResponse> sellers,
        @Schema(description = "현재 페이지", example = "0") int page,
        @Schema(description = "페이지 크기", example = "20") int size,
        @Schema(description = "전체 개수", example = "100") long totalCount,
        @Schema(description = "전체 페이지 수", example = "5") int totalPages,
        @Schema(description = "마지막 페이지 여부", example = "false") boolean isLast) {

    public static SellerPageV2ApiResponse of(
            List<SellerSummaryV2ApiResponse> sellers, int page, int size, long totalCount) {
        int totalPages = (int) Math.ceil((double) totalCount / size);
        boolean isLast = page >= totalPages - 1;
        return new SellerPageV2ApiResponse(sellers, page, size, totalCount, totalPages, isLast);
    }
}
