package com.ryuqq.setof.domain.seller.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 사업자등록번호 예외
 *
 * <p>사업자등록번호 형식이 올바르지 않은 경우 발생합니다.
 */
public class InvalidRegistrationNumberException extends DomainException {

    public InvalidRegistrationNumberException(String value, String reason) {
        super(
                SellerErrorCode.INVALID_REGISTRATION_NUMBER,
                String.format("사업자등록번호가 올바르지 않습니다. value: %s, reason: %s", value, reason),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
