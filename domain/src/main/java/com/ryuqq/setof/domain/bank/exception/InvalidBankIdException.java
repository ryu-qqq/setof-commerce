package com.ryuqq.setof.domain.bank.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 은행 ID 예외
 *
 * <p>은행 ID가 null이거나 0 이하인 경우 발생합니다.
 */
public class InvalidBankIdException extends DomainException {

    public InvalidBankIdException(Long bankId) {
        super(
                BankErrorCode.INVALID_BANK_ID,
                String.format("유효하지 않은 은행 ID: %s", bankId),
                Map.of("bankId", bankId != null ? bankId : "null"));
    }
}
