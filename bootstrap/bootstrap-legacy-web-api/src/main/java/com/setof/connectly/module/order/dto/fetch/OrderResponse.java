package com.setof.connectly.module.order.dto.fetch;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.order.dto.OrderProductDto;
import com.setof.connectly.module.payment.dto.payment.PaymentDetail;
import com.setof.connectly.module.payment.dto.receiver.ReceiverInfo;
import com.setof.connectly.module.payment.entity.embedded.BuyerInfo;
import com.setof.connectly.module.utils.NumberUtils;
import com.setof.connectly.module.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResponse {

    private PaymentDetail payment;
    private OrderProductDto orderProduct;
    private BuyerInfo buyerInfo;
    private ReceiverInfo receiverInfo;
    private double totalExpectedMileageAmount;

    @QueryProjection
    public OrderResponse(
            PaymentDetail payment,
            OrderProductDto orderProduct,
            BuyerInfo buyerInfo,
            ReceiverInfo receiverInfo) {
        this.payment = payment;
        this.orderProduct = orderProduct;
        this.buyerInfo = buyerInfo;
        this.receiverInfo = receiverInfo;
        this.totalExpectedMileageAmount = setTotalExpectedMileageAmount();
    }

    private double setTotalExpectedMileageAmount() {
        return NumberUtils.downDotNumber(
                SecurityUtils.getUserGrade().getMileageReserveRate(),
                orderProduct.getOrderAmount());
    }
}
