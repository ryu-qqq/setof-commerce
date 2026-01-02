package com.ryuqq.setof.domain.checkout.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * InvalidCheckoutIdException - 잘못된 CheckoutId 예외
 *
 * <p>CheckoutId가 null이거나 잘못된 형식인 경우 발생합니다.
 */
public class InvalidCheckoutIdException extends DomainException {

    public InvalidCheckoutIdException(String message) {
        super(CheckoutErrorCode.INVALID_CHECKOUT_ID, message);
    }

    public InvalidCheckoutIdException(String message, String invalidValue) {
        super(CheckoutErrorCode.INVALID_CHECKOUT_ID, message, Map.of("invalidValue", invalidValue));
    }
}
