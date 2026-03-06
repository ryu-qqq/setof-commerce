package com.ryuqq.setof.domain.exchange.vo;

/** 교환 사유 유형. */
public enum ExchangeReasonType {
    DEFECTIVE_PRODUCT("상품 불량"),
    WRONG_DELIVERY("오배송"),
    SIZE_MISMATCH("사이즈 불일치"),
    COLOR_MISMATCH("색상 불일치"),
    MISSING_PARTS("부품/구성품 누락"),
    OTHER("기타");

    private final String description;

    ExchangeReasonType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
