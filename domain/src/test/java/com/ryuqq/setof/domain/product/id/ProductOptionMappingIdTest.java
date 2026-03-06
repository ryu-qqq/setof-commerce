package com.ryuqq.setof.domain.product.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductOptionMappingId 테스트")
class ProductOptionMappingIdTest {

    @Nested
    @DisplayName("of() - ID 생성")
    class OfTest {

        @Test
        @DisplayName("유효한 값으로 ProductOptionMappingId를 생성한다")
        void createWithValidValue() {
            // when
            ProductOptionMappingId id = ProductOptionMappingId.of(1L);

            // then
            assertThat(id.value()).isEqualTo(1L);
        }

        @Test
        @DisplayName("null 값으로 생성하면 IllegalArgumentException이 발생한다")
        void throwExceptionForNullValue() {
            // when & then
            assertThatThrownBy(() -> ProductOptionMappingId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("ProductOptionMappingId 값은 null일 수 없습니다");
        }

        @Test
        @DisplayName("큰 값으로도 생성할 수 있다")
        void createWithLargeValue() {
            // when
            ProductOptionMappingId id = ProductOptionMappingId.of(Long.MAX_VALUE);

            // then
            assertThat(id.value()).isEqualTo(Long.MAX_VALUE);
        }
    }

    @Nested
    @DisplayName("forNew() - 신규 ID 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 ID는 null 값을 가진다")
        void forNewHasNullValue() {
            // when
            ProductOptionMappingId id = ProductOptionMappingId.forNew();

            // then
            assertThat(id.value()).isNull();
        }

        @Test
        @DisplayName("신규 ID는 isNew()가 true이다")
        void forNewIsNew() {
            // when
            ProductOptionMappingId id = ProductOptionMappingId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("isNew() - 신규 여부 확인")
    class IsNewTest {

        @Test
        @DisplayName("forNew()로 생성한 ID는 isNew()가 true이다")
        void isNewReturnsTrueForNewId() {
            // given
            ProductOptionMappingId id = ProductOptionMappingId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("of()로 생성한 ID는 isNew()가 false이다")
        void isNewReturnsFalseForExistingId() {
            // given
            ProductOptionMappingId id = ProductOptionMappingId.of(1L);

            // then
            assertThat(id.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ProductOptionMappingId는 동일하다")
        void equalWithSameValue() {
            // given
            ProductOptionMappingId id1 = ProductOptionMappingId.of(1L);
            ProductOptionMappingId id2 = ProductOptionMappingId.of(1L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 ProductOptionMappingId는 동일하지 않다")
        void notEqualWithDifferentValue() {
            // given
            ProductOptionMappingId id1 = ProductOptionMappingId.of(1L);
            ProductOptionMappingId id2 = ProductOptionMappingId.of(2L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }

        @Test
        @DisplayName("forNew()로 생성한 두 ID는 동일하다 (둘 다 null)")
        void forNewIdsAreEqual() {
            // given
            ProductOptionMappingId id1 = ProductOptionMappingId.forNew();
            ProductOptionMappingId id2 = ProductOptionMappingId.forNew();

            // then
            assertThat(id1).isEqualTo(id2);
        }
    }
}
