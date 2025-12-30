package com.ryuqq.setof.domain.order.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.order.exception.InvalidOrderMoneyException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderMoney VO")
class OrderMoneyTest {

    @Nested
    @DisplayName("생성")
    class Creation {

        @Test
        @DisplayName("양수 금액으로 생성할 수 있다")
        void shouldCreateWithPositiveAmount() {
            // given
            BigDecimal amount = BigDecimal.valueOf(30000);

            // when
            OrderMoney money = OrderMoney.of(amount);

            // then
            assertEquals(amount, money.value());
        }

        @Test
        @DisplayName("0 금액으로 생성할 수 있다")
        void shouldCreateWithZero() {
            // given & when
            OrderMoney money = OrderMoney.zero();

            // then
            assertEquals(BigDecimal.ZERO, money.value());
        }

        @Test
        @DisplayName("null 금액이면 예외가 발생한다")
        void shouldThrowExceptionWhenNull() {
            // when & then
            assertThrows(InvalidOrderMoneyException.class, () -> OrderMoney.of(null));
        }

        @Test
        @DisplayName("음수 금액이면 예외가 발생한다")
        void shouldThrowExceptionWhenNegative() {
            // given
            BigDecimal negativeAmount = BigDecimal.valueOf(-1000);

            // when & then
            assertThrows(InvalidOrderMoneyException.class, () -> OrderMoney.of(negativeAmount));
        }
    }

    @Nested
    @DisplayName("연산")
    class Operations {

        @Test
        @DisplayName("덧셈 연산이 가능하다")
        void shouldAdd() {
            // given
            OrderMoney money1 = OrderMoney.of(BigDecimal.valueOf(10000));
            OrderMoney money2 = OrderMoney.of(BigDecimal.valueOf(5000));

            // when
            OrderMoney result = money1.add(money2);

            // then
            assertEquals(BigDecimal.valueOf(15000), result.value());
        }

        @Test
        @DisplayName("뺄셈 연산이 가능하다")
        void shouldSubtract() {
            // given
            OrderMoney money1 = OrderMoney.of(BigDecimal.valueOf(10000));
            OrderMoney money2 = OrderMoney.of(BigDecimal.valueOf(3000));

            // when
            OrderMoney result = money1.subtract(money2);

            // then
            assertEquals(BigDecimal.valueOf(7000), result.value());
        }

        @Test
        @DisplayName("곱셈 연산이 가능하다")
        void shouldMultiply() {
            // given
            OrderMoney money = OrderMoney.of(BigDecimal.valueOf(10000));

            // when
            OrderMoney result = money.multiply(3);

            // then
            assertEquals(BigDecimal.valueOf(30000), result.value());
        }
    }

    @Nested
    @DisplayName("비교")
    class Comparison {

        @Test
        @DisplayName("양수 금액은 isPositive가 true를 반환한다")
        void shouldReturnTrueForPositive() {
            // given
            OrderMoney money = OrderMoney.of(BigDecimal.valueOf(1000));

            // when & then
            assertTrue(money.isPositive());
        }

        @Test
        @DisplayName("0 금액은 isZero가 true를 반환한다")
        void shouldReturnTrueForZero() {
            // given
            OrderMoney money = OrderMoney.zero();

            // when & then
            assertTrue(money.isZero());
            assertFalse(money.isPositive());
        }
    }
}
