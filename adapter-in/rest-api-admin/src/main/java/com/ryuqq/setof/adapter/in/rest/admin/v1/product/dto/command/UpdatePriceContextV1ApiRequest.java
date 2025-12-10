package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 가격 일괄 수정 Context Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "가격 일괄 수정 요청")
public record UpdatePriceContextV1ApiRequest(
        @Schema(description = "가격 명령 목록") List<PriceCommandV1ApiRequest> priceCommands) {

    @Schema(description = "가격 명령")
    public record PriceCommandV1ApiRequest(
            @Schema(description = "상품 그룹 ID", example = "12345",
                    requiredMode = Schema.RequiredMode.REQUIRED) Long productGroupId,
            @Schema(description = "정가", example = "50000",
                    requiredMode = Schema.RequiredMode.REQUIRED) java.math.BigDecimal regularPrice,
            @Schema(description = "현재가", example = "40000",
                    requiredMode = Schema.RequiredMode.REQUIRED) java.math.BigDecimal currentPrice) {
    }
}
