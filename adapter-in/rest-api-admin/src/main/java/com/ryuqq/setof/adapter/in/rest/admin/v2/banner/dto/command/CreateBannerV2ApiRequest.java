package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Banner 생성 요청 DTO
 *
 * @param title 배너 제목
 * @param bannerType 배너 타입
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "배너 생성 요청")
public record CreateBannerV2ApiRequest(
        @Schema(description = "배너 제목", example = "메인 배너") @NotBlank(message = "제목은 필수입니다")
                String title,
        @Schema(description = "배너 타입", example = "CATEGORY") @NotBlank(message = "배너 타입은 필수입니다")
                String bannerType,
        @Schema(description = "노출 시작일시", example = "2024-12-01T00:00:00Z")
                @NotNull(message = "노출 시작일시는 필수입니다")
                Instant displayStartDate,
        @Schema(description = "노출 종료일시", example = "2024-12-31T23:59:59Z")
                @NotNull(message = "노출 종료일시는 필수입니다")
                Instant displayEndDate) {}
