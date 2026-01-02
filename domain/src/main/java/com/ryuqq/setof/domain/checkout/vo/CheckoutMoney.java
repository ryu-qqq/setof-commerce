package com.ryuqq.setof.domain.checkout.vo;

import com.ryuqq.setof.domain.checkout.exception.InvalidCheckoutMoneyException;
import java.math.BigDecimal;

/**
 * CheckoutMoney Value Object
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
public record CheckoutMoney(BigDecimal value) {

    /** Compact Constructor - 검증 로직 */
    public CheckoutMoney {
        validate(value);
    }

    /**
     * Static Factory Method - 금액 생성
     *
     * @param value BigDecimal 값
     * @return CheckoutMoney 인스턴스
     * @throws InvalidCheckoutMoneyException value가 null이거나 음수인 경우
     */
    public static CheckoutMoney of(BigDecimal value) {
        return new CheckoutMoney(value);
    }

    /**
     * Static Factory Method - long 값으로 생성
     *
     * @param value long 값
     * @return CheckoutMoney 인스턴스
     */
    public static CheckoutMoney of(long value) {
        return new CheckoutMoney(BigDecimal.valueOf(value));
    }

    /**
     * Static Factory Method - 0원 생성
     *
     * @return 0원 CheckoutMoney 인스턴스
     */
    public static CheckoutMoney zero() {
        return new CheckoutMoney(BigDecimal.ZERO);
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
     * @return 합산된 CheckoutMoney 인스턴스
     */
    public CheckoutMoney add(CheckoutMoney other) {
        return new CheckoutMoney(this.value.add(other.value));
    }

    /**
     * 금액 빼기
     *
     * @param other 뺄 금액
     * @return 차감된 CheckoutMoney 인스턴스
     * @throws InvalidCheckoutMoneyException 결과가 음수인 경우
     */
    public CheckoutMoney subtract(CheckoutMoney other) {
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidCheckoutMoneyException(result, "차감 결과가 음수입니다");
        }
        return new CheckoutMoney(result);
    }

    /**
     * 금액 곱하기 (수량 계산용)
     *
     * @param quantity 수량
     * @return 곱셈 결과 CheckoutMoney 인스턴스
     */
    public CheckoutMoney multiply(int quantity) {
        if (quantity < 0) {
            throw new InvalidCheckoutMoneyException(value, "수량은 0 이상이어야 합니다");
        }
        return new CheckoutMoney(this.value.multiply(BigDecimal.valueOf(quantity)));
    }

    /**
     * 다른 금액보다 크거나 같은지 확인
     *
     * @param other 비교할 금액
     * @return 크거나 같으면 true
     */
    public boolean isGreaterThanOrEqual(CheckoutMoney other) {
        return this.value.compareTo(other.value) >= 0;
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
            throw new InvalidCheckoutMoneyException(BigDecimal.ZERO, "금액은 필수입니다");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidCheckoutMoneyException(value, "금액은 0 이상이어야 합니다");
        }
    }
}
