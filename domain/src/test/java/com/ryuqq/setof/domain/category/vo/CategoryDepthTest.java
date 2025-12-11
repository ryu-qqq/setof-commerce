package com.ryuqq.setof.domain.category.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.category.exception.InvalidCategoryDepthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * CategoryDepth Value Object 테스트
 *
 * <p>카테고리 깊이 (0 이상)
 *
 * <ul>
 *   <li>0: 최상위 (대분류)
 *   <li>1: 중분류
 *   <li>2: 소분류
 *   <li>3+: 세부 분류
 * </ul>
 */
@DisplayName("CategoryDepth Value Object")
class CategoryDepthTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("최상위 깊이(0)로 CategoryDepth 생성")
        void shouldCreateCategoryDepthWithRootDepth() {
            // Given
            Integer rootDepth = 0;

            // When
            CategoryDepth categoryDepth = CategoryDepth.of(rootDepth);

            // Then
            assertNotNull(categoryDepth);
            assertEquals(rootDepth, categoryDepth.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 깊이로 생성")
        @ValueSource(ints = {0, 1, 2, 3, 10, 100})
        void shouldCreateCategoryDepthWithVariousValidValues(int validDepth) {
            // When
            CategoryDepth categoryDepth = CategoryDepth.of(validDepth);

            // Then
            assertNotNull(categoryDepth);
            assertEquals(validDepth, categoryDepth.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenDepthIsNull() {
            // Given
            Integer nullDepth = null;

            // When & Then
            assertThrows(InvalidCategoryDepthException.class, () -> CategoryDepth.of(nullDepth));
        }

        @Test
        @DisplayName("음수 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenDepthIsNegative() {
            // Given
            Integer negativeDepth = -1;

            // When & Then
            assertThrows(
                    InvalidCategoryDepthException.class, () -> CategoryDepth.of(negativeDepth));
        }

        @ParameterizedTest
        @DisplayName("다양한 음수 값으로 생성 시 예외 발생")
        @ValueSource(ints = {-1, -10, -100, Integer.MIN_VALUE})
        void shouldThrowExceptionWhenDepthIsVariousNegativeValues(int negativeDepth) {
            // When & Then
            assertThrows(
                    InvalidCategoryDepthException.class, () -> CategoryDepth.of(negativeDepth));
        }
    }

    @Nested
    @DisplayName("isRoot() 메서드")
    class IsRootTest {

        @Test
        @DisplayName("깊이가 0이면 true 반환")
        void shouldReturnTrueWhenDepthIsZero() {
            // Given
            CategoryDepth categoryDepth = CategoryDepth.of(0);

            // When & Then
            assertTrue(categoryDepth.isRoot());
        }

        @Test
        @DisplayName("깊이가 0이 아니면 false 반환")
        void shouldReturnFalseWhenDepthIsNotZero() {
            // Given
            CategoryDepth categoryDepth = CategoryDepth.of(1);

            // When & Then
            assertFalse(categoryDepth.isRoot());
        }
    }

    @Nested
    @DisplayName("isMiddle() 메서드")
    class IsMiddleTest {

        @Test
        @DisplayName("깊이가 1이면 true 반환")
        void shouldReturnTrueWhenDepthIsOne() {
            // Given
            CategoryDepth categoryDepth = CategoryDepth.of(1);

            // When & Then
            assertTrue(categoryDepth.isMiddle());
        }

        @Test
        @DisplayName("깊이가 1이 아니면 false 반환")
        void shouldReturnFalseWhenDepthIsNotOne() {
            // Given
            CategoryDepth categoryDepth = CategoryDepth.of(0);

            // When & Then
            assertFalse(categoryDepth.isMiddle());
        }
    }

    @Nested
    @DisplayName("isSmall() 메서드")
    class IsSmallTest {

        @Test
        @DisplayName("깊이가 2이면 true 반환")
        void shouldReturnTrueWhenDepthIsTwo() {
            // Given
            CategoryDepth categoryDepth = CategoryDepth.of(2);

            // When & Then
            assertTrue(categoryDepth.isSmall());
        }

        @Test
        @DisplayName("깊이가 2가 아니면 false 반환")
        void shouldReturnFalseWhenDepthIsNotTwo() {
            // Given
            CategoryDepth categoryDepth = CategoryDepth.of(3);

            // When & Then
            assertFalse(categoryDepth.isSmall());
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 CategoryDepth는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            CategoryDepth depth1 = CategoryDepth.of(1);
            CategoryDepth depth2 = CategoryDepth.of(1);

            // When & Then
            assertEquals(depth1, depth2);
            assertEquals(depth1.hashCode(), depth2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 CategoryDepth는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            CategoryDepth depth1 = CategoryDepth.of(1);
            CategoryDepth depth2 = CategoryDepth.of(2);

            // When & Then
            assertNotEquals(depth1, depth2);
        }
    }
}
