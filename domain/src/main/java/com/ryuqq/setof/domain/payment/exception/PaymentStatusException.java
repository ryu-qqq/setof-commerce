package com.ryuqq.setof.domain.payment.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import com.ryuqq.setof.domain.payment.vo.PaymentStatus;
import java.util.Map;

/**
 * PaymentStatusException - 결제 상태 예외
 *
 * <p>결제의 상태 전이가 불가능한 경우 발생합니다.
 */
public class PaymentStatusException extends DomainException {

    /** 이미 승인된 결제 예외 */
    public static PaymentStatusException alreadyApproved(PaymentId paymentId) {
        return new PaymentStatusException(
                PaymentErrorCode.PAYMENT_ALREADY_APPROVED, paymentId, PaymentStatus.APPROVED);
    }

    /** 이미 취소된 결제 예외 */
    public static PaymentStatusException alreadyCancelled(PaymentId paymentId) {
        return new PaymentStatusException(
                PaymentErrorCode.PAYMENT_ALREADY_CANCELLED, paymentId, PaymentStatus.CANCELLED);
    }

    /** 이미 전액 환불된 결제 예외 */
    public static PaymentStatusException alreadyRefunded(PaymentId paymentId) {
        return new PaymentStatusException(
                PaymentErrorCode.PAYMENT_ALREADY_REFUNDED, paymentId, PaymentStatus.FULLY_REFUNDED);
    }

    /** 승인 불가 예외 */
    public static PaymentStatusException notApprovable(
            PaymentId paymentId, PaymentStatus currentStatus) {
        return new PaymentStatusException(
                PaymentErrorCode.PAYMENT_NOT_APPROVABLE, paymentId, currentStatus);
    }

    /** 환불 불가 예외 */
    public static PaymentStatusException notRefundable(
            PaymentId paymentId, PaymentStatus currentStatus) {
        return new PaymentStatusException(
                PaymentErrorCode.PAYMENT_NOT_REFUNDABLE, paymentId, currentStatus);
    }

    /** 취소 불가 예외 */
    public static PaymentStatusException notCancellable(
            PaymentId paymentId, PaymentStatus currentStatus) {
        return new PaymentStatusException(
                PaymentErrorCode.PAYMENT_NOT_CANCELLABLE, paymentId, currentStatus);
    }

    private PaymentStatusException(
            PaymentErrorCode errorCode, PaymentId paymentId, PaymentStatus status) {
        super(
                errorCode,
                String.format("결제 상태 오류 - paymentId: %s, status: %s", paymentId.value(), status),
                Map.of(
                        "paymentId", paymentId.value().toString(),
                        "status", status.name()));
    }
}
