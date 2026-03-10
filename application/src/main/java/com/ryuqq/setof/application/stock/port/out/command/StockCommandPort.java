package com.ryuqq.setof.application.stock.port.out.command;

/**
 * DB 재고 명령 Port.
 *
 * <p>Legacy product_stock 테이블의 재고를 차감/복원합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface StockCommandPort {

    /**
     * 재고를 차감합니다.
     *
     * <p>{@code UPDATE product_stock SET stock_quantity = stock_quantity - quantity}
     *
     * @param productId 상품 ID
     * @param quantity 차감 수량
     */
    void deduct(long productId, int quantity);

    /**
     * 재고를 복원합니다.
     *
     * <p>{@code UPDATE product_stock SET stock_quantity = stock_quantity + quantity}
     *
     * @param productId 상품 ID
     * @param quantity 복원 수량
     */
    void restore(long productId, int quantity);
}
