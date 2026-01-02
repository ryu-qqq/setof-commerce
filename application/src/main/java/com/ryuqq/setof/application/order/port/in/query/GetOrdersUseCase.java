package com.ryuqq.setof.application.order.port.in.query;

import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.application.order.dto.query.GetOrdersQuery;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;

/**
 * GetOrdersUseCase - 주문 목록 조회 UseCase
 *
 * <p>고객의 주문 목록을 조회합니다. 커서 기반 페이지네이션을 지원합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetOrdersUseCase {

    /**
     * 주문 목록 조회
     *
     * @param query 조회 조건 (memberId, orderStatuses, 기간, 페이징)
     * @return Slice 형태의 주문 목록
     */
    SliceResponse<OrderResponse> getOrders(GetOrdersQuery query);
}
