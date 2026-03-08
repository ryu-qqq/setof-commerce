package com.ryuqq.setof.application.cart.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.application.cart.CartCommandFixtures;
import com.ryuqq.setof.application.cart.CartDomainFixtures;
import com.ryuqq.setof.application.cart.dto.command.DeleteCartItemsCommand;
import com.ryuqq.setof.application.cart.factory.CartCommandFactory;
import com.ryuqq.setof.application.cart.manager.CartCommandManager;
import com.ryuqq.setof.application.cart.validator.CartValidator;
import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.exception.CartItemNotFoundException;
import java.time.Instant;
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
@DisplayName("DeleteCartItemsService 단위 테스트")
class DeleteCartItemsServiceTest {

    @InjectMocks private DeleteCartItemsService sut;

    @Mock private CartValidator validator;
    @Mock private CartCommandFactory factory;
    @Mock private CartCommandManager commandManager;

    @Nested
    @DisplayName("execute() - 장바구니 항목 삭제")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 장바구니 항목을 삭제하고 삭제된 항목 수를 반환한다")
        void execute_ValidCommand_ReturnsDeletedCount() {
            // given
            DeleteCartItemsCommand command = CartCommandFixtures.deleteCommandMultiple();
            List<CartItem> cartItems = CartDomainFixtures.persistedCartItems(command.cartIds());
            Instant now = CartDomainFixtures.fixedNow();
            StatusChangeContext<List<Long>> context =
                    new StatusChangeContext<>(command.cartIds(), now);

            given(validator.getExistingCartItems(command.cartIds(), command.userId()))
                    .willReturn(cartItems);
            given(factory.createDeleteContext(command)).willReturn(context);
            given(commandManager.persistAll(cartItems)).willReturn(cartItems);

            // when
            int result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(3);
            then(validator).should().getExistingCartItems(command.cartIds(), command.userId());
            then(factory).should().createDeleteContext(command);
            then(commandManager).should().persistAll(cartItems);
        }

        @Test
        @DisplayName("단건 삭제도 정상 동작한다")
        void execute_SingleItem_ReturnsOne() {
            // given
            DeleteCartItemsCommand command = CartCommandFixtures.deleteCommand();
            List<CartItem> cartItems = List.of(CartDomainFixtures.persistedCartItem(1L));
            Instant now = CartDomainFixtures.fixedNow();
            StatusChangeContext<List<Long>> context =
                    new StatusChangeContext<>(command.cartIds(), now);

            given(validator.getExistingCartItems(command.cartIds(), command.userId()))
                    .willReturn(cartItems);
            given(factory.createDeleteContext(command)).willReturn(context);
            given(commandManager.persistAll(cartItems)).willReturn(cartItems);

            // when
            int result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(1);
        }

        @Test
        @DisplayName("일부 항목이 존재하지 않으면 예외가 발생하고 Manager는 호출되지 않는다")
        void execute_SomeItemsNotFound_ThrowsExceptionWithoutCallingManager() {
            // given
            DeleteCartItemsCommand command = CartCommandFixtures.deleteCommand(List.of(1L, 999L));

            willThrow(new CartItemNotFoundException(999L))
                    .given(validator)
                    .getExistingCartItems(command.cartIds(), command.userId());

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(CartItemNotFoundException.class);

            then(factory).shouldHaveNoInteractions();
            then(commandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("remove()가 호출된 CartItem 목록이 persistAll에 전달된다")
        void execute_ItemsWithRemoveApplied_ArePassedToPersistAll() {
            // given
            DeleteCartItemsCommand command = CartCommandFixtures.deleteCommand();
            List<CartItem> cartItems = List.of(CartDomainFixtures.persistedCartItem(1L));
            Instant now = CartDomainFixtures.fixedNow();
            StatusChangeContext<List<Long>> context =
                    new StatusChangeContext<>(command.cartIds(), now);

            given(validator.getExistingCartItems(command.cartIds(), command.userId()))
                    .willReturn(cartItems);
            given(factory.createDeleteContext(command)).willReturn(context);
            given(commandManager.persistAll(cartItems)).willReturn(cartItems);

            // when
            sut.execute(command);

            // then
            then(commandManager).should().persistAll(cartItems);
        }
    }
}
