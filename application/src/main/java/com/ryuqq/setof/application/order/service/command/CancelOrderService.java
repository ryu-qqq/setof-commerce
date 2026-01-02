package com.ryuqq.setof.application.order.service.command;

import com.ryuqq.setof.application.order.assembler.OrderAssembler;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.manager.command.OrderPersistenceManager;
import com.ryuqq.setof.application.order.manager.query.OrderReadManager;
import com.ryuqq.setof.application.order.port.in.command.CancelOrderUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.order.aggregate.Order;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 취소 Service
 *
 * <p>주문을 취소합니다
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CancelOrderService implements CancelOrderUseCase {

    private final OrderReadManager orderReadManager;
    private final OrderPersistenceManager orderPersistenceManager;
    private final OrderAssembler orderAssembler;
    private final ClockHolder clockHolder;

    public CancelOrderService(
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
     * 주문 취소
     *
     * @param orderId 주문 ID (UUID String)
     * @return 취소된 주문 응답
     */
    @Override
    @Transactional
    public OrderResponse cancelOrder(String orderId) {
        Order order = orderReadManager.findById(orderId);
        Instant now = Instant.now(clockHolder.getClock());

        Order cancelledOrder = order.cancel(now);
        orderPersistenceManager.persist(cancelledOrder);
        return orderAssembler.toResponse(cancelledOrder);
    }
}
