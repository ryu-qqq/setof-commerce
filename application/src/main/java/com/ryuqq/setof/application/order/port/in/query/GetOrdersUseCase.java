package com.ryuqq.setof.application.order.port.in.query;

import com.ryuqq.setof.application.order.dto.query.OrderSearchParams;
import com.ryuqq.setof.application.order.dto.response.OrderSliceResult;

/**
 * GetOrdersUseCase - 주문 목록 조회 UseCase.
 *
 * <p>APP-UC-001: UseCase = 1 behavior.
 *
 * <p>APP-UC-002: UseCase 네이밍 규칙 - Get*UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetOrdersUseCase {

    /**
     * 주문 목록 조회 (커서 페이징 + 상태별 건수).
     *
     * @param params 주문 검색 파라미터
     * @return 주문 슬라이스 결과
     */
    OrderSliceResult execute(OrderSearchParams params);
}
