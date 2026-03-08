package com.ryuqq.setof.application.cart.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.cart.CartCommandFixtures;
import com.ryuqq.setof.application.cart.CartDomainFixtures;
import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.factory.CartCommandFactory;
import com.ryuqq.setof.application.cart.internal.CartUpsertCoordinator;
import com.ryuqq.setof.application.cart.validator.CartValidator;
import com.ryuqq.setof.domain.cart.Cart;
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
@DisplayName("AddCartItemService 단위 테스트")
class AddCartItemServiceTest {

    @InjectMocks private AddCartItemService sut;

    @Mock private CartValidator validator;
    @Mock private CartCommandFactory factory;
    @Mock private CartUpsertCoordinator coordinator;

    @Nested
    @DisplayName("execute() - 장바구니 항목 추가")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 장바구니 항목을 추가하고 저장된 CartItem 목록을 반환한다")
        void execute_ValidCommand_ReturnsPersistedCartItems() {
            // given
            AddCartItemCommand command = CartCommandFixtures.addCommand();
            Cart cart = CartDomainFixtures.cart();
            List<CartItem> expectedItems = CartDomainFixtures.persistedCartItems();

            given(factory.create(command)).willReturn(cart);
            given(coordinator.upsert(cart)).willReturn(expectedItems);

            // when
            List<CartItem> result = sut.execute(command);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).isEqualTo(expectedItems);
            then(factory).should().create(command);
            then(coordinator).should().upsert(cart);
        }

        @Test
        @DisplayName("Factory에서 생성한 Cart가 그대로 Coordinator에 전달된다")
        void execute_FactoryResult_IsPassedToCoordinator() {
            // given
            AddCartItemCommand command = CartCommandFixtures.addCommand();
            Cart cart = CartDomainFixtures.cart();
            List<CartItem> persistedItems = List.of(CartDomainFixtures.persistedCartItem());

            given(factory.create(command)).willReturn(cart);
            given(coordinator.upsert(cart)).willReturn(persistedItems);

            // when
            sut.execute(command);

            // then
            then(factory).should().create(command);
            then(coordinator).should().upsert(cart);
        }
    }
}
