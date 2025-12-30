package com.connectly.partnerAdmin.module.order.dto.query;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractUpdateOrder implements UpdateOrder {

    private long orderId;

    @NotNull(message = "주문 상태 값은 필수입니다.")
    private OrderStatus orderStatus;

    private Boolean byPass;

    @Override
    public String getChangeReason() {
        return "";
    }

    @Override
    public String getChangeDetailReason() {
        return "";
    }

    public boolean isByPass() {
        if(byPass == null) {
            return false;
        }
        return byPass;
    }
}
