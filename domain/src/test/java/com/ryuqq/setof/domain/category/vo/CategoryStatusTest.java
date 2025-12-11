package com.ryuqq.setof.domain.category.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** CategoryStatus Enum 테스트 */
@DisplayName("CategoryStatus Enum")
class CategoryStatusTest {

    @Nested
    @DisplayName("상태 값 테스트")
    class StatusValueTest {

        @Test
        @DisplayName("ACTIVE 상태의 설명은 '활성'이다")
        void shouldReturnActiveDescription() {
            // When & Then
            assertEquals("활성", CategoryStatus.ACTIVE.getDescription());
        }

        @Test
        @DisplayName("INACTIVE 상태의 설명은 '비활성'이다")
        void shouldReturnInactiveDescription() {
            // When & Then
            assertEquals("비활성", CategoryStatus.INACTIVE.getDescription());
        }
    }

    @Nested
    @DisplayName("isActive() 메서드")
    class IsActiveTest {

        @Test
        @DisplayName("ACTIVE 상태는 isActive()가 true를 반환한다")
        void shouldReturnTrueForActiveStatus() {
            // When & Then
            assertTrue(CategoryStatus.ACTIVE.isActive());
        }

        @Test
        @DisplayName("INACTIVE 상태는 isActive()가 false를 반환한다")
        void shouldReturnFalseForInactiveStatus() {
            // When & Then
            assertFalse(CategoryStatus.INACTIVE.isActive());
        }
    }

    @Nested
    @DisplayName("isInactive() 메서드")
    class IsInactiveTest {

        @Test
        @DisplayName("INACTIVE 상태는 isInactive()가 true를 반환한다")
        void shouldReturnTrueForInactiveStatus() {
            // When & Then
            assertTrue(CategoryStatus.INACTIVE.isInactive());
        }

        @Test
        @DisplayName("ACTIVE 상태는 isInactive()가 false를 반환한다")
        void shouldReturnFalseForActiveStatus() {
            // When & Then
            assertFalse(CategoryStatus.ACTIVE.isInactive());
        }
    }

    @Nested
    @DisplayName("Enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("CategoryStatus는 2개의 값을 가진다")
        void shouldHaveTwoValues() {
            // When & Then
            assertEquals(2, CategoryStatus.values().length);
        }

        @Test
        @DisplayName("문자열로 CategoryStatus를 찾을 수 있다")
        void shouldFindByName() {
            // When & Then
            assertEquals(CategoryStatus.ACTIVE, CategoryStatus.valueOf("ACTIVE"));
            assertEquals(CategoryStatus.INACTIVE, CategoryStatus.valueOf("INACTIVE"));
        }
    }
}
