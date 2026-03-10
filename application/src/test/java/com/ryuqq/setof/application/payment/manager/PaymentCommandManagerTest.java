package com.ryuqq.setof.application.payment.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.application.payment.port.out.command.PaymentCommandPort;
import com.ryuqq.setof.application.payment.port.out.command.PaymentCommandPort.PaymentCommandResult;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
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
@DisplayName("PaymentCommandManager 단위 테스트")
class PaymentCommandManagerTest {

    @InjectMocks private PaymentCommandManager sut;

    @Mock private PaymentCommandPort commandPort;

    @Nested
    @DisplayName("persist() - Payment 저장")
    class PersistTest {

        @Test
        @DisplayName("Payment를 저장하고 PaymentCommandResult를 반환한다")
        void persist_ValidPayment_ReturnsPaymentCommandResult() {
            // given
            Payment payment = PaymentCommandFixtures.payment();
            PaymentCommandResult expectedResult = new PaymentCommandResult(1L, "uuid-1234");

            given(commandPort.persist(payment)).willReturn(expectedResult);

            // when
            PaymentCommandResult result = sut.persist(payment);

            // then
            assertThat(result).isEqualTo(expectedResult);
            assertThat(result.paymentId()).isEqualTo(1L);
            assertThat(result.paymentUniqueId()).isEqualTo("uuid-1234");
        }

        @Test
        @DisplayName("PaymentCommandPort.persist에 Payment를 위임한다")
        void persist_DelegatesToCommandPort() {
            // given
            Payment payment = PaymentCommandFixtures.payment();
            PaymentCommandResult expectedResult = new PaymentCommandResult(1L, "uuid-1234");

            given(commandPort.persist(payment)).willReturn(expectedResult);

            // when
            sut.persist(payment);

            // then
            then(commandPort).should().persist(payment);
        }
    }
}
