package com.ryuqq.setof.domain.cart.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.cart.exception.InvalidCartItemIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("CartItemId VO")
class CartItemIdTest {

    @Nested
    @DisplayName("of() - 생성")
    class Of {

        @Test
        @DisplayName("양수 값으로 CartItemId를 생성할 수 있다")
        void shouldCreateWithPositiveValue() {
            // when
            CartItemId id = CartItemId.of(1L);

            // then
            assertEquals(1L, id.value());
        }

        @Test
        @DisplayName("null 값으로 생성 시 예외가 발생한다")
        void shouldThrowExceptionWhenValueIsNull() {
            // when & then
            assertThrows(InvalidCartItemIdException.class, () -> CartItemId.of(null));
        }

        @Test
        @DisplayName("0 이하의 값으로 생성 시 예외가 발생한다")
        void shouldThrowExceptionWhenValueIsNotPositive() {
            // when & then
            assertThrows(InvalidCartItemIdException.class, () -> CartItemId.of(0L));
            assertThrows(InvalidCartItemIdException.class, () -> CartItemId.of(-1L));
        }
    }

    @Nested
    @DisplayName("equals/hashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("같은 값을 가진 CartItemId는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // given
            CartItemId id1 = CartItemId.of(1L);
            CartItemId id2 = CartItemId.of(1L);

            // then
            assertEquals(id1, id2);
            assertEquals(id1.hashCode(), id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 CartItemId는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // given
            CartItemId id1 = CartItemId.of(1L);
            CartItemId id2 = CartItemId.of(2L);

            // then
            assertNotEquals(id1, id2);
        }
    }
}
