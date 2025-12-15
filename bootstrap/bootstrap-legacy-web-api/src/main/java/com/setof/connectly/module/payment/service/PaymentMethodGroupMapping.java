package com.setof.connectly.module.payment.service;

import com.setof.connectly.module.payment.enums.PaymentMethodGroup;
import com.setof.connectly.module.payment.enums.method.PaymentMethodEnum;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodGroupMapping {

    private static final Map<PaymentMethodEnum, PaymentMethodGroup> statusPayMethodMap =
            new HashMap<>();

    static {
        statusPayMethodMap.put(PaymentMethodEnum.CARD, PaymentMethodGroup.CARD);
        statusPayMethodMap.put(PaymentMethodEnum.KAKAO_PAY, PaymentMethodGroup.CARD);
        statusPayMethodMap.put(PaymentMethodEnum.NAVER_PAY, PaymentMethodGroup.CARD);

        statusPayMethodMap.put(PaymentMethodEnum.VBANK, PaymentMethodGroup.ACCOUNT);
        statusPayMethodMap.put(PaymentMethodEnum.VBANK_ESCROW, PaymentMethodGroup.ACCOUNT);

        statusPayMethodMap.put(PaymentMethodEnum.MILEAGE, PaymentMethodGroup.MILEAGE);
    }

    public static PaymentMethodGroup getGroupForMethod(PaymentMethodEnum payMethod) {
        return statusPayMethodMap.get(payMethod);
    }
}
