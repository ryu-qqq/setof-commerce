package com.ryuqq.setof.domain.category.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.category.exception.InvalidCategoryCodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * CategoryCode Value Object 테스트
 *
 * <p>카테고리 코드 (필수, 최대 100자)
 */
@DisplayName("CategoryCode Value Object")
class CategoryCodeTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 코드로 CategoryCode 생성")
        void shouldCreateCategoryCodeWithValidValue() {
            // Given
            String validCode = "FASHION001";

            // When
            CategoryCode categoryCode = CategoryCode.of(validCode);

            // Then
            assertNotNull(categoryCode);
            assertEquals(validCode, categoryCode.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 코드로 생성")
        @ValueSource(strings = {"A", "FASHION", "ELECTRONICS_001", "TOP_CATEGORY"})
        void shouldCreateCategoryCodeWithVariousValidValues(String validCode) {
            // When
            CategoryCode categoryCode = CategoryCode.of(validCode);

            // Then
            assertNotNull(categoryCode);
            assertEquals(validCode, categoryCode.value());
        }

        @Test
        @DisplayName("최대 길이(100자) 코드로 생성")
        void shouldCreateCategoryCodeWithMaxLength() {
            // Given
            String maxLengthCode = "A".repeat(100);

            // When
            CategoryCode categoryCode = CategoryCode.of(maxLengthCode);

            // Then
            assertNotNull(categoryCode);
            assertEquals(100, categoryCode.value().length());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenCodeIsNull() {
            // Given
            String nullCode = null;

            // When & Then
            assertThrows(InvalidCategoryCodeException.class, () -> CategoryCode.of(nullCode));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenCodeIsEmpty() {
            // Given
            String emptyCode = "";

            // When & Then
            assertThrows(InvalidCategoryCodeException.class, () -> CategoryCode.of(emptyCode));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenCodeIsBlank() {
            // Given
            String blankCode = "   ";

            // When & Then
            assertThrows(InvalidCategoryCodeException.class, () -> CategoryCode.of(blankCode));
        }

        @Test
        @DisplayName("100자 초과 코드로 생성 시 예외 발생")
        void shouldThrowExceptionWhenCodeExceedsMaxLength() {
            // Given
            String tooLongCode = "A".repeat(101);

            // When & Then
            assertThrows(InvalidCategoryCodeException.class, () -> CategoryCode.of(tooLongCode));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 CategoryCode는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            CategoryCode code1 = CategoryCode.of("FASHION001");
            CategoryCode code2 = CategoryCode.of("FASHION001");

            // When & Then
            assertEquals(code1, code2);
            assertEquals(code1.hashCode(), code2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 CategoryCode는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            CategoryCode code1 = CategoryCode.of("FASHION001");
            CategoryCode code2 = CategoryCode.of("FASHION002");

            // When & Then
            assertNotEquals(code1, code2);
        }
    }
}
