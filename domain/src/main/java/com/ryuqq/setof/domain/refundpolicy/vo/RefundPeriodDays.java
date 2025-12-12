package com.ryuqq.setof.domain.refundpolicy.vo;

import com.ryuqq.setof.domain.refundpolicy.exception.InvalidRefundPeriodDaysException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * 반품 기간 (일) Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>양수만 허용 (1일 이상)
 * </ul>
 *
 * @param value 반품 기간 (일)
 */
public record RefundPeriodDays(Integer value) {

    private static final int DEFAULT_PERIOD_DAYS = 7;
    private static final int MAX_PERIOD_DAYS = 365;

    /** Compact Constructor - 검증 로직 */
    public RefundPeriodDays {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 반품 기간 (일)
     * @return RefundPeriodDays 인스턴스
     * @throws InvalidRefundPeriodDaysException value가 null이거나 유효하지 않은 경우
     */
    public static RefundPeriodDays of(Integer value) {
        return new RefundPeriodDays(value);
    }

    /**
     * 기본값 생성 (7일)
     *
     * @return 기본 기간 RefundPeriodDays 인스턴스
     */
    public static RefundPeriodDays defaultPeriod() {
        return new RefundPeriodDays(DEFAULT_PERIOD_DAYS);
    }

    private static void validate(Integer value) {
        if (value == null) {
            throw new InvalidRefundPeriodDaysException(null, "반품 기간은 필수입니다.");
        }
        if (value <= 0) {
            throw new InvalidRefundPeriodDaysException(value, "반품 기간은 1일 이상이어야 합니다.");
        }
        if (value > MAX_PERIOD_DAYS) {
            throw new InvalidRefundPeriodDaysException(value, "반품 기간은 365일을 초과할 수 없습니다.");
        }
    }

    /**
     * 반품 가능 기간 내인지 확인
     *
     * @param orderDate 주문 일시
     * @param currentDate 현재 일시
     * @return 반품 가능 기간 내이면 true
     */
    public boolean isWithinPeriod(Instant orderDate, Instant currentDate) {
        if (orderDate == null || currentDate == null) {
            return false;
        }
        long daysBetween = ChronoUnit.DAYS.between(orderDate, currentDate);
        return daysBetween >= 0 && daysBetween <= value;
    }
}
