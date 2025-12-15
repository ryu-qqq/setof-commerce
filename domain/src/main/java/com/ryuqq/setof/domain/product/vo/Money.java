package com.ryuqq.setof.domain.product.vo;

import com.ryuqq.setof.domain.product.exception.InvalidMoneyException;
import java.math.BigDecimal;

/**
 * 금액 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>음수 금액 허용 안함
 * </ul>
 *
 * @param value 금액 값
 */
public record Money(BigDecimal value) {

    /** Compact Constructor - 검증 로직 */
    public Money {
        validate(value);
    }

    /**
     * Static Factory Method - 금액 생성
     *
     * @param value BigDecimal 값
     * @return Money 인스턴스
     * @throws InvalidMoneyException value가 null이거나 음수인 경우
     */
    public static Money of(BigDecimal value) {
        return new Money(value);
    }

    /**
     * Static Factory Method - 0원 생성
     *
     * @return 0원 Money 인스턴스
     */
    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    /**
     * 0원 여부 확인
     *
     * @return 0원이면 true
     */
    public boolean isZero() {
        return value.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 양수 여부 확인
     *
     * @return 0원보다 크면 true
     */
    public boolean isPositive() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 금액 더하기
     *
     * @param other 더할 금액
     * @return 합산된 Money 인스턴스
     */
    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    private static void validate(BigDecimal value) {
        if (value == null) {
            throw new InvalidMoneyException(BigDecimal.ZERO, "금액은 필수입니다");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMoneyException(value, "금액은 0 이상이어야 합니다");
        }
    }
}
