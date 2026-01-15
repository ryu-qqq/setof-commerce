package com.ryuqq.setof.batch.legacy.notification.enums;

/**
 * 알림톡 템플릿 코드
 *
 * @author development-team
 * @since 1.0.0
 */
public enum AlimTalkTemplateCode {
    // 회원
    MEMBER_JOIN("MEMBER_JOIN"),

    // 주문
    ORDER_ACCEPT("ORDER_ACCEPT"),
    ORDER_COMPLETE("ORDER_COMPLETE"),

    // 배송
    DELIVERY_START("DELIVERY_START"),

    // 취소
    CANCEL_REQUEST("CANCEL_REQUEST"),
    CANCEL_ORDER_S("CANCEL_ORDER_S"),
    CANCEL_ORDER_AUTO("CANCEL_ORDER_AUTO"),
    CANCEL_SALE("CANCEL_SALE"),
    CANCEL_NOTIFY("CANCEL_NOTIFY"),
    CANCEL_VCOMPLETE("CANCEL_VCOMPLETE"),

    // 반품
    RETURN_REQUEST("RETURN_REQUEST"),
    RETURN_REQUEST_S("RETURN_REQUEST_S"),
    RETURN_REJECT("RETURN_REJECT"),

    // CS
    CS_PRODUCT("CS_PRODUCT"),
    CS_PRODUCT_S("CS_PRODUCT_S"),
    CS_ORDER("CS_ORDER"),
    CS_ORDER_S("CS_ORDER_S"),

    // 마일리지
    MILEAGE_SOON_EXPIRE("MILEAGE_SOON_EXPIRE");

    private final String code;

    AlimTalkTemplateCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
