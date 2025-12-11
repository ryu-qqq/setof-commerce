package com.ryuqq.setof.domain.brand.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.brand.exception.InvalidBrandCodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * BrandCode Value Object 테스트
 *
 * <p>Zero-Tolerance Rules: - Lombok 금지 (Pure Java Record) - 불변성 보장 - 100자 이내 검증
 */
@DisplayName("BrandCode Value Object")
class BrandCodeTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 코드로 BrandCode 생성")
        void shouldCreateBrandCodeWithValidValue() {
            // Given
            String validCode = "NIKE001";

            // When
            BrandCode brandCode = BrandCode.of(validCode);

            // Then
            assertNotNull(brandCode);
            assertEquals(validCode, brandCode.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 코드로 생성")
        @ValueSource(strings = {"A", "AB", "NIKE001", "ADIDAS_KR_001"})
        void shouldCreateBrandCodeWithVariousValidValues(String validCode) {
            // When
            BrandCode brandCode = BrandCode.of(validCode);

            // Then
            assertNotNull(brandCode);
            assertEquals(validCode, brandCode.value());
        }

        @Test
        @DisplayName("최대 길이(100자) 코드로 생성")
        void shouldCreateBrandCodeWithMaxLength() {
            // Given
            String maxLengthCode = "A".repeat(100);

            // When
            BrandCode brandCode = BrandCode.of(maxLengthCode);

            // Then
            assertNotNull(brandCode);
            assertEquals(100, brandCode.value().length());
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
            assertThrows(InvalidBrandCodeException.class, () -> BrandCode.of(nullCode));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenCodeIsEmpty() {
            // Given
            String emptyCode = "";

            // When & Then
            assertThrows(InvalidBrandCodeException.class, () -> BrandCode.of(emptyCode));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenCodeIsBlank() {
            // Given
            String blankCode = "   ";

            // When & Then
            assertThrows(InvalidBrandCodeException.class, () -> BrandCode.of(blankCode));
        }

        @Test
        @DisplayName("100자 초과 코드로 생성 시 예외 발생")
        void shouldThrowExceptionWhenCodeExceedsMaxLength() {
            // Given
            String tooLongCode = "A".repeat(101);

            // When & Then
            assertThrows(InvalidBrandCodeException.class, () -> BrandCode.of(tooLongCode));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 BrandCode는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            BrandCode code1 = BrandCode.of("NIKE001");
            BrandCode code2 = BrandCode.of("NIKE001");

            // When & Then
            assertEquals(code1, code2);
            assertEquals(code1.hashCode(), code2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 BrandCode는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            BrandCode code1 = BrandCode.of("NIKE001");
            BrandCode code2 = BrandCode.of("ADIDAS001");

            // When & Then
            assertNotEquals(code1, code2);
        }
    }
}
