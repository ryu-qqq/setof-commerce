package com.ryuqq.setof.domain.cart.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.cart.exception.InvalidCartItemException;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("CartItem VO")
class CartItemTest {

    private static final Instant NOW = Instant.now();

    @Nested
    @DisplayName("forNew() - 신규 아이템 생성")
    class ForNew {

        @Test
        @DisplayName("신규 아이템을 생성할 수 있다")
        void shouldCreateNewItem() {
            // when
            CartItem item = CartItem.forNew(100L, 10L, 1L, 1L, 2, BigDecimal.valueOf(10000), NOW);

            // then
            assertNotNull(item);
            assertNull(item.id());
            assertTrue(item.isNew());
            assertEquals(100L, item.productStockId());
            assertEquals(10L, item.productId());
            assertEquals(1L, item.productGroupId());
            assertEquals(1L, item.sellerId());
            assertEquals(2, item.quantity());
            assertEquals(BigDecimal.valueOf(10000), item.unitPrice());
            assertTrue(item.selected());
            assertEquals(NOW, item.addedAt());
        }

        @Test
        @DisplayName("productStockId가 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenProductStockIdIsNull() {
            // when & then
            assertThrows(
                    InvalidCartItemException.class,
                    () -> CartItem.forNew(null, 10L, 1L, 1L, 2, BigDecimal.valueOf(10000), NOW));
        }

        @Test
        @DisplayName("productStockId가 0 이하이면 예외가 발생한다")
        void shouldThrowExceptionWhenProductStockIdIsNotPositive() {
            // when & then
            assertThrows(
                    InvalidCartItemException.class,
                    () -> CartItem.forNew(0L, 10L, 1L, 1L, 2, BigDecimal.valueOf(10000), NOW));

            assertThrows(
                    InvalidCartItemException.class,
                    () -> CartItem.forNew(-1L, 10L, 1L, 1L, 2, BigDecimal.valueOf(10000), NOW));
        }

        @Test
        @DisplayName("수량이 0 이하이면 예외가 발생한다")
        void shouldThrowExceptionWhenQuantityIsNotPositive() {
            // when & then
            assertThrows(
                    InvalidCartItemException.class,
                    () -> CartItem.forNew(100L, 10L, 1L, 1L, 0, BigDecimal.valueOf(10000), NOW));

            assertThrows(
                    InvalidCartItemException.class,
                    () -> CartItem.forNew(100L, 10L, 1L, 1L, -1, BigDecimal.valueOf(10000), NOW));
        }

        @Test
        @DisplayName("수량이 최대값(99)을 초과하면 예외가 발생한다")
        void shouldThrowExceptionWhenQuantityExceedsMax() {
            // when & then
            assertThrows(
                    InvalidCartItemException.class,
                    () -> CartItem.forNew(100L, 10L, 1L, 1L, 100, BigDecimal.valueOf(10000), NOW));
        }

        @Test
        @DisplayName("단가가 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenUnitPriceIsNull() {
            // when & then
            assertThrows(
                    InvalidCartItemException.class,
                    () -> CartItem.forNew(100L, 10L, 1L, 1L, 2, null, NOW));
        }
    }

    @Nested
    @DisplayName("withQuantity() - 수량 변경")
    class WithQuantity {

        @Test
        @DisplayName("수량을 변경할 수 있다")
        void shouldChangeQuantity() {
            // given
            CartItem item = createTestItem(2);

            // when
            CartItem updated = item.withQuantity(5);

            // then
            assertEquals(5, updated.quantity());
            assertEquals(2, item.quantity());
        }

        @Test
        @DisplayName("변경된 수량이 0 이하이면 예외가 발생한다")
        void shouldThrowExceptionWhenNewQuantityIsNotPositive() {
            // given
            CartItem item = createTestItem(2);

            // when & then
            assertThrows(InvalidCartItemException.class, () -> item.withQuantity(0));
            assertThrows(InvalidCartItemException.class, () -> item.withQuantity(-1));
        }

        @Test
        @DisplayName("변경된 수량이 최대값을 초과하면 예외가 발생한다")
        void shouldThrowExceptionWhenNewQuantityExceedsMax() {
            // given
            CartItem item = createTestItem(2);

            // when & then
            assertThrows(InvalidCartItemException.class, () -> item.withQuantity(100));
        }
    }

    @Nested
    @DisplayName("withSelected() - 선택 상태 변경")
    class WithSelected {

        @Test
        @DisplayName("선택 상태를 변경할 수 있다")
        void shouldChangeSelected() {
            // given
            CartItem item = createTestItem(2);
            assertTrue(item.selected());

            // when
            CartItem updated = item.withSelected(false);

            // then
            assertFalse(updated.selected());
            assertTrue(item.selected());
        }
    }

    @Nested
    @DisplayName("withId() - ID 할당")
    class WithId {

        @Test
        @DisplayName("ID를 할당할 수 있다")
        void shouldAssignId() {
            // given
            CartItem item = createTestItem(2);
            assertTrue(item.isNew());

            // when
            CartItem withId = item.withId(CartItemId.of(1L));

            // then
            assertFalse(withId.isNew());
            assertEquals(CartItemId.of(1L), withId.id());
        }
    }

    @Nested
    @DisplayName("mergeWith() - 수량 병합")
    class MergeWith {

        @Test
        @DisplayName("동일 상품의 수량을 병합할 수 있다")
        void shouldMergeQuantityForSameProduct() {
            // given
            CartItem item1 = createTestItem(2);
            CartItem item2 = CartItem.forNew(100L, 10L, 1L, 1L, 3, BigDecimal.valueOf(10000), NOW);

            // when
            CartItem merged = item1.mergeWith(item2);

            // then
            assertEquals(5, merged.quantity());
        }

        @Test
        @DisplayName("다른 상품과 병합 시 예외가 발생한다")
        void shouldThrowExceptionWhenMergingDifferentProducts() {
            // given
            CartItem item1 = createTestItem(2);
            CartItem item2 = CartItem.forNew(200L, 20L, 2L, 1L, 3, BigDecimal.valueOf(5000), NOW);

            // when & then
            assertThrows(InvalidCartItemException.class, () -> item1.mergeWith(item2));
        }

        @Test
        @DisplayName("병합 후 수량이 최대값을 초과하면 예외가 발생한다")
        void shouldThrowExceptionWhenMergedQuantityExceedsMax() {
            // given
            CartItem item1 = CartItem.forNew(100L, 10L, 1L, 1L, 50, BigDecimal.valueOf(10000), NOW);
            CartItem item2 = CartItem.forNew(100L, 10L, 1L, 1L, 50, BigDecimal.valueOf(10000), NOW);

            // when & then
            assertThrows(InvalidCartItemException.class, () -> item1.mergeWith(item2));
        }
    }

    @Nested
    @DisplayName("totalPrice() - 총 가격 계산")
    class TotalPrice {

        @Test
        @DisplayName("총 가격을 계산할 수 있다")
        void shouldCalculateTotalPrice() {
            // given
            CartItem item = CartItem.forNew(100L, 10L, 1L, 1L, 3, BigDecimal.valueOf(10000), NOW);

            // when
            BigDecimal total = item.totalPrice();

            // then
            assertEquals(BigDecimal.valueOf(30000), total);
        }
    }

    @Nested
    @DisplayName("isSameProduct() - 동일 상품 확인")
    class IsSameProduct {

        @Test
        @DisplayName("동일 상품인지 확인할 수 있다")
        void shouldCheckIfSameProduct() {
            // given
            CartItem item = createTestItem(2);

            // when & then
            assertTrue(item.isSameProduct(100L));
            assertFalse(item.isSameProduct(200L));
        }
    }

    @Nested
    @DisplayName("isSameSeller() - 동일 판매자 확인")
    class IsSameSeller {

        @Test
        @DisplayName("동일 판매자인지 확인할 수 있다")
        void shouldCheckIfSameSeller() {
            // given
            CartItem item = createTestItem(2);

            // when & then
            assertTrue(item.isSameSeller(1L));
            assertFalse(item.isSameSeller(2L));
        }
    }

    @Nested
    @DisplayName("restore() - 영속화 데이터 복원")
    class Restore {

        @Test
        @DisplayName("영속화된 데이터를 복원할 수 있다")
        void shouldRestoreFromPersistedData() {
            // given
            CartItemId id = CartItemId.of(1L);
            Long productStockId = 100L;
            Long productId = 10L;
            Long productGroupId = 1L;
            Long sellerId = 1L;
            int quantity = 2;
            BigDecimal unitPrice = BigDecimal.valueOf(10000);
            boolean selected = false;
            Instant addedAt = NOW.minusSeconds(3600);

            // when
            CartItem item =
                    CartItem.restore(
                            id,
                            productStockId,
                            productId,
                            productGroupId,
                            sellerId,
                            quantity,
                            unitPrice,
                            selected,
                            addedAt,
                            null); // deletedAt

            // then
            assertEquals(id, item.id());
            assertEquals(productStockId, item.productStockId());
            assertEquals(productId, item.productId());
            assertEquals(productGroupId, item.productGroupId());
            assertEquals(sellerId, item.sellerId());
            assertEquals(quantity, item.quantity());
            assertEquals(unitPrice, item.unitPrice());
            assertEquals(selected, item.selected());
            assertEquals(addedAt, item.addedAt());
            assertFalse(item.isNew());
        }
    }

    // ===== Helper Methods =====

    private CartItem createTestItem(int quantity) {
        return CartItem.forNew(100L, 10L, 1L, 1L, quantity, BigDecimal.valueOf(10000), NOW);
    }
}
