package com.ryuqq.setof.domain.discount.exception;

/**
 * 할인 예산 부족 예외.
 *
 * <p>할인 적용 시 잔여 예산이 부족할 때 발생합니다.
 */
public class InsufficientBudgetException extends DiscountException {

    private static final DiscountErrorCode ERROR_CODE = DiscountErrorCode.INSUFFICIENT_BUDGET;

    public InsufficientBudgetException() {
        super(ERROR_CODE);
    }

    public InsufficientBudgetException(Long discountPolicyId) {
        super(ERROR_CODE, String.format("할인 정책 %d의 예산이 부족합니다", discountPolicyId));
    }
}
