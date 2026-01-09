package com.ryuqq.setof.batch.legacy.notification.dto;

import java.time.LocalDateTime;

/**
 * 가상계좌 만료 예정 주문 DTO (1시간 내 만료 대상)
 *
 * @author development-team
 * @since 1.0.0
 */
public record VBankExpiryOrder(
        Long orderId,
        String phoneNumber,
        String orderNumber,
        String bankName,
        String accountNumber,
        Integer totalAmount,
        LocalDateTime expiryDate) {}
