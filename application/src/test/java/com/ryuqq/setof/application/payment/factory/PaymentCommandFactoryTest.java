package com.ryuqq.setof.application.payment.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.application.payment.dto.bundle.CartPaymentCreationBundle;
import com.ryuqq.setof.application.payment.dto.bundle.PaymentCreationBundle;
import com.ryuqq.setof.application.payment.dto.command.CartOrderItemCommand;
import com.ryuqq.setof.application.payment.dto.command.CreatePaymentCommand;
import com.ryuqq.setof.application.payment.dto.command.CreatePaymentInCartCommand;
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
@DisplayName("PaymentCommandFactory 단위 테스트")
class PaymentCommandFactoryTest {

    @InjectMocks private PaymentCommandFactory sut;

    @Mock private TimeProvider timeProvider;

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    @Nested
    @DisplayName("create() - CreatePaymentCommand → PaymentCreationBundle 변환")
    class CreateTest {

        @Test
        @DisplayName("CreatePaymentCommand를 PaymentCreationBundle로 변환한다")
        void create_ValidCommand_ReturnsPaymentCreationBundle() {
            // given
            CreatePaymentCommand command = PaymentCommandFixtures.createPaymentCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            PaymentCreationBundle bundle = sut.create(command);

            // then
            assertThat(bundle).isNotNull();
            assertThat(bundle.payment()).isNotNull();
            assertThat(bundle.order()).isNotNull();
            assertThat(bundle.receiverInfo()).isNotNull();
        }

        @Test
        @DisplayName("커맨드의 userId가 Payment와 Order에 반영된다")
        void create_UserIdReflected_InPaymentAndOrder() {
            // given
            CreatePaymentCommand command = PaymentCommandFixtures.createPaymentCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            PaymentCreationBundle bundle = sut.create(command);

            // then
            assertThat(bundle.payment().legacyUserIdValue())
                    .isEqualTo(PaymentCommandFixtures.DEFAULT_USER_ID);
            assertThat(bundle.order().legacyUserIdValue())
                    .isEqualTo(PaymentCommandFixtures.DEFAULT_USER_ID);
        }

        @Test
        @DisplayName("커맨드의 주문 항목 수만큼 StockDeductionItem이 생성된다")
        void create_OrderItems_GeneratesStockDeductionItems() {
            // given
            CreatePaymentCommand command = PaymentCommandFixtures.createPaymentCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            PaymentCreationBundle bundle = sut.create(command);

            // then
            assertThat(bundle.stockDeductionItems()).hasSize(command.orderItems().size());
        }

        @Test
        @DisplayName("StockDeductionItem에 productId와 quantity가 정확히 반영된다")
        void create_StockDeductionItemHasCorrectProductIdAndQuantity() {
            // given
            CreatePaymentCommand command = PaymentCommandFixtures.createPaymentCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            PaymentCreationBundle bundle = sut.create(command);

            // then
            assertThat(bundle.stockDeductionItems().get(0).productId())
                    .isEqualTo(PaymentCommandFixtures.DEFAULT_PRODUCT_ID);
            assertThat(bundle.stockDeductionItems().get(0).quantity())
                    .isEqualTo(PaymentCommandFixtures.DEFAULT_QUANTITY);
        }

        @Test
        @DisplayName("shippingInfo가 null이 아니면 ReceiverInfo가 생성된다")
        void create_WithShippingInfo_ReturnsNonNullReceiverInfo() {
            // given
            CreatePaymentCommand command = PaymentCommandFixtures.createPaymentCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            PaymentCreationBundle bundle = sut.create(command);

            // then
            assertThat(bundle.receiverInfo()).isNotNull();
            assertThat(bundle.receiverInfo().receiverName())
                    .isEqualTo(command.shippingInfo().receiverName());
        }

        @Test
        @DisplayName("refundAccountInfo가 null이면 refundAccountSnapshot이 null이다")
        void create_WithoutRefundAccountInfo_ReturnsNullRefundAccountSnapshot() {
            // given
            CreatePaymentCommand command = PaymentCommandFixtures.createPaymentCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            PaymentCreationBundle bundle = sut.create(command);

            // then
            assertThat(bundle.refundAccountSnapshot()).isNull();
        }

        @Test
        @DisplayName("refundAccountInfo가 있으면 refundAccountSnapshot이 생성된다")
        void create_WithRefundAccountInfo_ReturnsRefundAccountSnapshot() {
            // given
            CreatePaymentCommand command =
                    PaymentCommandFixtures.createPaymentCommandWithRefundAccount();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            PaymentCreationBundle bundle = sut.create(command);

            // then
            assertThat(bundle.refundAccountSnapshot()).isNotNull();
            assertThat(bundle.refundAccountSnapshot().bankCode())
                    .isEqualTo(command.refundAccountInfo().bankCode());
            assertThat(bundle.refundAccountSnapshot().accountNumber())
                    .isEqualTo(command.refundAccountInfo().accountNumber());
        }
    }

    @Nested
    @DisplayName("createForCart() - CreatePaymentInCartCommand → CartPaymentCreationBundle 변환")
    class CreateForCartTest {

        @Test
        @DisplayName("CreatePaymentInCartCommand를 CartPaymentCreationBundle로 변환한다")
        void createForCart_ValidCommand_ReturnsCartPaymentCreationBundle() {
            // given
            CreatePaymentInCartCommand command =
                    PaymentCommandFixtures.createPaymentInCartCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            CartPaymentCreationBundle bundle = sut.createForCart(command);

            // then
            assertThat(bundle).isNotNull();
            assertThat(bundle.paymentBundle()).isNotNull();
            assertThat(bundle.cartCheckoutItems()).isNotNull();
        }

        @Test
        @DisplayName("CartOrderItemCommand가 PaymentOrderItemCommand로 변환되어 번들에 포함된다")
        void createForCart_CartOrderItemsConverted_ToPaymentOrderItems() {
            // given
            CreatePaymentInCartCommand command =
                    PaymentCommandFixtures.createPaymentInCartCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            CartPaymentCreationBundle bundle = sut.createForCart(command);

            // then
            PaymentCreationBundle paymentBundle = bundle.paymentBundle();
            assertThat(paymentBundle.stockDeductionItems()).hasSize(command.orderItems().size());
        }

        @Test
        @DisplayName("cartId가 CartCheckoutItem에 반영된다")
        void createForCart_CartIdReflected_InCartCheckoutItems() {
            // given
            CreatePaymentInCartCommand command =
                    PaymentCommandFixtures.createPaymentInCartCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            CartPaymentCreationBundle bundle = sut.createForCart(command);

            // then
            CartOrderItemCommand cartItem = command.orderItems().get(0);
            assertThat(bundle.cartCheckoutItems().get(0).cartId()).isEqualTo(cartItem.cartId());
        }

        @Test
        @DisplayName("userId가 CartCheckoutItem에 반영된다")
        void createForCart_UserIdReflected_InCartCheckoutItems() {
            // given
            CreatePaymentInCartCommand command =
                    PaymentCommandFixtures.createPaymentInCartCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            CartPaymentCreationBundle bundle = sut.createForCart(command);

            // then
            assertThat(bundle.cartCheckoutItems().get(0).userId())
                    .isEqualTo(PaymentCommandFixtures.DEFAULT_USER_ID);
        }

        @Test
        @DisplayName("CartCheckoutItems 수는 CartOrderItems 수와 동일하다")
        void createForCart_CartCheckoutItemsCountMatchesOrderItemsCount() {
            // given
            List<CartOrderItemCommand> multipleItems =
                    List.of(
                            PaymentCommandFixtures.cartOrderItemCommand(1L),
                            PaymentCommandFixtures.cartOrderItemCommand(2L));
            CreatePaymentInCartCommand command =
                    new CreatePaymentInCartCommand(
                            PaymentCommandFixtures.DEFAULT_USER_ID,
                            PaymentCommandFixtures.DEFAULT_PAY_AMOUNT,
                            0L,
                            PaymentCommandFixtures.DEFAULT_PAY_METHOD,
                            PaymentCommandFixtures.shippingInfoCommand(),
                            null,
                            multipleItems);
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            CartPaymentCreationBundle bundle = sut.createForCart(command);

            // then
            assertThat(bundle.cartCheckoutItems()).hasSize(2);
        }

        @Test
        @DisplayName("CartOrderItemCommand.toPaymentOrderItem()을 통해 productId가 올바르게 변환된다")
        void createForCart_ProductIdConvertedCorrectly_ViaToPaymentOrderItem() {
            // given
            CreatePaymentInCartCommand command =
                    PaymentCommandFixtures.createPaymentInCartCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            CartPaymentCreationBundle bundle = sut.createForCart(command);

            // then
            assertThat(bundle.paymentBundle().stockDeductionItems().get(0).productId())
                    .isEqualTo(PaymentCommandFixtures.DEFAULT_PRODUCT_ID);
        }
    }
}
