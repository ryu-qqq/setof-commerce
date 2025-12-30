package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.discount.exception.InvalidPriorityException;

/**
 * 우선순위 Value Object
 *
 * <p>할인 정책의 적용 우선순위를 표현합니다. 낮은 숫자가 높은 우선순위를 의미합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param value 우선순위 (1 ~ 1000)
 */
public record Priority(int value) implements Comparable<Priority> {

    private static final int MIN_PRIORITY = 1;
    private static final int MAX_PRIORITY = 1000;
    private static final int DEFAULT_PRIORITY = 100;

    /** Compact Constructor - 검증 로직 */
    public Priority {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 우선순위 값
     * @return Priority 인스턴스
     */
    public static Priority of(int value) {
        return new Priority(value);
    }

    /**
     * Static Factory Method - 기본 우선순위 생성
     *
     * @return 기본 우선순위 (100) Priority 인스턴스
     */
    public static Priority defaultPriority() {
        return new Priority(DEFAULT_PRIORITY);
    }

    /**
     * Static Factory Method - 최고 우선순위 생성
     *
     * @return 최고 우선순위 (1) Priority 인스턴스
     */
    public static Priority highest() {
        return new Priority(MIN_PRIORITY);
    }

    /**
     * Static Factory Method - 최저 우선순위 생성
     *
     * @return 최저 우선순위 (1000) Priority 인스턴스
     */
    public static Priority lowest() {
        return new Priority(MAX_PRIORITY);
    }

    private static void validate(int value) {
        if (value < MIN_PRIORITY) {
            throw new InvalidPriorityException(value, "우선순위는 1 이상이어야 합니다.");
        }
        if (value > MAX_PRIORITY) {
            throw new InvalidPriorityException(value, "우선순위는 1000 이하여야 합니다.");
        }
    }

    /**
     * 다른 우선순위보다 높은지 확인
     *
     * @param other 비교할 우선순위
     * @return 이 우선순위가 더 높으면 (값이 작으면) true
     */
    public boolean isHigherThan(Priority other) {
        return this.value < other.value;
    }

    /**
     * 다른 우선순위보다 낮은지 확인
     *
     * @param other 비교할 우선순위
     * @return 이 우선순위가 더 낮으면 (값이 크면) true
     */
    public boolean isLowerThan(Priority other) {
        return this.value > other.value;
    }

    @Override
    public int compareTo(Priority other) {
        return Integer.compare(this.value, other.value);
    }
}
