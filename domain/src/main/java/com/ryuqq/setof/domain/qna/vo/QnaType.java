package com.ryuqq.setof.domain.qna.vo;

/**
 * QnaType - Q&A 유형 Enum VO.
 *
 * <p>DOM-VO-002: Enum VO displayName() 제공.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum QnaType {

    PRODUCT("상품 문의"),
    ORDER("주문 문의");

    private final String displayName;

    QnaType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
