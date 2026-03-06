package com.ryuqq.setof.domain.productgroup.vo;

/** 상품 그룹 옵션 유형. */
public enum OptionType {
    NONE("옵션 없음"),
    SINGLE("단일 옵션"),
    COMBINATION("조합 옵션");

    private final String displayName;

    OptionType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    /** 옵션 그룹이 필요한 유형인지 확인. */
    public boolean requiresOptionGroup() {
        return this == SINGLE || this == COMBINATION;
    }

    /** 예상되는 옵션 그룹 수 반환. NONE은 0, SINGLE은 1, COMBINATION은 2. */
    public int expectedOptionGroupCount() {
        return switch (this) {
            case NONE -> 0;
            case SINGLE -> 1;
            case COMBINATION -> 2;
        };
    }

    /**
     * 레거시 매핑용 팩토리 메서드.
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
