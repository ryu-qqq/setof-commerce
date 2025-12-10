package com.ryuqq.setof.domain.bank.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 은행 미존재 예외
 *
 * <p>요청한 은행 ID 또는 코드에 해당하는 은행이 존재하지 않을 때 발생합니다.
 */
public class BankNotFoundException extends DomainException {

    public BankNotFoundException(Long bankId) {
        super(
                BankErrorCode.BANK_NOT_FOUND,
                String.format("은행을 찾을 수 없습니다. bankId: %d", bankId),
                Map.of("bankId", bankId));
    }

    public BankNotFoundException(String bankCode) {
        super(
                BankErrorCode.BANK_NOT_FOUND,
                String.format("은행을 찾을 수 없습니다. bankCode: %s", bankCode),
                Map.of("bankCode", bankCode));
    }
}
