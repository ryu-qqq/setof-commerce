package com.ryuqq.setof.domain.checkout.exception;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.checkout.vo.CheckoutStatus;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * CheckoutStatusException - 결제 세션 상태 예외
 *
 * <p>결제 세션의 상태 전이가 불가능한 경우 발생합니다.
 */
public class CheckoutStatusException extends DomainException {

    /** 이미 완료된 결제 세션 예외 */
    public static CheckoutStatusException alreadyCompleted(CheckoutId checkoutId) {
        return new CheckoutStatusException(
                CheckoutErrorCode.CHECKOUT_ALREADY_COMPLETED, checkoutId, CheckoutStatus.COMPLETED);
    }

    /** 만료된 결제 세션 예외 */
    public static CheckoutStatusException expired(CheckoutId checkoutId) {
        return new CheckoutStatusException(
                CheckoutErrorCode.CHECKOUT_EXPIRED, checkoutId, CheckoutStatus.EXPIRED);
    }

    /** 결제 처리 시작 불가 예외 */
    public static CheckoutStatusException notProcessable(
            CheckoutId checkoutId, CheckoutStatus currentStatus) {
        return new CheckoutStatusException(
                CheckoutErrorCode.CHECKOUT_NOT_PROCESSABLE, checkoutId, currentStatus);
    }

    /** 결제 완료 불가 예외 */
    public static CheckoutStatusException notCompletable(
            CheckoutId checkoutId, CheckoutStatus currentStatus) {
        return new CheckoutStatusException(
                CheckoutErrorCode.CHECKOUT_NOT_COMPLETABLE, checkoutId, currentStatus);
    }

    private CheckoutStatusException(
            CheckoutErrorCode errorCode, CheckoutId checkoutId, CheckoutStatus status) {
        super(
                errorCode,
                String.format(
                        "결제 세션 상태 오류 - checkoutId: %s, status: %s", checkoutId.value(), status),
                Map.of(
                        "checkoutId", checkoutId.value().toString(),
                        "status", status.name()));
    }
}
