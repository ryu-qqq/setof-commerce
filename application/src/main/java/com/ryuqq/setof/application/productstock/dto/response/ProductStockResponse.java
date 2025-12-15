package com.ryuqq.setof.application.productstock.dto.response;

import java.time.Instant;

/**
 * ProductStock Response
 *
 * <p>재고 정보 응답 DTO
 *
 * @param productStockId 재고 ID
 * @param productId 상품 ID
 * @param quantity 현재 재고 수량
 * @param updatedAt 마지막 수정일시
 * @author development-team
 * @since 1.0.0
 */
public record ProductStockResponse(
        Long productStockId, Long productId, int quantity, Instant updatedAt) {

    /** Static Factory Method */
    public static ProductStockResponse of(
            Long productStockId, Long productId, int quantity, Instant updatedAt) {
        return new ProductStockResponse(productStockId, productId, quantity, updatedAt);
    }
}
