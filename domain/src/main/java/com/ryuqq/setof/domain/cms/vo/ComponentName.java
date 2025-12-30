package com.ryuqq.setof.domain.cms.vo;

/**
 * Component 이름 Value Object
 *
 * <p>최대 100자까지 허용, nullable
 *
 * @param value 이름 문자열 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record ComponentName(String value) {

    private static final int MAX_LENGTH = 100;

    /** Compact Constructor */
    public ComponentName {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("컴포넌트 이름은 " + MAX_LENGTH + "자를 초과할 수 없습니다");
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param value 이름 문자열
     * @return ComponentName 인스턴스
     */
    public static ComponentName of(String value) {
        return new ComponentName(value);
    }

    /**
     * 빈 이름 생성
     *
     * @return null 값을 가진 ComponentName
     */
    public static ComponentName empty() {
        return new ComponentName(null);
    }

    /**
     * 이름 존재 여부 확인
     *
     * @return 이름이 존재하면 true
     */
    public boolean hasValue() {
        return value != null && !value.isBlank();
    }
}
