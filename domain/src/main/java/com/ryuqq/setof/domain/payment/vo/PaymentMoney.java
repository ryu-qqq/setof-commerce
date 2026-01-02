package com.ryuqq.setof.domain.payment.vo;

import com.ryuqq.setof.domain.payment.exception.InvalidPaymentMoneyException;
import java.math.BigDecimal;

/**
 * PaymentMoney Value Object
 *
 * <p>결제 금액을 나타내는 Value Object입니다.
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
public record PaymentMoney(BigDecimal value) {

    /** Compact Constructor - 검증 로직 */
    public PaymentMoney {
        validate(value);
    }

    /**
     * Static Factory Method - 금액 생성
     *
     * @param value BigDecimal 값
     * @return PaymentMoney 인스턴스
     * @throws InvalidPaymentMoneyException value가 null이거나 음수인 경우
     */
    public static PaymentMoney of(BigDecimal value) {
        return new PaymentMoney(value);
    }

    /**
     * Static Factory Method - long 값으로 생성
     *
     * @param value long 값
     * @return PaymentMoney 인스턴스
     */
    public static PaymentMoney of(long value) {
        return new PaymentMoney(BigDecimal.valueOf(value));
    }

    /**
     * Static Factory Method - 0원 생성
     *
     * @return 0원 PaymentMoney 인스턴스
     */
    public static PaymentMoney zero() {
        return new PaymentMoney(BigDecimal.ZERO);
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
     * @return 합산된 PaymentMoney 인스턴스
     */
    public PaymentMoney add(PaymentMoney other) {
        return new PaymentMoney(this.value.add(other.value));
    }

    /**
     * 금액 빼기
     *
     * @param other 뺄 금액
     * @return 차감된 PaymentMoney 인스턴스
     * @throws InvalidPaymentMoneyException 결과가 음수인 경우
     */
    public PaymentMoney subtract(PaymentMoney other) {
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPaymentMoneyException(result, "차감 결과가 음수입니다");
        }
        return new PaymentMoney(result);
    }

    /**
     * 다른 금액보다 크거나 같은지 확인
     *
     * @param other 비교할 금액
     * @return 크거나 같으면 true
     */
    public boolean isGreaterThanOrEqual(PaymentMoney other) {
        return this.value.compareTo(other.value) >= 0;
    }

    /**
     * 다른 금액과 같은지 확인
     *
     * @param other 비교할 금액
     * @return 같으면 true
     */
    public boolean isEqualTo(PaymentMoney other) {
        return this.value.compareTo(other.value) == 0;
    }

    /**
     * long 값으로 변환
     *
     * @return long 값
     */
    public long toLong() {
        return value.longValue();
    }

    private static void validate(BigDecimal value) {
        if (value == null) {
            throw new InvalidPaymentMoneyException(BigDecimal.ZERO, "금액은 필수입니다");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPaymentMoneyException(value, "금액은 0 이상이어야 합니다");
        }
    }
}
