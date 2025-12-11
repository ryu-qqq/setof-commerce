package com.ryuqq.setof.domain.brand.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * BrandStatus Enum 테스트
 *
 * <p>브랜드 상태 열거형 테스트
 */
@DisplayName("BrandStatus Enum")
class BrandStatusTest {

    @Nested
    @DisplayName("상태 값 테스트")
    class StatusValueTest {

        @Test
        @DisplayName("ACTIVE 상태의 설명은 '활성'이다")
        void shouldReturnActiveDescription() {
            // When & Then
            assertEquals("활성", BrandStatus.ACTIVE.getDescription());
        }

        @Test
        @DisplayName("INACTIVE 상태의 설명은 '비활성'이다")
        void shouldReturnInactiveDescription() {
            // When & Then
            assertEquals("비활성", BrandStatus.INACTIVE.getDescription());
        }
    }

    @Nested
    @DisplayName("isActive() 메서드")
    class IsActiveTest {

        @Test
        @DisplayName("ACTIVE 상태는 isActive()가 true를 반환한다")
        void shouldReturnTrueForActiveStatus() {
            // When & Then
            assertTrue(BrandStatus.ACTIVE.isActive());
        }

        @Test
        @DisplayName("INACTIVE 상태는 isActive()가 false를 반환한다")
        void shouldReturnFalseForInactiveStatus() {
            // When & Then
            assertFalse(BrandStatus.INACTIVE.isActive());
        }
    }

    @Nested
    @DisplayName("isInactive() 메서드")
    class IsInactiveTest {

        @Test
        @DisplayName("INACTIVE 상태는 isInactive()가 true를 반환한다")
        void shouldReturnTrueForInactiveStatus() {
            // When & Then
            assertTrue(BrandStatus.INACTIVE.isInactive());
        }

        @Test
        @DisplayName("ACTIVE 상태는 isInactive()가 false를 반환한다")
        void shouldReturnFalseForActiveStatus() {
            // When & Then
            assertFalse(BrandStatus.ACTIVE.isInactive());
        }
    }

    @Nested
    @DisplayName("Enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("BrandStatus는 2개의 값을 가진다")
        void shouldHaveTwoValues() {
            // When & Then
            assertEquals(2, BrandStatus.values().length);
        }

        @Test
        @DisplayName("문자열로 BrandStatus를 찾을 수 있다")
        void shouldFindByName() {
            // When & Then
            assertEquals(BrandStatus.ACTIVE, BrandStatus.valueOf("ACTIVE"));
            assertEquals(BrandStatus.INACTIVE, BrandStatus.valueOf("INACTIVE"));
        }
    }
}
