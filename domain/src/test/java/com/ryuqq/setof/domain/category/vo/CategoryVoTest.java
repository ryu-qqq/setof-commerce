package com.ryuqq.setof.domain.category.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@Tag("unit")
@DisplayName("Category VO 테스트")
class CategoryVoTest {

    @Nested
    @DisplayName("CategoryName 테스트")
    class CategoryNameTest {

        @Test
        @DisplayName("유효한 카테고리명을 생성한다")
        void createValidCategoryName() {
            var categoryName = CategoryName.of("테스트카테고리");
            assertThat(categoryName.value()).isEqualTo("테스트카테고리");
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            var categoryName = CategoryName.of("  테스트카테고리  ");
            assertThat(categoryName.value()).isEqualTo("테스트카테고리");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> CategoryName.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("카테고리명");
        }

        @Test
        @DisplayName("50자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longName = "가".repeat(51);
            assertThatThrownBy(() -> CategoryName.of(longName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("50");
        }
    }

    @Nested
    @DisplayName("CategoryDisplayName 테스트")
    class CategoryDisplayNameTest {

        @Test
        @DisplayName("유효한 표시명을 생성한다")
        void createValidDisplayName() {
            var displayName = CategoryDisplayName.of("테스트 표시명");
            assertThat(displayName.value()).isEqualTo("테스트 표시명");
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            var displayName = CategoryDisplayName.of("  테스트 표시명  ");
            assertThat(displayName.value()).isEqualTo("테스트 표시명");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> CategoryDisplayName.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("카테고리 표시명");
        }

        @Test
        @DisplayName("50자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longName = "가".repeat(51);
            assertThatThrownBy(() -> CategoryDisplayName.of(longName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("50");
        }
    }

    @Nested
    @DisplayName("CategoryDepth 테스트")
    class CategoryDepthTest {

        @Test
        @DisplayName("유효한 카테고리 깊이를 생성한다")
        void createValidCategoryDepth() {
            var depth = CategoryDepth.of(1);
            assertThat(depth.value()).isEqualTo(1);
        }

        @Test
        @DisplayName("root()는 1을 반환한다")
        void rootReturnsOne() {
            var depth = CategoryDepth.root();
            assertThat(depth.value()).isEqualTo(1);
        }

        @Test
        @DisplayName("10은 유효한 값이다")
        void tenIsValid() {
            var depth = CategoryDepth.of(10);
            assertThat(depth.value()).isEqualTo(10);
        }

        @Test
        @DisplayName("0이면 예외가 발생한다")
        void throwExceptionForZero() {
            assertThatThrownBy(() -> CategoryDepth.of(0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("카테고리 깊이");
        }

        @Test
        @DisplayName("음수 값이면 예외가 발생한다")
        void throwExceptionForNegative() {
            assertThatThrownBy(() -> CategoryDepth.of(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("카테고리 깊이");
        }

        @Test
        @DisplayName("11 이상이면 예외가 발생한다")
        void throwExceptionForTooLarge() {
            assertThatThrownBy(() -> CategoryDepth.of(11))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("카테고리 깊이");
        }
    }

    @Nested
    @DisplayName("CategoryPath 테스트")
    class CategoryPathTest {

        @Test
        @DisplayName("유효한 경로를 생성한다")
        void createValidPath() {
            var path = CategoryPath.of("/1/2/3");
            assertThat(path.value()).isEqualTo("/1/2/3");
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            var path = CategoryPath.of("  /1/2  ");
            assertThat(path.value()).isEqualTo("/1/2");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> CategoryPath.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("카테고리 경로");
        }

        @Test
        @DisplayName("500자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longPath = "/" + "1/".repeat(250);
            assertThatThrownBy(() -> CategoryPath.of(longPath))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("500");
        }
    }

    @Nested
    @DisplayName("CategoryType 테스트")
    class CategoryTypeTest {

        @Test
        @DisplayName("모든 CategoryType 값이 존재한다")
        void allValuesExist() {
            assertThat(CategoryType.values())
                    .containsExactly(
                            CategoryType.NONE,
                            CategoryType.CLOTHING,
                            CategoryType.SHOSE,
                            CategoryType.BAG,
                            CategoryType.ACC);
        }
    }

    @Nested
    @DisplayName("TargetGroup 테스트")
    class TargetGroupTest {

        @Test
        @DisplayName("모든 TargetGroup 값이 존재한다")
        void allValuesExist() {
            assertThat(TargetGroup.values())
                    .containsExactly(
                            TargetGroup.MALE,
                            TargetGroup.FEMALE,
                            TargetGroup.KIDS,
                            TargetGroup.LIFE);
        }
    }
}
