package com.ryuqq.setof.domain.shipment.exception;

/**
 * 배송 정보를 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 배송 정보가 존재하지 않을 때 발생합니다.
 */
public class ShipmentNotFoundException extends ShipmentException {

    private static final ShipmentErrorCode ERROR_CODE = ShipmentErrorCode.SHIPMENT_NOT_FOUND;

    public ShipmentNotFoundException() {
        super(ERROR_CODE);
    }

    public ShipmentNotFoundException(Long id) {
        super(ERROR_CODE, String.format("ID가 %d인 배송 정보를 찾을 수 없습니다", id));
    }
}
