package com.setof.connectly.module.order.dto.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("refundOrder")
@SuperBuilder
@Getter
public class RefundOrder extends ClaimOrder {

    private String paymentAgencyId;
    private RefundAccountInfo refundAccountInfo;

    public void setRefundAccountInfo(RefundAccountInfo refundAccountInfo) {
        this.refundAccountInfo = refundAccountInfo;
    }
}
