package com.ryuqq.setof.domain.shipment.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 유효하지 않은 운송장 번호 예외
 *
 * <p>운송장 번호가 null, 빈 문자열, 또는 허용 형식에 맞지 않는 경우 발생합니다.
 */
public class InvalidInvoiceNumberException extends DomainException {

    public InvalidInvoiceNumberException(String invalidValue) {
        super(ShipmentErrorCode.INVALID_INVOICE_NUMBER, "Invalid invoice number: " + invalidValue);
    }
}
