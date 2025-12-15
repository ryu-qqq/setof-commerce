package com.setof.connectly.module.payment.dto.payment;

import com.setof.connectly.module.order.dto.order.OrderSheet;
import com.setof.connectly.module.payment.enums.method.PaymentMethodEnum;
import com.setof.connectly.module.payment.service.pay.PaymentQueryService;
import com.setof.connectly.module.user.dto.shipping.UserShippingInfo;
import java.util.List;

public interface BasePayment {
    long getPayAmount();

    PaymentMethodEnum getPayMethod();

    UserShippingInfo getShippingInfo();

    List<Long> getProductGroupIds();

    List<Long> getProductIds();

    long getMileageAmount();

    List<OrderSheet> getOrders();

    PaymentGatewayRequestDto processPayment(PaymentQueryService service);

    long getToTalQty();
}
