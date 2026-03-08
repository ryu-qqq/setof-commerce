package com.ryuqq.setof.domain.cart.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CartItemId Value Object 단위 테스트")
class CartItemIdTest {

    @Nested
    @DisplayName("of() - 영속 ID 생성")
    class OfTest {

        @Test
        @DisplayName("유효한 Long 값으로 CartItemId를 생성한다")
        void createWithValidLongValue() {
            // when
            CartItemId id = CartItemId.of(1L);

            // then
            assertThat(id.value()).isEqualTo(1L);
            assertThat(id.isNew()).isFalse();
        }

        @Test
        @DisplayName("큰 Long 값으로도 생성할 수 있다")
        void createWithLargeLongValue() {
            // when
            CartItemId id = CartItemId.of(Long.MAX_VALUE);

            // then
            assertThat(id.value()).isEqualTo(Long.MAX_VALUE);
        }

        @Test
        @DisplayName("null 값으로 of()를 호출하면 예외가 발생한다")
        void nullValueThrowsException() {
            // when & then
            assertThatThrownBy(() -> CartItemId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("CartItemId");
        }
    }

    @Nested
    @DisplayName("forNew() - 신규 ID 생성")
    class ForNewTest {

        @Test
        @DisplayName("forNew()로 생성하면 value가 null이다")
        void forNewHasNullValue() {
            // when
            CartItemId id = CartItemId.forNew();

            // then
            assertThat(id.value()).isNull();
        }

        @Test
        @DisplayName("forNew()로 생성하면 isNew()가 true이다")
        void forNewIsNew() {
            // when
            CartItemId id = CartItemId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("isNew() - 신규 여부 확인")
    class IsNewTest {

        @Test
        @DisplayName("value가 null이면 isNew()가 true이다")
        void isNewReturnsTrueWhenValueIsNull() {
            // given
            CartItemId id = CartItemId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("value가 있으면 isNew()가 false이다")
        void isNewReturnsFalseWhenValueExists() {
            // given
            CartItemId id = CartItemId.of(100L);

            // then
            assertThat(id.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성(equals/hashCode) 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값이면 동등하다")
        void sameValuesAreEqual() {
            // given
            CartItemId id1 = CartItemId.of(1L);
            CartItemId id2 = CartItemId.of(1L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값이면 동등하지 않다")
        void differentValuesAreNotEqual() {
            // given
            CartItemId id1 = CartItemId.of(1L);
            CartItemId id2 = CartItemId.of(2L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }

        @Test
        @DisplayName("forNew()로 생성한 두 ID는 동등하다 (value가 모두 null)")
        void twoNewIdsAreEqual() {
            // given
            CartItemId id1 = CartItemId.forNew();
            CartItemId id2 = CartItemId.forNew();

            // then
            assertThat(id1).isEqualTo(id2);
        }
    }
}
