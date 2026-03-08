package com.ryuqq.setof.domain.qna.vo;

/**
 * QnaDetailType - Q&A 상세 유형 Enum VO.
 *
 * <p>DOM-VO-002: Enum VO displayName() 제공.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum QnaDetailType {

    SIZE("사이즈"),
    DELIVERY("배송"),
    RESTOCK("재입고"),
    EXCHANGE_RETURN("교환/반품"),
    GENERAL("기타");

    private final String displayName;

    QnaDetailType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
