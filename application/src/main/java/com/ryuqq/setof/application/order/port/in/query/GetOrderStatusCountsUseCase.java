package com.ryuqq.setof.application.order.port.in.query;

import com.ryuqq.setof.application.order.dto.query.GetOrderStatusCountsQuery;
import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResponse;

/**
 * GetOrderStatusCountsUseCase - 주문 상태별 개수 조회 UseCase
 *
 * <p>고객의 주문 상태별 개수를 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetOrderStatusCountsUseCase {

    /**
     * 주문 상태별 개수 조회
     *
     * @param query 조회 조건 (memberId, statuses)
     * @return 상태별 개수 응답
     */
    OrderStatusCountResponse getStatusCounts(GetOrderStatusCountsQuery query);
}
