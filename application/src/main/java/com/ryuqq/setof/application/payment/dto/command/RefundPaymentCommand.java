package com.ryuqq.setof.application.payment.dto.command;

import java.math.BigDecimal;

/**
 * 결제 환불 Command
 *
 * @param paymentId 결제 ID (UUID String)
 * @param refundAmount 환불 금액
 * @author development-team
 * @since 1.0.0
 */
public record RefundPaymentCommand(String paymentId, BigDecimal refundAmount) {

    public RefundPaymentCommand {
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("paymentId는 필수입니다");
        }
        if (refundAmount == null || refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("refundAmount는 0보다 커야합니다");
        }
    }
}
