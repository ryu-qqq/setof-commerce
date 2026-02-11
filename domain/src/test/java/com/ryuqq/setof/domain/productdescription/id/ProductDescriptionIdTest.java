package com.ryuqq.setof.domain.productdescription.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductDescription ID 테스트")
class ProductDescriptionIdTest {

    @Nested
    @DisplayName("ProductGroupDescriptionId 테스트")
    class ProductGroupDescriptionIdTest {

        @Nested
        @DisplayName("of() - ID 생성")
        class OfTest {

            @Test
            @DisplayName("유효한 값으로 ID를 생성한다")
            void createValidId() {
                // when
                var id = ProductGroupDescriptionId.of(1L);

                // then
                assertThat(id.value()).isEqualTo(1L);
            }

            @Test
            @DisplayName("null 값이면 예외가 발생한다")
            void throwExceptionForNull() {
                // when & then
                assertThatThrownBy(() -> ProductGroupDescriptionId.of(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("ProductGroupDescriptionId");
            }
        }

        @Nested
        @DisplayName("forNew() - 신규 ID 생성")
        class ForNewTest {

            @Test
            @DisplayName("신규 ID는 null 값을 가진다")
            void forNewHasNullValue() {
                // when
                var id = ProductGroupDescriptionId.forNew();

                // then
                assertThat(id.value()).isNull();
            }
        }

        @Nested
        @DisplayName("isNew() - 신규 여부 확인")
        class IsNewTest {

            @Test
            @DisplayName("forNew()로 생성한 ID는 isNew가 true이다")
            void forNewIsNew() {
                // when
                var id = ProductGroupDescriptionId.forNew();

                // then
                assertThat(id.isNew()).isTrue();
            }

            @Test
            @DisplayName("of()로 생성한 ID는 isNew가 false이다")
            void ofIsNotNew() {
                // when
                var id = ProductGroupDescriptionId.of(1L);

                // then
                assertThat(id.isNew()).isFalse();
            }
        }

        @Nested
        @DisplayName("동등성 테스트")
        class EqualityTest {

            @Test
            @DisplayName("같은 값의 ID는 동등하다")
            void equalValues() {
                // given
                var id1 = ProductGroupDescriptionId.of(1L);
                var id2 = ProductGroupDescriptionId.of(1L);

                // then
                assertThat(id1).isEqualTo(id2);
            }

            @Test
            @DisplayName("다른 값의 ID는 동등하지 않다")
            void differentValues() {
                // given
                var id1 = ProductGroupDescriptionId.of(1L);
                var id2 = ProductGroupDescriptionId.of(2L);

                // then
                assertThat(id1).isNotEqualTo(id2);
            }
        }
    }

    @Nested
    @DisplayName("DescriptionImageId 테스트")
    class DescriptionImageIdTest {

        @Nested
        @DisplayName("of() - ID 생성")
        class OfTest {

            @Test
            @DisplayName("유효한 값으로 ID를 생성한다")
            void createValidId() {
                // when
                var id = DescriptionImageId.of(1L);

                // then
                assertThat(id.value()).isEqualTo(1L);
            }

            @Test
            @DisplayName("null 값이면 예외가 발생한다")
            void throwExceptionForNull() {
                // when & then
                assertThatThrownBy(() -> DescriptionImageId.of(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("DescriptionImageId");
            }
        }

        @Nested
        @DisplayName("forNew() - 신규 ID 생성")
        class ForNewTest {

            @Test
            @DisplayName("신규 ID는 null 값을 가진다")
            void forNewHasNullValue() {
                // when
                var id = DescriptionImageId.forNew();

                // then
                assertThat(id.value()).isNull();
            }
        }

        @Nested
        @DisplayName("isNew() - 신규 여부 확인")
        class IsNewTest {

            @Test
            @DisplayName("forNew()로 생성한 ID는 isNew가 true이다")
            void forNewIsNew() {
                // when
                var id = DescriptionImageId.forNew();

                // then
                assertThat(id.isNew()).isTrue();
            }

            @Test
            @DisplayName("of()로 생성한 ID는 isNew가 false이다")
            void ofIsNotNew() {
                // when
                var id = DescriptionImageId.of(1L);

                // then
                assertThat(id.isNew()).isFalse();
            }
        }

        @Nested
        @DisplayName("동등성 테스트")
        class EqualityTest {

            @Test
            @DisplayName("같은 값의 ID는 동등하다")
            void equalValues() {
                // given
                var id1 = DescriptionImageId.of(1L);
                var id2 = DescriptionImageId.of(1L);

                // then
                assertThat(id1).isEqualTo(id2);
            }

            @Test
            @DisplayName("다른 값의 ID는 동등하지 않다")
            void differentValues() {
                // given
                var id1 = DescriptionImageId.of(1L);
                var id2 = DescriptionImageId.of(2L);

                // then
                assertThat(id1).isNotEqualTo(id2);
            }
        }
    }
}
