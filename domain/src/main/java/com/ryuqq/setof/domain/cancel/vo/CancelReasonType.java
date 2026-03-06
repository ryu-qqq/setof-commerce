package com.ryuqq.setof.domain.cancel.vo;

/** 취소 사유 유형. */
public enum CancelReasonType {
    CHANGE_OF_MIND("단순 변심"),
    DUPLICATE_ORDER("중복 주문"),
    WRONG_PRODUCT("상품 잘못 선택"),
    WRONG_OPTION("옵션 잘못 선택"),
    DELIVERY_DELAY("배송 지연"),
    PRODUCT_UNAVAILABLE("상품 품절"),
    PRICE_CHANGE("가격 변동"),
    SERVICE_ISSUE("서비스 불만"),
    SELLER_REQUEST("판매자 요청"),
    OTHER("기타");

    private final String description;

    CancelReasonType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
