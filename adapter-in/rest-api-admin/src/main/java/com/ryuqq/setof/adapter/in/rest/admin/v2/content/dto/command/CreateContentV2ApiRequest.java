package com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Content 생성 요청 DTO
 *
 * @param title 제목
 * @param memo 메모 (nullable)
 * @param imageUrl 대표 이미지 URL (nullable)
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "콘텐츠 생성 요청")
public record CreateContentV2ApiRequest(
        @Schema(description = "콘텐츠 제목", example = "메인 페이지 신규 컬렉션") @NotBlank(message = "제목은 필수입니다")
                String title,
        @Schema(description = "메모", example = "2024년 여름 컬렉션 프로모션") String memo,
        @Schema(description = "대표 이미지 URL", example = "https://cdn.example.com/images/content1.jpg")
                String imageUrl,
        @Schema(description = "노출 시작일시", example = "2024-12-01T00:00:00Z")
                @NotNull(message = "노출 시작일시는 필수입니다")
                Instant displayStartDate,
        @Schema(description = "노출 종료일시", example = "2024-12-31T23:59:59Z")
                @NotNull(message = "노출 종료일시는 필수입니다")
                Instant displayEndDate) {}
