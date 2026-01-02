package com.connectly.partnerAdmin.module.payment.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethodEnum implements EnumType {

    CARD("신용/체크카드",1L),
    KAKAO_PAY("카카오 페이", 2L),
    NAVER_PAY("네이버 페이",3L),
    VBANK("가상 계좌",4L),
    VBANK_ESCROW("가상 계좌 (에스크로)",5L),

    MILEAGE("마일리지", 6L);

    private final String displayName;
    private final long paymentMethodId;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return this.name();
    }

    public boolean isVBank(){
        return this.equals(VBANK) || this.equals(VBANK_ESCROW);
    }

    public boolean isMileage(){
        return this.equals(MILEAGE) ;
    }

}
