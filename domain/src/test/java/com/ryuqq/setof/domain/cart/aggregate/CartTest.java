package com.ryuqq.setof.domain.cart.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.cart.exception.CartItemLimitExceededException;
import com.ryuqq.setof.domain.cart.exception.CartItemNotFoundException;
import com.ryuqq.setof.domain.cart.exception.QuantityLimitExceededException;
import com.ryuqq.setof.domain.cart.vo.CartId;
import com.ryuqq.setof.domain.cart.vo.CartItem;
import com.ryuqq.setof.domain.cart.vo.CartItemId;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Cart Aggregate")
class CartTest {

    private static final Instant NOW = Instant.now();
    private static final UUID MEMBER_ID = UUID.fromString("019538ab-faac-7ab6-9ab0-17e7f91a51c7");

    @Nested
    @DisplayName("forNew() - 신규 장바구니 생성")
    class ForNew {

        @Test
        @DisplayName("빈 장바구니를 생성할 수 있다")
        void shouldCreateEmptyCart() {
            // when
            Cart cart = Cart.forNew(MEMBER_ID, NOW);

            // then
            assertNotNull(cart);
            assertEquals(MEMBER_ID, cart.memberId());
            assertTrue(cart.isEmpty());
            assertTrue(cart.isNew());
            assertEquals(0, cart.itemCount());
        }

        @Test
        @DisplayName("memberId가 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenMemberIdIsNull() {
            // when & then
            assertThrows(IllegalArgumentException.class, () -> Cart.forNew(null, NOW));
        }
    }

    @Nested
    @DisplayName("addItem() - 아이템 추가")
    class AddItem {

        @Test
        @DisplayName("빈 장바구니에 아이템을 추가할 수 있다")
        void shouldAddItemToEmptyCart() {
            // given
            Cart cart = Cart.forNew(MEMBER_ID, NOW);
            CartItem item = createCartItem(100L, 2);

            // when
            Cart updated = cart.addItem(item, NOW);

            // then
            assertEquals(1, updated.itemCount());
            assertFalse(updated.isEmpty());
        }

        @Test
        @DisplayName("동일 상품 추가 시 수량이 합산된다")
        void shouldMergeQuantityForSameProduct() {
            // given
            Cart cart = Cart.forNew(MEMBER_ID, NOW);
            CartItem item1 = createCartItem(100L, 2);
            CartItem item2 = createCartItem(100L, 3);

            // when
            Cart updated = cart.addItem(item1, NOW).addItem(item2, NOW);

            // then
            assertEquals(1, updated.itemCount());
            assertEquals(5, updated.totalQuantity());
        }

        @Test
        @DisplayName("다른 상품 추가 시 별도 아이템으로 추가된다")
        void shouldAddSeparateItemForDifferentProduct() {
            // given
            Cart cart = Cart.forNew(MEMBER_ID, NOW);
            CartItem item1 = createCartItem(100L, 2);
            CartItem item2 = createCartItem(200L, 3);

            // when
            Cart updated = cart.addItem(item1, NOW).addItem(item2, NOW);

            // then
            assertEquals(2, updated.itemCount());
            assertEquals(5, updated.totalQuantity());
        }

        @Test
        @DisplayName("최대 개수(100개) 초과 시 예외가 발생한다")
        void shouldThrowExceptionWhenExceedMaxItemCount() {
            // given
            Cart cart = Cart.forNew(MEMBER_ID, NOW);
            for (int i = 1; i <= Cart.MAX_ITEM_COUNT; i++) {
                cart = cart.addItem(createCartItem((long) i, 1), NOW);
            }
            Cart fullCart = cart;
            CartItem extraItem = createCartItem(999L, 1);

            // when & then
            assertThrows(
                    CartItemLimitExceededException.class, () -> fullCart.addItem(extraItem, NOW));
        }

        @Test
        @DisplayName("동일 상품 합산 시 최대 수량(99개) 초과하면 예외가 발생한다")
        void shouldThrowExceptionWhenMergedQuantityExceedsMax() {
            // given
            Cart cart = Cart.forNew(MEMBER_ID, NOW);
            CartItem item1 = createCartItem(100L, 50);
            CartItem item2 = createCartItem(100L, 50);
            Cart cartWithItem = cart.addItem(item1, NOW);

            // when & then
            assertThrows(
                    QuantityLimitExceededException.class, () -> cartWithItem.addItem(item2, NOW));
        }
    }

    @Nested
    @DisplayName("updateQuantity() - 수량 변경")
    class UpdateQuantity {

        @Test
        @DisplayName("아이템 수량을 변경할 수 있다")
        void shouldUpdateItemQuantity() {
            // given
            Cart cart = createCartWithRestoredItem();
            CartItemId itemId = cart.items().get(0).id();

            // when
            Cart updated = cart.updateQuantity(itemId, 5, NOW);

            // then
            CartItem updatedItem = updated.findItemById(itemId).orElseThrow();
            assertEquals(5, updatedItem.quantity());
        }

        @Test
        @DisplayName("존재하지 않는 아이템 수량 변경 시 예외가 발생한다")
        void shouldThrowExceptionWhenItemNotFound() {
            // given
            Cart cart = createCartWithRestoredItem();
            CartItemId nonExistentId = CartItemId.of(999L);

            // when & then
            assertThrows(
                    CartItemNotFoundException.class,
                    () -> cart.updateQuantity(nonExistentId, 5, NOW));
        }
    }

    @Nested
    @DisplayName("updateSelected() - 선택 상태 변경")
    class UpdateSelected {

        @Test
        @DisplayName("아이템 선택 상태를 변경할 수 있다")
        void shouldUpdateItemSelected() {
            // given
            Cart cart = createCartWithRestoredItem();
            CartItemId itemId = cart.items().get(0).id();

            // when
            Cart updated = cart.updateSelected(itemId, false, NOW);

            // then
            CartItem updatedItem = updated.findItemById(itemId).orElseThrow();
            assertFalse(updatedItem.selected());
        }

        @Test
        @DisplayName("전체 선택/해제가 가능하다")
        void shouldUpdateAllSelected() {
            // given
            Cart cart = createCartWithMultipleRestoredItems();

            // when
            Cart allDeselected = cart.updateAllSelected(false, NOW);

            // then
            assertEquals(0, allDeselected.selectedItemCount());

            // when
            Cart allSelected = allDeselected.updateAllSelected(true, NOW);

            // then
            assertEquals(allSelected.itemCount(), allSelected.selectedItemCount());
        }
    }

    @Nested
    @DisplayName("removeItem() - 아이템 삭제")
    class RemoveItem {

        @Test
        @DisplayName("아이템을 삭제할 수 있다")
        void shouldRemoveItem() {
            // given
            Cart cart = createCartWithRestoredItem();
            CartItemId itemId = cart.items().get(0).id();

            // when
            Cart updated = cart.removeItem(itemId, NOW);

            // then
            assertTrue(updated.isEmpty());
        }

        @Test
        @DisplayName("여러 아이템을 한 번에 삭제할 수 있다")
        void shouldRemoveMultipleItems() {
            // given
            Cart cart = createCartWithMultipleRestoredItems();
            List<CartItemId> idsToRemove =
                    List.of(cart.items().get(0).id(), cart.items().get(1).id());
            int originalCount = cart.itemCount();

            // when
            Cart updated = cart.removeItems(idsToRemove, NOW);

            // then
            assertEquals(originalCount - 2, updated.itemCount());
        }

        @Test
        @DisplayName("선택된 아이템만 삭제할 수 있다")
        void shouldRemoveSelectedItems() {
            // given
            Cart cart = createCartWithMultipleRestoredItems();
            CartItemId firstItemId = cart.items().get(0).id();
            Cart withDeselected = cart.updateSelected(firstItemId, false, NOW);

            // when
            Cart updated = withDeselected.removeSelectedItems(NOW);

            // then
            assertEquals(1, updated.itemCount());
            assertFalse(updated.findItemById(firstItemId).isEmpty());
        }

        @Test
        @DisplayName("존재하지 않는 아이템 삭제 시 예외가 발생한다")
        void shouldThrowExceptionWhenRemovingNonExistentItem() {
            // given
            Cart cart = createCartWithRestoredItem();
            CartItemId nonExistentId = CartItemId.of(999L);

            // when & then
            assertThrows(
                    CartItemNotFoundException.class, () -> cart.removeItem(nonExistentId, NOW));
        }
    }

    @Nested
    @DisplayName("clear() - 장바구니 비우기")
    class Clear {

        @Test
        @DisplayName("장바구니를 비울 수 있다")
        void shouldClearCart() {
            // given
            Cart cart = createCartWithMultipleRestoredItems();

            // when
            Cart cleared = cart.clear(NOW);

            // then
            assertTrue(cleared.isEmpty());
            assertEquals(0, cleared.itemCount());
        }
    }

    @Nested
    @DisplayName("금액 계산")
    class AmountCalculation {

        @Test
        @DisplayName("총 금액이 올바르게 계산된다")
        void shouldCalculateTotalAmount() {
            // given
            Cart cart = Cart.forNew(MEMBER_ID, NOW);
            CartItem item1 = createCartItem(100L, 2, BigDecimal.valueOf(10000));
            CartItem item2 = createCartItem(200L, 3, BigDecimal.valueOf(5000));
            Cart updated = cart.addItem(item1, NOW).addItem(item2, NOW);

            // when
            BigDecimal total = updated.totalAmount();

            // then
            assertEquals(BigDecimal.valueOf(35000), total);
        }

        @Test
        @DisplayName("선택된 아이템 총 금액이 올바르게 계산된다")
        void shouldCalculateSelectedTotalAmount() {
            // given
            Cart cart = createCartWithMultipleRestoredItems();
            CartItemId firstItemId = cart.items().get(0).id();
            Cart withDeselected = cart.updateSelected(firstItemId, false, NOW);

            // when
            BigDecimal selectedTotal = withDeselected.selectedTotalAmount();
            BigDecimal total = withDeselected.totalAmount();

            // then
            assertTrue(selectedTotal.compareTo(total) < 0);
        }
    }

    @Nested
    @DisplayName("판매자별 그룹핑")
    class GroupBySeller {

        @Test
        @DisplayName("판매자별로 아이템을 그룹핑할 수 있다")
        void shouldGroupItemsBySeller() {
            // given
            Cart cart = Cart.forNew(MEMBER_ID, NOW);
            CartItem item1 = createCartItemWithSeller(100L, 1L, 2);
            CartItem item2 = createCartItemWithSeller(200L, 1L, 3);
            CartItem item3 = createCartItemWithSeller(300L, 2L, 1);
            Cart updated = cart.addItem(item1, NOW).addItem(item2, NOW).addItem(item3, NOW);

            // when
            Map<Long, List<CartItem>> grouped = updated.groupItemsBySeller();

            // then
            assertEquals(2, grouped.size());
            assertEquals(2, grouped.get(1L).size());
            assertEquals(1, grouped.get(2L).size());
        }
    }

    @Nested
    @DisplayName("softDeleteItems() - 아이템 소프트 딜리트")
    class SoftDeleteItems {

        @Test
        @DisplayName("지정한 CartItemId의 아이템을 소프트 딜리트할 수 있다")
        void shouldSoftDeleteSpecifiedItems() {
            // given
            Cart cart = createCartWithMultipleRestoredItems();
            List<CartItemId> idsToDelete = List.of(CartItemId.of(1L), CartItemId.of(2L));

            // when
            Cart deletedCart = cart.softDeleteItems(idsToDelete, NOW);

            // then
            assertEquals(1, deletedCart.activeItems().size());
            assertEquals(2, deletedCart.deletedItems().size());
            assertTrue(deletedCart.deletedItems().stream().allMatch(CartItem::isDeleted));
        }

        @Test
        @DisplayName("존재하지 않는 CartItemId는 무시한다")
        void shouldIgnoreNonExistentIds() {
            // given
            Cart cart = createCartWithMultipleRestoredItems();
            List<CartItemId> idsToDelete = List.of(CartItemId.of(999L));

            // when
            Cart deletedCart = cart.softDeleteItems(idsToDelete, NOW);

            // then
            assertEquals(3, deletedCart.activeItems().size());
            assertEquals(0, deletedCart.deletedItems().size());
        }
    }

    @Nested
    @DisplayName("restoreItems() - 아이템 복원")
    class RestoreItems {

        @Test
        @DisplayName("소프트 딜리트된 아이템을 복원할 수 있다")
        void shouldRestoreDeletedItems() {
            // given
            Cart cart = createCartWithMultipleRestoredItems();
            List<CartItemId> idsToDelete = List.of(CartItemId.of(1L), CartItemId.of(2L));
            Cart deletedCart = cart.softDeleteItems(idsToDelete, NOW);

            // when
            Cart restoredCart = deletedCart.restoreItems(idsToDelete, NOW);

            // then
            assertEquals(3, restoredCart.activeItems().size());
            assertEquals(0, restoredCart.deletedItems().size());
            assertTrue(restoredCart.activeItems().stream().allMatch(CartItem::isActive));
        }
    }

    @Nested
    @DisplayName("activeItems() / deletedItems() - 아이템 필터링")
    class ItemFiltering {

        @Test
        @DisplayName("활성 아이템만 조회할 수 있다")
        void shouldReturnOnlyActiveItems() {
            // given
            Cart cart = createCartWithMultipleRestoredItems();
            List<CartItemId> idsToDelete = List.of(CartItemId.of(1L));
            Cart deletedCart = cart.softDeleteItems(idsToDelete, NOW);

            // when
            List<CartItem> activeItems = deletedCart.activeItems();

            // then
            assertEquals(2, activeItems.size());
            assertTrue(activeItems.stream().noneMatch(item -> item.id().equals(CartItemId.of(1L))));
        }

        @Test
        @DisplayName("삭제된 아이템만 조회할 수 있다")
        void shouldReturnOnlyDeletedItems() {
            // given
            Cart cart = createCartWithMultipleRestoredItems();
            List<CartItemId> idsToDelete = List.of(CartItemId.of(1L));
            Cart deletedCart = cart.softDeleteItems(idsToDelete, NOW);

            // when
            List<CartItem> deletedItems = deletedCart.deletedItems();

            // then
            assertEquals(1, deletedItems.size());
            assertTrue(deletedItems.stream().allMatch(item -> item.id().equals(CartItemId.of(1L))));
        }
    }

    @Nested
    @DisplayName("restore() - 영속화 데이터 복원")
    class Restore {

        @Test
        @DisplayName("영속화된 데이터를 복원할 수 있다")
        void shouldRestoreFromPersistedData() {
            // given
            CartId id = CartId.of(1L);
            UUID memberId = UUID.fromString("019538ab-faac-7ab6-9ab0-17e7f91a51c7");
            List<CartItem> items =
                    List.of(
                            CartItem.restore(
                                    CartItemId.of(1L),
                                    100L,
                                    10L,
                                    1L,
                                    1L,
                                    2,
                                    BigDecimal.valueOf(10000),
                                    true,
                                    NOW,
                                    null));
            Instant createdAt = NOW.minusSeconds(3600);
            Instant updatedAt = NOW;

            // when
            Cart cart = Cart.restore(id, memberId, items, createdAt, updatedAt);

            // then
            assertEquals(id, cart.id());
            assertEquals(memberId, cart.memberId());
            assertEquals(1, cart.itemCount());
            assertFalse(cart.isNew());
            assertEquals(createdAt, cart.createdAt());
            assertEquals(updatedAt, cart.updatedAt());
        }
    }

    // ===== Helper Methods =====

    private CartItem createCartItem(Long productStockId, int quantity) {
        return createCartItem(productStockId, quantity, BigDecimal.valueOf(10000));
    }

    private CartItem createCartItem(Long productStockId, int quantity, BigDecimal unitPrice) {
        return CartItem.forNew(
                productStockId, productStockId * 10, productStockId, 1L, quantity, unitPrice, NOW);
    }

    private CartItem createCartItemWithSeller(Long productStockId, Long sellerId, int quantity) {
        return CartItem.forNew(
                productStockId,
                productStockId * 10,
                productStockId,
                sellerId,
                quantity,
                BigDecimal.valueOf(10000),
                NOW);
    }

    private Cart createCartWithRestoredItem() {
        CartId cartId = CartId.of(1L);
        List<CartItem> items = new ArrayList<>();
        items.add(
                CartItem.restore(
                        CartItemId.of(1L),
                        100L,
                        10L,
                        1L,
                        1L,
                        2,
                        BigDecimal.valueOf(10000),
                        true,
                        NOW,
                        null));
        return Cart.restore(cartId, MEMBER_ID, items, NOW, NOW);
    }

    private Cart createCartWithMultipleRestoredItems() {
        CartId cartId = CartId.of(1L);
        List<CartItem> items = new ArrayList<>();
        items.add(
                CartItem.restore(
                        CartItemId.of(1L),
                        100L,
                        10L,
                        1L,
                        1L,
                        2,
                        BigDecimal.valueOf(10000),
                        true,
                        NOW,
                        null));
        items.add(
                CartItem.restore(
                        CartItemId.of(2L),
                        200L,
                        20L,
                        2L,
                        1L,
                        3,
                        BigDecimal.valueOf(5000),
                        true,
                        NOW,
                        null));
        items.add(
                CartItem.restore(
                        CartItemId.of(3L),
                        300L,
                        30L,
                        3L,
                        2L,
                        1,
                        BigDecimal.valueOf(15000),
                        true,
                        NOW,
                        null));
        return Cart.restore(cartId, MEMBER_ID, items, NOW, NOW);
    }
}
