package com.ryuqq.setof.application.order.port.in.command;

import com.ryuqq.setof.application.order.dto.command.CreateOrderCommand;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;

/**
 * 주문 생성 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateOrderUseCase {

    /**
     * 주문 생성
     *
     * @param command 주문 생성 Command
     * @return 생성된 주문 응답
     */
    OrderResponse createOrder(CreateOrderCommand command);
}
