package com.ryuqq.setof.domain.product.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.product.exception.InvalidMoneyException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Money Value Object 테스트
 *
 * <p>금액에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("Money Value Object")
class MoneyTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 금액으로 Money 생성")
        void shouldCreateMoneyWithValidValue() {
            // Given
            BigDecimal validAmount = BigDecimal.valueOf(1000);

            // When
            Money money = Money.of(validAmount);

            // Then
            assertNotNull(money);
            assertEquals(validAmount, money.value());
        }

        @Test
        @DisplayName("zero()는 0원 Money를 반환한다")
        void shouldCreateZeroMoney() {
            // When
            Money money = Money.zero();

            // Then
            assertNotNull(money);
            assertEquals(BigDecimal.ZERO, money.value());
        }

        @Test
        @DisplayName("0원으로 Money 생성")
        void shouldCreateMoneyWithZero() {
            // When
            Money money = Money.of(BigDecimal.ZERO);

            // Then
            assertNotNull(money);
            assertEquals(BigDecimal.ZERO, money.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenValueIsNull() {
            // When & Then
            assertThrows(InvalidMoneyException.class, () -> Money.of(null));
        }

        @Test
        @DisplayName("음수 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenValueIsNegative() {
            // When & Then
            assertThrows(InvalidMoneyException.class, () -> Money.of(BigDecimal.valueOf(-1000)));
        }
    }

    @Nested
    @DisplayName("비즈니스 메서드 테스트")
    class BusinessMethodsTest {

        @Test
        @DisplayName("isPositive()는 양수이면 true를 반환한다")
        void shouldReturnTrueWhenPositive() {
            // Given
            Money money = Money.of(BigDecimal.valueOf(1000));

            // When & Then
            assertTrue(money.isPositive());
        }

        @Test
        @DisplayName("isPositive()는 0이면 false를 반환한다")
        void shouldReturnFalseWhenZero() {
            // Given
            Money money = Money.zero();

            // When & Then
            assertFalse(money.isPositive());
        }

        @Test
        @DisplayName("isZero()는 0이면 true를 반환한다")
        void shouldReturnTrueWhenIsZero() {
            // Given
            Money money = Money.zero();

            // When & Then
            assertTrue(money.isZero());
        }

        @Test
        @DisplayName("isZero()는 양수이면 false를 반환한다")
        void shouldReturnFalseWhenNotZero() {
            // Given
            Money money = Money.of(BigDecimal.valueOf(1000));

            // When & Then
            assertFalse(money.isZero());
        }

        @Test
        @DisplayName("add()는 두 금액을 더한다")
        void shouldAddTwoMoney() {
            // Given
            Money money1 = Money.of(BigDecimal.valueOf(1000));
            Money money2 = Money.of(BigDecimal.valueOf(500));

            // When
            Money result = money1.add(money2);

            // Then
            assertEquals(BigDecimal.valueOf(1500), result.value());
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 Money는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            Money money1 = Money.of(BigDecimal.valueOf(1000));
            Money money2 = Money.of(BigDecimal.valueOf(1000));

            // When & Then
            assertEquals(money1, money2);
            assertEquals(money1.hashCode(), money2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 Money는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            Money money1 = Money.of(BigDecimal.valueOf(1000));
            Money money2 = Money.of(BigDecimal.valueOf(2000));

            // When & Then
            assertNotEquals(money1, money2);
        }
    }
}
