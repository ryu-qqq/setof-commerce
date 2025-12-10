package com.ryuqq.setof.domain.refundaccount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 예금주명 예외
 *
 * <p>예금주명이 null, 빈 문자열, 또는 20자 초과인 경우 발생합니다.
 */
public class InvalidAccountHolderNameException extends DomainException {

    public InvalidAccountHolderNameException(String accountHolderName) {
        super(
                RefundAccountErrorCode.INVALID_ACCOUNT_HOLDER_NAME,
                String.format("유효하지 않은 예금주명: %s (1~20자 필요)", accountHolderName),
                Map.of(
                        "accountHolderName",
                        accountHolderName != null ? accountHolderName : "null"));
    }
}
