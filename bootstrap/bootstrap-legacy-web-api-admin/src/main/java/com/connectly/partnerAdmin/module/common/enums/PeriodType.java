package com.connectly.partnerAdmin.module.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum PeriodType implements EnumType {

    DISPLAY( "전시 기간"),
    INSERT("등록 기간"),
    PAYMENT("결제(주문) 기간"),
    POLICY("정책 적용 기간"),
    SETTLEMENT("정산 기간"),
    ORDER_HISTORY("주문 상태 변경 기간");

    private final String displayName;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return displayName;
    }


    public boolean isInsert(){
        return this.equals(INSERT);
    }
    public boolean isPolicy(){
        return this.equals(POLICY);
    }
    public boolean isDisplay(){
        return this.equals(DISPLAY);
    }
    public boolean isPayment(){
        return this.equals(PAYMENT);
    }
    public boolean isSettlement(){
        return this.equals(SETTLEMENT);
    }
    public boolean isHistory(){
        return this.equals(ORDER_HISTORY);
    }

}
