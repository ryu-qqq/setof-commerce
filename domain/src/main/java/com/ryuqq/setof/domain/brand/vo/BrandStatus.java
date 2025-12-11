package com.ryuqq.setof.domain.brand.vo;

/**
 * 브랜드 상태 Value Object (Enum)
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Enum 사용
 *   <li>불변성 보장 - Java Enum 특성
 *   <li>상태 변경 없이 읽기 전용 (배치로만 동기화)
 * </ul>
 *
 * <p>브랜드 상태:
 *
 * <ul>
 *   <li>ACTIVE: 활성 상태 (노출됨)
 *   <li>INACTIVE: 비활성 상태 (노출되지 않음)
 * </ul>
 */
public enum BrandStatus {
    ACTIVE("활성"),
    INACTIVE("비활성");

    private final String description;

    BrandStatus(String description) {
        this.description = description;
    }

    /**
     * 상태 설명 반환
     *
     * @return 상태 설명
     */
    public String getDescription() {
        return description;
    }

    /**
     * 활성 상태인지 확인
     *
     * @return 활성 상태이면 true
     */
    public boolean isActive() {
        return this == ACTIVE;
    }

    /**
     * 비활성 상태인지 확인
     *
     * @return 비활성 상태이면 true
     */
    public boolean isInactive() {
        return this == INACTIVE;
    }
}
