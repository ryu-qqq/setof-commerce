package com.ryuqq.setof.domain.qna.vo;

public enum QnaType {

    PRODUCT("상품 문의"),
    ORDER("주문 문의");

    private final String description;

    QnaType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isProductQna() {
        return this == PRODUCT;
    }

    public boolean isOrderQna() {
        return this == ORDER;
    }
}
