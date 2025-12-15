package com.setof.connectly.module.order.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.setof.connectly.module.order.enums.OrderStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class CreateOrder extends AbstractCreateOrder {
    private Long paymentId;

    @NotNull(message = "productGroupId는 필수입니다")
    private long productGroupId;

    @NotNull(message = "productId는 필수입니다")
    private long productId;

    @NotNull(message = "sellerId는 필수입니다")
    private long sellerId;

    @Max(value = 999, message = "상품 수량은 999를 넘을 수 없습니다.")
    @Min(value = 1, message = "상품 수량은 최소 1 입니다.")
    private int quantity;

    @Min(value = 100, message = "주문 총 금액은 100원 이상입니다.")
    private long orderAmount;

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.ORDER_COMPLETED;
    }

    public CreateOrder setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
        return this;
    }
}
