package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Banner 수정 요청 DTO
 *
 * @param title 배너 제목 (nullable)
 * @param displayStartDate 노출 시작일시 (nullable)
 * @param displayEndDate 노출 종료일시 (nullable)
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "배너 수정 요청")
public record UpdateBannerV2ApiRequest(
        @Schema(description = "배너 제목", example = "메인 배너 (수정)") String title,
        @Schema(description = "노출 시작일시", example = "2024-12-01T00:00:00Z") Instant displayStartDate,
        @Schema(description = "노출 종료일시", example = "2024-12-31T23:59:59Z")
                Instant displayEndDate) {}
