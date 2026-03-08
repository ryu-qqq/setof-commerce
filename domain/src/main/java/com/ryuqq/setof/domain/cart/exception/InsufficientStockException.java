package com.ryuqq.setof.domain.cart.exception;

/**
 * 재고 부족 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class InsufficientStockException extends CartException {

    public InsufficientStockException(long productId, int requested, int available) {
        super(
                CartErrorCode.INSUFFICIENT_STOCK,
                String.format("상품 %d 재고 부족: 요청=%d, 가용=%d", productId, requested, available));
    }
}
