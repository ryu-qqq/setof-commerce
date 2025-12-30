package com.ryuqq.setof.domain.shipment.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 운송장을 찾을 수 없음 예외
 *
 * <p>주어진 ID 또는 조건에 해당하는 운송장이 존재하지 않는 경우 발생합니다.
 */
public class ShipmentNotFoundException extends DomainException {

    public ShipmentNotFoundException(Long shipmentId) {
        super(ShipmentErrorCode.SHIPMENT_NOT_FOUND, "Shipment not found: " + shipmentId);
    }

    public ShipmentNotFoundException(String invoiceNumber) {
        super(
                ShipmentErrorCode.SHIPMENT_NOT_FOUND,
                "Shipment not found with invoice: " + invoiceNumber);
    }
}
