package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * 배너 슬라이드 등록 API 요청.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-003: Validation 어노테이션은 API Request에만 적용.
 *
 * @param title 슬라이드 제목
 * @param imageUrl 이미지 URL
 * @param linkUrl 링크 URL
 * @param displayOrder 노출 순서
 * @param displayStartAt 노출 시작 시각
 * @param displayEndAt 노출 종료 시각
 * @param active 노출 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배너 슬라이드 등록 요청")
public record RegisterBannerSlideApiRequest(
        @Schema(
                        description = "슬라이드 제목",
                        example = "여름 이벤트 슬라이드",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "슬라이드 제목은 필수입니다")
                String title,
        @Schema(
                        description = "이미지 URL",
                        example = "https://cdn.example.com/banner/image.jpg",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "이미지 URL은 필수입니다")
                String imageUrl,
        @Schema(
                        description = "링크 URL",
                        example = "/event/summer",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "링크 URL은 필수입니다")
                String linkUrl,
        @Schema(description = "노출 순서", example = "1") int displayOrder,
        @Schema(
                        description = "노출 시작 시각 (ISO 8601)",
                        example = "2025-01-01T00:00:00Z",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "노출 시작 시각은 필수입니다")
                Instant displayStartAt,
        @Schema(
                        description = "노출 종료 시각 (ISO 8601)",
                        example = "2025-12-31T23:59:59Z",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "노출 종료 시각은 필수입니다")
                Instant displayEndAt,
        @Schema(description = "노출 여부", example = "true") boolean active) {}
