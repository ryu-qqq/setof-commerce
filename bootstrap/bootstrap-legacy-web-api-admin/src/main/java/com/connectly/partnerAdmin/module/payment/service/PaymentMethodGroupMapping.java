package com.connectly.partnerAdmin.module.payment.service;

import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodEnum;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodGroup;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentMethodGroupMapping {

    private static final Map<PaymentMethodEnum, PaymentMethodGroup> statusPayMethodMap = new HashMap<>();

    static {
        statusPayMethodMap.put(PaymentMethodEnum.CARD, PaymentMethodGroup.CARD);
        statusPayMethodMap.put(PaymentMethodEnum.KAKAO_PAY, PaymentMethodGroup.CARD);
        statusPayMethodMap.put(PaymentMethodEnum.NAVER_PAY, PaymentMethodGroup.CARD);
        statusPayMethodMap.put(PaymentMethodEnum.MILEAGE, PaymentMethodGroup.CARD);

        statusPayMethodMap.put(PaymentMethodEnum.VBANK, PaymentMethodGroup.ACCOUNT);
        statusPayMethodMap.put(PaymentMethodEnum.VBANK_ESCROW, PaymentMethodGroup.ACCOUNT);


    }

    public static PaymentMethodGroup getGroupForMethod(PaymentMethodEnum payMethod){
        return statusPayMethodMap.get(payMethod);
    }
}
