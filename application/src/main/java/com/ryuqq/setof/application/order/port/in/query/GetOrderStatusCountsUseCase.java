package com.ryuqq.setof.application.order.port.in.query;

import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResult;
import java.util.List;

/**
 * GetOrderStatusCountsUseCase - 주문 상태별 건수 조회 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetOrderStatusCountsUseCase {

    /**
     * 사용자의 상태별 주문 건수 조회.
     *
     * @param userId 사용자 ID
     * @param orderStatuses 조회할 주문 상태 목록
     * @return 상태별 건수 목록
     */
    List<OrderStatusCountResult> execute(long userId, List<String> orderStatuses);
}
