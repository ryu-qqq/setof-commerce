package com.ryuqq.setof.domain.product.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductId 테스트")
class ProductIdTest {

    @Nested
    @DisplayName("of() - ID 생성")
    class OfTest {

        @Test
        @DisplayName("유효한 값으로 ProductId를 생성한다")
        void createWithValidValue() {
            // when
            ProductId productId = ProductId.of(1L);

            // then
            assertThat(productId.value()).isEqualTo(1L);
        }

        @Test
        @DisplayName("null 값으로 생성하면 IllegalArgumentException이 발생한다")
        void throwExceptionForNullValue() {
            // when & then
            assertThatThrownBy(() -> ProductId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("ProductId 값은 null일 수 없습니다");
        }
    }

    @Nested
    @DisplayName("forNew() - 신규 ID 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 ID는 null 값을 가진다")
        void forNewHasNullValue() {
            // when
            ProductId productId = ProductId.forNew();

            // then
            assertThat(productId.value()).isNull();
        }
    }

    @Nested
    @DisplayName("isNew() - 신규 여부 확인")
    class IsNewTest {

        @Test
        @DisplayName("forNew()로 생성한 ID는 isNew()가 true이다")
        void isNewReturnsTrueForNewId() {
            // given
            ProductId productId = ProductId.forNew();

            // then
            assertThat(productId.isNew()).isTrue();
        }

        @Test
        @DisplayName("of()로 생성한 ID는 isNew()가 false이다")
        void isNewReturnsFalseForExistingId() {
            // given
            ProductId productId = ProductId.of(1L);

            // then
            assertThat(productId.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ProductId는 동일하다")
        void equalWithSameValue() {
            // given
            ProductId id1 = ProductId.of(1L);
            ProductId id2 = ProductId.of(1L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 ProductId는 동일하지 않다")
        void notEqualWithDifferentValue() {
            // given
            ProductId id1 = ProductId.of(1L);
            ProductId id2 = ProductId.of(2L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }
    }
}
