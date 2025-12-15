package com.ryuqq.setof.domain.product.vo;

/**
 * 상품 옵션 타입 Value Object (Enum)
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Enum 사용
 *   <li>불변성 보장 - Java Enum 특성
 * </ul>
 *
 * <p>옵션 타입:
 *
 * <ul>
 *   <li>SINGLE: 단품 (옵션 없음)
 *   <li>ONE_LEVEL: 1단 옵션 (예: 색상만)
 *   <li>TWO_LEVEL: 2단 옵션 (예: 색상 + 사이즈)
 * </ul>
 */
public enum OptionType {
    SINGLE("단품"),
    ONE_LEVEL("1단 옵션"),
    TWO_LEVEL("2단 옵션");

    private final String displayName;

    OptionType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 표시명 반환
     *
     * @return 옵션 타입 표시명
     */
    public String displayName() {
        return displayName;
    }

    /**
     * 단품 여부 확인
     *
     * @return 단품이면 true
     */
    public boolean isSingle() {
        return this == SINGLE;
    }

    /**
     * 옵션 존재 여부 확인
     *
     * @return 옵션이 있으면 true (1단 또는 2단)
     */
    public boolean hasOption() {
        return this != SINGLE;
    }

    /**
     * 2단 옵션 여부 확인
     *
     * @return 2단 옵션이면 true
     */
    public boolean isTwoLevel() {
        return this == TWO_LEVEL;
    }
}
