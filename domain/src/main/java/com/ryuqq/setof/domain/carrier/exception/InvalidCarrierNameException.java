package com.ryuqq.setof.domain.carrier.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 유효하지 않은 택배사명 예외
 *
 * <p>택배사명이 null이거나 빈 문자열인 경우 발생합니다.
 */
public class InvalidCarrierNameException extends DomainException {

    public InvalidCarrierNameException(String value) {
        super(CarrierErrorCode.INVALID_CARRIER_NAME, "입력값: " + value);
    }
}
