package com.ryuqq.setof.domain.productgroup.vo;

/**
 * 옵션 유형.
 *
 * <p>상품그룹의 옵션 구성 방식을 정의합니다.
 */
public enum OptionType {

    /** 옵션 없음 (단일 상품) */
    NONE,

    /** 단일 옵션 (예: 사이즈만) */
    SINGLE,

    /** 조합 옵션 (예: 색상 + 사이즈) */
    COMBINATION;

    /**
     * 레거시 매핑용 팩토리 메서드.
     *
     * <p>레거시 DB의 option_type 문자열을 OptionType으로 변환합니다.
     *
     * @param legacyValue 레거시 옵션 타입 문자열
     * @return OptionType
     */
    public static OptionType fromLegacy(String legacyValue) {
        if (legacyValue == null || legacyValue.isBlank()) {
            return NONE;
        }
        return switch (legacyValue.toUpperCase().trim()) {
            case "SINGLE" -> SINGLE;
            case "COMBINATION" -> COMBINATION;
            default -> NONE;
        };
    }

    public boolean hasOptions() {
        return this != NONE;
    }

    public boolean isCombination() {
        return this == COMBINATION;
    }
}
