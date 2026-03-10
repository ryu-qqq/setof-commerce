package com.ryuqq.setof.domain.stock.vo;

/**
 * 재고 차감/복원 항목 VO.
 *
 * @param productId 상품 ID
 * @param quantity 차감/복원 수량
 * @author ryu-qqq
 * @since 1.1.0
 */
public record StockDeductionItem(long productId, int quantity) {

    public StockDeductionItem {
        if (productId <= 0) {
            throw new IllegalArgumentException("productId must be positive");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
