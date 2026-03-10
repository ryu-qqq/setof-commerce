package com.ryuqq.setof.application.order.dto.command;

/**
 * Legacy 주문 상태 변경 Command.
 *
 * <p>Legacy orders 테이블의 order_status 컬럼을 직접 업데이트하는 명령입니다.
 *
 * @param orderId 주문 ID
 * @param userId 사용자 ID
 * @param targetOrderStatus 목표 주문 상태 (Legacy enum 문자열)
 * @param changeReason 변경 사유
 * @param changeDetailReason 변경 상세 사유
 * @author ryu-qqq
 * @since 1.1.0
 */
public record UpdateLegacyOrderStatusCommand(
        long orderId,
        long userId,
        String targetOrderStatus,
        String changeReason,
        String changeDetailReason) {}
