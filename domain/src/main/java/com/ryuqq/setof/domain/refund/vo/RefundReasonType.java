package com.ryuqq.setof.domain.refund.vo;

/**
 * 반품 사유 유형.
 *
 * <p>반품 요청 시 선택 가능한 사유 유형을 정의합니다.
 */
public enum RefundReasonType {
    DEFECTIVE_PRODUCT("상품 불량"),
    WRONG_DELIVERY("오배송"),
    CHANGE_OF_MIND("단순 변심"),
    PRODUCT_MISMATCH("상품 상이"),
    MISSING_PARTS("부품/구성품 누락"),
    OTHER("기타");

    private final String description;

    RefundReasonType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
