package com.ryuqq.setof.application.cart.service.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.application.cart.CartCommandFixtures;
import com.ryuqq.setof.application.cart.CartDomainFixtures;
import com.ryuqq.setof.application.cart.dto.command.ModifyCartItemCommand;
import com.ryuqq.setof.application.cart.factory.CartCommandFactory;
import com.ryuqq.setof.application.cart.manager.CartCommandManager;
import com.ryuqq.setof.application.cart.validator.CartValidator;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.exception.CartItemNotFoundException;
import com.ryuqq.setof.domain.cart.id.CartItemId;
import com.ryuqq.setof.domain.cart.vo.CartItemUpdateData;
import com.ryuqq.setof.domain.cart.vo.CartQuantity;
import java.time.Instant;
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
@DisplayName("ModifyCartItemService 단위 테스트")
class ModifyCartItemServiceTest {

    @InjectMocks private ModifyCartItemService sut;

    @Mock private CartValidator validator;
    @Mock private CartCommandFactory factory;
    @Mock private CartCommandManager commandManager;

    @Nested
    @DisplayName("execute() - 장바구니 항목 수량 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 장바구니 항목 수량을 수정하고 Manager에 영속화한다")
        void execute_ValidCommand_UpdatesCartItemAndPersists() {
            // given
            ModifyCartItemCommand command = CartCommandFixtures.modifyCommand();
            CartItem cartItem = CartDomainFixtures.persistedCartItem(command.cartId());
            Instant now = CartDomainFixtures.fixedNow();
            UpdateContext<CartItemId, CartItemUpdateData> context =
                    new UpdateContext<>(
                            CartItemId.of(command.cartId()),
                            CartItemUpdateData.of(CartQuantity.of(command.newQuantity()), now),
                            now);
            CartItem persisted = CartDomainFixtures.persistedCartItem(command.cartId());

            given(validator.getExistingCartItem(command.cartId(), command.userId()))
                    .willReturn(cartItem);
            given(factory.createUpdateContext(command)).willReturn(context);
            given(commandManager.persist(cartItem)).willReturn(persisted);

            // when
            sut.execute(command);

            // then
            then(validator).should().getExistingCartItem(command.cartId(), command.userId());
            then(factory).should().createUpdateContext(command);
            then(commandManager).should().persist(cartItem);
        }

        @Test
        @DisplayName("존재하지 않는 장바구니 항목이면 예외가 발생하고 Factory/Manager는 호출되지 않는다")
        void execute_NonExistingCartItem_ThrowsExceptionWithoutCallingFactory() {
            // given
            ModifyCartItemCommand command = CartCommandFixtures.modifyCommand();

            willThrow(new CartItemNotFoundException(command.cartId()))
                    .given(validator)
                    .getExistingCartItem(command.cartId(), command.userId());

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(CartItemNotFoundException.class);

            then(factory).shouldHaveNoInteractions();
            then(commandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("update() 후 cartItem이 persist 대상으로 전달된다")
        void execute_AfterUpdate_CartItemIsPassedToPersist() {
            // given
            ModifyCartItemCommand command = CartCommandFixtures.modifyCommand(1L, 10);
            CartItem cartItem = CartDomainFixtures.persistedCartItem(1L);
            Instant now = CartDomainFixtures.fixedNow();
            UpdateContext<CartItemId, CartItemUpdateData> context =
                    new UpdateContext<>(
                            CartItemId.of(1L),
                            CartItemUpdateData.of(CartQuantity.of(10), now),
                            now);

            given(validator.getExistingCartItem(1L, command.userId())).willReturn(cartItem);
            given(factory.createUpdateContext(command)).willReturn(context);
            given(commandManager.persist(cartItem)).willReturn(cartItem);

            // when
            sut.execute(command);

            // then
            verify(commandManager).persist(cartItem);
        }
    }
}
