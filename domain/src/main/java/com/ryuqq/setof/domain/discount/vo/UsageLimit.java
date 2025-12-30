package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.discount.exception.InvalidUsageLimitException;

/**
 * 사용 제한 Value Object
 *
 * <p>할인 정책의 사용 횟수 제한을 표현합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param perCustomerLimit 고객당 사용 횟수 제한 (null이면 무제한)
 * @param totalLimit 전체 사용 횟수 제한 (null이면 무제한)
 * @param resetPeriod 리셋 주기 (null이면 리셋 없음)
 */
public record UsageLimit(Integer perCustomerLimit, Integer totalLimit, ResetPeriod resetPeriod) {

    private static final int MAX_LIMIT = 1_000_000;

    /** Compact Constructor - 검증 로직 */
    public UsageLimit {
        validate(perCustomerLimit, totalLimit);
    }

    /**
     * Static Factory Method
     *
     * @param perCustomerLimit 고객당 제한
     * @param totalLimit 전체 제한
     * @param resetPeriod 리셋 주기
     * @return UsageLimit 인스턴스
     */
    public static UsageLimit of(
            Integer perCustomerLimit, Integer totalLimit, ResetPeriod resetPeriod) {
        return new UsageLimit(perCustomerLimit, totalLimit, resetPeriod);
    }

    /**
     * Static Factory Method - 리셋 주기 없음
     *
     * @param perCustomerLimit 고객당 제한
     * @param totalLimit 전체 제한
     * @return UsageLimit 인스턴스
     */
    public static UsageLimit of(Integer perCustomerLimit, Integer totalLimit) {
        return new UsageLimit(perCustomerLimit, totalLimit, null);
    }

    /**
     * Static Factory Method - 무제한 생성
     *
     * @return 무제한 UsageLimit 인스턴스
     */
    public static UsageLimit unlimited() {
        return new UsageLimit(null, null, null);
    }

    /**
     * Static Factory Method - 고객당 제한만 설정
     *
     * @param perCustomerLimit 고객당 제한
     * @return UsageLimit 인스턴스
     */
    public static UsageLimit perCustomer(int perCustomerLimit) {
        return new UsageLimit(perCustomerLimit, null, null);
    }

    /**
     * Static Factory Method - 전체 제한만 설정
     *
     * @param totalLimit 전체 제한
     * @return UsageLimit 인스턴스
     */
    public static UsageLimit total(int totalLimit) {
        return new UsageLimit(null, totalLimit, null);
    }

    private static void validate(Integer perCustomerLimit, Integer totalLimit) {
        if (perCustomerLimit != null) {
            if (perCustomerLimit <= 0) {
                throw new InvalidUsageLimitException(perCustomerLimit, "고객당 제한은 1 이상이어야 합니다.");
            }
            if (perCustomerLimit > MAX_LIMIT) {
                throw new InvalidUsageLimitException(perCustomerLimit, "고객당 제한은 100만을 초과할 수 없습니다.");
            }
        }
        if (totalLimit != null) {
            if (totalLimit <= 0) {
                throw new InvalidUsageLimitException(totalLimit, "전체 제한은 1 이상이어야 합니다.");
            }
            if (totalLimit > MAX_LIMIT) {
                throw new InvalidUsageLimitException(totalLimit, "전체 제한은 100만을 초과할 수 없습니다.");
            }
        }
    }

    /**
     * 고객이 추가로 사용 가능한지 확인
     *
     * @param customerUsageCount 고객의 현재 사용 횟수
     * @return 사용 가능하면 true
     */
    public boolean canCustomerUse(int customerUsageCount) {
        return perCustomerLimit == null || customerUsageCount < perCustomerLimit;
    }

    /**
     * 전체 사용 횟수가 남았는지 확인
     *
     * @param totalUsageCount 전체 사용 횟수
     * @return 사용 가능하면 true
     */
    public boolean hasTotalCapacity(int totalUsageCount) {
        return totalLimit == null || totalUsageCount < totalLimit;
    }

    /**
     * 무제한인지 확인
     *
     * @return 무제한이면 true
     */
    public boolean isUnlimited() {
        return perCustomerLimit == null && totalLimit == null;
    }

    /**
     * 리셋이 필요한지 확인
     *
     * @return 리셋 주기가 설정되어 있으면 true
     */
    public boolean hasResetPeriod() {
        return resetPeriod != null;
    }

    /** 사용 제한 리셋 주기 Enum */
    public enum ResetPeriod {
        /** 매일 자정에 리셋 */
        DAILY("일별"),

        /** 매주 월요일 자정에 리셋 */
        WEEKLY("주별"),

        /** 매월 1일 자정에 리셋 */
        MONTHLY("월별");

        private final String description;

        ResetPeriod(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
