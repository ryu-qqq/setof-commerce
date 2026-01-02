package com.ryuqq.setof.application.order.manager.command;

import com.ryuqq.setof.application.order.port.out.command.OrderPersistencePort;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.vo.OrderId;
import org.springframework.stereotype.Component;

/**
 * 주문 영속성 Manager
 *
 * <p>주문 저장을 위한 Port 호출을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OrderPersistenceManager {

    private final OrderPersistencePort orderPersistencePort;

    public OrderPersistenceManager(OrderPersistencePort orderPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
    }

    /**
     * 주문 저장
     *
     * @param order 저장할 주문
     * @return 저장된 주문 ID
     */
    public OrderId persist(Order order) {
        return orderPersistencePort.persist(order);
    }
}
