package com.setof.connectly.module.order.dto.query;

import com.setof.connectly.module.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractUpdateOrder implements UpdateOrder {

    public long paymentId;
    public Long orderId;

    @NotNull(message = "주문 상태 값은 필수입니다.")
    public OrderStatus orderStatus;

    @Override
    public String getChangeReason() {
        return "";
    }

    @Override
    public String getChangeDetailReason() {
        return "";
    }

    @Override
    public Long getPaymentId() {
        return paymentId;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
