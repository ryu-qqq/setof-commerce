package com.ryuqq.setof.domain.checkout.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * DuplicateCheckoutException - 중복 결제 세션 요청 예외
 *
 * <p>동일한 멱등키로 이미 결제 세션이 처리 중인 경우 발생합니다.
 */
public class DuplicateCheckoutException extends DomainException {

    /**
     * 중복 요청 예외 생성
     *
     * @param idempotencyKey 멱등키
     * @return DuplicateCheckoutException 인스턴스
     */
    public static DuplicateCheckoutException forIdempotencyKey(String idempotencyKey) {
        return new DuplicateCheckoutException(idempotencyKey);
    }

    private DuplicateCheckoutException(String idempotencyKey) {
        super(
                CheckoutErrorCode.DUPLICATE_CHECKOUT_REQUEST,
                String.format("이미 처리 중인 결제 세션 요청입니다 - idempotencyKey: %s", idempotencyKey),
                Map.of("idempotencyKey", idempotencyKey));
    }
}
