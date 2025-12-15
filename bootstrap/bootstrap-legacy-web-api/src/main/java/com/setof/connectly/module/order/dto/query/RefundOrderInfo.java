package com.setof.connectly.module.order.dto.query;

import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class RefundOrderInfo implements RefundOrderSheet {

    private boolean allCanceled;
    private double expectedRefundAmount;
    private double expectedRefundMileage;
    private long orderId;
    private OrderStatus orderStatus;
    private long orderAmount;
    private String reason;
    private String detailReason;

    private RefundAccountInfo refundAccountInfo;

    @Override
    public void setRefundAccountInfo(RefundAccountInfo refundAccountInfo) {
        this.refundAccountInfo = refundAccountInfo;
    }
}
