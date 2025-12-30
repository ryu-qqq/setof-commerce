package com.ryuqq.setof.domain.payment.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.payment.exception.PaymentStatusException;
import com.ryuqq.setof.domain.payment.exception.RefundAmountException;
import com.ryuqq.setof.domain.payment.vo.PaymentMethod;
import com.ryuqq.setof.domain.payment.vo.PaymentMoney;
import com.ryuqq.setof.domain.payment.vo.PaymentStatus;
import com.ryuqq.setof.domain.payment.vo.PgProvider;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Payment Aggregate")
class PaymentTest {

    private static final Instant NOW = Instant.now();
    private static final PaymentMoney REQUESTED_AMOUNT = PaymentMoney.of(BigDecimal.valueOf(50000));

    @Nested
    @DisplayName("forNewRequest() - 신규 결제 요청 생성")
    class ForNewRequest {

        @Test
        @DisplayName("결제 요청을 생성할 수 있다")
        void shouldCreatePaymentRequest() {
            // given
            CheckoutId checkoutId = CheckoutId.forNew();
            PaymentMoney requestedAmount = PaymentMoney.of(BigDecimal.valueOf(50000));
            PaymentMethod method = PaymentMethod.CARD;
            PgProvider provider = PgProvider.PORTONE;

            // when
            Payment payment =
                    Payment.forNewRequest(checkoutId, provider, method, requestedAmount, NOW);

            // then
            assertNotNull(payment.id());
            assertEquals(checkoutId, payment.checkoutId());
            assertEquals(requestedAmount, payment.requestedAmount());
            assertEquals(method, payment.method());
            assertEquals(provider, payment.pgProvider());
            assertEquals(PaymentStatus.PENDING, payment.status());
        }

        @Test
        @DisplayName("checkoutId가 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenCheckoutIdIsNull() {
            // given
            PaymentMoney amount = PaymentMoney.of(BigDecimal.valueOf(50000));

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Payment.forNewRequest(
                                    null, PgProvider.PORTONE, PaymentMethod.CARD, amount, NOW));
        }
    }

    @Nested
    @DisplayName("결제 승인")
    class Approve {

        @Test
        @DisplayName("결제를 승인할 수 있다")
        void shouldApprovePayment() {
            // given
            Payment payment = createPendingPayment();
            String pgTransactionId = "pg_tx_123456";
            PaymentMoney approvedAmount = REQUESTED_AMOUNT;
            Instant approvedAt = Instant.now();

            // when
            Payment approved = payment.approve(pgTransactionId, approvedAmount, approvedAt);

            // then
            assertEquals(PaymentStatus.APPROVED, approved.status());
            assertEquals(pgTransactionId, approved.pgTransactionId());
            assertNotNull(approved.approvedAt());
        }

        @Test
        @DisplayName("PENDING 상태가 아니면 승인할 수 없다")
        void shouldNotApproveWhenNotPending() {
            // given
            Payment approved =
                    createPendingPayment().approve("pg_tx_123", REQUESTED_AMOUNT, Instant.now());

            // when & then
            assertThrows(
                    PaymentStatusException.class,
                    () -> approved.approve("pg_tx_456", REQUESTED_AMOUNT, Instant.now()));
        }
    }

    @Nested
    @DisplayName("결제 환불")
    class Refund {

        @Test
        @DisplayName("전액 환불이 가능하다")
        void shouldRefundFullAmount() {
            // given
            Payment approved =
                    createPendingPayment().approve("pg_tx_123", REQUESTED_AMOUNT, Instant.now());
            PaymentMoney refundAmount = approved.approvedAmount();
            Instant refundedAt = Instant.now();

            // when
            Payment refunded = approved.refund(refundAmount, refundedAt);

            // then
            assertEquals(PaymentStatus.FULLY_REFUNDED, refunded.status());
            assertEquals(refundAmount, refunded.refundedAmount());
        }

        @Test
        @DisplayName("부분 환불이 가능하다")
        void shouldRefundPartialAmount() {
            // given
            Payment approved =
                    createPendingPayment().approve("pg_tx_123", REQUESTED_AMOUNT, Instant.now());
            PaymentMoney refundAmount = PaymentMoney.of(BigDecimal.valueOf(10000));
            Instant refundedAt = Instant.now();

            // when
            Payment partialRefunded = approved.refund(refundAmount, refundedAt);

            // then
            assertEquals(PaymentStatus.PARTIAL_REFUNDED, partialRefunded.status());
            assertEquals(refundAmount, partialRefunded.refundedAmount());
        }

        @Test
        @DisplayName("결제 금액을 초과하는 환불은 불가능하다")
        void shouldNotRefundMoreThanPaidAmount() {
            // given
            Payment approved =
                    createPendingPayment().approve("pg_tx_123", REQUESTED_AMOUNT, Instant.now());
            PaymentMoney excessAmount = PaymentMoney.of(BigDecimal.valueOf(100000));

            // when & then
            assertThrows(
                    RefundAmountException.class,
                    () -> approved.refund(excessAmount, Instant.now()));
        }
    }

    @Nested
    @DisplayName("결제 취소")
    class Cancel {

        @Test
        @DisplayName("PENDING 상태의 결제를 취소할 수 있다")
        void shouldCancelPendingPayment() {
            // given
            Payment payment = createPendingPayment();
            Instant cancelledAt = Instant.now();

            // when
            Payment cancelled = payment.cancel(cancelledAt);

            // then
            assertEquals(PaymentStatus.CANCELLED, cancelled.status());
            assertNotNull(cancelled.cancelledAt());
        }
    }

    private Payment createPendingPayment() {
        return Payment.forNewRequest(
                CheckoutId.forNew(), PgProvider.PORTONE, PaymentMethod.CARD, REQUESTED_AMOUNT, NOW);
    }
}
