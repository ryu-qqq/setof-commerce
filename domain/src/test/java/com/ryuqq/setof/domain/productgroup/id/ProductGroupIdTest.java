package com.ryuqq.setof.domain.productgroup.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroup ID 테스트")
class ProductGroupIdTest {

    @Nested
    @DisplayName("ProductGroupId 테스트")
    class ProductGroupIdInnerTest {

        @Nested
        @DisplayName("of() - 팩토리 메서드")
        class OfTest {

            @Test
            @DisplayName("유효한 값으로 ProductGroupId를 생성한다")
            void createWithValidValue() {
                // when
                var id = ProductGroupId.of(1L);

                // then
                assertThat(id.value()).isEqualTo(1L);
                assertThat(id.isNew()).isFalse();
            }

            @Test
            @DisplayName("null 값이면 예외가 발생한다")
            void throwExceptionForNull() {
                assertThatThrownBy(() -> ProductGroupId.of(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("null");
            }
        }

        @Nested
        @DisplayName("forNew() - 신규 ID 생성")
        class ForNewTest {

            @Test
            @DisplayName("신규 ID를 생성한다")
            void createNewId() {
                // when
                var id = ProductGroupId.forNew();

                // then
                assertThat(id.value()).isNull();
                assertThat(id.isNew()).isTrue();
            }
        }

        @Nested
        @DisplayName("isNew() - 신규 여부 확인")
        class IsNewTest {

            @Test
            @DisplayName("forNew()로 생성한 ID는 신규이다")
            void forNewIsNew() {
                assertThat(ProductGroupId.forNew().isNew()).isTrue();
            }

            @Test
            @DisplayName("of()로 생성한 ID는 신규가 아니다")
            void ofIsNotNew() {
                assertThat(ProductGroupId.of(1L).isNew()).isFalse();
            }
        }

        @Nested
        @DisplayName("동등성 테스트")
        class EqualityTest {

            @Test
            @DisplayName("같은 값의 ProductGroupId는 동등하다")
            void equalIds() {
                var id1 = ProductGroupId.of(1L);
                var id2 = ProductGroupId.of(1L);
                assertThat(id1).isEqualTo(id2);
            }

            @Test
            @DisplayName("다른 값의 ProductGroupId는 동등하지 않다")
            void notEqualIds() {
                var id1 = ProductGroupId.of(1L);
                var id2 = ProductGroupId.of(2L);
                assertThat(id1).isNotEqualTo(id2);
            }
        }
    }

    @Nested
    @DisplayName("ProductGroupImageId 테스트")
    class ProductGroupImageIdInnerTest {

        @Nested
        @DisplayName("of() - 팩토리 메서드")
        class OfTest {

            @Test
            @DisplayName("유효한 값으로 ProductGroupImageId를 생성한다")
            void createWithValidValue() {
                // when
                var id = ProductGroupImageId.of(1L);

                // then
                assertThat(id.value()).isEqualTo(1L);
                assertThat(id.isNew()).isFalse();
            }

            @Test
            @DisplayName("null 값이면 예외가 발생한다")
            void throwExceptionForNull() {
                assertThatThrownBy(() -> ProductGroupImageId.of(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("null");
            }
        }

        @Nested
        @DisplayName("forNew() - 신규 ID 생성")
        class ForNewTest {

            @Test
            @DisplayName("신규 ID를 생성한다")
            void createNewId() {
                // when
                var id = ProductGroupImageId.forNew();

                // then
                assertThat(id.value()).isNull();
                assertThat(id.isNew()).isTrue();
            }
        }

        @Nested
        @DisplayName("isNew() - 신규 여부 확인")
        class IsNewTest {

            @Test
            @DisplayName("forNew()로 생성한 ID는 신규이다")
            void forNewIsNew() {
                assertThat(ProductGroupImageId.forNew().isNew()).isTrue();
            }

            @Test
            @DisplayName("of()로 생성한 ID는 신규가 아니다")
            void ofIsNotNew() {
                assertThat(ProductGroupImageId.of(1L).isNew()).isFalse();
            }
        }

        @Nested
        @DisplayName("동등성 테스트")
        class EqualityTest {

            @Test
            @DisplayName("같은 값의 ProductGroupImageId는 동등하다")
            void equalIds() {
                var id1 = ProductGroupImageId.of(1L);
                var id2 = ProductGroupImageId.of(1L);
                assertThat(id1).isEqualTo(id2);
            }

            @Test
            @DisplayName("다른 값의 ProductGroupImageId는 동등하지 않다")
            void notEqualIds() {
                var id1 = ProductGroupImageId.of(1L);
                var id2 = ProductGroupImageId.of(2L);
                assertThat(id1).isNotEqualTo(id2);
            }
        }
    }
}
