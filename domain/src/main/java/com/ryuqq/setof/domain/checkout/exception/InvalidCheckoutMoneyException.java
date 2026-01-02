package com.ryuqq.setof.domain.checkout.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * InvalidCheckoutMoneyException - 잘못된 금액 예외
 *
 * <p>금액이 null이거나 음수인 경우 발생합니다.
 */
public class InvalidCheckoutMoneyException extends DomainException {

    public InvalidCheckoutMoneyException(BigDecimal value, String message) {
        super(
                CheckoutErrorCode.INVALID_CHECKOUT_MONEY,
                message,
                Map.of("value", value != null ? value.toString() : "null"));
    }
}
