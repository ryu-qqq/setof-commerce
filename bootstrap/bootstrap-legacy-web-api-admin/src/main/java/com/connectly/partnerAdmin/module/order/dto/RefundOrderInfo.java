package com.connectly.partnerAdmin.module.order.dto;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class RefundOrderInfo {

    private boolean isAllCanceled;
    private double expectedRefundAmount;
    private double expectedRefundMileage;
    private long orderId;
    private long paymentId;
    private OrderStatus orderStatus;
    private long orderAmount;

    private long userId;

    public boolean isCanceledOrder(){
        return this.orderStatus.isCancelRequestConfirmed() || this.orderStatus.isReturnRequestConfirmed()
                || this.orderStatus.isSaleCancelled();
    }

}
