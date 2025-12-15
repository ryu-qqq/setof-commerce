package com.setof.connectly.module.payment.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.setof.connectly.module.order.dto.order.CreateOrder;
import com.setof.connectly.module.order.dto.order.OrderSheet;
import com.setof.connectly.module.payment.annotation.ValidPayment;
import com.setof.connectly.module.payment.annotation.ValidPrice;
import com.setof.connectly.module.payment.annotation.ValidUserMileage;
import com.setof.connectly.module.payment.service.pay.PaymentQueryService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@ValidUserMileage
@ValidPrice
@ValidPayment
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class CreatePayment extends AbstractPayment {

    @Valid private List<CreateOrder> orders;

    @Override
    public List<Long> getProductGroupIds() {
        return orders.stream().map(CreateOrder::getProductGroupId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getProductIds() {
        return orders.stream().map(CreateOrder::getProductId).collect(Collectors.toList());
    }

    @Override
    public List<OrderSheet> getOrders() {
        return new ArrayList<>(orders);
    }

    public void setPaymentId(long paymentId) {
        orders.forEach(co -> co.setPaymentId(paymentId));
    }

    @JsonIgnore
    @Override
    public PaymentGatewayRequestDto processPayment(PaymentQueryService service) {
        return service.doPay(this);
    }

    @Override
    public long getToTalQty() {
        return orders.stream().mapToLong(CreateOrder::getQuantity).sum();
    }
}
