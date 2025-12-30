package com.ryuqq.setof.domain.checkout.exception;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;
import java.util.UUID;

/**
 * CheckoutNotFoundException - 결제 세션 미존재 예외
 *
 * <p>요청한 결제 세션이 존재하지 않는 경우 발생합니다.
 */
public class CheckoutNotFoundException extends DomainException {

    public CheckoutNotFoundException(CheckoutId checkoutId) {
        super(
                CheckoutErrorCode.CHECKOUT_NOT_FOUND,
                String.format("결제 세션을 찾을 수 없습니다: %s", checkoutId.value()),
                Map.of("checkoutId", checkoutId.value().toString()));
    }

    public CheckoutNotFoundException(UUID checkoutId) {
        super(
                CheckoutErrorCode.CHECKOUT_NOT_FOUND,
                String.format("결제 세션을 찾을 수 없습니다: %s", checkoutId),
                Map.of("checkoutId", checkoutId.toString()));
    }
}
