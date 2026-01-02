package com.ryuqq.setof.application.payment.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 결제 응답 DTO
 *
 * @param paymentId 결제 ID (UUID)
 * @param checkoutId 체크아웃 ID (UUID)
 * @param pgProvider PG사
 * @param pgTransactionId PG사 거래 ID
 * @param method 결제 수단
 * @param status 결제 상태
 * @param requestedAmount 요청 금액
 * @param approvedAmount 승인 금액
 * @param refundedAmount 환불 금액
 * @param approvedAt 승인 시각
 * @param cancelledAt 취소 시각
 * @param createdAt 생성 시각
 * @author development-team
 * @since 1.0.0
 */
public record PaymentResponse(
        String paymentId,
        String checkoutId,
        String pgProvider,
        String pgTransactionId,
        String method,
        String status,
        BigDecimal requestedAmount,
        BigDecimal approvedAmount,
        BigDecimal refundedAmount,
        Instant approvedAt,
        Instant cancelledAt,
        Instant createdAt) {}
