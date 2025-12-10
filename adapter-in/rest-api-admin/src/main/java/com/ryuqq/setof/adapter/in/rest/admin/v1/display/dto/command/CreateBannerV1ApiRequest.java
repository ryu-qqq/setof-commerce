package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * V1 배너 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배너 생성 요청")
public record CreateBannerV1ApiRequest(
        @Schema(description = "배너명", example = "메인 배너") @NotBlank String bannerName,
        @Schema(description = "배너 타입", example = "MAIN") String bannerType,
        @Schema(description = "표시 여부", example = "true") @NotNull Boolean displayYn,
        @Schema(description = "링크 URL", example = "https://example.com") String linkUrl) {
}
