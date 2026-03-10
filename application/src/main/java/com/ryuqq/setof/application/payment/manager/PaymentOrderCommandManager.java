package com.ryuqq.setof.application.payment.manager;

import com.ryuqq.setof.application.payment.port.out.command.OrderCreationPort;
import com.ryuqq.setof.domain.order.aggregate.Order;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * OrderCommandManager - 결제 시 주문 영속 Manager.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentOrderCommandManager {

    private final OrderCreationPort orderCreationPort;

    public PaymentOrderCommandManager(OrderCreationPort orderCreationPort) {
        this.orderCreationPort = orderCreationPort;
    }

    public List<Long> persist(Order order) {
        return orderCreationPort.persist(order);
    }
}
