package com.ryuqq.setof.application.payment.dto.command;

/**
 * 결제 실패 Command
 *
 * <p>PG사로부터 결제 실패 처리를 위한 Command입니다.
 *
 * @param paymentId 결제 ID (UUID String)
 * @param failReason 실패 사유 (선택)
 * @author development-team
 * @since 1.0.0
 */
public record FailPaymentCommand(String paymentId, String failReason) {

    public FailPaymentCommand {
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("paymentId는 필수입니다");
        }
    }

    /** 실패 사유 없이 생성 */
    public static FailPaymentCommand of(String paymentId) {
        return new FailPaymentCommand(paymentId, null);
    }

    /** 실패 사유와 함께 생성 */
    public static FailPaymentCommand of(String paymentId, String failReason) {
        return new FailPaymentCommand(paymentId, failReason);
    }
}
