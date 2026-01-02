package com.ryuqq.setof.application.order.service.query;

import com.ryuqq.setof.application.order.assembler.OrderAssembler;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.manager.query.OrderReadManager;
import com.ryuqq.setof.application.order.port.in.query.GetOrderUseCase;
import com.ryuqq.setof.domain.order.aggregate.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 조회 Service
 *
 * <p>주문 정보를 조회합니다
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetOrderService implements GetOrderUseCase {

    private final OrderReadManager orderReadManager;
    private final OrderAssembler orderAssembler;

    public GetOrderService(OrderReadManager orderReadManager, OrderAssembler orderAssembler) {
        this.orderReadManager = orderReadManager;
        this.orderAssembler = orderAssembler;
    }

    /**
     * 주문 조회
     *
     * @param orderId 주문 ID (UUID String)
     * @return 주문 응답
     */
    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(String orderId) {
        Order order = orderReadManager.findById(orderId);
        return orderAssembler.toResponse(order);
    }

    /**
     * 주문 번호로 조회
     *
     * @param orderNumber 주문 번호
     * @return 주문 응답
     */
    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderReadManager.findByOrderNumber(orderNumber);
        return orderAssembler.toResponse(order);
    }
}
