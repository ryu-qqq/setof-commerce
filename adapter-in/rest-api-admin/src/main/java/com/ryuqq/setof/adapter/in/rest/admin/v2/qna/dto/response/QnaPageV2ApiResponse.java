package com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * QnA Page V2 API Response
 *
 * <p>문의 페이지 API 응답 DTO
 *
 * @param qnas 문의 목록
 * @param page 현재 페이지 (0부터 시작)
 * @param size 페이지 크기
 * @param totalCount 전체 개수
 * @param totalPages 전체 페이지 수
 * @param isLast 마지막 페이지 여부
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "문의 페이지 응답")
public record QnaPageV2ApiResponse(
        @Schema(description = "문의 목록")
        List<QnaSummaryV2ApiResponse> qnas,

        @Schema(description = "현재 페이지 (0부터 시작)", example = "0")
        int page,

        @Schema(description = "페이지 크기", example = "20")
        int size,

        @Schema(description = "전체 개수", example = "100")
        long totalCount,

        @Schema(description = "전체 페이지 수", example = "5")
        int totalPages,

        @Schema(description = "마지막 페이지 여부", example = "false")
        boolean isLast) {

    public static QnaPageV2ApiResponse of(
            List<QnaSummaryV2ApiResponse> qnas, int page, int size, long totalCount) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalCount / size) : 0;
        boolean isLast = page >= totalPages - 1;
        return new QnaPageV2ApiResponse(qnas, page, size, totalCount, totalPages, isLast);
    }
}
