package com.ryuqq.setof.domain.faq.vo;

/**
 * FaqType - FAQ 유형 Enum Value Object.
 *
 * <p>DOM-VO-001: Record/Enum 기반 VO.
 *
 * <p>DOM-VO-002: Enum VO displayName() 제공.
 *
 * <p>레거시 FaqType과 동일한 값을 가지되, 신규 도메인 패키지에 독립적으로 정의합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum FaqType {

    /** 회원/로그인 */
    MEMBER_LOGIN("회원/로그인"),

    /** 상품/판매자 */
    PRODUCT_SELLER("상품/판매자"),

    /** 배송 */
    SHIPPING("배송"),

    /** 주문/결제 */
    ORDER_PAYMENT("주문/결제"),

    /** 취소/환불 */
    CANCEL_REFUND("취소/환불"),

    /** 교환/반품 */
    EXCHANGE_RETURN("교환/반품"),

    /** 상단 고정 FAQ */
    TOP("상단 고정");

    private final String displayName;

    FaqType(String displayName) {
        this.displayName = displayName;
    }

    /** 사용자 표시용 이름. */
    public String displayName() {
        return displayName;
    }

    /** TOP 유형 여부. */
    public boolean isTop() {
        return this == TOP;
    }
}
