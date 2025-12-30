package com.ryuqq.setof.application.order.service.command;

import com.ryuqq.setof.application.order.assembler.OrderAssembler;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.manager.command.OrderPersistenceManager;
import com.ryuqq.setof.application.order.manager.query.OrderReadManager;
import com.ryuqq.setof.application.order.port.in.command.ShipOrderUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.order.aggregate.Order;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 배송 시작 Service
 *
 * <p>배송을 시작합니다
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ShipOrderService implements ShipOrderUseCase {

    private final OrderReadManager orderReadManager;
    private final OrderPersistenceManager orderPersistenceManager;
    private final OrderAssembler orderAssembler;
    private final ClockHolder clockHolder;

    public ShipOrderService(
            OrderReadManager orderReadManager,
            OrderPersistenceManager orderPersistenceManager,
            OrderAssembler orderAssembler,
            ClockHolder clockHolder) {
        this.orderReadManager = orderReadManager;
        this.orderPersistenceManager = orderPersistenceManager;
        this.orderAssembler = orderAssembler;
        this.clockHolder = clockHolder;
    }

    /**
     * 배송 시작
     *
     * @param orderId 주문 ID (UUID String)
     * @return 배송 중 상태의 주문 응답
     */
    @Override
    @Transactional
    public OrderResponse shipOrder(String orderId) {
        Order order = orderReadManager.findById(orderId);
        Instant now = Instant.now(clockHolder.getClock());

        Order shippedOrder = order.ship(now);
        orderPersistenceManager.persist(shippedOrder);
        return orderAssembler.toResponse(shippedOrder);
    }
}
