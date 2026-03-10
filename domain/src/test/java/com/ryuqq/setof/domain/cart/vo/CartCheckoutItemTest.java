package com.ryuqq.setof.domain.cart.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CartCheckoutItem Value Object 단위 테스트")
class CartCheckoutItemTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("양수의 cartId와 userId로 생성한다")
        void createWithValidValues() {
            // when
            CartCheckoutItem item = new CartCheckoutItem(1L, 2L);

            // then
            assertThat(item.cartId()).isEqualTo(1L);
            assertThat(item.userId()).isEqualTo(2L);
        }

        @Test
        @DisplayName("cartId가 0이면 예외가 발생한다")
        void createWithZeroCartId_ThrowsException() {
            assertThatThrownBy(() -> new CartCheckoutItem(0L, 1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cartId must be positive");
        }

        @Test
        @DisplayName("cartId가 음수이면 예외가 발생한다")
        void createWithNegativeCartId_ThrowsException() {
            assertThatThrownBy(() -> new CartCheckoutItem(-1L, 1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cartId must be positive");
        }

        @Test
        @DisplayName("userId가 0이면 예외가 발생한다")
        void createWithZeroUserId_ThrowsException() {
            assertThatThrownBy(() -> new CartCheckoutItem(1L, 0L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("userId must be positive");
        }

        @Test
        @DisplayName("userId가 음수이면 예외가 발생한다")
        void createWithNegativeUserId_ThrowsException() {
            assertThatThrownBy(() -> new CartCheckoutItem(1L, -1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("userId must be positive");
        }

        @Test
        @DisplayName("cartId와 userId 모두 0이면 예외가 발생한다")
        void createWithBothZero_ThrowsException() {
            assertThatThrownBy(() -> new CartCheckoutItem(0L, 0L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 cartId와 userId는 동일하다")
        void sameValuesAreEqual() {
            // given
            CartCheckoutItem item1 = new CartCheckoutItem(1L, 2L);
            CartCheckoutItem item2 = new CartCheckoutItem(1L, 2L);

            // then
            assertThat(item1).isEqualTo(item2);
            assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
        }

        @Test
        @DisplayName("다른 cartId는 동일하지 않다")
        void differentCartIdNotEqual() {
            // given
            CartCheckoutItem item1 = new CartCheckoutItem(1L, 2L);
            CartCheckoutItem item2 = new CartCheckoutItem(3L, 2L);

            // then
            assertThat(item1).isNotEqualTo(item2);
        }

        @Test
        @DisplayName("다른 userId는 동일하지 않다")
        void differentUserIdNotEqual() {
            // given
            CartCheckoutItem item1 = new CartCheckoutItem(1L, 2L);
            CartCheckoutItem item2 = new CartCheckoutItem(1L, 99L);

            // then
            assertThat(item1).isNotEqualTo(item2);
        }
    }
}
