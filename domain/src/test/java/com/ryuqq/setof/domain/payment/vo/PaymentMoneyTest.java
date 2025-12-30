package com.ryuqq.setof.domain.payment.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.payment.exception.InvalidPaymentMoneyException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PaymentMoney VO")
class PaymentMoneyTest {

    @Nested
    @DisplayName("생성")
    class Creation {

        @Test
        @DisplayName("양수 금액으로 생성할 수 있다")
        void shouldCreateWithPositiveAmount() {
            // given
            BigDecimal amount = BigDecimal.valueOf(50000);

            // when
            PaymentMoney money = PaymentMoney.of(amount);

            // then
            assertEquals(amount, money.value());
        }

        @Test
        @DisplayName("0 금액으로 생성할 수 있다")
        void shouldCreateWithZero() {
            // given & when
            PaymentMoney money = PaymentMoney.zero();

            // then
            assertEquals(BigDecimal.ZERO, money.value());
        }

        @Test
        @DisplayName("null 금액이면 예외가 발생한다")
        void shouldThrowExceptionWhenNull() {
            // when & then
            assertThrows(InvalidPaymentMoneyException.class, () -> PaymentMoney.of(null));
        }

        @Test
        @DisplayName("음수 금액이면 예외가 발생한다")
        void shouldThrowExceptionWhenNegative() {
            // given
            BigDecimal negativeAmount = BigDecimal.valueOf(-1000);

            // when & then
            assertThrows(InvalidPaymentMoneyException.class, () -> PaymentMoney.of(negativeAmount));
        }
    }

    @Nested
    @DisplayName("연산")
    class Operations {

        @Test
        @DisplayName("덧셈 연산이 가능하다")
        void shouldAdd() {
            // given
            PaymentMoney money1 = PaymentMoney.of(BigDecimal.valueOf(10000));
            PaymentMoney money2 = PaymentMoney.of(BigDecimal.valueOf(5000));

            // when
            PaymentMoney result = money1.add(money2);

            // then
            assertEquals(BigDecimal.valueOf(15000), result.value());
        }

        @Test
        @DisplayName("뺄셈 연산이 가능하다")
        void shouldSubtract() {
            // given
            PaymentMoney money1 = PaymentMoney.of(BigDecimal.valueOf(10000));
            PaymentMoney money2 = PaymentMoney.of(BigDecimal.valueOf(3000));

            // when
            PaymentMoney result = money1.subtract(money2);

            // then
            assertEquals(BigDecimal.valueOf(7000), result.value());
        }
    }

    @Nested
    @DisplayName("비교")
    class Comparison {

        @Test
        @DisplayName("양수 금액은 isPositive가 true를 반환한다")
        void shouldReturnTrueForPositive() {
            // given
            PaymentMoney money = PaymentMoney.of(BigDecimal.valueOf(1000));

            // when & then
            assertTrue(money.isPositive());
        }

        @Test
        @DisplayName("0 금액은 isZero가 true를 반환한다")
        void shouldReturnTrueForZero() {
            // given
            PaymentMoney money = PaymentMoney.zero();

            // when & then
            assertTrue(money.isZero());
            assertFalse(money.isPositive());
        }

        @Test
        @DisplayName("다른 금액보다 크거나 같을 때 isGreaterThanOrEqual이 true를 반환한다")
        void shouldReturnTrueWhenGreaterOrEqual() {
            // given
            PaymentMoney money1 = PaymentMoney.of(BigDecimal.valueOf(10000));
            PaymentMoney money2 = PaymentMoney.of(BigDecimal.valueOf(5000));
            PaymentMoney money3 = PaymentMoney.of(BigDecimal.valueOf(10000));

            // when & then
            assertTrue(money1.isGreaterThanOrEqual(money2));
            assertTrue(money1.isGreaterThanOrEqual(money3));
            assertFalse(money2.isGreaterThanOrEqual(money1));
        }

        @Test
        @DisplayName("다른 금액과 같을 때 isEqualTo가 true를 반환한다")
        void shouldReturnTrueWhenEqual() {
            // given
            PaymentMoney money1 = PaymentMoney.of(BigDecimal.valueOf(10000));
            PaymentMoney money2 = PaymentMoney.of(BigDecimal.valueOf(10000));

            // when & then
            assertTrue(money1.isEqualTo(money2));
        }
    }
}
