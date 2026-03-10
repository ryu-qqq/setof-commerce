package com.ryuqq.setof.application.payment.port.out.query;

/**
 * 상품 가격 조회 Port.
 *
 * <p>결제 시 가격 검증을 위해 상품의 현재 판매가를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ProductPriceQueryPort {

    /**
     * 상품 그룹의 현재 판매가를 조회합니다.
     *
     * @param productGroupId 상품 그룹 ID
     * @return 현재 판매가 (current_price)
     */
    long getCurrentPrice(long productGroupId);
}
