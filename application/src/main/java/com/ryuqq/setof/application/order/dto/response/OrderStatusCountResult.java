package com.ryuqq.setof.application.order.dto.response;

import com.ryuqq.setof.domain.order.vo.OrderStatusCount;

/**
 * OrderStatusCountResult - 주문 상태별 건수 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param orderStatus 주문 상태
 * @param count 해당 상태의 건수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record OrderStatusCountResult(String orderStatus, long count) {

    /**
     * Domain VO → Result 변환.
     *
     * @param vo OrderStatusCount 도메인 VO
     * @return OrderStatusCountResult
     */
    public static OrderStatusCountResult from(OrderStatusCount vo) {
        return new OrderStatusCountResult(vo.orderStatus(), vo.count());
    }
}
