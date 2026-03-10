package com.ryuqq.setof.application.order.dto.response;

/**
 * 주문 상태 변경 결과.
 *
 * @param orderId 주문 ID
 * @param userId 사용자 ID
 * @param toBeOrderStatus 변경 후 상태
 * @param asIsOrderStatus 변경 전 상태
 * @param changeReason 변경 사유
 * @param changeDetailReason 변경 상세 사유
 * @author ryu-qqq
 * @since 1.1.0
 */
public record UpdateOrderResult(
        long orderId,
        long userId,
        String toBeOrderStatus,
        String asIsOrderStatus,
        String changeReason,
        String changeDetailReason) {}
