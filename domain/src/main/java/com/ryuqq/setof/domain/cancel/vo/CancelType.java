package com.ryuqq.setof.domain.cancel.vo;

/**
 * 취소 유형.
 *
 * <p>구매자 취소와 판매자 취소를 구분합니다. 판매자 취소의 경우 자동 승인되어 APPROVED 상태로 시작합니다.
 */
public enum CancelType {
    BUYER_CANCEL("구매자 취소"),
    SELLER_CANCEL("판매자 취소");

    private final String description;

    CancelType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    /**
     * 구매자 취소인지 확인합니다.
     *
     * @return 구매자 취소 여부
     */
    public boolean isBuyerCancel() {
        return this == BUYER_CANCEL;
    }

    /**
     * 판매자 취소인지 확인합니다.
     *
     * <p>판매자 취소는 자동 승인되어 APPROVED 상태로 시작합니다.
     *
     * @return 판매자 취소 여부
     */
    public boolean isSellerCancel() {
        return this == SELLER_CANCEL;
    }
}
