package com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.response;

import com.ryuqq.setof.application.content.dto.response.ContentResponse;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * @since 2.0.0
 */
@Schema(description = "콘텐츠 응답")
public record ContentV2ApiResponse(
        @Schema(description = "콘텐츠 ID", example = "1") Long contentId,
        @Schema(description = "콘텐츠 제목", example = "메인 페이지 신규 컬렉션") String title,
        @Schema(description = "메모", example = "2024년 여름 컬렉션 프로모션") String memo,
        @Schema(description = "대표 이미지 URL", example = "https://cdn.example.com/images/content1.jpg")
                String imageUrl,
        @Schema(description = "상태", example = "ACTIVE") String status,
        @Schema(description = "노출 시작일시", example = "2024-12-01T00:00:00Z") Instant displayStartDate,
        @Schema(description = "노출 종료일시", example = "2024-12-31T23:59:59Z") Instant displayEndDate,
        @Schema(description = "생성일시", example = "2024-11-01T10:00:00Z") Instant createdAt,
        @Schema(description = "수정일시", example = "2024-11-15T14:30:00Z") Instant updatedAt) {

    /**
     * Application Layer 응답으로부터 변환
     *
     * @param response Application Layer 응답
     * @return API 응답
     */
    public static ContentV2ApiResponse from(ContentResponse response) {
        return new ContentV2ApiResponse(
                response.contentId(),
                response.title(),
                response.memo(),
                response.imageUrl(),
                response.status(),
                response.displayStartDate(),
                response.displayEndDate(),
                response.createdAt(),
                response.updatedAt());
    }
}
