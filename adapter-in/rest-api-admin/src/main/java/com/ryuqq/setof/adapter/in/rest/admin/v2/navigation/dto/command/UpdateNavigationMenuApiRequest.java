package com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * 네비게이션 메뉴 수정 API 요청.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-003: Validation 어노테이션은 API Request에만 적용.
 *
 * <p>API-DTO-004: Update Request에 ID 포함 금지 (PathVariable로 전달).
 *
 * @param title 메뉴명
 * @param linkUrl 이동 URL
 * @param displayOrder 노출 순서
 * @param displayStartAt 노출 시작 시각
 * @param displayEndAt 노출 종료 시각
 * @param active 노출 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "네비게이션 메뉴 수정 요청")
public record UpdateNavigationMenuApiRequest(
        @Schema(description = "메뉴명", example = "이벤트", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "메뉴명은 필수입니다")
                String title,
        @Schema(
                        description = "이동 URL",
                        example = "/event",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "이동 URL은 필수입니다")
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
