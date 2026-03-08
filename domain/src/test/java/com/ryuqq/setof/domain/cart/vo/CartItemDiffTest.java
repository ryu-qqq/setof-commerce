package com.ryuqq.setof.domain.cart.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.setof.commerce.domain.cart.CartFixtures;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CartItemDiff Value Object 단위 테스트")
class CartItemDiffTest {

    @Nested
    @DisplayName("of() - 생성")
    class OfTest {

        @Test
        @DisplayName("added/updated/occurredAt으로 CartItemDiff를 생성한다")
        void createWithAllFields() {
            // given
            List<CartItem> added = List.of(CartFixtures.newCartItem(201L));
            List<CartItem> updated = List.of(CartFixtures.activeCartItem(1L, 200L));
            Instant occurredAt = CommonVoFixtures.now();

            // when
            CartItemDiff diff = CartItemDiff.of(added, updated, occurredAt);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.updated()).hasSize(1);
            assertThat(diff.occurredAt()).isEqualTo(occurredAt);
        }

        @Test
        @DisplayName("빈 리스트로 CartItemDiff를 생성할 수 있다")
        void createWithEmptyLists() {
            // when
            CartItemDiff diff = CartItemDiff.of(List.of(), List.of(), CommonVoFixtures.now());

            // then
            assertThat(diff.added()).isEmpty();
            assertThat(diff.updated()).isEmpty();
        }

        @Test
        @DisplayName("생성된 CartItemDiff의 added 리스트는 불변이다")
        void addedListIsImmutable() {
            // given
            CartItemDiff diff =
                    CartItemDiff.of(
                            List.of(CartFixtures.newCartItem()), List.of(), CommonVoFixtures.now());

            // when & then
            try {
                diff.added().add(CartFixtures.newCartItem());
                throw new AssertionError("UnsupportedOperationException이 발생해야 합니다");
            } catch (UnsupportedOperationException e) {
                // 예상된 예외
            }
        }

        @Test
        @DisplayName("생성된 CartItemDiff의 updated 리스트는 불변이다")
        void updatedListIsImmutable() {
            // given
            CartItemDiff diff =
                    CartItemDiff.of(
                            List.of(),
                            List.of(CartFixtures.activeCartItem()),
                            CommonVoFixtures.now());

            // when & then
            try {
                diff.updated().add(CartFixtures.activeCartItem());
                throw new AssertionError("UnsupportedOperationException이 발생해야 합니다");
            } catch (UnsupportedOperationException e) {
                // 예상된 예외
            }
        }
    }

    @Nested
    @DisplayName("allItems() - 전체 아이템 반환 (updated + added 순서)")
    class AllItemsTest {

        @Test
        @DisplayName("allItems()는 updated 후 added 순서로 합산한다")
        void allItemsReturnsUpdatedThenAdded() {
            // given
            CartItem updatedItem = CartFixtures.activeCartItem(1L, 200L);
            CartItem addedItem = CartFixtures.newCartItem(201L);
            CartItemDiff diff =
                    CartItemDiff.of(
                            List.of(addedItem), List.of(updatedItem), CommonVoFixtures.now());

            // when
            List<CartItem> allItems = diff.allItems();

            // then
            assertThat(allItems).hasSize(2);
            assertThat(allItems.get(0)).isEqualTo(updatedItem); // updated가 먼저
            assertThat(allItems.get(1)).isEqualTo(addedItem); // added가 나중
        }

        @Test
        @DisplayName("updated만 있으면 allItems()는 updated만 반환한다")
        void allItemsReturnsOnlyUpdatedWhenNoAdded() {
            // given
            CartItem updatedItem = CartFixtures.activeCartItem();
            CartItemDiff diff =
                    CartItemDiff.of(List.of(), List.of(updatedItem), CommonVoFixtures.now());

            // when
            List<CartItem> allItems = diff.allItems();

            // then
            assertThat(allItems).hasSize(1);
            assertThat(allItems).containsExactly(updatedItem);
        }

        @Test
        @DisplayName("added만 있으면 allItems()는 added만 반환한다")
        void allItemsReturnsOnlyAddedWhenNoUpdated() {
            // given
            CartItem addedItem = CartFixtures.newCartItem();
            CartItemDiff diff =
                    CartItemDiff.of(List.of(addedItem), List.of(), CommonVoFixtures.now());

            // when
            List<CartItem> allItems = diff.allItems();

            // then
            assertThat(allItems).hasSize(1);
            assertThat(allItems).containsExactly(addedItem);
        }

        @Test
        @DisplayName("added와 updated 모두 없으면 allItems()는 빈 목록을 반환한다")
        void allItemsReturnsEmptyWhenBothEmpty() {
            // given
            CartItemDiff diff = CartItemDiff.of(List.of(), List.of(), CommonVoFixtures.now());

            // when
            List<CartItem> allItems = diff.allItems();

            // then
            assertThat(allItems).isEmpty();
        }

        @Test
        @DisplayName("allItems()가 반환하는 리스트는 불변이다")
        void allItemsReturnsImmutableList() {
            // given
            CartItemDiff diff =
                    CartItemDiff.of(
                            List.of(CartFixtures.newCartItem()),
                            List.of(CartFixtures.activeCartItem()),
                            CommonVoFixtures.now());

            // when & then
            try {
                diff.allItems().add(CartFixtures.newCartItem());
                throw new AssertionError("UnsupportedOperationException이 발생해야 합니다");
            } catch (UnsupportedOperationException e) {
                // 예상된 예외
            }
        }

        @Test
        @DisplayName("allItems()의 총 개수는 added + updated 합계이다")
        void allItemsSizeEqualsSumOfAddedAndUpdated() {
            // given
            List<CartItem> added =
                    List.of(CartFixtures.newCartItem(201L), CartFixtures.newCartItem(202L));
            List<CartItem> updated =
                    List.of(
                            CartFixtures.activeCartItem(1L, 200L),
                            CartFixtures.activeCartItem(2L, 203L),
                            CartFixtures.activeCartItem(3L, 204L));
            CartItemDiff diff = CartItemDiff.of(added, updated, CommonVoFixtures.now());

            // when
            List<CartItem> allItems = diff.allItems();

            // then
            assertThat(allItems).hasSize(added.size() + updated.size());
        }
    }

    @Nested
    @DisplayName("hasNoChanges() - 변경 없음 여부 확인")
    class HasNoChangesTest {

        @Test
        @DisplayName("added와 updated 모두 비어있으면 hasNoChanges()가 true이다")
        void hasNoChangesReturnsTrueWhenBothEmpty() {
            // given
            CartItemDiff diff = CartItemDiff.of(List.of(), List.of(), CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isTrue();
        }

        @Test
        @DisplayName("added가 있으면 hasNoChanges()가 false이다")
        void hasNoChangesReturnsFalseWhenAdded() {
            // given
            CartItemDiff diff =
                    CartItemDiff.of(
                            List.of(CartFixtures.newCartItem()), List.of(), CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("updated가 있으면 hasNoChanges()가 false이다")
        void hasNoChangesReturnsFalseWhenUpdated() {
            // given
            CartItemDiff diff =
                    CartItemDiff.of(
                            List.of(),
                            List.of(CartFixtures.activeCartItem()),
                            CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("added와 updated 모두 있으면 hasNoChanges()가 false이다")
        void hasNoChangesReturnsFalseWhenBothPresent() {
            // given
            CartItemDiff diff =
                    CartItemDiff.of(
                            List.of(CartFixtures.newCartItem()),
                            List.of(CartFixtures.activeCartItem()),
                            CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }
    }
}
