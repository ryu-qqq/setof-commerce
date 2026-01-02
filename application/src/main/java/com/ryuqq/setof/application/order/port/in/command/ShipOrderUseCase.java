package com.ryuqq.setof.application.order.port.in.command;

import com.ryuqq.setof.application.order.dto.response.OrderResponse;

/**
 * 배송 시작 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ShipOrderUseCase {

    /**
     * 배송 시작
     *
     * @param orderId 주문 ID (UUID String)
     * @return 배송 중 상태의 주문 응답
     */
    OrderResponse shipOrder(String orderId);
}
