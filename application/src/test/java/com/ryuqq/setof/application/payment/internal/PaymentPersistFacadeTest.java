package com.ryuqq.setof.application.payment.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.payment.PaymentCommandFixtures;
import com.ryuqq.setof.application.payment.dto.bundle.PaymentCreationBundle;
import com.ryuqq.setof.application.payment.internal.PaymentPersistFacade.PaymentPersistResult;
import com.ryuqq.setof.application.payment.manager.PaymentCommandManager;
import com.ryuqq.setof.application.payment.manager.PaymentOrderCommandManager;
import com.ryuqq.setof.application.payment.manager.RefundAccountSnapshotManager;
import com.ryuqq.setof.application.payment.manager.ShippingSnapshotCommandManager;
import com.ryuqq.setof.application.payment.port.out.command.PaymentCommandPort.PaymentCommandResult;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
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
@DisplayName("PaymentPersistFacade 단위 테스트")
class PaymentPersistFacadeTest {

    @InjectMocks private PaymentPersistFacade sut;

    @Mock private PaymentCommandManager paymentCommandManager;
    @Mock private PaymentOrderCommandManager orderCommandManager;
    @Mock private ShippingSnapshotCommandManager shippingSnapshotCommandManager;
    @Mock private RefundAccountSnapshotManager refundAccountSnapshotManager;

    @Nested
    @DisplayName("persist() - 결제 번들 영속화")
    class PersistTest {

        @Test
        @DisplayName("Payment 영속화 후 paymentId가 Order에 할당되고 나머지 도메인이 순서대로 저장된다")
        void persist_ValidBundle_SavesInOrder() {
            // given
            PaymentCreationBundle bundle = PaymentCommandFixtures.paymentCreationBundle();
            Payment payment = bundle.payment();
            Order order = bundle.order();

            PaymentCommandResult paymentResult = new PaymentCommandResult(1L, "uuid-1234");
            List<Long> orderIds = List.of(100L, 101L);

            given(paymentCommandManager.persist(payment)).willReturn(paymentResult);
            given(orderCommandManager.persist(order)).willReturn(orderIds);
            willDoNothing().given(shippingSnapshotCommandManager).persist(order);

            // when
            PaymentPersistResult result = sut.persist(bundle);

            // then
            assertThat(result).isNotNull();
            assertThat(result.paymentId()).isEqualTo(paymentResult.paymentId());
            assertThat(result.paymentUniqueId()).isEqualTo(paymentResult.paymentUniqueId());
            assertThat(result.orderIds()).isEqualTo(orderIds);
        }

        @Test
        @DisplayName(
                "Payment persist → assignPaymentId → Order persist → ShippingSnapshot 순서로 호출된다")
        void persist_CallsInCorrectOrder() {
            // given
            PaymentCreationBundle bundle = PaymentCommandFixtures.paymentCreationBundle();
            Payment payment = bundle.payment();
            Order order = bundle.order();

            PaymentCommandResult paymentResult = new PaymentCommandResult(1L, "uuid-1234");
            List<Long> orderIds = List.of(100L);

            given(paymentCommandManager.persist(payment)).willReturn(paymentResult);
            given(orderCommandManager.persist(order)).willReturn(orderIds);
            willDoNothing().given(shippingSnapshotCommandManager).persist(order);

            // when
            sut.persist(bundle);

            // then
            org.mockito.InOrder inOrder =
                    org.mockito.Mockito.inOrder(
                            paymentCommandManager,
                            orderCommandManager,
                            shippingSnapshotCommandManager);
            inOrder.verify(paymentCommandManager).persist(payment);
            inOrder.verify(orderCommandManager).persist(order);
            inOrder.verify(shippingSnapshotCommandManager).persist(order);
        }

        @Test
        @DisplayName("refundAccountSnapshot이 null이면 RefundAccountSnapshotManager가 호출되지 않는다")
        void persist_NullRefundAccountSnapshot_SkipsRefundAccountPersist() {
            // given
            PaymentCreationBundle bundle = PaymentCommandFixtures.paymentCreationBundle();
            Payment payment = bundle.payment();
            Order order = bundle.order();

            PaymentCommandResult paymentResult = new PaymentCommandResult(1L, "uuid-1234");
            List<Long> orderIds = List.of(100L);

            given(paymentCommandManager.persist(payment)).willReturn(paymentResult);
            given(orderCommandManager.persist(order)).willReturn(orderIds);
            willDoNothing().given(shippingSnapshotCommandManager).persist(order);

            // when
            sut.persist(bundle);

            // then
            then(refundAccountSnapshotManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("refundAccountSnapshot이 있으면 RefundAccountSnapshotManager가 호출된다")
        void persist_WithRefundAccountSnapshot_CallsRefundAccountPersist() {
            // given
            PaymentCreationBundle bundle =
                    PaymentCommandFixtures.paymentCreationBundleWithRefundAccount();
            Payment payment = bundle.payment();
            Order order = bundle.order();

            PaymentCommandResult paymentResult = new PaymentCommandResult(2L, "uuid-5678");
            List<Long> orderIds = List.of(200L);

            given(paymentCommandManager.persist(payment)).willReturn(paymentResult);
            given(orderCommandManager.persist(order)).willReturn(orderIds);
            willDoNothing().given(shippingSnapshotCommandManager).persist(order);
            willDoNothing()
                    .given(refundAccountSnapshotManager)
                    .persist(bundle.refundAccountSnapshot());

            // when
            sut.persist(bundle);

            // then
            then(refundAccountSnapshotManager).should().persist(bundle.refundAccountSnapshot());
        }

        @Test
        @DisplayName("Order에 paymentId가 올바르게 할당된다")
        void persist_AssignsPaymentIdToOrder() {
            // given
            PaymentCreationBundle bundle = PaymentCommandFixtures.paymentCreationBundle();
            Payment payment = bundle.payment();
            Order order = bundle.order();

            long expectedPaymentId = 42L;
            PaymentCommandResult paymentResult =
                    new PaymentCommandResult(expectedPaymentId, "uuid-9999");
            List<Long> orderIds = List.of(100L);

            given(paymentCommandManager.persist(payment)).willReturn(paymentResult);
            given(orderCommandManager.persist(order)).willReturn(orderIds);
            willDoNothing().given(shippingSnapshotCommandManager).persist(order);

            // when
            sut.persist(bundle);

            // then
            assertThat(order.paymentId()).isEqualTo(expectedPaymentId);
        }
    }
}
