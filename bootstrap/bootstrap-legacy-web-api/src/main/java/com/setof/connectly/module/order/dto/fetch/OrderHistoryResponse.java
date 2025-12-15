package com.setof.connectly.module.order.dto.fetch;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.shipment.enums.ShipmentCompanyCode;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHistoryResponse {
    private long orderId;
    private String changeReason;
    private String changeDetailReason;
    private OrderStatus orderStatus;
    private String invoiceNo;
    private String shipmentCompanyCode;
    private LocalDateTime updateDate;

    @QueryProjection
    public OrderHistoryResponse(long orderId, OrderStatus orderStatus, LocalDateTime updateDate) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.updateDate = updateDate;
    }

    @QueryProjection
    public OrderHistoryResponse(
            long orderId,
            String changeReason,
            String changeDetailReason,
            OrderStatus orderStatus,
            String invoiceNo,
            ShipmentCompanyCode shipmentCompanyCode,
            LocalDateTime updateDate) {
        this.orderId = orderId;
        this.changeReason = changeReason;
        this.changeDetailReason = changeDetailReason;
        this.orderStatus = orderStatus;
        this.invoiceNo = invoiceNo;
        this.shipmentCompanyCode = shipmentCompanyCode == null ? "" : shipmentCompanyCode.getName();
        this.updateDate = updateDate;
    }
}
