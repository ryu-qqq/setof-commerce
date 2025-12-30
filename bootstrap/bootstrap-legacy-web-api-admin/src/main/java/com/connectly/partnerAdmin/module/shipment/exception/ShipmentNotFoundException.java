package com.connectly.partnerAdmin.module.shipment.exception;

public class ShipmentNotFoundException extends ShipmentException{

    public static final String CODE = "SHIPMENT-404";
    public static final String MESSAGE = "해당 주문에 대한 배송정보가  존재하지 않습니다 ::: ";
    public static final org.springframework.http.HttpStatus HttpStatus = org.springframework.http.HttpStatus.NOT_FOUND;

    public ShipmentNotFoundException(long orderId) {
        super(CODE, HttpStatus, MESSAGE + orderId);
    }
}
