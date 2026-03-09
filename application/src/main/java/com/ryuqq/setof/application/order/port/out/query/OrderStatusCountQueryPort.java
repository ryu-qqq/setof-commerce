package com.ryuqq.setof.application.order.port.out.query;

import com.ryuqq.setof.domain.order.vo.OrderStatusCount;
import java.util.List;

/**
 * OrderStatusCountQueryPort - 주문 상태별 건수 조회 Port.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface OrderStatusCountQueryPort {

    /**
     * 사용자의 상태별 주문 건수 조회 (최근 3개월).
     *
     * @param userId 사용자 ID
     * @param orderStatuses 조회할 주문 상태 목록
     * @return 상태별 건수 목록
     */
    List<OrderStatusCount> countByStatus(long userId, List<String> orderStatuses);
}
