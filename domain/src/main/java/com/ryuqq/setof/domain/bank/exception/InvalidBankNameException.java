package com.ryuqq.setof.domain.bank.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 은행 이름 예외
 *
 * <p>은행 이름이 null, 빈 문자열, 또는 30자 초과인 경우 발생합니다.
 */
public class InvalidBankNameException extends DomainException {

    public InvalidBankNameException(String bankName) {
        super(
                BankErrorCode.INVALID_BANK_NAME,
                String.format("유효하지 않은 은행 이름: %s", bankName),
                Map.of("bankName", bankName != null ? bankName : "null"));
    }
}
