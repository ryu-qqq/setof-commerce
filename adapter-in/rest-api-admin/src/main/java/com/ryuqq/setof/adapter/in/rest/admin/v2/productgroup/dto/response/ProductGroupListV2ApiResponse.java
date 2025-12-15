package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 상품그룹 목록 응답 DTO (페이지네이션 포함)
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품그룹 목록 응답")
public record ProductGroupListV2ApiResponse(
        @Schema(description = "상품그룹 목록") List<ProductGroupSummaryV2ApiResponse> items,
        @Schema(description = "현재 페이지", example = "0") int page,
        @Schema(description = "페이지 크기", example = "20") int size,
        @Schema(description = "전체 개수", example = "150") long totalCount,
        @Schema(description = "전체 페이지 수", example = "8") int totalPages,
        @Schema(description = "마지막 페이지 여부", example = "false") boolean last) {

    /**
     * 목록 응답 생성
     *
     * @param items 상품그룹 요약 목록
     * @param page 현재 페이지
     * @param size 페이지 크기
     * @param totalCount 전체 개수
     * @return 목록 응답 DTO
     */
    public static ProductGroupListV2ApiResponse of(
            List<ProductGroupSummaryV2ApiResponse> items, int page, int size, long totalCount) {
        int totalPages = (int) Math.ceil((double) totalCount / size);
        boolean isLast = (page + 1) >= totalPages;
        return new ProductGroupListV2ApiResponse(items, page, size, totalCount, totalPages, isLast);
    }
}
