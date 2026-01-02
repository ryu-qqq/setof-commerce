package com.ryuqq.setof.domain.orderevent.vo;

/**
 * OrderActorType - 이벤트 수행자 타입
 *
 * <p>주문 이벤트를 발생시킨 주체를 구분합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public enum OrderActorType {

    /** 고객 */
    CUSTOMER("고객"),

    /** 판매자 */
    SELLER("판매자"),

    /** 관리자 */
    ADMIN("관리자"),

    /** 시스템 (자동 처리) */
    SYSTEM("시스템");

    private final String description;

    OrderActorType(String description) {
        this.description = description;
    }

    /**
     * 수행자 타입 설명 반환
     *
     * @return 수행자 설명
     */
    public String description() {
        return description;
    }

    /**
     * 사람이 수행한 이벤트인지 확인
     *
     * @return SYSTEM이 아니면 true
     */
    public boolean isHumanActor() {
        return this != SYSTEM;
    }
}
