package com.ryuqq.setof.domain.order.vo;

/**
 * 주문 상태별 건수 VO.
 *
 * <p>레거시 주문 상태(String)와 건수를 담는 불변 값 객체입니다.
 *
 * @param orderStatus 주문 상태 (레거시 OrderStatus 문자열)
 * @param count 해당 상태의 건수
 */
public record OrderStatusCount(String orderStatus, long count) {

    public static OrderStatusCount of(String orderStatus, long count) {
        return new OrderStatusCount(orderStatus, count);
    }
}
