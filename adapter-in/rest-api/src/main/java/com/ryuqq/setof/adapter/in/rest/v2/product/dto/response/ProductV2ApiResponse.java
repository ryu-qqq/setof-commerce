package com.ryuqq.setof.adapter.in.rest.v2.product.dto.response;

import com.ryuqq.setof.application.product.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * 상품(SKU) V2 응답
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품(SKU) 응답")
public record ProductV2ApiResponse(
        @Schema(description = "상품 ID", example = "1") Long productId,
        @Schema(description = "상품그룹 ID", example = "1") Long productGroupId,
        @Schema(description = "옵션 타입", example = "SINGLE") String optionType,
        @Schema(description = "옵션1 명", example = "색상") String option1Name,
        @Schema(description = "옵션1 값", example = "블랙") String option1Value,
        @Schema(description = "옵션2 명", example = "사이즈") String option2Name,
        @Schema(description = "옵션2 값", example = "M") String option2Value,
        @Schema(description = "추가금액", example = "0") BigDecimal additionalPrice,
        @Schema(description = "품절 여부", example = "false") boolean soldOut,
        @Schema(description = "노출 여부", example = "true") boolean displayYn) {

    /**
     * Application Response로부터 변환
     *
     * @param response Application 응답
     * @return API 응답
     */
    public static ProductV2ApiResponse from(ProductResponse response) {
        return new ProductV2ApiResponse(
                response.productId(),
                response.productGroupId(),
                response.optionType(),
                response.option1Name(),
                response.option1Value(),
                response.option2Name(),
                response.option2Value(),
                response.additionalPrice(),
                response.soldOut(),
                response.displayYn());
    }
}
