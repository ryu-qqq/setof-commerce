package com.ryuqq.setof.domain.payment.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import com.ryuqq.setof.domain.payment.vo.PaymentMoney;
import java.util.Map;

/**
 * RefundAmountException - 환불 금액 예외
 *
 * <p>환불 금액이 유효하지 않은 경우 발생합니다.
 */
public class RefundAmountException extends DomainException {

    /** 환불 금액 초과 예외 */
    public static RefundAmountException exceedsAvailable(
            PaymentId paymentId, PaymentMoney requestedAmount, PaymentMoney availableAmount) {
        return new RefundAmountException(
                PaymentErrorCode.REFUND_EXCEEDS_AVAILABLE,
                paymentId,
                requestedAmount,
                availableAmount);
    }

    private RefundAmountException(
            PaymentErrorCode errorCode,
            PaymentId paymentId,
            PaymentMoney requestedAmount,
            PaymentMoney availableAmount) {
        super(
                errorCode,
                String.format(
                        "환불 금액 초과 - 요청: %s, 가능: %s",
                        requestedAmount.value(), availableAmount.value()),
                Map.of(
                        "paymentId", paymentId.value().toString(),
                        "requestedAmount", requestedAmount.value().toString(),
                        "availableAmount", availableAmount.value().toString()));
    }
}
