package com.setof.connectly.module.payment.dto.refund;

import com.setof.connectly.module.order.enums.OrderStatus;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class RefundPaymentDto {
    private long paymentId;
    private long orderId;
    private double refundAmount;
    private double refundMileageAmount;
    private OrderStatus orderStatus;
}
