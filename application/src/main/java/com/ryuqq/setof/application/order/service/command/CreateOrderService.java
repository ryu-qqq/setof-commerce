package com.ryuqq.setof.application.order.service.command;

import com.ryuqq.setof.application.order.assembler.OrderAssembler;
import com.ryuqq.setof.application.order.dto.command.CreateOrderCommand;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.factory.command.OrderCommandFactory;
import com.ryuqq.setof.application.order.manager.command.OrderPersistenceManager;
import com.ryuqq.setof.application.order.port.in.command.CreateOrderUseCase;
import com.ryuqq.setof.domain.order.aggregate.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 생성 Service
 *
 * <p>주문을 생성합니다
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateOrderService implements CreateOrderUseCase {

    private final OrderCommandFactory orderCommandFactory;
    private final OrderPersistenceManager orderPersistenceManager;
    private final OrderAssembler orderAssembler;

    public CreateOrderService(
            OrderCommandFactory orderCommandFactory,
            OrderPersistenceManager orderPersistenceManager,
            OrderAssembler orderAssembler) {
        this.orderCommandFactory = orderCommandFactory;
        this.orderPersistenceManager = orderPersistenceManager;
        this.orderAssembler = orderAssembler;
    }

    /**
     * 주문 생성
     *
     * @param command 주문 생성 Command
     * @return 생성된 주문 응답
     */
    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderCommand command) {
        Order order = orderCommandFactory.createOrder(command);
        orderPersistenceManager.persist(order);
        return orderAssembler.toResponse(order);
    }
}
