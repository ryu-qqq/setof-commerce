package com.ryuqq.setof.application.order.service.query;

import com.ryuqq.setof.application.order.dto.query.GetOrderStatusCountsQuery;
import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResponse;
import com.ryuqq.setof.application.order.port.in.query.GetOrderStatusCountsUseCase;
import com.ryuqq.setof.application.order.port.out.query.OrderQueryPort;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * GetOrderStatusCountsService - 주문 상태별 개수 조회 서비스
 *
 * <p>고객의 주문 상태별 개수를 조회합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>주문 상태별 개수 조회 비즈니스 로직
 *   <li>QueryPort를 통한 데이터 조회
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class GetOrderStatusCountsService implements GetOrderStatusCountsUseCase {

    private final OrderQueryPort orderQueryPort;

    public GetOrderStatusCountsService(OrderQueryPort orderQueryPort) {
        this.orderQueryPort = orderQueryPort;
    }

    /**
     * 주문 상태별 개수 조회
     *
     * @param query 조회 조건 (memberId, statuses)
     * @return 상태별 개수 응답
     */
    @Override
    public OrderStatusCountResponse getStatusCounts(GetOrderStatusCountsQuery query) {
        Map<String, Long> counts =
                orderQueryPort.getOrderStatusCounts(query.memberId(), query.statuses());

        return OrderStatusCountResponse.of(counts);
    }
}
