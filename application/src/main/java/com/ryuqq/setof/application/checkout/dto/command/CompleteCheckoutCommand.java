package com.ryuqq.setof.application.checkout.dto.command;

import java.math.BigDecimal;

/**
 * 체크아웃 완료 Command
 *
 * <p>PG 결제 완료 후 체크아웃을 완료 처리하기 위한 Command입니다.
 *
 * @param paymentId PG 결제 ID (UUIDv7 String)
 * @param pgTransactionId PG사 거래 ID
 * @param approvedAmount PG사 승인 금액
 * @author development-team
 * @since 1.0.0
 */
public record CompleteCheckoutCommand(
        String paymentId, String pgTransactionId, BigDecimal approvedAmount) {

    public CompleteCheckoutCommand {
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("paymentId는 필수입니다");
        }
        if (pgTransactionId == null || pgTransactionId.isBlank()) {
            throw new IllegalArgumentException("pgTransactionId는 필수입니다");
        }
        if (approvedAmount == null || approvedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("approvedAmount는 0보다 커야 합니다");
        }
    }
}
