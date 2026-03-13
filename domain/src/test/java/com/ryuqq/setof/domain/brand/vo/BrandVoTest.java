package com.ryuqq.setof.domain.brand.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@Tag("unit")
@DisplayName("Brand VO 테스트")
class BrandVoTest {

    @Nested
    @DisplayName("BrandName 테스트")
    class BrandNameTest {

        @Test
        @DisplayName("유효한 브랜드명을 생성한다")
        void createValidBrandName() {
            var brandName = BrandName.of("테스트브랜드");
            assertThat(brandName.value()).isEqualTo("테스트브랜드");
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            var brandName = BrandName.of("  테스트브랜드  ");
            assertThat(brandName.value()).isEqualTo("테스트브랜드");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> BrandName.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("브랜드명");
        }

        @Test
        @DisplayName("50자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longName = "가".repeat(51);
            assertThatThrownBy(() -> BrandName.of(longName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("50");
        }
    }

    @Nested
    @DisplayName("BrandIconImageUrl 테스트")
    class BrandIconImageUrlTest {

        @Test
        @DisplayName("유효한 URL을 생성한다")
        void createValidUrl() {
            var url = BrandIconImageUrl.of("https://example.com/icon.png");
            assertThat(url.value()).isEqualTo("https://example.com/icon.png");
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            var url = BrandIconImageUrl.of("  https://example.com/icon.png  ");
            assertThat(url.value()).isEqualTo("https://example.com/icon.png");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> BrandIconImageUrl.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("브랜드 아이콘 이미지 URL");
        }

        @Test
        @DisplayName("255자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longUrl = "https://example.com/" + "a".repeat(250);
            assertThatThrownBy(() -> BrandIconImageUrl.of(longUrl))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("255");
        }
    }

    @Nested
    @DisplayName("DisplayKoreanName 테스트")
    class DisplayKoreanNameTest {

        @Test
        @DisplayName("유효한 한글 표시명을 생성한다")
        void createValidDisplayKoreanName() {
            var name = DisplayKoreanName.of("나이키");
            assertThat(name.value()).isEqualTo("나이키");
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            var name = DisplayKoreanName.of("  나이키  ");
            assertThat(name.value()).isEqualTo("나이키");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> DisplayKoreanName.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("한글 표시명");
        }

        @Test
        @DisplayName("100자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longName = "가".repeat(101);
            assertThatThrownBy(() -> DisplayKoreanName.of(longName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("100");
        }
    }

    @Nested
    @DisplayName("DisplayEnglishName 테스트")
    class DisplayEnglishNameTest {

        @Test
        @DisplayName("유효한 영문 표시명을 생성한다")
        void createValidDisplayEnglishName() {
            var name = DisplayEnglishName.of("NIKE");
            assertThat(name.value()).isEqualTo("NIKE");
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            var name = DisplayEnglishName.of("  NIKE  ");
            assertThat(name.value()).isEqualTo("NIKE");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> DisplayEnglishName.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("영문 표시명");
        }

        @Test
        @DisplayName("100자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longName = "a".repeat(101);
            assertThatThrownBy(() -> DisplayEnglishName.of(longName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("100");
        }
    }

    @Nested
    @DisplayName("DisplayOrder 테스트")
    class DisplayOrderTest {

        @Test
        @DisplayName("유효한 표시 순서를 생성한다")
        void createValidDisplayOrder() {
            var order = DisplayOrder.of(1);
            assertThat(order.value()).isEqualTo(1);
        }

        @Test
        @DisplayName("0은 유효한 값이다")
        void zeroIsValid() {
            var order = DisplayOrder.of(0);
            assertThat(order.value()).isZero();
        }

        @Test
        @DisplayName("기본 순서는 0이다")
        void defaultOrderIsZero() {
            var order = DisplayOrder.defaultOrder();
            assertThat(order.value()).isZero();
        }

        @Test
        @DisplayName("음수 값이면 예외가 발생한다")
        void throwExceptionForNegative() {
            assertThatThrownBy(() -> DisplayOrder.of(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("표시 순서");
        }

        @Test
        @DisplayName("9999 초과 시 예외가 발생한다")
        void throwExceptionForTooLarge() {
            assertThatThrownBy(() -> DisplayOrder.of(10000))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("표시 순서");
        }

        @Test
        @DisplayName("동등한 순서는 같다")
        void equalityTest() {
            var order1 = DisplayOrder.of(5);
            var order2 = DisplayOrder.of(5);
            assertThat(order1).isEqualTo(order2);
        }
    }
}
