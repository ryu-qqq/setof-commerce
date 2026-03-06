package com.ryuqq.setof.domain.productdescription.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DescriptionImageId ID VO 테스트")
class DescriptionImageIdTest {

    @Nested
    @DisplayName("of() - ID 생성")
    class OfTest {

        @Test
        @DisplayName("유효한 값으로 ID를 생성한다")
        void createValidId() {
            // when
            DescriptionImageId id = DescriptionImageId.of(1L);

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
            DescriptionImageId id = DescriptionImageId.forNew();

            // then
            assertThat(id.value()).isNull();
        }

        @Test
        @DisplayName("신규 ID는 isNew()가 true이다")
        void forNewIsNew() {
            // when
            DescriptionImageId id = DescriptionImageId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("isNew() - 신규 여부 확인")
    class IsNewTest {

        @Test
        @DisplayName("of()로 생성한 ID는 isNew()가 false이다")
        void ofIsNotNew() {
            // when
            DescriptionImageId id = DescriptionImageId.of(1L);

            // then
            assertThat(id.isNew()).isFalse();
        }

        @Test
        @DisplayName("forNew()로 생성한 ID는 isNew()가 true이다")
        void forNewIsNew() {
            // when
            DescriptionImageId id = DescriptionImageId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값의 ID는 동등하다")
        void equalValues() {
            // given
            DescriptionImageId id1 = DescriptionImageId.of(1L);
            DescriptionImageId id2 = DescriptionImageId.of(1L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값의 ID는 동등하지 않다")
        void differentValuesAreNotEqual() {
            // given
            DescriptionImageId id1 = DescriptionImageId.of(1L);
            DescriptionImageId id2 = DescriptionImageId.of(2L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }

        @Test
        @DisplayName("forNew()로 생성한 두 ID는 동등하다")
        void twoForNewIdsAreEqual() {
            // given
            DescriptionImageId id1 = DescriptionImageId.forNew();
            DescriptionImageId id2 = DescriptionImageId.forNew();

            // then
            assertThat(id1).isEqualTo(id2);
        }
    }
}
