package com.ryuqq.setof.application.cart.port.out.query;

/**
 * 장바구니 재고 조회 Port.
 *
 * <p>현재 구현체: 레거시 DB(product_stock) 조회. 추후 Redis 기반 재고 시스템 구축 시 구현체만 교체.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface CartStockQueryPort {

    int fetchStockQuantity(long productId);
}
