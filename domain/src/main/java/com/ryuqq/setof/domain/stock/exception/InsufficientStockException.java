package com.ryuqq.setof.domain.stock.exception;

/**
 * 재고 부족 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class InsufficientStockException extends StockException {

    public InsufficientStockException(long productId) {
        super(StockErrorCode.INSUFFICIENT_STOCK, String.format("상품 %d 재고가 부족합니다", productId));
    }
}
