package com.ryuqq.setof.application.order.service.query;

import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResult;
import com.ryuqq.setof.application.order.manager.OrderReadManager;
import com.ryuqq.setof.application.order.port.in.query.GetOrderStatusCountsUseCase;
import com.ryuqq.setof.domain.order.vo.OrderStatusCount;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetOrderStatusCountsService - 주문 상태별 건수 조회 서비스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetOrderStatusCountsService implements GetOrderStatusCountsUseCase {

    private final OrderReadManager orderReadManager;

    public GetOrderStatusCountsService(OrderReadManager orderReadManager) {
        this.orderReadManager = orderReadManager;
    }

    @Override
    public List<OrderStatusCountResult> execute(long userId, List<String> orderStatuses) {
        List<OrderStatusCount> counts = orderReadManager.countByStatus(userId, orderStatuses);
        return counts.stream().map(OrderStatusCountResult::from).toList();
    }
}
