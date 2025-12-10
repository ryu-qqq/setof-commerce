package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * V1 상품 재고 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 재고 수정 요청")
public record UpdateProductStockV1ApiRequest(
        @Schema(description = "상품 ID", example = "123",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "상품 ID는 필수입니다.") Long productId,
        @Schema(description = "재고 수량", example = "100",
                requiredMode = Schema.RequiredMode.REQUIRED) @Max(value = 9999,
                        message = "재고 수량은 9999를 넘을 수 없습니다.") @Min(value = 0,
                                message = "재고 수량은 0 이상이어야 합니다.") Integer productStockQuantity) {
}
