package com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.response;

import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * 재고 조회 API 응답 DTO
 *
 * <p>상품 재고 정보 응답
 *
 * @param productStockId 재고 ID
 * @param productId 상품 ID
 * @param quantity 현재 재고 수량
 * @param updatedAt 마지막 수정일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "재고 조회 응답")
public record ProductStockV2ApiResponse(
        @Schema(description = "재고 ID", example = "1") Long productStockId,
        @Schema(description = "상품 ID", example = "1001") Long productId,
        @Schema(description = "현재 재고 수량", example = "50") int quantity,
        @Schema(description = "마지막 수정일시") Instant updatedAt) {

    /** Application Response -> API Response 변환 */
    public static ProductStockV2ApiResponse from(ProductStockResponse response) {
        return new ProductStockV2ApiResponse(
                response.productStockId(),
                response.productId(),
                response.quantity(),
                response.updatedAt());
    }
}
