package com.ryuqq.setof.domain.productgroupimage.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupImageId Value Object 테스트")
class ProductGroupImageIdTest {

    @Nested
    @DisplayName("of() - 팩토리 메서드")
    class OfTest {

        @Test
        @DisplayName("유효한 값으로 ProductGroupImageId를 생성한다")
        void createWithValidValue() {
            // when
            ProductGroupImageId id = ProductGroupImageId.of(10L);

            // then
            assertThat(id.value()).isEqualTo(10L);
            assertThat(id.isNew()).isFalse();
        }

        @Test
        @DisplayName("null 값이면 IllegalArgumentException이 발생한다")
        void throwExceptionForNull() {
            // when & then
            assertThatThrownBy(() -> ProductGroupImageId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null");
        }
    }

    @Nested
    @DisplayName("forNew() - 신규 ID 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 ID를 생성하면 value가 null이다")
        void createNewIdHasNullValue() {
            // when
            ProductGroupImageId id = ProductGroupImageId.forNew();

            // then
            assertThat(id.value()).isNull();
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("forNew()로 생성한 ID는 isNew()가 true이다")
        void forNewIsNew() {
            // when & then
            assertThat(ProductGroupImageId.forNew().isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("isNew() - 신규 여부 확인")
    class IsNewTest {

        @Test
        @DisplayName("value가 null이면 신규이다")
        void nullValueIsNew() {
            // given
            ProductGroupImageId id = ProductGroupImageId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("value가 있으면 신규가 아니다")
        void nonNullValueIsNotNew() {
            // given
            ProductGroupImageId id = ProductGroupImageId.of(1L);

            // then
            assertThat(id.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값의 ProductGroupImageId는 동등하다")
        void sameValueEquals() {
            // given
            ProductGroupImageId id1 = ProductGroupImageId.of(100L);
            ProductGroupImageId id2 = ProductGroupImageId.of(100L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값의 ProductGroupImageId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            ProductGroupImageId id1 = ProductGroupImageId.of(100L);
            ProductGroupImageId id2 = ProductGroupImageId.of(200L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }

        @Test
        @DisplayName("두 forNew() ID는 둘 다 null값이므로 동등하다")
        void twoForNewIdsAreEqual() {
            // given
            ProductGroupImageId id1 = ProductGroupImageId.forNew();
            ProductGroupImageId id2 = ProductGroupImageId.forNew();

            // then
            assertThat(id1).isEqualTo(id2);
        }
    }
}
