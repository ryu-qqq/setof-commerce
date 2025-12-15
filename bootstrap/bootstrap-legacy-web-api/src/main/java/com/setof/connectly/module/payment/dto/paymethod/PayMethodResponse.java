package com.setof.connectly.module.payment.dto.paymethod;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.payment.enums.method.PaymentMethodEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayMethodResponse {

    private String displayName;

    @NotNull(message = "payMethod는 필수입니다")
    private PaymentMethodEnum payMethod;

    @NotNull(message = "paymentMethodMerchantKey는 필수입니다")
    private String paymentMethodMerchantKey;

    @QueryProjection
    public PayMethodResponse(PaymentMethodEnum payMethod, String paymentMethodMerchantKey) {
        this.displayName = payMethod.getDisplayName();
        this.payMethod = payMethod;
        this.paymentMethodMerchantKey = paymentMethodMerchantKey;
    }
}
