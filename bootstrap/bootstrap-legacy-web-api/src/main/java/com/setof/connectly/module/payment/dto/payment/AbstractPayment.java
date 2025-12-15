package com.setof.connectly.module.payment.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.setof.connectly.module.payment.enums.method.PaymentMethodEnum;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import com.setof.connectly.module.user.dto.shipping.UserShippingInfo;
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
public abstract class AbstractPayment implements BasePayment {

    protected long payAmount;
    protected long mileageAmount;

    @NotNull(message = "payMethod는 필수입니다")
    private PaymentMethodEnum payMethod;

    protected UserShippingInfo shippingInfo;

    protected RefundAccountInfo refundAccount;
}
