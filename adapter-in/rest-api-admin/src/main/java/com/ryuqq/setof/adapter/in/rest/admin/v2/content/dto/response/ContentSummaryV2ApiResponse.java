package com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.response;

import com.ryuqq.setof.application.content.dto.response.ContentSummaryResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Content 요약 응답 DTO
 *
 * @param contentId 콘텐츠 ID
 * @param title 제목
 * @param status 상태
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "콘텐츠 요약 응답")
public record ContentSummaryV2ApiResponse(
        @Schema(description = "콘텐츠 ID", example = "1") Long contentId,
        @Schema(description = "콘텐츠 제목", example = "메인 페이지 신규 컬렉션") String title,
        @Schema(description = "상태", example = "ACTIVE") String status,
        @Schema(description = "노출 시작일시", example = "2024-12-01T00:00:00Z") Instant displayStartDate,
        @Schema(description = "노출 종료일시", example = "2024-12-31T23:59:59Z") Instant displayEndDate,
        @Schema(description = "컴포넌트 수", example = "5") int componentCount) {

    /**
     * Application Layer 응답으로부터 변환
     *
     * @param response Application Layer 응답
     * @return API 응답
     */
    public static ContentSummaryV2ApiResponse from(ContentSummaryResponse response) {
        return new ContentSummaryV2ApiResponse(
                response.contentId(),
                response.title(),
                response.status(),
                response.displayStartDate(),
                response.displayEndDate(),
                response.componentCount());
    }
}
