package com.ryuqq.setof.application.order.port.out.command;

/**
 * Legacy 주문 명령 Port.
 *
 * <p>Legacy orders 테이블의 order_status 컬럼을 업데이트하는 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyOrderCommandPort {

    /**
     * Legacy 주문 상태를 변경합니다.
     *
     * @param orderId 주문 ID
     * @param targetOrderStatus 목표 주문 상태 (LegacyOrderStatus enum name)
     * @return 변경 전 주문 상태 (LegacyOrderStatus enum name)
     */
    String updateOrderStatus(long orderId, String targetOrderStatus);
}
