package com.ryuqq.setof.domain.carrier.vo;

/**
 * 택배사 상태 Value Object (Enum)
 *
 * <p>택배사의 활성/비활성 상태를 나타냅니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Enum 사용
 * </ul>
 */
public enum CarrierStatus {

    /** 활성 상태 - 배송 추적 가능 */
    ACTIVE("활성"),

    /** 비활성 상태 - 배송 추적 불가 */
    INACTIVE("비활성");

    private final String description;

    CarrierStatus(String description) {
        this.description = description;
    }

    /**
     * 상태 설명 반환
     *
     * @return 상태 설명
     */
    public String description() {
        return description;
    }

    /**
     * 기본 상태 반환
     *
     * @return ACTIVE 상태
     */
    public static CarrierStatus defaultStatus() {
        return ACTIVE;
    }

    /**
     * 활성 상태 여부 확인
     *
     * @return 활성 상태이면 true
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
}
