package com.ryuqq.setof.domain.refundaccount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 계좌번호 예외
 *
 * <p>계좌번호가 null, 빈 문자열, 형식이 올바르지 않거나 길이가 맞지 않는 경우 발생합니다.
 */
public class InvalidAccountNumberException extends DomainException {

    public InvalidAccountNumberException(String accountNumber) {
        super(
                RefundAccountErrorCode.INVALID_ACCOUNT_NUMBER,
                String.format("유효하지 않은 계좌번호: %s (8~30자리 숫자 필요)", accountNumber),
                Map.of("accountNumber", accountNumber != null ? accountNumber : "null"));
    }
}
