package com.connectly.partnerAdmin.module.mileage.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MileageIssueType implements EnumType {

    JOIN("회원 가입"),
    EVENT("이벤트"),
    ORDER("주문 적립"),
    REVIEW("리뷰 작성 적립"),
    REFUND("주문 환불"),
    EXPIRED("유효기간 만료");

    private final String displayName;


    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }

    public boolean isOrder(){
        return this.equals(ORDER);
    }

    public boolean isRefund(){
        return this.equals(REFUND);
    }

    public boolean isExpired(){
        return this.equals(EXPIRED);
    }

}
