package com.ryuqq.setof.application.cart.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.cart.CartCommandFixtures;
import com.ryuqq.setof.application.cart.CartDomainFixtures;
import com.ryuqq.setof.application.cart.manager.CartReadManager;
import com.ryuqq.setof.application.cart.manager.CartStockReadManager;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.exception.CartItemNotFoundException;
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
@DisplayName("CartValidator 단위 테스트")
class CartValidatorTest {

    @InjectMocks private CartValidator sut;

    @Mock private CartReadManager readManager;
    @Mock private CartStockReadManager stockReadManager;

    @Nested
    @DisplayName("getExistingCartItem() - 단건 장바구니 항목 조회 및 존재 검증")
    class GetExistingCartItemTest {

        @Test
        @DisplayName("존재하는 장바구니 항목을 cartId와 userId로 조회하여 반환한다")
        void getExistingCartItem_ExistingItem_ReturnsCartItem() {
            // given
            Long cartId = CartCommandFixtures.CART_ID;
            Long userId = CartCommandFixtures.USER_ID;
            CartItem expected = CartDomainFixtures.persistedCartItem(cartId);

            given(readManager.getByCartIdAndUserId(cartId, userId)).willReturn(expected);

            // when
            CartItem result = sut.getExistingCartItem(cartId, userId);

            // then
            assertThat(result).isEqualTo(expected);
            then(readManager).should().getByCartIdAndUserId(cartId, userId);
        }

        @Test
        @DisplayName("존재하지 않는 장바구니 항목이면 ReadManager에서 예외가 전파된다")
        void getExistingCartItem_NonExistingItem_PropagatesException() {
            // given
            Long cartId = 999L;
            Long userId = CartCommandFixtures.USER_ID;

            given(readManager.getByCartIdAndUserId(cartId, userId))
                    .willThrow(new CartItemNotFoundException(cartId));

            // when & then
            assertThatThrownBy(() -> sut.getExistingCartItem(cartId, userId))
                    .isInstanceOf(CartItemNotFoundException.class);

            then(readManager).should().getByCartIdAndUserId(cartId, userId);
        }
    }

    @Nested
    @DisplayName("getExistingCartItems() - 복수 장바구니 항목 조회 및 전체 존재 검증")
    class GetExistingCartItemsTest {

        @Test
        @DisplayName("요청한 모든 cartId가 조회되면 CartItem 목록을 반환한다")
        void getExistingCartItems_AllFound_ReturnsCartItemList() {
            // given
            List<Long> cartIds = List.of(1L, 2L, 3L);
            Long userId = CartCommandFixtures.USER_ID;
            List<CartItem> found = CartDomainFixtures.persistedCartItems(cartIds);

            given(readManager.findByCartIdsAndUserId(cartIds, userId)).willReturn(found);

            // when
            List<CartItem> result = sut.getExistingCartItems(cartIds, userId);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).isEqualTo(found);
            then(readManager).should().findByCartIdsAndUserId(cartIds, userId);
        }

        @Test
        @DisplayName("요청한 cartId 중 일부가 누락되면 CartItemNotFoundException이 발생한다")
        void getExistingCartItems_SomeMissing_ThrowsCartItemNotFoundException() {
            // given
            List<Long> cartIds = List.of(1L, 2L, 999L);
            Long userId = CartCommandFixtures.USER_ID;
            List<CartItem> partialFound = CartDomainFixtures.persistedCartItems(List.of(1L, 2L));

            given(readManager.findByCartIdsAndUserId(cartIds, userId)).willReturn(partialFound);

            // when & then
            assertThatThrownBy(() -> sut.getExistingCartItems(cartIds, userId))
                    .isInstanceOf(CartItemNotFoundException.class);
        }

        @Test
        @DisplayName("첫 번째 누락 ID로 예외가 발생한다")
        void getExistingCartItems_MissingId_ExceptionContainsMissingId() {
            // given
            Long missingId = 999L;
            List<Long> cartIds = List.of(1L, missingId);
            Long userId = CartCommandFixtures.USER_ID;
            List<CartItem> partialFound = CartDomainFixtures.persistedCartItems(List.of(1L));

            given(readManager.findByCartIdsAndUserId(cartIds, userId)).willReturn(partialFound);

            // when & then
            assertThatThrownBy(() -> sut.getExistingCartItems(cartIds, userId))
                    .isInstanceOf(CartItemNotFoundException.class)
                    .hasMessageContaining(String.valueOf(missingId));
        }

        @Test
        @DisplayName("단건 요청에서도 누락이면 예외가 발생한다")
        void getExistingCartItems_SingleMissing_ThrowsException() {
            // given
            List<Long> cartIds = List.of(999L);
            Long userId = CartCommandFixtures.USER_ID;

            given(readManager.findByCartIdsAndUserId(cartIds, userId)).willReturn(List.of());

            // when & then
            assertThatThrownBy(() -> sut.getExistingCartItems(cartIds, userId))
                    .isInstanceOf(CartItemNotFoundException.class);
        }

        @Test
        @DisplayName("요청 수와 조회 수가 일치하면 예외가 발생하지 않는다")
        void getExistingCartItems_ExactMatch_NoException() {
            // given
            List<Long> cartIds = List.of(1L);
            Long userId = CartCommandFixtures.USER_ID;
            List<CartItem> found = CartDomainFixtures.persistedCartItems(cartIds);

            given(readManager.findByCartIdsAndUserId(cartIds, userId)).willReturn(found);

            // when
            List<CartItem> result = sut.getExistingCartItems(cartIds, userId);

            // then
            assertThat(result).hasSize(1);
        }
    }
}
