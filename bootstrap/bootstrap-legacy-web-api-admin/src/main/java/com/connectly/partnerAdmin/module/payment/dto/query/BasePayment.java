package com.connectly.partnerAdmin.module.payment.dto.query;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodEnum;
import com.connectly.partnerAdmin.module.user.dto.UserShippingInfo;

import java.util.List;


public interface BasePayment {

    long getUserId();
    Money getPayAmount();
    PaymentMethodEnum getPayMethod();
    UserShippingInfo getShippingInfo();
    Money getMileageAmount();
    List<OrderSheet> getOrders();
    SiteName getSiteName();
    String getPaymentUniqueId();

}
