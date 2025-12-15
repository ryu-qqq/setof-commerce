package com.setof.connectly.module.shipment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.shipment.enums.DeliveryStatus;
import com.setof.connectly.module.shipment.enums.ShipmentCompanyCode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentShipmentInfo {

    private long orderId;
    private DeliveryStatus deliveryStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String shipmentCompanyCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String invoice;

    @JsonIgnore private LocalDateTime insertDate;
    private Yn isAfter7DaysDelivery;
    private Yn isAfter3MonthsDelivery;

    @QueryProjection
    public PaymentShipmentInfo(
            long orderId,
            DeliveryStatus deliveryStatus,
            ShipmentCompanyCode shipmentCompanyCode,
            String invoice,
            LocalDateTime insertDate) {
        this.orderId = orderId;
        this.deliveryStatus = deliveryStatus;
        this.shipmentCompanyCode = shipmentCompanyCode.getCode();
        this.invoice = invoice;
        this.insertDate = insertDate;
        this.isAfter7DaysDelivery = setIsAfter7DaysDelivery();
        this.isAfter3MonthsDelivery = setIsAfter3MonthsDelivery();
    }

    private Yn setIsAfter7DaysDelivery() {
        if (deliveryStatus.isDeliveryProcessing() && insertDate != null) {
            Duration duration = Duration.between(insertDate, LocalDateTime.now());
            if (duration.toDays() >= 7) {
                return Yn.Y;
            }
        }
        return Yn.N;
    }

    public Yn setIsAfter3MonthsDelivery() {
        if (deliveryStatus.isDeliveryProcessing() && insertDate != null) {
            Period period = Period.between(insertDate.toLocalDate(), LocalDate.now());
            if (period.getMonths() >= 3) {
                return Yn.Y;
            }
        }
        return Yn.N;
    }
}
