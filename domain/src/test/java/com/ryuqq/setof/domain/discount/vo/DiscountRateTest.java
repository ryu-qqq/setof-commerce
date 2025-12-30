package com.ryuqq.setof.domain.discount.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.discount.exception.InvalidDiscountRateException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** DiscountRate Value Object 테스트 */
@DisplayName("DiscountRate Value Object")
class DiscountRateTest {

    @Nested
    @DisplayName("생성 검증")
    class Creation {

        @Test
        @DisplayName("0% ~ 100% 사이의 할인율을 생성할 수 있다")
        void shouldCreateValidDiscountRate() {
            // when
            DiscountRate rate10 = DiscountRate.of(10);
            DiscountRate rate50 = DiscountRate.of(new BigDecimal("50.5"));
            DiscountRate rate100 = DiscountRate.of(100);

            // then
            assertEquals(new BigDecimal("10.00"), rate10.value());
            assertEquals(new BigDecimal("50.50"), rate50.value());
            assertEquals(new BigDecimal("100.00"), rate100.value());
        }

        @Test
        @DisplayName("null 할인율은 예외가 발생한다")
        void shouldThrowExceptionForNullRate() {
            // when & then
            assertThrows(InvalidDiscountRateException.class, () -> DiscountRate.of(null));
        }

        @Test
        @DisplayName("음수 할인율은 예외가 발생한다")
        void shouldThrowExceptionForNegativeRate() {
            // when & then
            assertThrows(InvalidDiscountRateException.class, () -> DiscountRate.of(-1));
        }

        @Test
        @DisplayName("100%를 초과하는 할인율은 예외가 발생한다")
        void shouldThrowExceptionForRateOver100() {
            // when & then
            assertThrows(
                    InvalidDiscountRateException.class,
                    () -> DiscountRate.of(new BigDecimal("100.01")));
        }
    }

    @Nested
    @DisplayName("할인 금액 계산")
    class CalculateDiscountAmount {

        @Test
        @DisplayName("정확한 할인 금액을 계산한다")
        void shouldCalculateDiscountAmountCorrectly() {
            // given
            DiscountRate rate = DiscountRate.of(10); // 10%

            // when
            long discount = rate.calculateDiscountAmount(50000L);

            // then
            assertEquals(5000L, discount);
        }

        @Test
        @DisplayName("소수점 이하는 버림한다")
        void shouldRoundDownDecimalPlaces() {
            // given
            DiscountRate rate = DiscountRate.of(new BigDecimal("15.55")); // 15.55%

            // when
            long discount = rate.calculateDiscountAmount(10000L);

            // then
            assertEquals(1555L, discount); // 10000 * 0.1555 = 1555
        }
    }

    @Nested
    @DisplayName("상태 확인")
    class StateCheck {

        @Test
        @DisplayName("0% 할인인지 확인할 수 있다")
        void shouldCheckIfZero() {
            // given
            DiscountRate zeroRate = DiscountRate.of(0);
            DiscountRate nonZeroRate = DiscountRate.of(10);

            // then
            assertTrue(zeroRate.isZero());
            assertFalse(nonZeroRate.isZero());
        }
    }
}
