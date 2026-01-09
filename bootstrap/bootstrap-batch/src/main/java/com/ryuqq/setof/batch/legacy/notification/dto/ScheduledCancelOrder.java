package com.ryuqq.setof.batch.legacy.notification.dto;

import java.time.LocalDateTime;

/**
 * 취소 예정 주문 DTO (24시간 대기 후 자동 취소 대상)
 *
 * @author development-team
 * @since 1.0.0
 */
public record ScheduledCancelOrder(
        Long orderId,
        String phoneNumber,
        String orderNumber,
        String productName,
        LocalDateTime cancelRequestDate) {}
