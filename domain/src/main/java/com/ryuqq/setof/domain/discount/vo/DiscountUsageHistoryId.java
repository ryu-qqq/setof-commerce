package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.discount.exception.InvalidDiscountUsageHistoryIdException;

/**
 * DiscountUsageHistoryId Value Object
 *
 * <p>할인 사용 히스토리의 고유 식별자입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Auto Increment 기반 Long ID
 * </ul>
 *
 * @param value 히스토리 ID (양수)
 */
public record DiscountUsageHistoryId(Long value) {

    /** Compact Constructor - 검증 로직 */
    public DiscountUsageHistoryId {
        validate(value);
    }

    /**
     * Static Factory Method - 기존 ID 복원
     *
     * @param value ID 값
     * @return DiscountUsageHistoryId 인스턴스
     */
    public static DiscountUsageHistoryId of(Long value) {
        return new DiscountUsageHistoryId(value);
    }

    private static void validate(Long value) {
        if (value == null) {
            throw new InvalidDiscountUsageHistoryIdException(
                    null, "DiscountUsageHistoryId는 필수입니다.");
        }
        if (value <= 0) {
            throw new InvalidDiscountUsageHistoryIdException(
                    value, "DiscountUsageHistoryId는 양수여야 합니다.");
        }
    }
}
