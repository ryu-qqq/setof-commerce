package com.ryuqq.setof.domain.qna.vo;

/**
 * QnaDetailType - Q&A 상세 유형 Enum VO.
 *
 * <p>레거시 DB의 qna_detail_type 컬럼과 1:1 매핑.
 *
 * <p>DOM-VO-002: Enum VO displayName() 제공.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum QnaDetailType {
    SIZE("사이즈"),
    SHIPMENT("배송"),
    RESTOCK("재고"),
    ORDER_PAYMENT("주문/결제"),
    CANCEL("취소"),
    EXCHANGE("교환"),
    AS("AS"),
    REFUND("반품"),
    ETC("기타");

    private final String displayName;

    QnaDetailType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
