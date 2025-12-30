package com.connectly.partnerAdmin.module.order.dto;

import java.time.LocalDateTime;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.dto.settlement.SettlementInfo;
import com.connectly.partnerAdmin.module.payment.dto.payment.PaymentDetail;
import com.connectly.partnerAdmin.module.payment.dto.receiver.ReceiverInfo;
import com.connectly.partnerAdmin.module.payment.dto.shipment.PaymentShipmentInfo;
import com.connectly.partnerAdmin.module.payment.entity.embedded.BuyerInfo;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResponse {
    private long orderId;
    private BuyerInfo buyerInfo;
    private PaymentDetail payment;
    private ReceiverInfo receiverInfo;
    private PaymentShipmentInfo paymentShipmentInfo;
    private SettlementInfo settlementInfo;

    private OrderProduct orderProduct;

    @QueryProjection
    public OrderResponse(long orderId, BuyerInfo buyerInfo, PaymentDetail payment, ReceiverInfo receiverInfo, PaymentShipmentInfo paymentShipmentInfo, SettlementInfo settlementInfo) {
        this.orderId = orderId;
        this.buyerInfo = buyerInfo;
        this.payment = payment;
        this.receiverInfo = receiverInfo;
        this.paymentShipmentInfo = paymentShipmentInfo;
        this.settlementInfo = settlementInfo;
    }

    public void setOrderProduct(OrderProduct orderProduct, LocalDateTime paymentDate) {
        this.orderProduct = orderProduct;

        if(payment.getSiteName().isOurMall()){
            Money realPaymentAmount = orderProduct.getOrderAmount().minus(orderProduct.getTotalExpectedRefundMileageAmount());
            this.payment.setPaymentAmount(realPaymentAmount);
            return;
        }

        if(paymentDate.isBefore(LocalDateTime.of(2025, 4, 15, 0, 0))){
            Money realPaymentAmount = orderProduct.getOrderAmount().minus(orderProduct.getTotalExpectedRefundMileageAmount());
            this.payment.setPaymentAmount(realPaymentAmount);
        }
    }

}

