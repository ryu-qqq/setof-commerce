package com.ryuqq.setof.application.order.port.in.command;

import com.ryuqq.setof.application.order.dto.response.OrderResponse;

/**
 * 주문 취소 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CancelOrderUseCase {

    /**
     * 주문 취소
     *
     * @param orderId 주문 ID (UUID String)
     * @return 취소된 주문 응답
     */
    OrderResponse cancelOrder(String orderId);
}
