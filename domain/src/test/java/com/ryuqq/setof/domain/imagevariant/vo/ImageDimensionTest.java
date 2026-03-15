package com.ryuqq.setof.domain.imagevariant.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ImageDimension Value Object 테스트")
class ImageDimensionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("너비와 높이로 ImageDimension을 생성한다")
        void createWithWidthAndHeight() {
            // when
            ImageDimension dimension = ImageDimension.of(600, 600);

            // then
            assertThat(dimension.width()).isEqualTo(600);
            assertThat(dimension.height()).isEqualTo(600);
        }

        @Test
        @DisplayName("null 너비와 높이로 생성한다 (ORIGINAL_WEBP 케이스)")
        void createWithNullWidthAndHeight() {
            // when
            ImageDimension dimension = ImageDimension.of(null, null);

            // then
            assertThat(dimension.width()).isNull();
            assertThat(dimension.height()).isNull();
        }

        @Test
        @DisplayName("너비만 null인 경우도 생성 가능하다")
        void createWithNullWidthOnly() {
            // when
            ImageDimension dimension = ImageDimension.of(null, 600);

            // then
            assertThat(dimension.width()).isNull();
            assertThat(dimension.height()).isEqualTo(600);
        }
    }

    @Nested
    @DisplayName("hasValues() - 유효한 크기 여부 확인")
    class HasValuesTest {

        @Test
        @DisplayName("너비와 높이가 모두 있으면 hasValues()가 true이다")
        void hasValuesTrueWhenBothPresent() {
            ImageDimension dimension = ImageDimension.of(300, 300);
            assertThat(dimension.hasValues()).isTrue();
        }

        @Test
        @DisplayName("너비가 null이면 hasValues()가 false이다")
        void hasValuesFalseWhenWidthIsNull() {
            ImageDimension dimension = ImageDimension.of(null, 300);
            assertThat(dimension.hasValues()).isFalse();
        }

        @Test
        @DisplayName("높이가 null이면 hasValues()가 false이다")
        void hasValuesFalseWhenHeightIsNull() {
            ImageDimension dimension = ImageDimension.of(300, null);
            assertThat(dimension.hasValues()).isFalse();
        }

        @Test
        @DisplayName("둘 다 null이면 hasValues()가 false이다")
        void hasValuesFalseWhenBothNull() {
            ImageDimension dimension = ImageDimension.of(null, null);
            assertThat(dimension.hasValues()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 너비와 높이면 동일하다")
        void sameWidthAndHeightAreEqual() {
            // given
            ImageDimension d1 = ImageDimension.of(600, 600);
            ImageDimension d2 = ImageDimension.of(600, 600);

            // then
            assertThat(d1).isEqualTo(d2);
            assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
        }

        @Test
        @DisplayName("다른 너비이면 동일하지 않다")
        void differentWidthNotEqual() {
            // given
            ImageDimension d1 = ImageDimension.of(300, 300);
            ImageDimension d2 = ImageDimension.of(600, 300);

            // then
            assertThat(d1).isNotEqualTo(d2);
        }

        @Test
        @DisplayName("null 값을 가진 dimension은 서로 동일하다")
        void nullDimensionsAreEqual() {
            // given
            ImageDimension d1 = ImageDimension.of(null, null);
            ImageDimension d2 = ImageDimension.of(null, null);

            // then
            assertThat(d1).isEqualTo(d2);
        }
    }
}
