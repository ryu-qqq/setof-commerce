package com.ryuqq.setof.domain.cms.vo;

/**
 * 노출 순서 Value Object
 *
 * <p>0 이상의 정수로 표현되는 정렬 순서
 *
 * @param value 정렬 순서 값
 * @author development-team
 * @since 1.0.0
 */
public record DisplayOrder(int value) {

    /** 기본 순서값 */
    public static final DisplayOrder DEFAULT = new DisplayOrder(0);

    /** Compact Constructor */
    public DisplayOrder {
        if (value < 0) {
            throw new IllegalArgumentException("노출 순서는 0 이상이어야 합니다: " + value);
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param value 순서 값
     * @return DisplayOrder 인스턴스
     */
    public static DisplayOrder of(int value) {
        return new DisplayOrder(value);
    }

    /**
     * 다른 순서보다 앞인지 확인
     *
     * @param other 비교 대상
     * @return 앞이면 true
     */
    public boolean isBefore(DisplayOrder other) {
        return this.value < other.value;
    }

    /**
     * 다른 순서보다 뒤인지 확인
     *
     * @param other 비교 대상
     * @return 뒤면 true
     */
    public boolean isAfter(DisplayOrder other) {
        return this.value > other.value;
    }
}
