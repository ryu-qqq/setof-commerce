package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 배너 그룹 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배너 그룹 응답")
public record BannerGroupV1ApiResponse(
        @Schema(description = "배너 ID", example = "1") Long bannerId,
        @Schema(description = "배너명", example = "메인 배너") String bannerName,
        @Schema(description = "배너 타입", example = "MAIN") String bannerType,
        @Schema(description = "표시 여부", example = "true") Boolean displayYn,
        @Schema(description = "생성일시") LocalDateTime createdAt,
        @Schema(description = "수정일시") LocalDateTime updatedAt) {}
