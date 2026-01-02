package com.ryuqq.setof.application.content.dto.response;

import java.time.Instant;

/**
 * Content 요약 응답 DTO (목록용)
 *
 * @param contentId 콘텐츠 ID
 * @param title 제목
 * @param status 상태
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @param componentCount 포함된 컴포넌트 수
 * @author development-team
 * @since 1.0.0
 */
public record ContentSummaryResponse(
        Long contentId,
        String title,
        String status,
        Instant displayStartDate,
        Instant displayEndDate,
        int componentCount) {

    /** Static Factory Method */
    public static ContentSummaryResponse of(
            Long contentId,
            String title,
            String status,
            Instant displayStartDate,
            Instant displayEndDate,
            int componentCount) {
        return new ContentSummaryResponse(
                contentId, title, status, displayStartDate, displayEndDate, componentCount);
    }
}
