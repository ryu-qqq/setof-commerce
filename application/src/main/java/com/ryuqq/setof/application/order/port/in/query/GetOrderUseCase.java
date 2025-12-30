package com.ryuqq.setof.application.order.port.in.query;

import com.ryuqq.setof.application.order.dto.response.OrderResponse;

/**
 * 주문 조회 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetOrderUseCase {

    /**
     * 주문 조회
     *
     * @param orderId 주문 ID (UUID String)
     * @return 주문 응답
     */
    OrderResponse getOrder(String orderId);

    /**
     * 주문 번호로 조회
     *
     * @param orderNumber 주문 번호
     * @return 주문 응답
     */
    OrderResponse getOrderByOrderNumber(String orderNumber);
}
