package com.ryuqq.setof.domain.productstock.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.productstock.exception.InvalidProductStockIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProductStockId Value Object 테스트")
class ProductStockIdTest {

    @Nested
    @DisplayName("of() 테스트")
    class OfTest {

        @Test
        @DisplayName("유효한 ID로 생성 성공")
        void shouldCreateWithValidId() {
            // given
            Long value = 1L;

            // when
            ProductStockId id = ProductStockId.of(value);

            // then
            assertThat(id.value()).isEqualTo(1L);
        }

        @Test
        @DisplayName("null ID로 생성 시 isNew() true 반환")
        void shouldReturnIsNewTrueWhenIdIsNull() {
            // when
            ProductStockId id = ProductStockId.of(null);

            // then
            assertThat(id.value()).isNull();
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("0 이하 ID로 생성 시 예외 발생")
        void shouldThrowWhenIdIsZeroOrNegative() {
            // when & then
            assertThatThrownBy(() -> ProductStockId.of(0L))
                    .isInstanceOf(InvalidProductStockIdException.class);

            assertThatThrownBy(() -> ProductStockId.of(-1L))
                    .isInstanceOf(InvalidProductStockIdException.class);
        }
    }

    @Nested
    @DisplayName("forNew() 테스트")
    class ForNewTest {

        @Test
        @DisplayName("신규 생성용 ID 생성 성공")
        void shouldCreateForNew() {
            // when
            ProductStockId id = ProductStockId.forNew();

            // then
            assertThat(id.value()).isNull();
            assertThat(id.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("isNew() 테스트")
    class IsNewTest {

        @Test
        @DisplayName("forNew()로 생성된 ID는 isNew() true")
        void shouldReturnTrueWhenCreatedWithForNew() {
            // given
            ProductStockId id = ProductStockId.forNew();

            // when & then
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("of()로 생성된 ID는 isNew() false")
        void shouldReturnFalseWhenCreatedWithOf() {
            // given
            ProductStockId id = ProductStockId.of(1L);

            // when & then
            assertThat(id.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값이면 동등")
        void shouldBeEqualWhenSameValue() {
            // given
            ProductStockId id1 = ProductStockId.of(1L);
            ProductStockId id2 = ProductStockId.of(1L);

            // when & then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값이면 동등하지 않음")
        void shouldNotBeEqualWhenDifferentValue() {
            // given
            ProductStockId id1 = ProductStockId.of(1L);
            ProductStockId id2 = ProductStockId.of(2L);

            // when & then
            assertThat(id1).isNotEqualTo(id2);
        }
    }
}
