package com.ryuqq.setof.domain.checkout.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * InvalidCheckoutItemException - 잘못된 결제 항목 예외
 *
 * <p>결제 항목이 유효하지 않은 경우 발생합니다.
 */
public class InvalidCheckoutItemException extends DomainException {

    public InvalidCheckoutItemException(String message) {
        super(CheckoutErrorCode.INVALID_CHECKOUT_ITEM, message);
    }
}
