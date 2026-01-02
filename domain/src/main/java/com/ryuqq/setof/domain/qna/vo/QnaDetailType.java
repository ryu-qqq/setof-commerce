package com.ryuqq.setof.domain.qna.vo;

public enum QnaDetailType {

    SIZE("사이즈"),
    DELIVERY("배송"),
    RESTOCK("재입고"),
    PRODUCT_DETAIL("상품 상세"),
    OTHER("기타"),
    ORDER_CHANGE("주문 변경"),
    ORDER_CANCEL("주문 취소"),
    RETURN("반품"),
    EXCHANGE("교환"),
    REFUND("환불");

    private final String description;

    QnaDetailType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
