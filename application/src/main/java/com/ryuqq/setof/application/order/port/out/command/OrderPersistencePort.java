package com.ryuqq.setof.application.order.port.out.command;

import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.vo.OrderId;

/**
 * 주문 저장 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface OrderPersistencePort {

    /**
     * 주문 저장
     *
     * @param order 저장할 주문
     * @return 저장된 주문 ID
     */
    OrderId persist(Order order);
}
