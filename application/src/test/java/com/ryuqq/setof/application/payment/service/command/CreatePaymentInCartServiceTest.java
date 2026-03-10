package com.ryuqq.setof.application.payment.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.application.payment.dto.bundle.CartPaymentCreationBundle;
import com.ryuqq.setof.application.payment.dto.bundle.PaymentCreationBundle;
import com.ryuqq.setof.application.payment.dto.command.CreatePaymentInCartCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentGatewayResult;
import com.ryuqq.setof.application.payment.factory.PaymentCommandFactory;
import com.ryuqq.setof.application.payment.internal.CartPaymentPersistFacade;
import com.ryuqq.setof.application.payment.internal.PaymentCreationCoordinator;
import com.ryuqq.setof.application.payment.internal.PaymentPersistFacade.PaymentPersistResult;
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
@DisplayName("CreatePaymentInCartService 단위 테스트")
class CreatePaymentInCartServiceTest {

    @InjectMocks private CreatePaymentInCartService sut;

    @Mock private PaymentCommandFactory paymentCommandFactory;
    @Mock private PaymentCreationCoordinator paymentCreationCoordinator;
    @Mock private CartPaymentPersistFacade cartPaymentPersistFacade;

    @Nested
    @DisplayName("execute() - 장바구니 구매 결제 준비")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 장바구니 커맨드로 결제를 생성하고 PaymentGatewayResult를 반환한다")
        void execute_ValidCommand_ReturnsPaymentGatewayResult() {
            // given
            CreatePaymentInCartCommand command =
                    PaymentCommandFixtures.createPaymentInCartCommand();
            CartPaymentCreationBundle cartBundle =
                    PaymentCommandFixtures.cartPaymentCreationBundle();
            PaymentPersistResult persistResult =
                    new PaymentPersistResult(1L, "cart-uuid-1234", List.of(100L, 101L));

            given(paymentCommandFactory.createForCart(command)).willReturn(cartBundle);
            given(cartPaymentPersistFacade.persist(cartBundle)).willReturn(persistResult);

            // when
            PaymentGatewayResult result = sut.execute(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.paymentId()).isEqualTo(persistResult.paymentId());
            assertThat(result.paymentUniqueId()).isEqualTo(persistResult.paymentUniqueId());
            assertThat(result.orderIds()).isEqualTo(persistResult.orderIds());
            assertThat(result.expectedMileageAmount()).isZero();
        }

        @Test
        @DisplayName("Factory.createForCart → Coordinator → CartPaymentPersistFacade 순서로 호출된다")
        void execute_CallsInCorrectOrder_FactoryThenCoordinatorThenCartFacade() {
            // given
            CreatePaymentInCartCommand command =
                    PaymentCommandFixtures.createPaymentInCartCommand();
            CartPaymentCreationBundle cartBundle =
                    PaymentCommandFixtures.cartPaymentCreationBundle();
            PaymentCreationBundle paymentBundle = cartBundle.paymentBundle();
            PaymentPersistResult persistResult =
                    new PaymentPersistResult(1L, "cart-uuid-1234", List.of(100L));

            given(paymentCommandFactory.createForCart(command)).willReturn(cartBundle);
            given(cartPaymentPersistFacade.persist(cartBundle)).willReturn(persistResult);

            // when
            sut.execute(command);

            // then
            then(paymentCommandFactory).should().createForCart(command);
            then(paymentCreationCoordinator)
                    .should()
                    .validateAndDeductStock(
                            paymentBundle.payment(),
                            paymentBundle.order(),
                            paymentBundle.stockDeductionItems());
            then(cartPaymentPersistFacade).should().persist(cartBundle);
        }

        @Test
        @DisplayName("CartPaymentCreationBundle에서 paymentBundle을 추출하여 Coordinator에 전달한다")
        void execute_ExtractsPaymentBundleFromCartBundle_PassesToCoordinator() {
            // given
            CreatePaymentInCartCommand command =
                    PaymentCommandFixtures.createPaymentInCartCommand();
            CartPaymentCreationBundle cartBundle =
                    PaymentCommandFixtures.cartPaymentCreationBundle();
            PaymentPersistResult persistResult =
                    new PaymentPersistResult(1L, "cart-uuid-1234", List.of(100L));

            given(paymentCommandFactory.createForCart(command)).willReturn(cartBundle);
            given(cartPaymentPersistFacade.persist(cartBundle)).willReturn(persistResult);

            // when
            sut.execute(command);

            // then
            PaymentCreationBundle paymentBundle = cartBundle.paymentBundle();
            then(paymentCreationCoordinator)
                    .should()
                    .validateAndDeductStock(
                            paymentBundle.payment(),
                            paymentBundle.order(),
                            paymentBundle.stockDeductionItems());
        }
    }
}
