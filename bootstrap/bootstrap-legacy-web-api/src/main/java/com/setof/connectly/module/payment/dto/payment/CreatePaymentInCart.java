package com.setof.connectly.module.payment.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.setof.connectly.module.order.dto.order.CreateOrder;
import com.setof.connectly.module.order.dto.order.CreateOrderInCart;
import com.setof.connectly.module.order.dto.order.OrderSheet;
import com.setof.connectly.module.payment.annotation.ValidPayment;
import com.setof.connectly.module.payment.annotation.ValidPrice;
import com.setof.connectly.module.payment.annotation.ValidUserMileage;
import com.setof.connectly.module.payment.service.pay.PaymentQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ValidUserMileage
@ValidPrice
@ValidPayment
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreatePaymentInCart extends AbstractPayment {

    @Valid
    @Size(min = 1, message = "적어도 하나 이상의 주문이 필요합니다.")
    private List<CreateOrderInCart> orders;

    @Override
    public List<Long> getProductGroupIds() {
        return orders.stream().map(CreateOrder::getProductGroupId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getProductIds() {
        return orders.stream().map(CreateOrder::getProductId).collect(Collectors.toList());
    }

    public List<Long> getCartIds() {
        return orders.stream().map(CreateOrderInCart::getCartId).collect(Collectors.toList());
    }

    public void setPaymentId(long paymentId) {
        orders.forEach(co -> co.setPaymentId(paymentId));
    }

    @Override
    public List<OrderSheet> getOrders() {
        return new ArrayList<>(orders);
    }

    @Override
    public long getToTalQty() {
        return orders.stream().mapToLong(CreateOrder::getQuantity).sum();
    }

    @JsonIgnore
    @Override
    public PaymentGatewayRequestDto processPayment(PaymentQueryService service) {
        return service.doPayInCart(this);
    }
}
