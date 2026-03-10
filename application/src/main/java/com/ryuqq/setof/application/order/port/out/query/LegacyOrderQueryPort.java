package com.ryuqq.setof.application.order.port.out.query;

/**
 * Legacy 주문 조회 Port (Command 검증용).
 *
 * <p>주문 상태 변경 전 현재 상태를 조회하거나 사용자 검증에 사용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyOrderQueryPort {

    /**
     * 주문의 현재 상태를 조회합니다.
     *
     * @param orderId 주문 ID
     * @return 현재 주문 상태 (LegacyOrderStatus enum name)
     */
    String fetchOrderStatus(long orderId);

    /**
     * 주문의 소유자 ID를 조회합니다.
     *
     * @param orderId 주문 ID
     * @return 사용자 ID
     */
    long fetchOrderUserId(long orderId);
}
