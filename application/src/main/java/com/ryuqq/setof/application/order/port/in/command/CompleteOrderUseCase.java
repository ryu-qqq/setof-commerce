package com.ryuqq.setof.application.order.port.in.command;

import com.ryuqq.setof.application.order.dto.response.OrderResponse;

/**
 * 구매 확정 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CompleteOrderUseCase {

    /**
     * 구매 확정
     *
     * @param orderId 주문 ID (UUID String)
     * @return 완료 상태의 주문 응답
     */
    OrderResponse completeOrder(String orderId);
}
