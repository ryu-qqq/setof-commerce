package com.ryuqq.setof.application.order.port.in.query;

import com.ryuqq.setof.application.order.dto.response.OrderHistoryResult;
import java.util.List;

/**
 * GetOrderHistoryUseCase - 주문 이력 조회 UseCase.
 *
 * <p>APP-UC-001: 1 UseCase = 1 행위.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetOrderHistoryUseCase {

    /**
     * 주문 ID로 이력 목록 조회.
     *
     * @param orderId 주문 ID
     * @return 주문 이력 결과 목록
     */
    List<OrderHistoryResult> execute(long orderId);
}
