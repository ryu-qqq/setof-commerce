package com.ryuqq.setof.domain.product.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.product.exception.InvalidProductGroupIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * ProductGroupId Value Object 테스트
 *
 * <p>Zero-Tolerance Rules: - Lombok 금지 (Pure Java Record) - 불변성 보장 - 양수 ID 값 검증
 */
@DisplayName("ProductGroupId Value Object")
class ProductGroupIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 ID로 ProductGroupId 생성")
        void shouldCreateProductGroupIdWithValidValue() {
            // Given
            Long validId = 1L;

            // When
            ProductGroupId productGroupId = ProductGroupId.of(validId);

            // Then
            assertNotNull(productGroupId);
            assertEquals(validId, productGroupId.value());
            assertFalse(productGroupId.isNew());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 ID로 생성")
        @ValueSource(longs = {1L, 10L, 100L, 1000L, Long.MAX_VALUE})
        void shouldCreateProductGroupIdWithVariousValidValues(Long validId) {
            // When
            ProductGroupId productGroupId = ProductGroupId.of(validId);

            // Then
            assertNotNull(productGroupId);
            assertEquals(validId, productGroupId.value());
        }
    }

    @Nested
    @DisplayName("forNew() - 신규 ID 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 ID는 null 값을 가진다")
        void shouldCreateNewIdWithNullValue() {
            // When
            ProductGroupId newId = ProductGroupId.forNew();

            // Then
            assertNotNull(newId);
            assertNull(newId.value());
            assertTrue(newId.isNew());
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
            assertThrows(InvalidProductGroupIdException.class, () -> ProductGroupId.of(nullId));
        }

        @Test
        @DisplayName("0 이하의 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsZero() {
            // Given
            Long zeroId = 0L;

            // When & Then
            assertThrows(InvalidProductGroupIdException.class, () -> ProductGroupId.of(zeroId));
        }

        @Test
        @DisplayName("음수 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsNegative() {
            // Given
            Long negativeId = -1L;

            // When & Then
            assertThrows(InvalidProductGroupIdException.class, () -> ProductGroupId.of(negativeId));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ProductGroupId는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            ProductGroupId id1 = ProductGroupId.of(1L);
            ProductGroupId id2 = ProductGroupId.of(1L);

            // When & Then
            assertEquals(id1, id2);
            assertEquals(id1.hashCode(), id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 ProductGroupId는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            ProductGroupId id1 = ProductGroupId.of(1L);
            ProductGroupId id2 = ProductGroupId.of(2L);

            // When & Then
            assertNotEquals(id1, id2);
        }
    }
}
