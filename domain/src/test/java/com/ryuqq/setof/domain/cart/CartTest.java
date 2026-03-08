package com.ryuqq.setof.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.vo.CartItemDiff;
import com.setof.commerce.domain.cart.CartFixtures;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Cart 도메인 객체 단위 테스트")
class CartTest {

    @Nested
    @DisplayName("of() - 정적 팩토리 메서드")
    class OfTest {

        @Test
        @DisplayName("필수 필드로 Cart를 생성한다")
        void createCartWithRequiredFields() {
            // when
            Cart cart = CartFixtures.defaultCart();

            // then
            assertThat(cart.memberId()).isEqualTo(CartFixtures.DEFAULT_MEMBER_ID);
            assertThat(cart.userId()).isEqualTo(CartFixtures.DEFAULT_USER_ID);
            assertThat(cart.occurredAt()).isNotNull();
            assertThat(cart.items()).isNotEmpty();
        }

        @Test
        @DisplayName("아이템 목록은 방어적 복사(불변)로 저장된다")
        void itemListIsImmutable() {
            // given
            Cart cart = CartFixtures.defaultCart();

            // when & then
            try {
                cart.items().add(CartFixtures.newCartItem());
                throw new AssertionError("UnsupportedOperationException이 발생해야 합니다");
            } catch (UnsupportedOperationException e) {
                // 예상된 예외
            }
        }

        @Test
        @DisplayName("size()는 아이템 수를 반환한다")
        void sizeReturnsItemCount() {
            // given
            List<CartItem> items =
                    List.of(
                            CartFixtures.newCartItem(201L),
                            CartFixtures.newCartItem(202L),
                            CartFixtures.newCartItem(203L));
            Cart cart = CartFixtures.cartWith(items);

            // then
            assertThat(cart.size()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("productIds() - 상품 ID 목록 반환")
    class ProductIdsTest {

        @Test
        @DisplayName("Cart의 아이템 목록에서 productId 목록을 반환한다")
        void productIdsReturnsAllProductIds() {
            // given
            List<Long> expectedProductIds = List.of(201L, 202L, 203L);
            Cart cart = CartFixtures.cartWithProductIds(expectedProductIds);

            // when
            List<Long> productIds = cart.productIds();

            // then
            assertThat(productIds).containsExactlyInAnyOrderElementsOf(expectedProductIds);
        }

        @Test
        @DisplayName("아이템이 없으면 빈 목록을 반환한다")
        void productIdsReturnsEmptyWhenNoItems() {
            // given
            Cart cart = CartFixtures.cartWith(List.of());

            // when
            List<Long> productIds = cart.productIds();

            // then
            assertThat(productIds).isEmpty();
        }
    }

    @Nested
    @DisplayName("diff() - 기존 아이템과 비교하여 Diff 생성")
    class DiffTest {

        @Test
        @DisplayName("기존 아이템이 없으면 모두 added로 분류된다")
        void allItemsAddedWhenNoExistingItems() {
            // given
            List<CartItem> newItems =
                    List.of(CartFixtures.newCartItem(201L), CartFixtures.newCartItem(202L));
            Cart cart = CartFixtures.cartWith(newItems);

            // when
            CartItemDiff diff = cart.diff(List.of());

            // then
            assertThat(diff.added()).hasSize(2);
            assertThat(diff.updated()).isEmpty();
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("신규 아이템의 productId가 기존 아이템과 일치하면 updated로 분류된다")
        void matchingProductIdIsClassifiedAsUpdated() {
            // given
            Long productId = CartFixtures.DEFAULT_PRODUCT_ID;
            Cart cart = CartFixtures.cartWith(List.of(CartFixtures.newCartItem(productId, 3)));

            CartItem existingItem = CartFixtures.activeCartItem(productId, 2);
            List<CartItem> existingItems = List.of(existingItem);

            // when
            CartItemDiff diff = cart.diff(existingItems);

            // then
            assertThat(diff.updated()).hasSize(1);
            assertThat(diff.added()).isEmpty();
        }

        @Test
        @DisplayName("기존 아이템과 매칭되면 수량이 증가된다")
        void matchingItemHasIncreasedQuantity() {
            // given
            Long productId = CartFixtures.DEFAULT_PRODUCT_ID;
            int existingQuantity = 2;
            int additionalQuantity = 3;

            Cart cart =
                    CartFixtures.cartWith(
                            List.of(CartFixtures.newCartItem(productId, additionalQuantity)));

            CartItem existingItem = CartFixtures.activeCartItem(productId, existingQuantity);
            List<CartItem> existingItems = List.of(existingItem);

            // when
            cart.diff(existingItems);

            // then - existingItem의 수량이 기존(2) + 추가(3) = 5로 증가
            assertThat(existingItem.quantityValue())
                    .isEqualTo(existingQuantity + additionalQuantity);
        }

        @Test
        @DisplayName("기존에 없는 productId는 added로 분류된다")
        void newProductIdIsClassifiedAsAdded() {
            // given
            Long existingProductId = 200L;
            Long newProductId = 201L;

            Cart cart = CartFixtures.cartWith(List.of(CartFixtures.newCartItem(newProductId)));
            CartItem existingItem = CartFixtures.activeCartItem(existingProductId, 2);

            // when
            CartItemDiff diff = cart.diff(List.of(existingItem));

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.added().get(0).productIdValue()).isEqualTo(newProductId);
            assertThat(diff.updated()).isEmpty();
        }

        @Test
        @DisplayName("일부는 updated, 일부는 added로 혼합 분류된다")
        void mixedItemsAreClassifiedCorrectly() {
            // given
            Long existingProductId = 200L;
            Long newProductId = 201L;

            List<CartItem> newItems =
                    List.of(
                            CartFixtures.newCartItem(existingProductId, 2), // 기존 아이템 -> updated
                            CartFixtures.newCartItem(newProductId, 1)); // 신규 아이템 -> added
            Cart cart = CartFixtures.cartWith(newItems);

            CartItem existingItem = CartFixtures.activeCartItem(existingProductId, 3);
            List<CartItem> existingItems = List.of(existingItem);

            // when
            CartItemDiff diff = cart.diff(existingItems);

            // then
            assertThat(diff.updated()).hasSize(1);
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.updated().get(0).productIdValue()).isEqualTo(existingProductId);
            assertThat(diff.added().get(0).productIdValue()).isEqualTo(newProductId);
        }

        @Test
        @DisplayName("Cart 아이템 전부 기존 아이템과 매칭되면 added가 없다")
        void allMatchingItemsProduceNoAdded() {
            // given
            Long productId1 = 200L;
            Long productId2 = 201L;

            Cart cart =
                    CartFixtures.cartWith(
                            List.of(
                                    CartFixtures.newCartItem(productId1, 1),
                                    CartFixtures.newCartItem(productId2, 1)));

            List<CartItem> existingItems =
                    List.of(
                            CartFixtures.activeCartItem(productId1, 2),
                            CartFixtures.activeCartItem(productId2, 3));

            // when
            CartItemDiff diff = cart.diff(existingItems);

            // then
            assertThat(diff.updated()).hasSize(2);
            assertThat(diff.added()).isEmpty();
        }

        @Test
        @DisplayName("diff()가 반환하는 CartItemDiff의 occurredAt은 Cart의 occurredAt이다")
        void diffOccurredAtMatchesCartOccurredAt() {
            // given
            Cart cart = CartFixtures.cartWith(List.of(CartFixtures.newCartItem(201L)));

            // when
            CartItemDiff diff = cart.diff(List.of());

            // then
            assertThat(diff.occurredAt()).isEqualTo(cart.occurredAt());
        }

        @Test
        @DisplayName("기존 아이템과 신규 아이템 모두 없으면 hasNoChanges가 true이다")
        void emptyCartWithNoExistingItemsHasNoChanges() {
            // given
            Cart cart = CartFixtures.cartWith(List.of());

            // when
            CartItemDiff diff = cart.diff(List.of());

            // then
            assertThat(diff.hasNoChanges()).isTrue();
        }
    }
}
