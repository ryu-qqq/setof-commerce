package com.ryuqq.setof.application.order.manager;

import com.ryuqq.setof.application.order.port.out.query.OrderDetailQueryPort;
import com.ryuqq.setof.application.order.port.out.query.OrderHistoryQueryPort;
import com.ryuqq.setof.application.order.port.out.query.OrderStatusCountQueryPort;
import com.ryuqq.setof.domain.order.query.OrderSearchCriteria;
import com.ryuqq.setof.domain.order.vo.OrderHistory;
import com.ryuqq.setof.domain.order.vo.OrderStatusCount;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderReadManager - 주문 조회 Manager.
 *
 * <p>Port를 래핑하여 트랜잭션 경계를 설정합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class OrderReadManager {

    private final OrderStatusCountQueryPort orderStatusCountQueryPort;
    private final OrderHistoryQueryPort orderHistoryQueryPort;
    private final OrderDetailQueryPort orderDetailQueryPort;

    public OrderReadManager(
            OrderStatusCountQueryPort orderStatusCountQueryPort,
            OrderHistoryQueryPort orderHistoryQueryPort,
            OrderDetailQueryPort orderDetailQueryPort) {
        this.orderStatusCountQueryPort = orderStatusCountQueryPort;
        this.orderHistoryQueryPort = orderHistoryQueryPort;
        this.orderDetailQueryPort = orderDetailQueryPort;
    }

    /**
     * 주문 ID 목록 조회 (커서 기반 페이징).
     *
     * @param criteria 검색 조건
     * @return 주문 ID 목록
     */
    @Transactional(readOnly = true)
    public List<Long> fetchOrderIds(OrderSearchCriteria criteria) {
        return orderDetailQueryPort.fetchOrderIds(criteria);
    }

    @Transactional(readOnly = true)
    public List<OrderStatusCount> countByStatus(long userId, List<String> orderStatuses) {
        return orderStatusCountQueryPort.countByStatus(userId, orderStatuses);
    }

    /**
     * 주문 ID로 이력 목록 조회.
     *
     * @param orderId 주문 ID
     * @return 주문 이력 목록 (도메인 VO)
     */
    @Transactional(readOnly = true)
    public List<OrderHistory> findHistoriesByOrderId(long orderId) {
        return orderHistoryQueryPort.findByOrderId(orderId);
    }
}
