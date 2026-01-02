package com.ryuqq.setof.application.payment.dto.command;

import java.math.BigDecimal;

/**
 * 결제 승인 Command
 *
 * @param paymentId 결제 ID (UUID String)
 * @param pgTransactionId PG사 거래 ID
 * @param approvedAmount 승인 금액
 * @author development-team
 * @since 1.0.0
 */
public record ApprovePaymentCommand(
        String paymentId, String pgTransactionId, BigDecimal approvedAmount) {

    public ApprovePaymentCommand {
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("paymentId는 필수입니다");
        }
        if (pgTransactionId == null || pgTransactionId.isBlank()) {
            throw new IllegalArgumentException("pgTransactionId는 필수입니다");
        }
        if (approvedAmount == null || approvedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("approvedAmount는 0보다 커야합니다");
        }
    }
}
