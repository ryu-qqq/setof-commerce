package com.connectly.partnerAdmin.module.order.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHistoryResponse {
    private long orderId;

    @Builder.Default
    private String changeReason = "";

    @Builder.Default
    private String changeDetailReason = "";

    private OrderStatus orderStatus;

    @Builder.Default
    private String invoiceNo = "";

    @Builder.Default
    private ShipmentCompanyCode shipmentCompanyCode = ShipmentCompanyCode.REFER_DETAIL;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    @QueryProjection
    public OrderHistoryResponse(long orderId, String changeReason, String changeDetailReason, OrderStatus orderStatus,
                                String invoiceNo, ShipmentCompanyCode shipmentCompanyCode, LocalDateTime updateDate) {
        this.orderId = orderId;
        this.changeReason = changeReason;
        this.changeDetailReason = changeDetailReason;
        this.orderStatus = orderStatus;
        this.invoiceNo = invoiceNo;
        this.shipmentCompanyCode = shipmentCompanyCode;
        this.updateDate = updateDate;
    }

}
