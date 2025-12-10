package com.ryuqq.setof.domain.refundaccount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 계좌 검증 실패 예외
 *
 * <p>외부 계좌 검증 API 호출 결과 검증에 실패한 경우 발생합니다.
 */
public class AccountVerificationFailedException extends DomainException {

    public AccountVerificationFailedException(String bankCode, String accountNumber, String reason) {
        super(
                RefundAccountErrorCode.ACCOUNT_VERIFICATION_FAILED,
                String.format("계좌 검증에 실패했습니다. bankCode: %s, reason: %s", bankCode, reason),
                Map.of("bankCode", bankCode,
                        "accountNumber", maskAccountNumber(accountNumber),
                        "reason", reason));
    }

    public AccountVerificationFailedException(String bankCode, String accountNumber) {
        super(
                RefundAccountErrorCode.ACCOUNT_VERIFICATION_FAILED,
                String.format("계좌 검증에 실패했습니다. bankCode: %s", bankCode),
                Map.of("bankCode", bankCode,
                        "accountNumber", maskAccountNumber(accountNumber)));
    }

    private static String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() <= 8) {
            return "****";
        }
        return accountNumber.substring(0, 4) + "****" + accountNumber.substring(accountNumber.length() - 4);
    }
}
