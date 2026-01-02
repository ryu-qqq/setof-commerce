package com.connectly.partnerAdmin.module.payment.dto.query;


import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodEnum;
import com.connectly.partnerAdmin.module.user.dto.RefundAccountInfo;
import com.connectly.partnerAdmin.module.user.dto.UserShippingInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public abstract class AbstractPayment implements BasePayment {

    protected Money payAmount;

    protected Money mileageAmount;

    @NotNull(message = "payMethod는 필수입니다")
    protected PaymentMethodEnum payMethod;

    protected UserShippingInfo shippingInfo;

    protected RefundAccountInfo refundAccount;

}
