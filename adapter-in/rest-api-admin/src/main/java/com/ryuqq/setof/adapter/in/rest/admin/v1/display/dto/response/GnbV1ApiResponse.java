package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 GNB Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "GNB 응답")
public record GnbV1ApiResponse(
        @Schema(description = "GNB ID", example = "1") Long gnbId,
        @Schema(description = "GNB명", example = "홈") String name,
        @Schema(description = "링크 URL", example = "/") String linkUrl,
        @Schema(description = "정렬 순서", example = "1") Integer sortOrder,
        @Schema(description = "표시 여부", example = "true") Boolean displayYn,
        @Schema(description = "생성일시") LocalDateTime createdAt,
        @Schema(description = "수정일시") LocalDateTime updatedAt) {
}
