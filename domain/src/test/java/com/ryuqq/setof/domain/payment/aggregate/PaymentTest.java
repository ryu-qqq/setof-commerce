package com.ryuqq.setof.domain.payment.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.payment.exception.InvalidPaymentStatusException;
import com.ryuqq.setof.domain.payment.exception.PaymentException;
import com.ryuqq.setof.domain.payment.vo.PaymentMethodType;
import com.ryuqq.setof.domain.payment.vo.PaymentStatus;
import com.setof.commerce.domain.payment.PaymentFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Payment Aggregate 단위 테스트")
class PaymentTest {

    @Nested
    @DisplayName("forNew() - 신규 결제 생성")
    class ForNewTest {

        @Test
        @DisplayName("필수 정보로 신규 결제를 PROCESSING 상태로 생성한다")
        void createNewPaymentWithProcessingStatus() {
            // given
            Instant now = CommonVoFixtures.now();

            // when
            Payment payment =
                    Payment.forNew(
                            PaymentFixtures.defaultLegacyOrderId(),
                            PaymentFixtures.defaultMemberId(),
                            PaymentFixtures.defaultLegacyUserId(),
                            PaymentFixtures.defaultPaymentAmount(),
                            PaymentMethodType.CARD,
                            PaymentFixtures.defaultBuyerInfo(),
                            PaymentFixtures.defaultUsedMileage(),
                            now);

            // then
            assertThat(payment.isNew()).isTrue();
            assertThat(payment.paymentStatus()).isEqualTo(PaymentStatus.PROCESSING);
            assertThat(payment.methodType()).isEqualTo(PaymentMethodType.CARD);
            assertThat(payment.paymentAmount()).isEqualTo(PaymentFixtures.defaultPaymentAmount());
            assertThat(payment.buyerInfo()).isEqualTo(PaymentFixtures.defaultBuyerInfo());
            assertThat(payment.pgInfo()).isNull();
            assertThat(payment.cardInfo()).isNull();
            assertThat(payment.paidAt()).isNull();
            assertThat(payment.cancelledAt()).isNull();
            assertThat(payment.createdAt()).isEqualTo(now);
            assertThat(payment.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("회원 ID 없이 신규 결제를 생성할 수 있다")
        void createNewPaymentWithoutMemberId() {
            // when
            Payment payment = PaymentFixtures.newPaymentWithoutMember();

            // then
            assertThat(payment.memberId()).isNull();
            assertThat(payment.memberIdValue()).isNull();
            assertThat(payment.paymentStatus()).isEqualTo(PaymentStatus.PROCESSING);
        }

        @Test
        @DisplayName("마일리지 사용 금액이 반영된 결제를 생성한다")
        void createNewPaymentWithMileage() {
            // when
            Payment payment = PaymentFixtures.newPaymentWithMileage();

            // then
            assertThat(payment.usedMileage().isUsed()).isTrue();
            assertThat(payment.usedMileage().amount()).isEqualTo(5000L);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 COMPLETED 상태의 결제를 복원한다")
        void reconstituteCompletedPayment() {
            // when
            Payment payment = PaymentFixtures.completedPayment();

            // then
            assertThat(payment.isNew()).isFalse();
            assertThat(payment.paymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
            assertThat(payment.pgInfo()).isNotNull();
            assertThat(payment.cardInfo()).isNotNull();
            assertThat(payment.paidAt()).isNotNull();
        }

        @Test
        @DisplayName("영속성에서 FAILED 상태의 결제를 복원한다")
        void reconstituteFailedPayment() {
            // when
            Payment payment = PaymentFixtures.failedPayment();

            // then
            assertThat(payment.paymentStatus()).isEqualTo(PaymentStatus.FAILED);
            assertThat(payment.pgInfo()).isNull();
        }
    }

    @Nested
    @DisplayName("complete() - 결제 완료 처리")
    class CompleteTest {

        @Test
        @DisplayName("PROCESSING 상태에서 COMPLETED로 전이한다")
        void completePaymentFromProcessing() {
            // given
            Payment payment = PaymentFixtures.processingPayment();
            Instant now = CommonVoFixtures.now();

            // when
            payment.complete(
                    PaymentFixtures.defaultPgTransactionInfo(),
                    PaymentFixtures.defaultCardPaymentInfo(),
                    null,
                    now);

            // then
            assertThat(payment.paymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
            assertThat(payment.isCompleted()).isTrue();
            assertThat(payment.pgInfo()).isNotNull();
            assertThat(payment.cardInfo()).isNotNull();
            assertThat(payment.paidAt()).isEqualTo(now);
            assertThat(payment.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("이미 완료된 결제를 다시 완료 처리하면 예외가 발생한다")
        void completeAlreadyCompletedPayment_ThrowsException() {
            // given
            Payment payment = PaymentFixtures.completedPayment();

            // when & then
            assertThatThrownBy(
                            () ->
                                    payment.complete(
                                            PaymentFixtures.defaultPgTransactionInfo(),
                                            PaymentFixtures.defaultCardPaymentInfo(),
                                            null,
                                            CommonVoFixtures.now()))
                    .isInstanceOf(InvalidPaymentStatusException.class);
        }
    }

    @Nested
    @DisplayName("fail() - 결제 실패 처리")
    class FailTest {

        @Test
        @DisplayName("PROCESSING 상태에서 FAILED로 전이한다")
        void failPaymentFromProcessing() {
            // given
            Payment payment = PaymentFixtures.processingPayment();
            Instant now = CommonVoFixtures.now();

            // when
            payment.fail(now);

            // then
            assertThat(payment.paymentStatus()).isEqualTo(PaymentStatus.FAILED);
            assertThat(payment.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("COMPLETED 상태에서 FAILED로 전이하면 예외가 발생한다")
        void failCompletedPayment_ThrowsException() {
            // given
            Payment payment = PaymentFixtures.completedPayment();

            // when & then
            assertThatThrownBy(() -> payment.fail(CommonVoFixtures.now()))
                    .isInstanceOf(InvalidPaymentStatusException.class);
        }
    }

    @Nested
    @DisplayName("cancel() - 결제 취소 처리")
    class CancelTest {

        @Test
        @DisplayName("PROCESSING 상태에서 CANCELLED로 전이한다")
        void cancelPaymentFromProcessing() {
            // given
            Payment payment = PaymentFixtures.processingPayment();
            Instant now = CommonVoFixtures.now();

            // when
            payment.cancel(now);

            // then
            assertThat(payment.paymentStatus()).isEqualTo(PaymentStatus.CANCELLED);
            assertThat(payment.cancelledAt()).isEqualTo(now);
            assertThat(payment.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("partialRefund() - 부분 환불 처리")
    class PartialRefundTest {

        @Test
        @DisplayName("COMPLETED 상태에서 PARTIALLY_REFUNDED로 전이한다")
        void partialRefundFromCompleted() {
            // given
            Payment payment = PaymentFixtures.completedPayment();
            Money refundAmount = Money.of(10000);
            Instant now = CommonVoFixtures.now();

            // when
            payment.partialRefund(refundAmount, now);

            // then
            assertThat(payment.paymentStatus()).isEqualTo(PaymentStatus.PARTIALLY_REFUNDED);
            assertThat(payment.isRefundable()).isTrue();
        }

        @Test
        @DisplayName("환불 금액이 결제 금액 이상이면 예외가 발생한다")
        void partialRefundExceedsPaymentAmount_ThrowsException() {
            // given
            Payment payment = PaymentFixtures.completedPayment();
            Money refundAmount = Money.of(50000); // 결제 금액과 동일

            // when & then
            assertThatThrownBy(() -> payment.partialRefund(refundAmount, CommonVoFixtures.now()))
                    .isInstanceOf(PaymentException.class);
        }
    }

    @Nested
    @DisplayName("fullRefund() - 전액 환불 처리")
    class FullRefundTest {

        @Test
        @DisplayName("COMPLETED 상태에서 REFUNDED로 전이한다")
        void fullRefundFromCompleted() {
            // given
            Payment payment = PaymentFixtures.completedPayment();
            Instant now = CommonVoFixtures.now();

            // when
            payment.fullRefund(now);

            // then
            assertThat(payment.paymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
            assertThat(payment.cancelledAt()).isEqualTo(now);
            assertThat(payment.isRefundable()).isFalse();
        }

        @Test
        @DisplayName("PARTIALLY_REFUNDED 상태에서 REFUNDED로 전이한다")
        void fullRefundFromPartiallyRefunded() {
            // given
            Payment payment = PaymentFixtures.partiallyRefundedPayment();
            Instant now = CommonVoFixtures.now();

            // when
            payment.fullRefund(now);

            // then
            assertThat(payment.paymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
        }

        @Test
        @DisplayName("FAILED 상태에서 전액 환불하면 예외가 발생한다")
        void fullRefundFromFailed_ThrowsException() {
            // given
            Payment payment = PaymentFixtures.failedPayment();

            // when & then
            assertThatThrownBy(() -> payment.fullRefund(CommonVoFixtures.now()))
                    .isInstanceOf(InvalidPaymentStatusException.class);
        }
    }

    @Nested
    @DisplayName("isRefundable() / isCompleted() - 상태 조회 메서드")
    class StateQueryTest {

        @Test
        @DisplayName("COMPLETED 상태 결제는 환불 가능하다")
        void completedPaymentIsRefundable() {
            assertThat(PaymentFixtures.completedPayment().isRefundable()).isTrue();
        }

        @Test
        @DisplayName("PARTIALLY_REFUNDED 상태 결제는 환불 가능하다")
        void partiallyRefundedPaymentIsRefundable() {
            assertThat(PaymentFixtures.partiallyRefundedPayment().isRefundable()).isTrue();
        }

        @Test
        @DisplayName("PROCESSING 상태 결제는 환불 불가능하다")
        void processingPaymentIsNotRefundable() {
            assertThat(PaymentFixtures.processingPayment().isRefundable()).isFalse();
        }

        @Test
        @DisplayName("COMPLETED 상태 결제는 isCompleted()가 true를 반환한다")
        void completedPaymentIsCompleted() {
            assertThat(PaymentFixtures.completedPayment().isCompleted()).isTrue();
        }

        @Test
        @DisplayName("PROCESSING 상태 결제는 isCompleted()가 false를 반환한다")
        void processingPaymentIsNotCompleted() {
            assertThat(PaymentFixtures.processingPayment().isCompleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("idValue()는 PaymentId의 원시값을 반환한다")
        void idValueReturnsLongValue() {
            // given
            Payment payment = PaymentFixtures.completedPayment();

            // then
            assertThat(payment.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("orderIdValue()는 LegacyOrderId의 원시값을 반환한다")
        void orderIdValueReturnsLongValue() {
            // given
            Payment payment = PaymentFixtures.completedPayment();

            // then
            assertThat(payment.orderIdValue()).isEqualTo(100L);
        }

        @Test
        @DisplayName("legacyUserIdValue()는 LegacyUserId의 원시값을 반환한다")
        void legacyUserIdValueReturnsLongValue() {
            // given
            Payment payment = PaymentFixtures.completedPayment();

            // then
            assertThat(payment.legacyUserIdValue()).isEqualTo(999L);
        }

        @Test
        @DisplayName("memberIdValue()는 MemberId의 원시값을 반환한다")
        void memberIdValueReturnsStringValue() {
            // given
            Payment payment = PaymentFixtures.completedPayment();

            // then
            assertThat(payment.memberIdValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("paymentAmountValue()는 결제 금액의 int 값을 반환한다")
        void paymentAmountValueReturnsIntValue() {
            // given
            Payment payment = PaymentFixtures.completedPayment();

            // then
            assertThat(payment.paymentAmountValue()).isEqualTo(50000);
        }
    }
}
