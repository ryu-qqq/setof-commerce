package com.ryuqq.setof.domain.category.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.category.exception.InvalidCategoryIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** CategoryId Value Object 테스트 */
@DisplayName("CategoryId Value Object")
class CategoryIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 ID로 CategoryId 생성")
        void shouldCreateCategoryIdWithValidValue() {
            // Given
            Long validId = 1L;

            // When
            CategoryId categoryId = CategoryId.of(validId);

            // Then
            assertNotNull(categoryId);
            assertEquals(validId, categoryId.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 ID로 생성")
        @ValueSource(longs = {1L, 10L, 100L, 1000L, Long.MAX_VALUE})
        void shouldCreateCategoryIdWithVariousValidValues(Long validId) {
            // When
            CategoryId categoryId = CategoryId.of(validId);

            // Then
            assertNotNull(categoryId);
            assertEquals(validId, categoryId.value());
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
            assertThrows(InvalidCategoryIdException.class, () -> CategoryId.of(nullId));
        }

        @Test
        @DisplayName("0 이하의 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsZero() {
            // Given
            Long zeroId = 0L;

            // When & Then
            assertThrows(InvalidCategoryIdException.class, () -> CategoryId.of(zeroId));
        }

        @Test
        @DisplayName("음수 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsNegative() {
            // Given
            Long negativeId = -1L;

            // When & Then
            assertThrows(InvalidCategoryIdException.class, () -> CategoryId.of(negativeId));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 CategoryId는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            CategoryId id1 = CategoryId.of(1L);
            CategoryId id2 = CategoryId.of(1L);

            // When & Then
            assertEquals(id1, id2);
            assertEquals(id1.hashCode(), id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 CategoryId는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            CategoryId id1 = CategoryId.of(1L);
            CategoryId id2 = CategoryId.of(2L);

            // When & Then
            assertNotEquals(id1, id2);
        }
    }
}
