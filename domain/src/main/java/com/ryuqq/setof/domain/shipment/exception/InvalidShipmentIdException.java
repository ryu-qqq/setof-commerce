package com.ryuqq.setof.domain.shipment.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 유효하지 않은 운송장 ID 예외
 *
 * <p>운송장 ID가 null이거나 0 이하인 경우 발생합니다.
 */
public class InvalidShipmentIdException extends DomainException {

    public InvalidShipmentIdException(Long invalidId) {
        super(ShipmentErrorCode.INVALID_SHIPMENT_ID, "Invalid shipment ID: " + invalidId);
    }
}
