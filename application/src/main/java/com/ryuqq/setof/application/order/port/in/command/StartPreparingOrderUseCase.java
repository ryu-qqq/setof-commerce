package com.ryuqq.setof.application.order.port.in.command;

import com.ryuqq.setof.application.order.dto.response.OrderResponse;

/**
 * 상품 준비 시작 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface StartPreparingOrderUseCase {

    /**
     * 상품 준비 시작
     *
     * @param orderId 주문 ID (UUID String)
     * @return 준비 중 상태의 주문 응답
     */
    OrderResponse startPreparing(String orderId);
}
