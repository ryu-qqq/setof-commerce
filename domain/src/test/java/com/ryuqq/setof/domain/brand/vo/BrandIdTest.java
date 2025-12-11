package com.ryuqq.setof.domain.brand.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.brand.exception.InvalidBrandIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * BrandId Value Object 테스트
 *
 * <p>Zero-Tolerance Rules: - Lombok 금지 (Pure Java Record) - 불변성 보장 - 양수 ID 값 검증
 */
@DisplayName("BrandId Value Object")
class BrandIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 ID로 BrandId 생성")
        void shouldCreateBrandIdWithValidValue() {
            // Given
            Long validId = 1L;

            // When
            BrandId brandId = BrandId.of(validId);

            // Then
            assertNotNull(brandId);
            assertEquals(validId, brandId.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 ID로 생성")
        @ValueSource(longs = {1L, 10L, 100L, 1000L, Long.MAX_VALUE})
        void shouldCreateBrandIdWithVariousValidValues(Long validId) {
            // When
            BrandId brandId = BrandId.of(validId);

            // Then
            assertNotNull(brandId);
            assertEquals(validId, brandId.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsNull() {
            // Given
            Long nullId = null;

            // When & Then
            assertThrows(InvalidBrandIdException.class, () -> BrandId.of(nullId));
        }

        @Test
        @DisplayName("0 이하의 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsZero() {
            // Given
            Long zeroId = 0L;

            // When & Then
            assertThrows(InvalidBrandIdException.class, () -> BrandId.of(zeroId));
        }

        @Test
        @DisplayName("음수 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsNegative() {
            // Given
            Long negativeId = -1L;

            // When & Then
            assertThrows(InvalidBrandIdException.class, () -> BrandId.of(negativeId));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 BrandId는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            BrandId id1 = BrandId.of(1L);
            BrandId id2 = BrandId.of(1L);

            // When & Then
            assertEquals(id1, id2);
            assertEquals(id1.hashCode(), id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 BrandId는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            BrandId id1 = BrandId.of(1L);
            BrandId id2 = BrandId.of(2L);

            // When & Then
            assertNotEquals(id1, id2);
        }
    }
}
