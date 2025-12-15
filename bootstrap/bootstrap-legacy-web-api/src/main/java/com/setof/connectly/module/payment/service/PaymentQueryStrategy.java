package com.setof.connectly.module.payment.service;

import com.setof.connectly.module.common.provider.AbstractProvider;
import com.setof.connectly.module.payment.enums.PaymentMethodGroup;
import com.setof.connectly.module.payment.enums.method.PaymentMethodEnum;
import com.setof.connectly.module.payment.service.pay.PaymentQueryService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PaymentQueryStrategy
        extends AbstractProvider<PaymentMethodGroup, PaymentQueryService> {

    public PaymentQueryStrategy(List<PaymentQueryService> services) {
        for (PaymentQueryService service : services) {
            map.put(service.getPaymentMethodGroup(), service);
        }
    }

    public PaymentQueryService getServiceByPayMethod(PaymentMethodEnum payMethod) {
        PaymentMethodGroup groupForMethod = PaymentMethodGroupMapping.getGroupForMethod(payMethod);
        return get(groupForMethod);
    }
}
