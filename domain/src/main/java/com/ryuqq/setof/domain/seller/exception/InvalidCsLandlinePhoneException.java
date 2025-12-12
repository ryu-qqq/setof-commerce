package com.ryuqq.setof.domain.seller.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 CS 유선전화 번호 예외
 *
 * <p>CS 유선전화 번호 형식이 올바르지 않은 경우 발생합니다.
 */
public class InvalidCsLandlinePhoneException extends DomainException {

    public InvalidCsLandlinePhoneException(String value, String reason) {
        super(
                SellerErrorCode.INVALID_CS_LANDLINE_PHONE,
                String.format("CS 유선전화 번호가 올바르지 않습니다. value: %s, reason: %s", value, reason),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
