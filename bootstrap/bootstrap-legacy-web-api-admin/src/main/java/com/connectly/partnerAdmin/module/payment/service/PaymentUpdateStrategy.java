package com.connectly.partnerAdmin.module.payment.service;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodEnum;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodGroup;
import com.connectly.partnerAdmin.module.payment.service.refund.RefundService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentUpdateStrategy extends AbstractProvider<PaymentMethodGroup, RefundService> {

    public PaymentUpdateStrategy(List<RefundService> services){
        for (RefundService service : services) {
            map.put(service.getPaymentMethodGroup(), service);
        }
    }

    public RefundService getServiceByPayMethod(PaymentMethodEnum payMethod) {
        PaymentMethodGroup groupForMethod = PaymentMethodGroupMapping.getGroupForMethod(payMethod);
        return get(groupForMethod);
    }

}