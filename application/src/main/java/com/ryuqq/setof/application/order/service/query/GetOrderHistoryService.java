package com.ryuqq.setof.application.order.service.query;

import com.ryuqq.setof.application.order.dto.response.OrderHistoryResult;
import com.ryuqq.setof.application.order.manager.OrderReadManager;
import com.ryuqq.setof.application.order.port.in.query.GetOrderHistoryUseCase;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetOrderHistoryService - 주문 이력 조회 서비스.
 *
 * <p>OrderReadManager를 통해 주문 이력을 조회하고 Result로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetOrderHistoryService implements GetOrderHistoryUseCase {

    private final OrderReadManager orderReadManager;

    public GetOrderHistoryService(OrderReadManager orderReadManager) {
        this.orderReadManager = orderReadManager;
    }

    @Override
    public List<OrderHistoryResult> execute(long orderId) {
        return orderReadManager.findHistoriesByOrderId(orderId).stream()
                .map(OrderHistoryResult::from)
                .toList();
    }
}
