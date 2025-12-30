package com.connectly.partnerAdmin.module.payment.dto;


import com.connectly.partnerAdmin.module.order.dto.OrderProduct;
import com.connectly.partnerAdmin.module.payment.dto.payment.PaymentDetail;
import com.connectly.partnerAdmin.module.payment.dto.receiver.ReceiverInfo;
import com.connectly.partnerAdmin.module.payment.dto.shipment.PaymentShipmentInfo;
import com.connectly.partnerAdmin.module.payment.entity.embedded.BuyerInfo;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentResponse {

    private BuyerInfo buyerInfo;
    private PaymentDetail payment;
    private ReceiverInfo receiverInfo;
    private PaymentShipmentInfo shipmentInfo;
    @Setter
    private List<OrderProduct> orderProducts = new ArrayList<>();


    @QueryProjection
    public PaymentResponse(BuyerInfo buyerInfo, PaymentDetail payment, ReceiverInfo receiverInfo, PaymentShipmentInfo shipmentInfo) {
        this.buyerInfo = buyerInfo;
        this.payment = payment;
        this.receiverInfo = receiverInfo;
        this.shipmentInfo = shipmentInfo;
    }


}
