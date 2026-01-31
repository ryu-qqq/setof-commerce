package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Money Value Object 테스트")
class MoneyTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("양수 금액으로 Money를 생성한다")
        void createMoneyWithPositiveValue() {
            // when
            Money money = Money.of(1000);

            // then
            assertThat(money.value()).isEqualTo(1000);
        }

        @Test
        @DisplayName("0원으로 Money를 생성한다")
        void createMoneyWithZero() {
            // when
            Money money = Money.of(0);

            // then
            assertThat(money.value()).isEqualTo(0);
            assertThat(money.isZero()).isTrue();
        }

        @Test
        @DisplayName("zero() 팩토리 메서드로 0원 Money를 생성한다")
        void createZeroMoney() {
            // when
            Money money = Money.zero();

            // then
            assertThat(money.value()).isEqualTo(0);
            assertThat(money.isZero()).isTrue();
        }

        @Test
        @DisplayName("음수 금액으로 생성하면 예외가 발생한다")
        void createMoneyWithNegativeValueThrowsException() {
            // when & then
            assertThatThrownBy(() -> Money.of(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("0 이상");
        }
    }

    @Nested
    @DisplayName("add() - 덧셈 연산")
    class AddTest {

        @Test
        @DisplayName("두 Money를 더한다")
        void addTwoMoneys() {
            // given
            Money money1 = Money.of(1000);
            Money money2 = Money.of(500);

            // when
            Money result = money1.add(money2);

            // then
            assertThat(result.value()).isEqualTo(1500);
        }

        @Test
        @DisplayName("0원을 더하면 같은 값이 반환된다")
        void addZeroReturnsOriginalValue() {
            // given
            Money money = Money.of(1000);
            Money zero = Money.zero();

            // when
            Money result = money.add(zero);

            // then
            assertThat(result.value()).isEqualTo(1000);
        }

        @Test
        @DisplayName("덧셈은 불변성을 유지한다")
        void addIsImmutable() {
            // given
            Money money1 = Money.of(1000);
            Money money2 = Money.of(500);

            // when
            Money result = money1.add(money2);

            // then
            assertThat(money1.value()).isEqualTo(1000);
            assertThat(money2.value()).isEqualTo(500);
            assertThat(result).isNotSameAs(money1);
        }
    }

    @Nested
    @DisplayName("subtract() - 뺄셈 연산")
    class SubtractTest {

        @Test
        @DisplayName("두 Money를 뺀다")
        void subtractTwoMoneys() {
            // given
            Money money1 = Money.of(1000);
            Money money2 = Money.of(300);

            // when
            Money result = money1.subtract(money2);

            // then
            assertThat(result.value()).isEqualTo(700);
        }

        @Test
        @DisplayName("같은 값을 빼면 0원이 된다")
        void subtractSameValueReturnsZero() {
            // given
            Money money1 = Money.of(1000);
            Money money2 = Money.of(1000);

            // when
            Money result = money1.subtract(money2);

            // then
            assertThat(result.isZero()).isTrue();
        }

        @Test
        @DisplayName("더 큰 금액을 빼면 예외가 발생한다")
        void subtractLargerValueThrowsException() {
            // given
            Money money1 = Money.of(500);
            Money money2 = Money.of(1000);

            // when & then
            assertThatThrownBy(() -> money1.subtract(money2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("음수");
        }
    }

    @Nested
    @DisplayName("multiply() - 곱셈 연산")
    class MultiplyTest {

        @Test
        @DisplayName("Money에 정수를 곱한다")
        void multiplyMoney() {
            // given
            Money money = Money.of(1000);

            // when
            Money result = money.multiply(3);

            // then
            assertThat(result.value()).isEqualTo(3000);
        }

        @Test
        @DisplayName("0을 곱하면 0원이 된다")
        void multiplyByZeroReturnsZero() {
            // given
            Money money = Money.of(1000);

            // when
            Money result = money.multiply(0);

            // then
            assertThat(result.isZero()).isTrue();
        }

        @Test
        @DisplayName("음수를 곱하면 예외가 발생한다")
        void multiplyByNegativeThrowsException() {
            // given
            Money money = Money.of(1000);

            // when & then
            assertThatThrownBy(() -> money.multiply(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("0 이상");
        }
    }

    @Nested
    @DisplayName("비교 연산 테스트")
    class ComparisonTest {

        @Test
        @DisplayName("isGreaterThan()은 큰 경우 true를 반환한다")
        void isGreaterThanReturnsTrue() {
            // given
            Money larger = Money.of(1000);
            Money smaller = Money.of(500);

            // then
            assertThat(larger.isGreaterThan(smaller)).isTrue();
            assertThat(smaller.isGreaterThan(larger)).isFalse();
        }

        @Test
        @DisplayName("isGreaterThan()은 같은 경우 false를 반환한다")
        void isGreaterThanReturnsFalseWhenEqual() {
            // given
            Money money1 = Money.of(1000);
            Money money2 = Money.of(1000);

            // then
            assertThat(money1.isGreaterThan(money2)).isFalse();
        }

        @Test
        @DisplayName("isGreaterThanOrEqual()은 같거나 큰 경우 true를 반환한다")
        void isGreaterThanOrEqualReturnsTrue() {
            // given
            Money larger = Money.of(1000);
            Money equal = Money.of(1000);
            Money smaller = Money.of(500);

            // then
            assertThat(larger.isGreaterThanOrEqual(equal)).isTrue();
            assertThat(larger.isGreaterThanOrEqual(smaller)).isTrue();
            assertThat(smaller.isGreaterThanOrEqual(larger)).isFalse();
        }

        @Test
        @DisplayName("isLessThan()은 작은 경우 true를 반환한다")
        void isLessThanReturnsTrue() {
            // given
            Money larger = Money.of(1000);
            Money smaller = Money.of(500);

            // then
            assertThat(smaller.isLessThan(larger)).isTrue();
            assertThat(larger.isLessThan(smaller)).isFalse();
        }

        @Test
        @DisplayName("isLessThanOrEqual()은 같거나 작은 경우 true를 반환한다")
        void isLessThanOrEqualReturnsTrue() {
            // given
            Money larger = Money.of(1000);
            Money equal = Money.of(1000);
            Money smaller = Money.of(500);

            // then
            assertThat(smaller.isLessThanOrEqual(larger)).isTrue();
            assertThat(equal.isLessThanOrEqual(larger)).isTrue();
            assertThat(larger.isLessThanOrEqual(smaller)).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값의 Money는 동등하다")
        void moneyWithSameValueAreEqual() {
            // given
            Money money1 = Money.of(1000);
            Money money2 = Money.of(1000);

            // then
            assertThat(money1).isEqualTo(money2);
            assertThat(money1.hashCode()).isEqualTo(money2.hashCode());
        }

        @Test
        @DisplayName("다른 값의 Money는 동등하지 않다")
        void moneyWithDifferentValueAreNotEqual() {
            // given
            Money money1 = Money.of(1000);
            Money money2 = Money.of(2000);

            // then
            assertThat(money1).isNotEqualTo(money2);
        }
    }
}
