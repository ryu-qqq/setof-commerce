package com.ryuqq.setof.application.order.port.in.command;

import com.ryuqq.setof.application.order.dto.response.OrderResponse;

/**
 * 배송 완료 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeliverOrderUseCase {

    /**
     * 배송 완료
     *
     * @param orderId 주문 ID (UUID String)
     * @return 배송 완료 상태의 주문 응답
     */
    OrderResponse deliverOrder(String orderId);
}
