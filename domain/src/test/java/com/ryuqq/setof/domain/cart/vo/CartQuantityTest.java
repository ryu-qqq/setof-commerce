package com.ryuqq.setof.domain.cart.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CartQuantity Value Object 단위 테스트")
class CartQuantityTest {

    @Nested
    @DisplayName("of() - 생성")
    class CreationTest {

        @Test
        @DisplayName("유효한 수량 1로 생성한다")
        void createWithMinValue() {
            // when
            CartQuantity quantity = CartQuantity.of(1);

            // then
            assertThat(quantity.value()).isEqualTo(1);
        }

        @Test
        @DisplayName("유효한 수량 999로 생성한다")
        void createWithMaxValue() {
            // when
            CartQuantity quantity = CartQuantity.of(999);

            // then
            assertThat(quantity.value()).isEqualTo(999);
        }

        @Test
        @DisplayName("중간 수량으로 생성한다")
        void createWithMiddleValue() {
            // when
            CartQuantity quantity = CartQuantity.of(50);

            // then
            assertThat(quantity.value()).isEqualTo(50);
        }

        @Test
        @DisplayName("수량이 0이면 예외가 발생한다")
        void zeroQuantityThrowsException() {
            // when & then
            assertThatThrownBy(() -> CartQuantity.of(0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("1");
        }

        @Test
        @DisplayName("수량이 음수이면 예외가 발생한다")
        void negativeQuantityThrowsException() {
            // when & then
            assertThatThrownBy(() -> CartQuantity.of(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("수량이 1000이면 예외가 발생한다")
        void quantityOver999ThrowsException() {
            // when & then
            assertThatThrownBy(() -> CartQuantity.of(1000))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("999");
        }
    }

    @Nested
    @DisplayName("increase() - 수량 증가")
    class IncreaseTest {

        @Test
        @DisplayName("수량을 증가시키면 새 CartQuantity를 반환한다")
        void increaseReturnsNewCartQuantity() {
            // given
            CartQuantity quantity = CartQuantity.of(3);

            // when
            CartQuantity increased = quantity.increase(2);

            // then
            assertThat(increased.value()).isEqualTo(5);
        }

        @Test
        @DisplayName("increase() 후 원본 CartQuantity는 변경되지 않는다")
        void increaseIsImmutable() {
            // given
            CartQuantity quantity = CartQuantity.of(3);

            // when
            quantity.increase(2);

            // then
            assertThat(quantity.value()).isEqualTo(3);
        }

        @Test
        @DisplayName("수량 증가로 999를 초과하면 예외가 발생한다")
        void increaseOverMaxThrowsException() {
            // given
            CartQuantity quantity = CartQuantity.of(998);

            // when & then
            assertThatThrownBy(() -> quantity.increase(2))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("decrease() - 수량 감소")
    class DecreaseTest {

        @Test
        @DisplayName("수량을 감소시키면 새 CartQuantity를 반환한다")
        void decreaseReturnsNewCartQuantity() {
            // given
            CartQuantity quantity = CartQuantity.of(5);

            // when
            CartQuantity decreased = quantity.decrease(2);

            // then
            assertThat(decreased.value()).isEqualTo(3);
        }

        @Test
        @DisplayName("decrease() 후 원본 CartQuantity는 변경되지 않는다")
        void decreaseIsImmutable() {
            // given
            CartQuantity quantity = CartQuantity.of(5);

            // when
            quantity.decrease(2);

            // then
            assertThat(quantity.value()).isEqualTo(5);
        }

        @Test
        @DisplayName("수량 감소로 0 이하가 되면 예외가 발생한다")
        void decreaseBelowMinThrowsException() {
            // given
            CartQuantity quantity = CartQuantity.of(1);

            // when & then
            assertThatThrownBy(() -> quantity.decrease(1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("동등성(equals/hashCode) 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값이면 동등하다")
        void sameValuesAreEqual() {
            // given
            CartQuantity q1 = CartQuantity.of(5);
            CartQuantity q2 = CartQuantity.of(5);

            // then
            assertThat(q1).isEqualTo(q2);
            assertThat(q1.hashCode()).isEqualTo(q2.hashCode());
        }

        @Test
        @DisplayName("다른 값이면 동등하지 않다")
        void differentValuesAreNotEqual() {
            // given
            CartQuantity q1 = CartQuantity.of(3);
            CartQuantity q2 = CartQuantity.of(5);

            // then
            assertThat(q1).isNotEqualTo(q2);
        }
    }
}
