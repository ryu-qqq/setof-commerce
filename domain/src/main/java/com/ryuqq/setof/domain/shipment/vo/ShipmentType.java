package com.ryuqq.setof.domain.shipment.vo;

/**
 * 배송 유형 Value Object (Enum)
 *
 * <p>배송 방식을 나타냅니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Enum 사용
 * </ul>
 */
public enum ShipmentType {

    /** 일반 배송 - 표준 배송 서비스 */
    STANDARD("일반배송"),

    /** 당일 배송 - 주문 당일 배송 */
    SAME_DAY("당일배송"),

    /** 새벽 배송 - 새벽 시간대 배송 */
    DAWN("새벽배송"),

    /** 퀵 배송 - 오토바이/차량 직접 배송 */
    QUICK("퀵배송");

    private final String description;

    ShipmentType(String description) {
        this.description = description;
    }

    /**
     * 유형 설명 반환
     *
     * @return 배송 유형 설명
     */
    public String description() {
        return description;
    }

    /**
     * 기본 배송 유형 반환
     *
     * @return STANDARD 유형
     */
    public static ShipmentType defaultType() {
        return STANDARD;
    }
}
