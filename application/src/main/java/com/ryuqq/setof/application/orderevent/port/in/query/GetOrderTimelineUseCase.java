package com.ryuqq.setof.application.orderevent.port.in.query;

import com.ryuqq.setof.application.orderevent.dto.response.OrderTimelineResponse;

/**
 * GetOrderTimelineUseCase - 주문 타임라인 조회 UseCase
 *
 * @author development-team
 * @since 2.0.0
 */
public interface GetOrderTimelineUseCase {

    /**
     * 주문 타임라인 조회
     *
     * @param orderId 주문 ID
     * @return 주문 타임라인 응답
     */
    OrderTimelineResponse getTimeline(String orderId);
}
