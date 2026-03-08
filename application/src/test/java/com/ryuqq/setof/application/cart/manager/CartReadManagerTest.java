package com.ryuqq.setof.application.cart.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.cart.CartDomainFixtures;
import com.ryuqq.setof.application.cart.CartQueryFixtures;
import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.application.cart.port.out.query.CartItemQueryPort;
import com.ryuqq.setof.application.cart.port.out.query.CartQueryPort;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.exception.CartItemNotFoundException;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import com.ryuqq.setof.domain.cart.query.CartSortKey;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
@DisplayName("CartReadManager 단위 테스트")
class CartReadManagerTest {

    @InjectMocks private CartReadManager sut;

    @Mock private CartQueryPort queryPort;
    @Mock private CartItemQueryPort itemQueryPort;

    private CartSearchCriteria defaultCriteria() {
        return CartSearchCriteria.of(
                CartQueryFixtures.MEMBER_ID,
                CartQueryFixtures.USER_ID,
                CursorQueryContext.of(
                        CartSortKey.defaultKey(),
                        SortDirection.DESC,
                        CursorPageRequest.afterId(null, 20)));
    }

    @Nested
    @DisplayName("fetchCarts() - 장바구니 목록 조회")
    class FetchCartsTest {

        @Test
        @DisplayName("Criteria로 장바구니 목록을 조회한다")
        void fetchCarts_ValidCriteria_ReturnsCartItemResults() {
            // given
            CartSearchCriteria criteria = defaultCriteria();
            List<CartItemResult> expected = CartQueryFixtures.cartItemResults();

            given(queryPort.fetchCarts(criteria)).willReturn(expected);

            // when
            List<CartItemResult> result = sut.fetchCarts(criteria);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchCarts(criteria);
        }

        @Test
        @DisplayName("장바구니가 없으면 빈 목록을 반환한다")
        void fetchCarts_EmptyResult_ReturnsEmptyList() {
            // given
            CartSearchCriteria criteria = defaultCriteria();

            given(queryPort.fetchCarts(criteria)).willReturn(Collections.emptyList());

            // when
            List<CartItemResult> result = sut.fetchCarts(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("countCarts() - 장바구니 수 조회")
    class CountCartsTest {

        @Test
        @DisplayName("Criteria에 맞는 장바구니 수를 반환한다")
        void countCarts_ValidCriteria_ReturnsCount() {
            // given
            CartSearchCriteria criteria = defaultCriteria();
            long expected = 5L;

            given(queryPort.countCarts(criteria)).willReturn(expected);

            // when
            long result = sut.countCarts(criteria);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().countCarts(criteria);
        }
    }

    @Nested
    @DisplayName("fetchCartCount() - 장바구니 개수 결과 조회")
    class FetchCartCountTest {

        @Test
        @DisplayName("Criteria로 CartCountResult를 조회한다")
        void fetchCartCount_ValidCriteria_ReturnsCartCountResult() {
            // given
            CartSearchCriteria criteria = defaultCriteria();
            CartCountResult expected = CartQueryFixtures.cartCountResult(3L);

            given(queryPort.fetchCartCount(criteria)).willReturn(expected);

            // when
            CartCountResult result = sut.fetchCartCount(criteria);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.cartQuantity()).isEqualTo(3L);
            then(queryPort).should().fetchCartCount(criteria);
        }
    }

    @Nested
    @DisplayName("getByCartIdAndUserId() - cartId + userId로 단건 조회")
    class GetByCartIdAndUserIdTest {

        @Test
        @DisplayName("존재하는 CartItem을 반환한다")
        void getByCartIdAndUserId_ExistingItem_ReturnsCartItem() {
            // given
            Long cartId = 1L;
            Long userId = CartQueryFixtures.USER_ID;
            CartItem expected = CartDomainFixtures.persistedCartItem(cartId);

            given(itemQueryPort.findByCartIdAndUserId(cartId, userId))
                    .willReturn(Optional.of(expected));

            // when
            CartItem result = sut.getByCartIdAndUserId(cartId, userId);

            // then
            assertThat(result).isEqualTo(expected);
            then(itemQueryPort).should().findByCartIdAndUserId(cartId, userId);
        }

        @Test
        @DisplayName("존재하지 않으면 CartItemNotFoundException이 발생한다")
        void getByCartIdAndUserId_NonExisting_ThrowsCartItemNotFoundException() {
            // given
            Long cartId = 999L;
            Long userId = CartQueryFixtures.USER_ID;

            given(itemQueryPort.findByCartIdAndUserId(cartId, userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getByCartIdAndUserId(cartId, userId))
                    .isInstanceOf(CartItemNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findByCartIdsAndUserId() - 복수 cartId + userId로 조회")
    class FindByCartIdsAndUserIdTest {

        @Test
        @DisplayName("요청한 cartId 목록에 해당하는 CartItem 목록을 반환한다")
        void findByCartIdsAndUserId_ValidIds_ReturnsCartItems() {
            // given
            List<Long> cartIds = List.of(1L, 2L);
            Long userId = CartQueryFixtures.USER_ID;
            List<CartItem> expected = CartDomainFixtures.persistedCartItems(cartIds);

            given(itemQueryPort.findByCartIdsAndUserId(cartIds, userId)).willReturn(expected);

            // when
            List<CartItem> result = sut.findByCartIdsAndUserId(cartIds, userId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).isEqualTo(expected);
            then(itemQueryPort).should().findByCartIdsAndUserId(cartIds, userId);
        }

        @Test
        @DisplayName("조회 결과가 없으면 빈 목록을 반환한다")
        void findByCartIdsAndUserId_NoResults_ReturnsEmptyList() {
            // given
            List<Long> cartIds = List.of(999L);
            Long userId = CartQueryFixtures.USER_ID;

            given(itemQueryPort.findByCartIdsAndUserId(cartIds, userId))
                    .willReturn(Collections.emptyList());

            // when
            List<CartItem> result = sut.findByCartIdsAndUserId(cartIds, userId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findExistingByProductIds() - productId로 기존 CartItem 조회")
    class FindExistingByProductIdsTest {

        @Test
        @DisplayName("productId 목록으로 기존 장바구니 항목을 조회한다")
        void findExistingByProductIds_ValidProductIds_ReturnsExistingItems() {
            // given
            List<Long> productIds = List.of(20L, 21L);
            Long userId = CartQueryFixtures.USER_ID;
            List<CartItem> expected =
                    List.of(
                            CartDomainFixtures.persistedCartItem(1L, 20L),
                            CartDomainFixtures.persistedCartItem(2L, 21L));

            given(itemQueryPort.findExistingByProductIds(productIds, userId)).willReturn(expected);

            // when
            List<CartItem> result = sut.findExistingByProductIds(productIds, userId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).isEqualTo(expected);
            then(itemQueryPort).should().findExistingByProductIds(productIds, userId);
        }

        @Test
        @DisplayName("기존 항목이 없으면 빈 목록을 반환한다")
        void findExistingByProductIds_NoExistingItems_ReturnsEmptyList() {
            // given
            List<Long> productIds = List.of(99L);
            Long userId = CartQueryFixtures.USER_ID;

            given(itemQueryPort.findExistingByProductIds(productIds, userId))
                    .willReturn(Collections.emptyList());

            // when
            List<CartItem> result = sut.findExistingByProductIds(productIds, userId);

            // then
            assertThat(result).isEmpty();
        }
    }
}
