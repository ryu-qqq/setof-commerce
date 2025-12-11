package com.ryuqq.setof.domain.brand.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.brand.exception.InvalidBrandNameEnException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * BrandNameEn Value Object 테스트
 *
 * <p>영문 브랜드명 (선택, null 허용, 최대 255자)
 */
@DisplayName("BrandNameEn Value Object")
class BrandNameEnTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 영문명으로 BrandNameEn 생성")
        void shouldCreateBrandNameEnWithValidValue() {
            // Given
            String validName = "Nike";

            // When
            BrandNameEn brandNameEn = BrandNameEn.of(validName);

            // Then
            assertNotNull(brandNameEn);
            assertEquals(validName, brandNameEn.value());
        }

        @Test
        @DisplayName("null 값으로 BrandNameEn 생성 (선택 필드)")
        void shouldCreateBrandNameEnWithNull() {
            // Given
            String nullName = null;

            // When
            BrandNameEn brandNameEn = BrandNameEn.of(nullName);

            // Then
            assertNotNull(brandNameEn);
            assertNull(brandNameEn.value());
        }

        @Test
        @DisplayName("empty() 팩토리로 빈 BrandNameEn 생성")
        void shouldCreateEmptyBrandNameEn() {
            // When
            BrandNameEn brandNameEn = BrandNameEn.empty();

            // Then
            assertNotNull(brandNameEn);
            assertNull(brandNameEn.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 영문명으로 생성")
        @ValueSource(strings = {"A", "Nike", "Adidas Korea", "LOUIS VUITTON"})
        void shouldCreateBrandNameEnWithVariousValidValues(String validName) {
            // When
            BrandNameEn brandNameEn = BrandNameEn.of(validName);

            // Then
            assertNotNull(brandNameEn);
            assertEquals(validName, brandNameEn.value());
        }

        @Test
        @DisplayName("최대 길이(255자) 영문명으로 생성")
        void shouldCreateBrandNameEnWithMaxLength() {
            // Given
            String maxLengthName = "A".repeat(255);

            // When
            BrandNameEn brandNameEn = BrandNameEn.of(maxLengthName);

            // Then
            assertNotNull(brandNameEn);
            assertEquals(255, brandNameEn.value().length());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsEmpty() {
            // Given
            String emptyName = "";

            // When & Then
            assertThrows(InvalidBrandNameEnException.class, () -> BrandNameEn.of(emptyName));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsBlank() {
            // Given
            String blankName = "   ";

            // When & Then
            assertThrows(InvalidBrandNameEnException.class, () -> BrandNameEn.of(blankName));
        }

        @Test
        @DisplayName("255자 초과 영문명으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameExceedsMaxLength() {
            // Given
            String tooLongName = "A".repeat(256);

            // When & Then
            assertThrows(InvalidBrandNameEnException.class, () -> BrandNameEn.of(tooLongName));
        }
    }

    @Nested
    @DisplayName("hasValue() 메서드")
    class HasValueTest {

        @Test
        @DisplayName("값이 있으면 true 반환")
        void shouldReturnTrueWhenHasValue() {
            // Given
            BrandNameEn brandNameEn = BrandNameEn.of("Nike");

            // When & Then
            assertTrue(brandNameEn.hasValue());
        }

        @Test
        @DisplayName("값이 null이면 false 반환")
        void shouldReturnFalseWhenNull() {
            // Given
            BrandNameEn brandNameEn = BrandNameEn.empty();

            // When & Then
            assertFalse(brandNameEn.hasValue());
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 BrandNameEn는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            BrandNameEn name1 = BrandNameEn.of("Nike");
            BrandNameEn name2 = BrandNameEn.of("Nike");

            // When & Then
            assertEquals(name1, name2);
            assertEquals(name1.hashCode(), name2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 BrandNameEn는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            BrandNameEn name1 = BrandNameEn.of("Nike");
            BrandNameEn name2 = BrandNameEn.of("Adidas");

            // When & Then
            assertNotEquals(name1, name2);
        }

        @Test
        @DisplayName("빈 BrandNameEn는 서로 동등하다")
        void shouldBeEqualWhenBothEmpty() {
            // Given
            BrandNameEn empty1 = BrandNameEn.empty();
            BrandNameEn empty2 = BrandNameEn.empty();

            // When & Then
            assertEquals(empty1, empty2);
        }
    }
}
