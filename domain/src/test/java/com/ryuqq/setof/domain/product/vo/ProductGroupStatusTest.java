package com.ryuqq.setof.domain.product.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ProductGroupStatus Enum 테스트
 *
 * <p>상품그룹 상태에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("ProductGroupStatus Enum")
class ProductGroupStatusTest {

    @Nested
    @DisplayName("displayName() - 표시명 반환")
    class DisplayNameTest {

        @Test
        @DisplayName("ACTIVE의 표시명은 '활성'이다")
        void shouldReturnCorrectDisplayNameForActive() {
            // When & Then
            assertEquals("활성", ProductGroupStatus.ACTIVE.displayName());
        }

        @Test
        @DisplayName("INACTIVE의 표시명은 '비활성'이다")
        void shouldReturnCorrectDisplayNameForInactive() {
            // When & Then
            assertEquals("비활성", ProductGroupStatus.INACTIVE.displayName());
        }

        @Test
        @DisplayName("DELETED의 표시명은 '삭제됨'이다")
        void shouldReturnCorrectDisplayNameForDeleted() {
            // When & Then
            assertEquals("삭제됨", ProductGroupStatus.DELETED.displayName());
        }
    }

    @Nested
    @DisplayName("isActive() - 활성 상태 확인")
    class IsActiveTest {

        @Test
        @DisplayName("ACTIVE는 활성 상태이다")
        void shouldReturnTrueForActive() {
            // When & Then
            assertTrue(ProductGroupStatus.ACTIVE.isActive());
        }

        @Test
        @DisplayName("INACTIVE는 활성 상태가 아니다")
        void shouldReturnFalseForInactive() {
            // When & Then
            assertFalse(ProductGroupStatus.INACTIVE.isActive());
        }

        @Test
        @DisplayName("DELETED는 활성 상태가 아니다")
        void shouldReturnFalseForDeleted() {
            // When & Then
            assertFalse(ProductGroupStatus.DELETED.isActive());
        }
    }

    @Nested
    @DisplayName("isDeleted() - 삭제 상태 확인")
    class IsDeletedTest {

        @Test
        @DisplayName("ACTIVE는 삭제 상태가 아니다")
        void shouldReturnFalseForActive() {
            // When & Then
            assertFalse(ProductGroupStatus.ACTIVE.isDeleted());
        }

        @Test
        @DisplayName("INACTIVE는 삭제 상태가 아니다")
        void shouldReturnFalseForInactive() {
            // When & Then
            assertFalse(ProductGroupStatus.INACTIVE.isDeleted());
        }

        @Test
        @DisplayName("DELETED는 삭제 상태이다")
        void shouldReturnTrueForDeleted() {
            // When & Then
            assertTrue(ProductGroupStatus.DELETED.isDeleted());
        }
    }

    @Nested
    @DisplayName("isSellable() - 판매 가능 상태 확인")
    class IsSellableTest {

        @Test
        @DisplayName("ACTIVE는 판매 가능 상태이다")
        void shouldReturnTrueForActive() {
            // When & Then
            assertTrue(ProductGroupStatus.ACTIVE.isSellable());
        }

        @Test
        @DisplayName("INACTIVE는 판매 가능 상태가 아니다")
        void shouldReturnFalseForInactive() {
            // When & Then
            assertFalse(ProductGroupStatus.INACTIVE.isSellable());
        }

        @Test
        @DisplayName("DELETED는 판매 가능 상태가 아니다")
        void shouldReturnFalseForDeleted() {
            // When & Then
            assertFalse(ProductGroupStatus.DELETED.isSellable());
        }
    }

    @Nested
    @DisplayName("isEditable() - 수정 가능 상태 확인")
    class IsEditableTest {

        @Test
        @DisplayName("ACTIVE는 수정 가능 상태이다")
        void shouldReturnTrueForActive() {
            // When & Then
            assertTrue(ProductGroupStatus.ACTIVE.isEditable());
        }

        @Test
        @DisplayName("INACTIVE는 수정 가능 상태이다")
        void shouldReturnTrueForInactive() {
            // When & Then
            assertTrue(ProductGroupStatus.INACTIVE.isEditable());
        }

        @Test
        @DisplayName("DELETED는 수정 가능 상태가 아니다")
        void shouldReturnFalseForDeleted() {
            // When & Then
            assertFalse(ProductGroupStatus.DELETED.isEditable());
        }
    }

    @Nested
    @DisplayName("Enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("ProductGroupStatus는 3개의 값을 가진다")
        void shouldHaveThreeValues() {
            // When & Then
            assertEquals(3, ProductGroupStatus.values().length);
        }
    }
}
