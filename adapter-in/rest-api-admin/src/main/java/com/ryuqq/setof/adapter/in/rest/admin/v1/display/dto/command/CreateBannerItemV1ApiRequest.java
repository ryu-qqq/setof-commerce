package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * V1 배너 아이템 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배너 아이템 생성 요청")
public record CreateBannerItemV1ApiRequest(
        @Schema(description = "배너 ID", example = "1") @NotNull Long bannerId,
        @Schema(description = "이미지 URL", example = "https://example.com/image.jpg") String imageUrl,
        @Schema(description = "링크 URL", example = "https://example.com") String linkUrl,
        @Schema(description = "정렬 순서", example = "1") Integer sortOrder) {
}
