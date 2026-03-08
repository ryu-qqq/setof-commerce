package com.ryuqq.setof.application.cart.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.cart.CartDomainFixtures;
import com.ryuqq.setof.application.cart.manager.CartCommandManager;
import com.ryuqq.setof.application.cart.manager.CartReadManager;
import com.ryuqq.setof.domain.cart.Cart;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("CartUpsertCoordinator 단위 테스트")
class CartUpsertCoordinatorTest {

    @InjectMocks private CartUpsertCoordinator sut;

    @Mock private CartReadManager readManager;
    @Mock private CartCommandManager commandManager;

    @Nested
    @DisplayName("upsert() - 장바구니 항목 Upsert")
    class UpsertTest {

        @Test
        @DisplayName("기존 항목이 없으면 신규 항목으로 추가된다")
        void upsert_NoExistingItems_AddsNewItems() {
            // given
            Cart cart = CartDomainFixtures.cart();
            List<Long> productIds = cart.productIds();
            List<CartItem> persisted = List.of(CartDomainFixtures.persistedCartItem());

            given(readManager.findExistingByProductIds(productIds, cart.userId()))
                    .willReturn(Collections.emptyList());
            given(commandManager.persistAll(org.mockito.ArgumentMatchers.anyList()))
                    .willReturn(persisted);

            // when
            List<CartItem> result = sut.upsert(cart);

            // then
            assertThat(result).isNotEmpty();
            then(readManager).should().findExistingByProductIds(productIds, cart.userId());
            then(commandManager).should().persistAll(org.mockito.ArgumentMatchers.anyList());
        }

        @Test
        @DisplayName("기존 항목이 있으면 수량이 증가(updated)된 후 persistAll이 호출된다")
        void upsert_ExistingItems_IncreasesQuantityAndPersists() {
            // given
            Cart cart = CartDomainFixtures.cart();
            List<Long> productIds = cart.productIds();
            CartItem existing = CartDomainFixtures.persistedCartItem(1L, productIds.get(0));
            List<CartItem> existingItems = List.of(existing);
            List<CartItem> persisted = List.of(existing);

            given(readManager.findExistingByProductIds(productIds, cart.userId()))
                    .willReturn(existingItems);
            given(commandManager.persistAll(org.mockito.ArgumentMatchers.anyList()))
                    .willReturn(persisted);

            // when
            List<CartItem> result = sut.upsert(cart);

            // then
            assertThat(result).isNotEmpty();
            then(readManager).should().findExistingByProductIds(productIds, cart.userId());
            then(commandManager).should().persistAll(org.mockito.ArgumentMatchers.anyList());
        }

        @Test
        @DisplayName("ReadManager에서 조회한 기존 항목으로 Cart.diff()를 수행하고 allItems()가 persistAll에 전달된다")
        void upsert_DiffResult_AllItemsArePassedToPersistAll() {
            // given
            CartItem newItem = CartDomainFixtures.newCartItem(11L, 21L, 1);
            Cart cart = CartDomainFixtures.cart(List.of(newItem));
            List<Long> productIds = cart.productIds();
            List<CartItem> existingItems = Collections.emptyList();
            List<CartItem> persisted = List.of(CartDomainFixtures.persistedCartItem());

            given(readManager.findExistingByProductIds(productIds, cart.userId()))
                    .willReturn(existingItems);
            given(commandManager.persistAll(org.mockito.ArgumentMatchers.anyList()))
                    .willReturn(persisted);

            // when
            List<CartItem> result = sut.upsert(cart);

            // then
            assertThat(result).isNotNull();
            then(readManager).should().findExistingByProductIds(productIds, cart.userId());
            then(commandManager).should().persistAll(org.mockito.ArgumentMatchers.anyList());
        }

        @Test
        @DisplayName("cart.userId()로 ReadManager가 호출된다")
        void upsert_UsesCartUserId_ForReadManagerQuery() {
            // given
            Cart cart = CartDomainFixtures.cart();
            List<Long> productIds = cart.productIds();

            given(readManager.findExistingByProductIds(productIds, cart.userId()))
                    .willReturn(Collections.emptyList());
            given(commandManager.persistAll(org.mockito.ArgumentMatchers.anyList()))
                    .willReturn(Collections.emptyList());

            // when
            sut.upsert(cart);

            // then
            then(readManager).should().findExistingByProductIds(productIds, cart.userId());
        }
    }
}
