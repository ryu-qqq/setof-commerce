package com.connectly.partnerAdmin.module.order.dto.query;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.user.dto.RefundAccountInfo;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class RefundOrder {
    private long paymentId;
    private long orderId;
    private String paymentAgencyId;
    private Money refundAmount;

    @Setter
    private RefundAccountInfo refundAccountInfo;
}
