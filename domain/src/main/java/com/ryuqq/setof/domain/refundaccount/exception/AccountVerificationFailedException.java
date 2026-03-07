package com.ryuqq.setof.domain.refundaccount.exception;

/**
 * 계좌 실명 검증 실패 예외.
 *
 * <p>환불 계좌 등록 시 계좌 실명 검증에 실패하면 발생합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class AccountVerificationFailedException extends RefundAccountException {

    private static final RefundAccountErrorCode ERROR_CODE =
            RefundAccountErrorCode.ACCOUNT_VERIFICATION_FAILED;

    public AccountVerificationFailedException() {
        super(ERROR_CODE);
    }

    public AccountVerificationFailedException(String bankName, String accountNumber) {
        super(ERROR_CODE, String.format("계좌 실명 검증 실패: %s %s", bankName, accountNumber));
    }
}
