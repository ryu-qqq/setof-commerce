package com.ryuqq.setof.application.content.dto.response;

import java.time.Instant;

/**
 * Content 응답 DTO
 *
 * @param contentId 콘텐츠 ID
 * @param title 제목
 * @param memo 메모
 * @param imageUrl 대표 이미지 URL
 * @param status 상태
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 1.0.0
 */
public record ContentResponse(
        Long contentId,
        String title,
        String memo,
        String imageUrl,
        String status,
        Instant displayStartDate,
        Instant displayEndDate,
        Instant createdAt,
        Instant updatedAt) {

    /** Static Factory Method */
    public static ContentResponse of(
            Long contentId,
            String title,
            String memo,
            String imageUrl,
            String status,
            Instant displayStartDate,
            Instant displayEndDate,
            Instant createdAt,
            Instant updatedAt) {
        return new ContentResponse(
                contentId,
                title,
                memo,
                imageUrl,
                status,
                displayStartDate,
                displayEndDate,
                createdAt,
                updatedAt);
    }
}
