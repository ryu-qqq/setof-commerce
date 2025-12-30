package com.ryuqq.setof.application.order.port.out.query;

import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.query.criteria.OrderSearchCriteria;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderNumber;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 주문 조회 Port
 *
 * <p>주문 도메인 조회 작업을 위한 Port-Out 인터페이스입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface OrderQueryPort {

    /**
     * 주문 ID로 조회
     *
     * @param orderId 주문 ID
     * @return Optional Order
     */
    Optional<Order> findById(OrderId orderId);

    /**
     * 주문 번호로 조회
     *
     * @param orderNumber 주문 번호
     * @return Optional Order
     */
    Optional<Order> findByOrderNumber(OrderNumber orderNumber);

    /**
     * Legacy 주문 ID로 조회
     *
     * <p>V1 API 호환을 위한 Legacy ID 조회 메서드
     *
     * @param legacyOrderId Legacy 주문 ID
     * @return Optional Order
     */
    Optional<Order> findByLegacyId(Long legacyOrderId);

    /**
     * Criteria 기반 주문 목록 조회 (커서 기반 페이징)
     *
     * <p>Slice 방식으로 limit + 1 조회하여 hasNext 판단
     *
     * @param criteria 검색 조건
     * @return Order 목록 (fetchSize 개)
     */
    List<Order> findByCriteria(OrderSearchCriteria criteria);

    /**
     * Criteria 기반 주문 개수 조회
     *
     * @param criteria 검색 조건
     * @return 주문 개수
     */
    long countByCriteria(OrderSearchCriteria criteria);

    /**
     * 회원 ID와 상태별 주문 개수 조회
     *
     * @param memberId 회원 ID
     * @param statuses 조회할 상태 목록 (nullable - null이면 전체 상태)
     * @return 상태별 개수 Map (status → count)
     */
    Map<String, Long> getOrderStatusCounts(String memberId, List<String> statuses);
}
