package com.ryuqq.setof.application.order.service.command;

import com.ryuqq.setof.application.order.assembler.OrderAssembler;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.manager.command.OrderPersistenceManager;
import com.ryuqq.setof.application.order.manager.query.OrderReadManager;
import com.ryuqq.setof.application.order.port.in.command.StartPreparingOrderUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.order.aggregate.Order;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품 준비 시작 Service
 *
 * <p>상품 준비를 시작합니다
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class StartPreparingOrderService implements StartPreparingOrderUseCase {

    private final OrderReadManager orderReadManager;
    private final OrderPersistenceManager orderPersistenceManager;
    private final OrderAssembler orderAssembler;
    private final ClockHolder clockHolder;

    public StartPreparingOrderService(
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
     * 상품 준비 시작
     *
     * @param orderId 주문 ID (UUID String)
     * @return 준비 중 상태의 주문 응답
     */
    @Override
    @Transactional
    public OrderResponse startPreparing(String orderId) {
        Order order = orderReadManager.findById(orderId);
        Instant now = Instant.now(clockHolder.getClock());

        Order preparingOrder = order.startPreparing(now);
        orderPersistenceManager.persist(preparingOrder);
        return orderAssembler.toResponse(preparingOrder);
    }
}
