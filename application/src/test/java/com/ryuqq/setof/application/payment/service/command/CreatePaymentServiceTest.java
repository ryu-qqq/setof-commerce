package com.ryuqq.setof.application.payment.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.application.payment.dto.bundle.PaymentCreationBundle;
import com.ryuqq.setof.application.payment.dto.command.CreatePaymentCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentGatewayResult;
import com.ryuqq.setof.application.payment.factory.PaymentCommandFactory;
import com.ryuqq.setof.application.payment.internal.PaymentCreationCoordinator;
import com.ryuqq.setof.application.payment.internal.PaymentPersistFacade;
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
@DisplayName("CreatePaymentService 단위 테스트")
class CreatePaymentServiceTest {

    @InjectMocks private CreatePaymentService sut;

    @Mock private PaymentCommandFactory paymentCommandFactory;
    @Mock private PaymentCreationCoordinator paymentCreationCoordinator;
    @Mock private PaymentPersistFacade paymentPersistFacade;

    @Nested
    @DisplayName("execute() - 직접 구매 결제 준비")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 결제를 생성하고 PaymentGatewayResult를 반환한다")
        void execute_ValidCommand_ReturnsPaymentGatewayResult() {
            // given
            CreatePaymentCommand command = PaymentCommandFixtures.createPaymentCommand();
            PaymentCreationBundle bundle = PaymentCommandFixtures.paymentCreationBundle();
            PaymentPersistResult persistResult =
                    new PaymentPersistResult(1L, "uuid-1234", List.of(100L, 101L));

            given(paymentCommandFactory.create(command)).willReturn(bundle);
            given(paymentPersistFacade.persist(bundle)).willReturn(persistResult);

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
        @DisplayName("Factory → Coordinator → Facade 순서로 호출된다")
        void execute_CallsInCorrectOrder_FactoryThenCoordinatorThenFacade() {
            // given
            CreatePaymentCommand command = PaymentCommandFixtures.createPaymentCommand();
            PaymentCreationBundle bundle = PaymentCommandFixtures.paymentCreationBundle();
            PaymentPersistResult persistResult =
                    new PaymentPersistResult(1L, "uuid-1234", List.of(100L));

            given(paymentCommandFactory.create(command)).willReturn(bundle);
            given(paymentPersistFacade.persist(bundle)).willReturn(persistResult);

            // when
            sut.execute(command);

            // then
            then(paymentCommandFactory).should().create(command);
            then(paymentCreationCoordinator)
                    .should()
                    .validateAndDeductStock(
                            bundle.payment(), bundle.order(), bundle.stockDeductionItems());
            then(paymentPersistFacade).should().persist(bundle);
        }

        @Test
        @DisplayName("환불 계좌 정보가 있는 커맨드도 정상 처리된다")
        void execute_CommandWithRefundAccount_ReturnsPaymentGatewayResult() {
            // given
            CreatePaymentCommand command =
                    PaymentCommandFixtures.createPaymentCommandWithRefundAccount();
            PaymentCreationBundle bundle =
                    PaymentCommandFixtures.paymentCreationBundleWithRefundAccount();
            PaymentPersistResult persistResult =
                    new PaymentPersistResult(2L, "uuid-5678", List.of(200L));

            given(paymentCommandFactory.create(command)).willReturn(bundle);
            given(paymentPersistFacade.persist(bundle)).willReturn(persistResult);

            // when
            PaymentGatewayResult result = sut.execute(command);

            // then
            assertThat(result.paymentId()).isEqualTo(2L);
            assertThat(result.paymentUniqueId()).isEqualTo("uuid-5678");
        }
    }
}
