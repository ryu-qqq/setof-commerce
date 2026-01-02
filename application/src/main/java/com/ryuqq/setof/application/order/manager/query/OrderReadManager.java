package com.ryuqq.setof.application.order.manager.query;

import com.ryuqq.setof.application.order.port.out.query.OrderQueryPort;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.exception.OrderNotFoundException;
import com.ryuqq.setof.domain.order.query.criteria.OrderSearchCriteria;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderNumber;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 주문 조회 Manager
 *
 * <p>주문 조회를 위한 Port 호출을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OrderReadManager {

    private final OrderQueryPort orderQueryPort;

    public OrderReadManager(OrderQueryPort orderQueryPort) {
        this.orderQueryPort = orderQueryPort;
    }

    /**
     * 주문 조회
     *
     * @param orderId 주문 ID (UUID String)
     * @return Order 도메인 객체
     * @throws OrderNotFoundException 주문이 존재하지 않는 경우
     */
    public Order findById(String orderId) {
        OrderId id = OrderId.fromString(orderId);
        return orderQueryPort.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    /**
     * 주문 번호로 조회
     *
     * @param orderNumber 주문 번호
     * @return Order 도메인 객체
     * @throws OrderNotFoundException 주문이 존재하지 않는 경우
     */
    public Order findByOrderNumber(String orderNumber) {
        OrderNumber number = OrderNumber.of(orderNumber);
        return orderQueryPort
                .findByOrderNumber(number)
                .orElseThrow(() -> new OrderNotFoundException(number.value()));
    }

    /**
     * 검색 조건으로 주문 목록 조회 (커서 기반 페이징)
     *
     * <p>Slice 방식으로 limit + 1 조회하여 hasNext 판단
     *
     * @param criteria 검색 조건 Criteria
     * @return Order 목록 (fetchSize 개)
     */
    public List<Order> findByCriteria(OrderSearchCriteria criteria) {
        return orderQueryPort.findByCriteria(criteria);
    }

    /**
     * 검색 조건에 맞는 주문 총 개수 조회
     *
     * @param criteria 검색 조건 Criteria
     * @return 총 개수
     */
    public long countByCriteria(OrderSearchCriteria criteria) {
        return orderQueryPort.countByCriteria(criteria);
    }

    /**
     * 회원 ID와 상태별 주문 개수 조회
     *
     * @param memberId 회원 ID
     * @param statuses 조회할 상태 목록 (nullable - null이면 전체 상태)
     * @return 상태별 개수 Map (status → count)
     */
    public Map<String, Long> getOrderStatusCounts(String memberId, List<String> statuses) {
        return orderQueryPort.getOrderStatusCounts(memberId, statuses);
    }
}
