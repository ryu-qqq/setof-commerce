package com.ryuqq.setof.domain.shipment.exception;

import com.ryuqq.setof.domain.shipment.vo.DeliveryStatus;

/**
 * 유효하지 않은 배송 상태 전이 예외.
 *
 * <p>허용되지 않는 배송 상태 전이를 시도할 때 발생합니다.
 */
public class InvalidDeliveryStatusTransitionException extends ShipmentException {

    private static final ShipmentErrorCode ERROR_CODE =
            ShipmentErrorCode.INVALID_DELIVERY_STATUS_TRANSITION;

    public InvalidDeliveryStatusTransitionException() {
        super(ERROR_CODE);
    }

    public InvalidDeliveryStatusTransitionException(DeliveryStatus from, DeliveryStatus to) {
        super(ERROR_CODE, String.format("%s에서 %s로의 배송 상태 전이는 허용되지 않습니다", from, to));
    }
}
