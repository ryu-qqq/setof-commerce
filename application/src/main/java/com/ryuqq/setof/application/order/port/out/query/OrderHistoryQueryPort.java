package com.ryuqq.setof.application.order.port.out.query;

import com.ryuqq.setof.domain.order.vo.OrderHistory;
import java.util.List;

/**
 * OrderHistoryQueryPort - 주문 이력 조회 Port.
 *
 * <p>orders_history + shipment 테이블에서 주문 이력을 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface OrderHistoryQueryPort {

    /**
     * 주문 ID로 이력 목록 조회.
     *
     * @param orderId 주문 ID
     * @return 주문 이력 목록 (도메인 VO)
     */
    List<OrderHistory> findByOrderId(long orderId);
}
