package com.ryuqq.setof.domain.claim.vo;

/**
 * 수거 방법 유형.
 *
 * <p>클레임 발생 시 상품 수거 방법을 나타냅니다.
 */
public enum ShipmentMethodType {
    SELLER_ARRANGED("판매자 수거"),
    BUYER_RETURN("구매자 직접 반송"),
    DIRECT_PICKUP("직접 수거");

    private final String displayName;

    ShipmentMethodType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
