package com.ryuqq.setof.domain.bank.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 은행 코드 예외
 *
 * <p>은행 코드가 3자리 숫자 형식이 아닌 경우 발생합니다.
 */
public class InvalidBankCodeException extends DomainException {

    public InvalidBankCodeException(String bankCode) {
        super(
                BankErrorCode.INVALID_BANK_CODE,
                String.format("유효하지 않은 은행 코드: %s (3자리 숫자 필요)", bankCode),
                Map.of("bankCode", bankCode != null ? bankCode : "null"));
    }
}
