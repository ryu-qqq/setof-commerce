package com.connectly.partnerAdmin.module.payment.dto.shipment;

import com.connectly.partnerAdmin.module.shipment.enums.DeliveryStatus;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentShipmentInfo {
    private DeliveryStatus deliveryStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String shipmentCompanyCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String invoice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shipmentCompletedDate;

    @QueryProjection
    public PaymentShipmentInfo(DeliveryStatus deliveryStatus, ShipmentCompanyCode shipmentCompanyCode, String invoice, LocalDateTime shipmentCompletedDate) {
        this.deliveryStatus = deliveryStatus;
        this.shipmentCompanyCode = shipmentCompanyCode.getCode();
        this.invoice = invoice;
        this.shipmentCompletedDate = shipmentCompletedDate;
    }

}
