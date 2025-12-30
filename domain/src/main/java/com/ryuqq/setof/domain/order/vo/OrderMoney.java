package com.ryuqq.setof.domain.order.vo;

import com.ryuqq.setof.domain.order.exception.InvalidOrderMoneyException;
import java.math.BigDecimal;

/**
 * OrderMoney Value Object
 *
 * <p>주문 금액을 나타내는 Value Object입니다.
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
public record OrderMoney(BigDecimal value) {

    /** Compact Constructor - 검증 로직 */
    public OrderMoney {
        validate(value);
    }

    /**
     * Static Factory Method - 금액 생성
     *
     * @param value BigDecimal 값
     * @return OrderMoney 인스턴스
     */
    public static OrderMoney of(BigDecimal value) {
        return new OrderMoney(value);
    }

    /**
     * Static Factory Method - long 값으로 생성
     *
     * @param value long 값
     * @return OrderMoney 인스턴스
     */
    public static OrderMoney of(long value) {
        return new OrderMoney(BigDecimal.valueOf(value));
    }

    /**
     * Static Factory Method - 0원 생성
     *
     * @return 0원 OrderMoney 인스턴스
     */
    public static OrderMoney zero() {
        return new OrderMoney(BigDecimal.ZERO);
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
     * @return 합산된 OrderMoney 인스턴스
     */
    public OrderMoney add(OrderMoney other) {
        return new OrderMoney(this.value.add(other.value));
    }

    /**
     * 금액 빼기
     *
     * @param other 뺄 금액
     * @return 차감된 OrderMoney 인스턴스
     */
    public OrderMoney subtract(OrderMoney other) {
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOrderMoneyException(result, "차감 결과가 음수입니다");
        }
        return new OrderMoney(result);
    }

    /**
     * 금액 곱하기 (수량 계산용)
     *
     * @param quantity 수량
     * @return 곱셈 결과 OrderMoney 인스턴스
     */
    public OrderMoney multiply(int quantity) {
        if (quantity < 0) {
            throw new InvalidOrderMoneyException(value, "수량은 0 이상이어야 합니다");
        }
        return new OrderMoney(this.value.multiply(BigDecimal.valueOf(quantity)));
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
            throw new InvalidOrderMoneyException(BigDecimal.ZERO, "금액은 필수입니다");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOrderMoneyException(value, "금액은 0 이상이어야 합니다");
        }
    }
}
