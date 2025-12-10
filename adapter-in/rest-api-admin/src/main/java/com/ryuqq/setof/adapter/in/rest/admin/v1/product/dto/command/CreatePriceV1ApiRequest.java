package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * V1 가격 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "가격 생성 요청")
public record CreatePriceV1ApiRequest(
        @Schema(description = "정가", example = "50000",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "정가는 필수입니다.") BigDecimal regularPrice,
        @Schema(description = "현재가", example = "40000",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "현재가는 필수입니다.") BigDecimal currentPrice) {
}
