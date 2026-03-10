package com.ryuqq.setof.application.stock.port.out.query;

/**
 * DB 재고 조회 Port.
 *
 * <p>Legacy product_stock 테이블에서 재고를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface StockQueryPort {

    /**
     * 상품의 현재 재고를 조회합니다.
     *
     * @param productId 상품 ID (product_stock.product_id)
     * @return 현재 재고 수량
     */
    int getQuantity(long productId);
}
