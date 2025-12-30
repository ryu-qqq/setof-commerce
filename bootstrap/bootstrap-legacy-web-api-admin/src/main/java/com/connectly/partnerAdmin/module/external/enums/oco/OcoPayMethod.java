package com.connectly.partnerAdmin.module.external.enums.oco;

import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OcoPayMethod {


    OCO0(0, PaymentMethodEnum.VBANK),
    OCO1(1, PaymentMethodEnum.CARD),
    OCO2(2, PaymentMethodEnum.CARD),
    OCO3(3, PaymentMethodEnum.CARD),
    OCO4(4, PaymentMethodEnum.CARD),
    OCO5(5, PaymentMethodEnum.CARD),
    OCO6(6, PaymentMethodEnum.KAKAO_PAY),
    OCO7(7, PaymentMethodEnum.CARD),
    OCO8(8, PaymentMethodEnum.NAVER_PAY),
    OCO9(9, PaymentMethodEnum.CARD),
    OCO10(10, PaymentMethodEnum.CARD),
    OCO11(11, PaymentMethodEnum.CARD),
    OCO12(12, PaymentMethodEnum.CARD),
    OCO13(13, PaymentMethodEnum.CARD),
    OCO14(14, PaymentMethodEnum.CARD),
    OCO15(15, PaymentMethodEnum.CARD),
    OCO16(16, PaymentMethodEnum.CARD),
    OCO17(17, PaymentMethodEnum.CARD),
    OCO18(18, PaymentMethodEnum.CARD),
    OCO19(19, PaymentMethodEnum.CARD),
    OCO20(20, PaymentMethodEnum.CARD),
    OCO21(21, PaymentMethodEnum.CARD),
    OCO22(22, PaymentMethodEnum.CARD),
    OCO23(23, PaymentMethodEnum.CARD),
    OCO24(24, PaymentMethodEnum.CARD),
    OCO25(25, PaymentMethodEnum.CARD);

    private final int code;
    private final PaymentMethodEnum paymentMethodEnum;

    public static OcoPayMethod of(int code){
        return Arrays.stream(OcoPayMethod.values())
                .filter(c -> c.code == code)
                .findAny()
                .orElseGet(()-> OCO0);
    }


}
