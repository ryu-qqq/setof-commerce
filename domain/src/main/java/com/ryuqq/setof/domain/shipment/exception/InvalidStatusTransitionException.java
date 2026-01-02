package com.ryuqq.setof.domain.shipment.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 유효하지 않은 배송 상태 전이 예외
 *
 * <p>배송 상태 변경이 허용되지 않는 경우 발생합니다. 예: DELIVERED 상태에서 PENDING으로 변경 시도
 */
public class InvalidStatusTransitionException extends DomainException {

    public InvalidStatusTransitionException(String from, String to) {
        super(
                ShipmentErrorCode.INVALID_STATUS_TRANSITION,
                String.format("Cannot transition from %s to %s", from, to));
    }
}
