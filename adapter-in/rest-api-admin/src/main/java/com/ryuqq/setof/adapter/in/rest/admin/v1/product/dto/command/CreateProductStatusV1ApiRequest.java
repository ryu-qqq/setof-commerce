package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * V1 상품 상태 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 상태 생성 요청")
public record CreateProductStatusV1ApiRequest(
        @Schema(description = "품절 여부 (Y/N)", example = "N",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "품절 여부는 필수입니다.") String soldOutYn,
        @Schema(description = "전시 여부 (Y/N)", example = "Y",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "전시 여부는 필수입니다.") String displayYn) {
}
