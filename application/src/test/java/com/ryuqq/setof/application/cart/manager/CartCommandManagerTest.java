package com.ryuqq.setof.application.cart.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.cart.CartDomainFixtures;
import com.ryuqq.setof.application.cart.port.out.command.CartCommandPort;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
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
@DisplayName("CartCommandManager 단위 테스트")
class CartCommandManagerTest {

    @InjectMocks private CartCommandManager sut;

    @Mock private CartCommandPort commandPort;

    @Nested
    @DisplayName("persist() - 단건 장바구니 항목 저장")
    class PersistTest {

        @Test
        @DisplayName("CartItem을 저장하고 저장된 CartItem을 반환한다")
        void persist_SavesCartItem_ReturnsSavedCartItem() {
            // given
            CartItem cartItem = CartDomainFixtures.persistedCartItem();

            given(commandPort.persist(cartItem)).willReturn(cartItem);

            // when
            CartItem result = sut.persist(cartItem);

            // then
            assertThat(result).isEqualTo(cartItem);
            then(commandPort).should().persist(cartItem);
        }
    }

    @Nested
    @DisplayName("persistAll() - 복수 장바구니 항목 저장")
    class PersistAllTest {

        @Test
        @DisplayName("CartItem 목록을 저장하고 저장된 목록을 반환한다")
        void persistAll_SavesAllCartItems_ReturnsSavedList() {
            // given
            List<CartItem> cartItems = CartDomainFixtures.persistedCartItems();

            given(commandPort.persistAll(cartItems)).willReturn(cartItems);

            // when
            List<CartItem> result = sut.persistAll(cartItems);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).isEqualTo(cartItems);
            then(commandPort).should().persistAll(cartItems);
        }

        @Test
        @DisplayName("단건 목록도 persistAll로 처리된다")
        void persistAll_SingleItem_ReturnsSingleItemList() {
            // given
            List<CartItem> cartItems = List.of(CartDomainFixtures.persistedCartItem(1L));

            given(commandPort.persistAll(cartItems)).willReturn(cartItems);

            // when
            List<CartItem> result = sut.persistAll(cartItems);

            // then
            assertThat(result).hasSize(1);
            then(commandPort).should().persistAll(cartItems);
        }
    }
}
