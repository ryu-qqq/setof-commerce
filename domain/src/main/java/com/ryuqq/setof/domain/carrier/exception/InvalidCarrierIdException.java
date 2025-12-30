package com.ryuqq.setof.domain.carrier.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 유효하지 않은 택배사 ID 예외
 *
 * <p>택배사 ID가 null이거나 0 이하인 경우 발생합니다.
 */
public class InvalidCarrierIdException extends DomainException {

    public InvalidCarrierIdException(Long value) {
        super(CarrierErrorCode.INVALID_CARRIER_ID, "입력값: " + value);
    }
}
