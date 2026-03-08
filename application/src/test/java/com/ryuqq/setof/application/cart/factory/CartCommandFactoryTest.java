package com.ryuqq.setof.application.cart.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.cart.CartCommandFixtures;
import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.DeleteCartItemsCommand;
import com.ryuqq.setof.application.cart.dto.command.ModifyCartItemCommand;
import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.domain.cart.Cart;
import com.ryuqq.setof.domain.cart.id.CartItemId;
import com.ryuqq.setof.domain.cart.vo.CartItemUpdateData;
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
@DisplayName("CartCommandFactory 단위 테스트")
class CartCommandFactoryTest {

    @InjectMocks private CartCommandFactory sut;

    @Mock private TimeProvider timeProvider;

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    @Nested
    @DisplayName("create() - AddCartItemCommand → Cart 변환")
    class CreateTest {

        @Test
        @DisplayName("AddCartItemCommand를 Cart 도메인 객체로 변환한다")
        void create_ValidCommand_ReturnsCart() {
            // given
            AddCartItemCommand command = CartCommandFixtures.addCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            Cart result = sut.create(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.memberId()).isEqualTo(command.memberId());
            assertThat(result.userId()).isEqualTo(command.userId());
        }

        @Test
        @DisplayName("커맨드의 항목 수만큼 CartItem이 생성된다")
        void create_MultipleItems_CreatesCorrectNumberOfCartItems() {
            // given
            AddCartItemCommand command = CartCommandFixtures.addCommandWithMultipleItems();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            Cart result = sut.create(command);

            // then
            assertThat(result.items()).hasSize(command.items().size());
        }

        @Test
        @DisplayName("생성된 CartItem의 productId가 커맨드와 일치한다")
        void create_CartItems_HaveCorrectProductIds() {
            // given
            AddCartItemCommand command = CartCommandFixtures.addCommand();
            AddCartItemCommand.CartItemDetail detail = command.items().get(0);
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            Cart result = sut.create(command);

            // then
            assertThat(result.items().get(0).productIdValue()).isEqualTo(detail.productId());
        }

        @Test
        @DisplayName("생성된 CartItem의 수량이 커맨드와 일치한다")
        void create_CartItems_HaveCorrectQuantity() {
            // given
            AddCartItemCommand command = CartCommandFixtures.addCommand(5);
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            Cart result = sut.create(command);

            // then
            assertThat(result.items().get(0).quantityValue()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("createUpdateContext() - ModifyCartItemCommand → UpdateContext 변환")
    class CreateUpdateContextTest {

        @Test
        @DisplayName("ModifyCartItemCommand를 UpdateContext로 변환한다")
        void createUpdateContext_ValidCommand_ReturnsUpdateContext() {
            // given
            ModifyCartItemCommand command = CartCommandFixtures.modifyCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            UpdateContext<CartItemId, CartItemUpdateData> result = sut.createUpdateContext(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id().value()).isEqualTo(command.cartId());
        }

        @Test
        @DisplayName("UpdateContext의 updateData에 새 수량이 반영된다")
        void createUpdateContext_NewQuantity_IsReflectedInUpdateData() {
            // given
            ModifyCartItemCommand command = CartCommandFixtures.modifyCommand(1L, 7);
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            UpdateContext<CartItemId, CartItemUpdateData> result = sut.createUpdateContext(command);

            // then
            assertThat(result.updateData().quantity().value()).isEqualTo(7);
        }

        @Test
        @DisplayName("UpdateContext의 changedAt이 TimeProvider에서 제공한 시간과 일치한다")
        void createUpdateContext_ChangedAt_MatchesTimeProvider() {
            // given
            ModifyCartItemCommand command = CartCommandFixtures.modifyCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            UpdateContext<CartItemId, CartItemUpdateData> result = sut.createUpdateContext(command);

            // then
            assertThat(result.changedAt()).isEqualTo(FIXED_NOW);
        }
    }

    @Nested
    @DisplayName("createDeleteContext() - DeleteCartItemsCommand → StatusChangeContext 변환")
    class CreateDeleteContextTest {

        @Test
        @DisplayName("DeleteCartItemsCommand를 StatusChangeContext로 변환한다")
        void createDeleteContext_ValidCommand_ReturnsStatusChangeContext() {
            // given
            DeleteCartItemsCommand command = CartCommandFixtures.deleteCommandMultiple();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            StatusChangeContext<List<Long>> result = sut.createDeleteContext(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).containsExactlyInAnyOrderElementsOf(command.cartIds());
        }

        @Test
        @DisplayName("StatusChangeContext의 changedAt이 TimeProvider에서 제공한 시간과 일치한다")
        void createDeleteContext_ChangedAt_MatchesTimeProvider() {
            // given
            DeleteCartItemsCommand command = CartCommandFixtures.deleteCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            StatusChangeContext<List<Long>> result = sut.createDeleteContext(command);

            // then
            assertThat(result.changedAt()).isEqualTo(FIXED_NOW);
        }

        @Test
        @DisplayName("단건 삭제 커맨드에서도 StatusChangeContext가 정상 생성된다")
        void createDeleteContext_SingleId_ReturnsContextWithSingleId() {
            // given
            DeleteCartItemsCommand command = CartCommandFixtures.deleteCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            StatusChangeContext<List<Long>> result = sut.createDeleteContext(command);

            // then
            assertThat(result.id()).hasSize(1);
            assertThat(result.id()).contains(CartCommandFixtures.CART_ID);
        }
    }
}
