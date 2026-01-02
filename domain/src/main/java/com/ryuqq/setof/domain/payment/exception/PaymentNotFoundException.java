package com.ryuqq.setof.domain.payment.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import java.util.Map;
import java.util.UUID;

/**
 * PaymentNotFoundException - 결제 미존재 예외
 *
 * <p>요청한 결제가 존재하지 않는 경우 발생합니다.
 */
public class PaymentNotFoundException extends DomainException {

    public PaymentNotFoundException(PaymentId paymentId) {
        super(
                PaymentErrorCode.PAYMENT_NOT_FOUND,
                String.format("결제 정보를 찾을 수 없습니다: %s", paymentId.value()),
                Map.of("paymentId", paymentId.value().toString()));
    }

    public PaymentNotFoundException(UUID paymentId) {
        super(
                PaymentErrorCode.PAYMENT_NOT_FOUND,
                String.format("결제 정보를 찾을 수 없습니다: %s", paymentId),
                Map.of("paymentId", paymentId.toString()));
    }

    public PaymentNotFoundException(Long legacyPaymentId) {
        super(
                PaymentErrorCode.PAYMENT_NOT_FOUND,
                String.format("결제 정보를 찾을 수 없습니다: Legacy ID %d", legacyPaymentId),
                Map.of("legacyPaymentId", legacyPaymentId.toString()));
    }
}
