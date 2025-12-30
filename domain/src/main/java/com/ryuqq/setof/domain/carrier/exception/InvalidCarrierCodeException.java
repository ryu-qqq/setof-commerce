package com.ryuqq.setof.domain.carrier.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 유효하지 않은 택배사 코드 예외
 *
 * <p>택배사 코드가 null, 빈 문자열이거나 형식에 맞지 않는 경우 발생합니다.
 */
public class InvalidCarrierCodeException extends DomainException {

    public InvalidCarrierCodeException(String value) {
        super(CarrierErrorCode.INVALID_CARRIER_CODE, "입력값: " + value);
    }
}
