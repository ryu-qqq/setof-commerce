package com.ryuqq.setof.application.payment.internal;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.cart.manager.CartCommandManager;
import com.ryuqq.setof.application.cart.manager.CartReadManager;
import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.vo.CartCheckoutItem;
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
@DisplayName("CartCheckoutCoordinator 단위 테스트")
class CartCheckoutCoordinatorTest {

    @InjectMocks private CartCheckoutCoordinator sut;

    @Mock private CartReadManager cartReadManager;
    @Mock private CartCommandManager cartCommandManager;

    @Nested
    @DisplayName("checkoutAndRemove() - 장바구니 소프트 삭제 처리")
    class CheckoutAndRemoveTest {

        @Test
        @DisplayName("cartCheckoutItems가 비어있으면 아무것도 호출되지 않는다")
        void checkoutAndRemove_EmptyList_DoesNothing() {
            // when
            sut.checkoutAndRemove(List.of());

            // then
            then(cartReadManager).shouldHaveNoInteractions();
            then(cartCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("cartReadManager.findByCartIdsAndUserId를 호출하여 CartItem을 조회한다")
        void checkoutAndRemove_ValidItems_QueriesCartItemsByCartIdsAndUserId() {
            // given
            long userId = PaymentCommandFixtures.DEFAULT_USER_ID;
            List<CartCheckoutItem> checkoutItems =
                    List.of(new CartCheckoutItem(1L, userId), new CartCheckoutItem(2L, userId));
            List<Long> expectedCartIds = List.of(1L, 2L);

            List<CartItem> cartItems = buildCartItems();
            given(cartReadManager.findByCartIdsAndUserId(expectedCartIds, userId))
                    .willReturn(cartItems);
            given(cartCommandManager.persistAll(anyList())).willReturn(cartItems);

            // when
            sut.checkoutAndRemove(checkoutItems);

            // then
            then(cartReadManager).should().findByCartIdsAndUserId(expectedCartIds, userId);
        }

        @Test
        @DisplayName("조회한 CartItem들의 remove()가 호출된 후 persistAll이 호출된다")
        void checkoutAndRemove_AfterRemove_PersistsAllCartItems() {
            // given
            long userId = PaymentCommandFixtures.DEFAULT_USER_ID;
            List<CartCheckoutItem> checkoutItems =
                    List.of(new CartCheckoutItem(1L, userId), new CartCheckoutItem(2L, userId));

            List<CartItem> cartItems = buildCartItems();
            given(cartReadManager.findByCartIdsAndUserId(anyList(), anyLong()))
                    .willReturn(cartItems);
            given(cartCommandManager.persistAll(cartItems)).willReturn(cartItems);

            // when
            sut.checkoutAndRemove(checkoutItems);

            // then
            then(cartCommandManager).should().persistAll(cartItems);
        }

        @Test
        @DisplayName("userId는 checkoutItems의 첫 번째 항목에서 추출된다")
        void checkoutAndRemove_UserIdExtractedFromFirstItem() {
            // given
            long userId = 999L;
            List<CartCheckoutItem> checkoutItems =
                    List.of(new CartCheckoutItem(10L, userId), new CartCheckoutItem(20L, userId));

            List<CartItem> cartItems = buildCartItems();
            given(cartReadManager.findByCartIdsAndUserId(List.of(10L, 20L), userId))
                    .willReturn(cartItems);
            given(cartCommandManager.persistAll(cartItems)).willReturn(cartItems);

            // when
            sut.checkoutAndRemove(checkoutItems);

            // then
            then(cartReadManager).should().findByCartIdsAndUserId(List.of(10L, 20L), userId);
        }

        @Test
        @DisplayName("단일 CartCheckoutItem으로도 정상 처리된다")
        void checkoutAndRemove_SingleItem_ProcessesCorrectly() {
            // given
            long userId = PaymentCommandFixtures.DEFAULT_USER_ID;
            long cartId = PaymentCommandFixtures.DEFAULT_CART_ID;
            List<CartCheckoutItem> checkoutItems = List.of(new CartCheckoutItem(cartId, userId));

            List<CartItem> cartItems = List.of(buildSingleCartItem());
            given(cartReadManager.findByCartIdsAndUserId(List.of(cartId), userId))
                    .willReturn(cartItems);
            given(cartCommandManager.persistAll(cartItems)).willReturn(cartItems);

            // when
            sut.checkoutAndRemove(checkoutItems);

            // then
            then(cartReadManager).should().findByCartIdsAndUserId(List.of(cartId), userId);
            then(cartCommandManager).should().persistAll(cartItems);
        }

        // ===== 헬퍼 =====

        private List<CartItem> buildCartItems() {
            return List.of(buildSingleCartItem(), buildSingleCartItem());
        }

        private CartItem buildSingleCartItem() {
            return com.ryuqq.setof.application.cart.CartDomainFixtures.persistedCartItem(1L);
        }
    }
}
