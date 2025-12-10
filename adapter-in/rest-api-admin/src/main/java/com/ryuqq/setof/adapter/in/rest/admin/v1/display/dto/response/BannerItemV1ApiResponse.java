package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 배너 아이템 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배너 아이템 응답")
public record BannerItemV1ApiResponse(
        @Schema(description = "배너 아이템 ID", example = "1") Long bannerItemId,
        @Schema(description = "배너 ID", example = "1") Long bannerId,
        @Schema(description = "이미지 URL", example = "https://example.com/image.jpg") String imageUrl,
        @Schema(description = "링크 URL", example = "https://example.com") String linkUrl,
        @Schema(description = "정렬 순서", example = "1") Integer sortOrder,
        @Schema(description = "생성일시") LocalDateTime createdAt,
        @Schema(description = "수정일시") LocalDateTime updatedAt) {
}
