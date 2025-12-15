package com.setof.connectly.module.qna.enums;

import com.setof.connectly.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QnaDetailType implements EnumType {

    SIZE("사이즈"),
    SHIPMENT("배송"),
    RESTOCK("재고"),
    ORDER_PAYMENT("주문/결제"),
    CANCEL("취소"),
    EXCHANGE("교환"),
    AS("AS"),
    REFUND("반품"),
    ETC("기타"),
    ;

    private final String displayName;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return displayName;
    }
}
