package com.ryuqq.setof.domain.orderevent.vo;

/**
 * OrderEventSource - 주문 이벤트 출처
 *
 * <p>이벤트가 어디서 발생했는지 구분합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public enum OrderEventSource {

    /** 주문 도메인에서 발생 */
    ORDER("주문"),

    /** 클레임 도메인에서 발생 */
    CLAIM("클레임"),

    /** 결제 도메인에서 발생 */
    PAYMENT("결제"),

    /** 배송 도메인에서 발생 */
    SHIPPING("배송");

    private final String description;

    OrderEventSource(String description) {
        this.description = description;
    }

    /**
     * 출처 설명 반환
     *
     * @return 출처 설명
     */
    public String description() {
        return description;
    }
}
