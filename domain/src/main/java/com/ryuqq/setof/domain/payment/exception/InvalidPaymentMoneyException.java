package com.ryuqq.setof.domain.payment.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * InvalidPaymentMoneyException - 잘못된 결제 금액 예외
 *
 * <p>금액이 null이거나 음수인 경우 발생합니다.
 */
public class InvalidPaymentMoneyException extends DomainException {

    public InvalidPaymentMoneyException(BigDecimal value, String message) {
        super(
                PaymentErrorCode.INVALID_PAYMENT_MONEY,
                message,
                Map.of("value", value != null ? value.toString() : "null"));
    }
}
