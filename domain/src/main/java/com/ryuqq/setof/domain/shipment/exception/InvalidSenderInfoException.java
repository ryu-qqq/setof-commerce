package com.ryuqq.setof.domain.shipment.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 유효하지 않은 발송인 정보 예외
 *
 * <p>발송인 정보(이름, 연락처 등)가 null이거나 유효하지 않은 경우 발생합니다.
 */
public class InvalidSenderInfoException extends DomainException {

    public InvalidSenderInfoException(String detail) {
        super(ShipmentErrorCode.INVALID_SENDER_INFO, "Invalid sender info: " + detail);
    }
}
