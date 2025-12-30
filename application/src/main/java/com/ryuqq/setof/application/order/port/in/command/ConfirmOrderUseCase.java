package com.ryuqq.setof.application.order.port.in.command;

import com.ryuqq.setof.application.order.dto.response.OrderResponse;

/**
 * 주문 확정 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ConfirmOrderUseCase {

    /**
     * 주문 확정
     *
     * @param orderId 주문 ID (UUID String)
     * @return 확정된 주문 응답
     */
    OrderResponse confirmOrder(String orderId);
}
