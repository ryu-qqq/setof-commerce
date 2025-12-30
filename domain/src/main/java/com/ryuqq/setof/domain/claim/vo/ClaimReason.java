package com.ryuqq.setof.domain.claim.vo;

/**
 * ClaimReason - 클레임 사유
 *
 * <p>클레임 요청 시 선택할 수 있는 사유를 정의합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public enum ClaimReason {

    // ========== 고객 귀책 ==========

    /** 단순 변심 */
    CHANGE_OF_MIND("단순 변심", true),

    /** 주문 실수 (수량, 옵션 등) */
    ORDER_MISTAKE("주문 실수", true),

    /** 더 저렴한 상품 발견 */
    FOUND_CHEAPER("더 저렴한 상품 발견", true),

    /** 배송 지연 (고객 사유) */
    DELIVERY_DELAY_CUSTOMER("배송 지연으로 인한 취소", true),

    // ========== 판매자 귀책 ==========

    /** 상품 불량/파손 */
    DEFECTIVE("상품 불량/파손", false),

    /** 오배송 (다른 상품 배송) */
    WRONG_ITEM("오배송", false),

    /** 상품 설명과 다름 */
    NOT_AS_DESCRIBED("상품 설명과 다름", false),

    /** 사이즈/색상 불일치 */
    SIZE_COLOR_MISMATCH("사이즈/색상 불일치", false),

    /** 배송 중 파손 */
    DAMAGED_IN_SHIPPING("배송 중 파손", false),

    /** 누락 상품 */
    MISSING_ITEM("누락 상품", false),

    // ========== 기타 ==========

    /** 기타 */
    OTHER("기타", true);

    private final String description;
    private final boolean customerFault;

    ClaimReason(String description, boolean customerFault) {
        this.description = description;
        this.customerFault = customerFault;
    }

    /**
     * 사유 설명 반환
     *
     * @return 사유 설명
     */
    public String description() {
        return description;
    }

    /**
     * 고객 귀책 여부
     *
     * @return 고객 귀책이면 true
     */
    public boolean isCustomerFault() {
        return customerFault;
    }

    /**
     * 판매자 귀책 여부
     *
     * @return 판매자 귀책이면 true
     */
    public boolean isSellerFault() {
        return !customerFault;
    }

    /**
     * 반품 배송비 부담자 결정
     *
     * @return 고객 귀책이면 CUSTOMER, 아니면 SELLER
     */
    public String returnShippingPayer() {
        return customerFault ? "CUSTOMER" : "SELLER";
    }
}
