package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * 배너 그룹 수정 API 요청.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-003: Validation 어노테이션은 API Request에만 적용.
 *
 * <p>API-DTO-004: Update Request에 ID 미포함. ID는 PathVariable로 전달.
 *
 * <p>슬라이드 수정은 {@link UpdateBannerSlidesApiRequest}를 통해 별도로 처리합니다.
 *
 * @param title 배너 그룹명
 * @param bannerType 배너 타입 (BannerType enum 이름)
 * @param displayStartAt 노출 시작 시각
 * @param displayEndAt 노출 종료 시각
 * @param active 노출 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배너 그룹 수정 요청")
public record UpdateBannerGroupApiRequest(
        @Schema(
                        description = "배너 그룹명",
                        example = "메인 배너",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "배너 그룹명은 필수입니다")
                String title,
        @Schema(
                        description = "배너 타입",
                        example = "MAIN_BANNER",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "배너 타입은 필수입니다")
                String bannerType,
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
