package com.ryuqq.setof.domain.discount.exception;

/**
 * 할인 정책을 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 할인 정책이 존재하지 않을 때 발생합니다.
 */
public class DiscountPolicyNotFoundException extends DiscountException {

    private static final DiscountErrorCode ERROR_CODE = DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND;

    public DiscountPolicyNotFoundException() {
        super(ERROR_CODE);
    }

    public DiscountPolicyNotFoundException(Long id) {
        super(ERROR_CODE, String.format("ID가 %d인 할인 정책을 찾을 수 없습니다", id));
    }
}
