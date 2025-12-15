package com.ryuqq.setof.domain.product.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ProductStatus Value Object 테스트
 *
 * <p>상품 상태(품절/노출 여부)에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("ProductStatus Value Object")
class ProductStatusTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("of() 메서드로 ProductStatus 생성")
        void shouldCreateProductStatusWithOf() {
            // When
            ProductStatus status = ProductStatus.of(false, true);

            // Then
            assertNotNull(status);
            assertFalse(status.soldOut());
            assertTrue(status.displayYn());
        }

        @Test
        @DisplayName("defaultStatus()는 품절 아니고 노출 상태를 반환한다")
        void shouldCreateDefaultStatus() {
            // When
            ProductStatus status = ProductStatus.defaultStatus();

            // Then
            assertNotNull(status);
            assertFalse(status.soldOut());
            assertTrue(status.displayYn());
        }
    }

    @Nested
    @DisplayName("상태 변경 테스트")
    class StatusChangeTest {

        @Test
        @DisplayName("markSoldOut()은 품절 상태로 변경한다")
        void shouldMarkAsSoldOut() {
            // Given
            ProductStatus status = ProductStatus.defaultStatus();
            assertFalse(status.soldOut());

            // When
            ProductStatus soldOutStatus = status.markSoldOut();

            // Then
            assertTrue(soldOutStatus.soldOut());
            assertEquals(status.displayYn(), soldOutStatus.displayYn());
        }

        @Test
        @DisplayName("markInStock()은 재고 있음 상태로 변경한다")
        void shouldMarkAsInStock() {
            // Given
            ProductStatus status = ProductStatus.of(true, true);
            assertTrue(status.soldOut());

            // When
            ProductStatus inStockStatus = status.markInStock();

            // Then
            assertFalse(inStockStatus.soldOut());
            assertEquals(status.displayYn(), inStockStatus.displayYn());
        }

        @Test
        @DisplayName("show()는 노출 상태로 변경한다")
        void shouldShow() {
            // Given
            ProductStatus status = ProductStatus.of(false, false);
            assertFalse(status.displayYn());

            // When
            ProductStatus shownStatus = status.show();

            // Then
            assertTrue(shownStatus.displayYn());
            assertEquals(status.soldOut(), shownStatus.soldOut());
        }

        @Test
        @DisplayName("hide()는 숨김 상태로 변경한다")
        void shouldHide() {
            // Given
            ProductStatus status = ProductStatus.defaultStatus();
            assertTrue(status.displayYn());

            // When
            ProductStatus hiddenStatus = status.hide();

            // Then
            assertFalse(hiddenStatus.displayYn());
            assertEquals(status.soldOut(), hiddenStatus.soldOut());
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StatusCheckTest {

        @Test
        @DisplayName("isDisplayed()는 노출 상태일 때 true를 반환한다")
        void shouldReturnTrueWhenDisplayed() {
            // Given
            ProductStatus status = ProductStatus.of(false, true);

            // When & Then
            assertTrue(status.isDisplayed());
        }

        @Test
        @DisplayName("isDisplayed()는 숨김 상태일 때 false를 반환한다")
        void shouldReturnFalseWhenHidden() {
            // Given
            ProductStatus status = ProductStatus.of(false, false);

            // When & Then
            assertFalse(status.isDisplayed());
        }

        @Test
        @DisplayName("isSellable()은 품절이 아니고 노출 중일 때 true를 반환한다")
        void shouldReturnTrueWhenSellable() {
            // Given
            ProductStatus status = ProductStatus.of(false, true);

            // When & Then
            assertTrue(status.isSellable());
        }

        @Test
        @DisplayName("isSellable()은 품절이면 false를 반환한다")
        void shouldReturnFalseWhenSoldOut() {
            // Given
            ProductStatus status = ProductStatus.of(true, true);

            // When & Then
            assertFalse(status.isSellable());
        }

        @Test
        @DisplayName("isSellable()은 숨김이면 false를 반환한다")
        void shouldReturnFalseWhenNotDisplayed() {
            // Given
            ProductStatus status = ProductStatus.of(false, false);

            // When & Then
            assertFalse(status.isSellable());
        }

        @Test
        @DisplayName("isSellable()은 품절이고 숨김이면 false를 반환한다")
        void shouldReturnFalseWhenSoldOutAndHidden() {
            // Given
            ProductStatus status = ProductStatus.of(true, false);

            // When & Then
            assertFalse(status.isSellable());
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ProductStatus는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            ProductStatus status1 = ProductStatus.of(false, true);
            ProductStatus status2 = ProductStatus.of(false, true);

            // When & Then
            assertEquals(status1, status2);
            assertEquals(status1.hashCode(), status2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 ProductStatus는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            ProductStatus status1 = ProductStatus.of(false, true);
            ProductStatus status2 = ProductStatus.of(true, false);

            // When & Then
            assertNotEquals(status1, status2);
        }
    }
}
