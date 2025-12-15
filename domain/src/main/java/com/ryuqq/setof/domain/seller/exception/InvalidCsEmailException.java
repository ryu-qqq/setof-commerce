package com.ryuqq.setof.domain.seller.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 CS 이메일 예외
 *
 * <p>CS 이메일 형식이 올바르지 않은 경우 발생합니다.
 */
public class InvalidCsEmailException extends DomainException {

    public InvalidCsEmailException(String value, String reason) {
        super(
                SellerErrorCode.INVALID_CS_EMAIL,
                String.format("CS 이메일이 올바르지 않습니다. value: %s, reason: %s", value, reason),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
