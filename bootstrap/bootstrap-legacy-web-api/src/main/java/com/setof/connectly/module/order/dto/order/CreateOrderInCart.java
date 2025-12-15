package com.setof.connectly.module.order.dto.order;

import com.setof.connectly.module.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class CreateOrderInCart extends CreateOrder {

    @NotNull(message = "cartId는 필수입니다")
    private long cartId;

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.ORDER_COMPLETED;
    }

    @Override
    public long getSellerId() {
        return super.getSellerId();
    }

    @Override
    public Long getPaymentId() {
        return super.getPaymentId();
    }
}
