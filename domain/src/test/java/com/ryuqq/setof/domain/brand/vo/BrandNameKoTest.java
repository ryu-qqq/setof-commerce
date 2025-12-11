package com.ryuqq.setof.domain.brand.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.brand.exception.InvalidBrandNameKoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * BrandNameKo Value Object 테스트
 *
 * <p>한글 브랜드명 (필수, 최대 255자)
 */
@DisplayName("BrandNameKo Value Object")
class BrandNameKoTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 한글명으로 BrandNameKo 생성")
        void shouldCreateBrandNameKoWithValidValue() {
            // Given
            String validName = "나이키";

            // When
            BrandNameKo brandNameKo = BrandNameKo.of(validName);

            // Then
            assertNotNull(brandNameKo);
            assertEquals(validName, brandNameKo.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 한글명으로 생성")
        @ValueSource(strings = {"가", "나이키", "아디다스 코리아", "Nike Korea 나이키"})
        void shouldCreateBrandNameKoWithVariousValidValues(String validName) {
            // When
            BrandNameKo brandNameKo = BrandNameKo.of(validName);

            // Then
            assertNotNull(brandNameKo);
            assertEquals(validName, brandNameKo.value());
        }

        @Test
        @DisplayName("최대 길이(255자) 한글명으로 생성")
        void shouldCreateBrandNameKoWithMaxLength() {
            // Given
            String maxLengthName = "가".repeat(255);

            // When
            BrandNameKo brandNameKo = BrandNameKo.of(maxLengthName);

            // Then
            assertNotNull(brandNameKo);
            assertEquals(255, brandNameKo.value().length());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsNull() {
            // Given
            String nullName = null;

            // When & Then
            assertThrows(InvalidBrandNameKoException.class, () -> BrandNameKo.of(nullName));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsEmpty() {
            // Given
            String emptyName = "";

            // When & Then
            assertThrows(InvalidBrandNameKoException.class, () -> BrandNameKo.of(emptyName));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsBlank() {
            // Given
            String blankName = "   ";

            // When & Then
            assertThrows(InvalidBrandNameKoException.class, () -> BrandNameKo.of(blankName));
        }

        @Test
        @DisplayName("255자 초과 한글명으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameExceedsMaxLength() {
            // Given
            String tooLongName = "가".repeat(256);

            // When & Then
            assertThrows(InvalidBrandNameKoException.class, () -> BrandNameKo.of(tooLongName));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 BrandNameKo는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            BrandNameKo name1 = BrandNameKo.of("나이키");
            BrandNameKo name2 = BrandNameKo.of("나이키");

            // When & Then
            assertEquals(name1, name2);
            assertEquals(name1.hashCode(), name2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 BrandNameKo는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            BrandNameKo name1 = BrandNameKo.of("나이키");
            BrandNameKo name2 = BrandNameKo.of("아디다스");

            // When & Then
            assertNotEquals(name1, name2);
        }
    }
}
