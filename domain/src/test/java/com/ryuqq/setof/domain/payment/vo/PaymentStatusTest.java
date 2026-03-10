package com.ryuqq.setof.domain.payment.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("PaymentStatus 열거형 단위 테스트")
class PaymentStatusTest {

    @Nested
    @DisplayName("상태 전이 허용 규칙 테스트")
    class TransitionRuleTest {

        @Test
        @DisplayName("PROCESSING은 COMPLETED로 전이 가능하다")
        void processingCanTransitToCompleted() {
            assertThat(PaymentStatus.PROCESSING.canTransitionTo(PaymentStatus.COMPLETED)).isTrue();
        }

        @Test
        @DisplayName("PROCESSING은 FAILED로 전이 가능하다")
        void processingCanTransitToFailed() {
            assertThat(PaymentStatus.PROCESSING.canTransitionTo(PaymentStatus.FAILED)).isTrue();
        }

        @Test
        @DisplayName("PROCESSING은 CANCELLED로 전이 가능하다")
        void processingCanTransitToCancelled() {
            assertThat(PaymentStatus.PROCESSING.canTransitionTo(PaymentStatus.CANCELLED)).isTrue();
        }

        @Test
        @DisplayName("PROCESSING은 PARTIALLY_REFUNDED로 전이할 수 없다")
        void processingCannotTransitToPartiallyRefunded() {
            assertThat(PaymentStatus.PROCESSING.canTransitionTo(PaymentStatus.PARTIALLY_REFUNDED))
                    .isFalse();
        }

        @Test
        @DisplayName("COMPLETED는 PARTIALLY_REFUNDED로 전이 가능하다")
        void completedCanTransitToPartiallyRefunded() {
            assertThat(PaymentStatus.COMPLETED.canTransitionTo(PaymentStatus.PARTIALLY_REFUNDED))
                    .isTrue();
        }

        @Test
        @DisplayName("COMPLETED는 REFUNDED로 전이 가능하다")
        void completedCanTransitToRefunded() {
            assertThat(PaymentStatus.COMPLETED.canTransitionTo(PaymentStatus.REFUNDED)).isTrue();
        }

        @Test
        @DisplayName("COMPLETED는 FAILED로 전이할 수 없다")
        void completedCannotTransitToFailed() {
            assertThat(PaymentStatus.COMPLETED.canTransitionTo(PaymentStatus.FAILED)).isFalse();
        }

        @Test
        @DisplayName("PARTIALLY_REFUNDED는 REFUNDED로 전이 가능하다")
        void partiallyRefundedCanTransitToRefunded() {
            assertThat(PaymentStatus.PARTIALLY_REFUNDED.canTransitionTo(PaymentStatus.REFUNDED))
                    .isTrue();
        }

        @Test
        @DisplayName("REFUNDED는 어떤 상태로도 전이할 수 없다 (최종 상태)")
        void refundedCannotTransitToAnyState() {
            for (PaymentStatus status : PaymentStatus.values()) {
                assertThat(PaymentStatus.REFUNDED.canTransitionTo(status)).isFalse();
            }
        }

        @Test
        @DisplayName("FAILED는 어떤 상태로도 전이할 수 없다 (최종 상태)")
        void failedCannotTransitToAnyState() {
            for (PaymentStatus status : PaymentStatus.values()) {
                assertThat(PaymentStatus.FAILED.canTransitionTo(status)).isFalse();
            }
        }

        @Test
        @DisplayName("CANCELLED는 어떤 상태로도 전이할 수 없다 (최종 상태)")
        void cancelledCannotTransitToAnyState() {
            for (PaymentStatus status : PaymentStatus.values()) {
                assertThat(PaymentStatus.CANCELLED.canTransitionTo(status)).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("환불 가능 여부 테스트")
    class RefundableTest {

        @Test
        @DisplayName("COMPLETED 상태는 환불 가능하다")
        void completedIsRefundable() {
            assertThat(PaymentStatus.COMPLETED.isRefundable()).isTrue();
        }

        @Test
        @DisplayName("PARTIALLY_REFUNDED 상태는 환불 가능하다")
        void partiallyRefundedIsRefundable() {
            assertThat(PaymentStatus.PARTIALLY_REFUNDED.isRefundable()).isTrue();
        }

        @Test
        @DisplayName("PROCESSING 상태는 환불 불가능하다")
        void processingIsNotRefundable() {
            assertThat(PaymentStatus.PROCESSING.isRefundable()).isFalse();
        }

        @Test
        @DisplayName("FAILED 상태는 환불 불가능하다")
        void failedIsNotRefundable() {
            assertThat(PaymentStatus.FAILED.isRefundable()).isFalse();
        }

        @Test
        @DisplayName("REFUNDED 상태는 환불 불가능하다")
        void refundedIsNotRefundable() {
            assertThat(PaymentStatus.REFUNDED.isRefundable()).isFalse();
        }
    }

    @Nested
    @DisplayName("최종 상태 여부 테스트")
    class FinalStateTest {

        @Test
        @DisplayName("REFUNDED는 최종 상태이다")
        void refundedIsFinal() {
            assertThat(PaymentStatus.REFUNDED.isFinal()).isTrue();
        }

        @Test
        @DisplayName("FAILED는 최종 상태이다")
        void failedIsFinal() {
            assertThat(PaymentStatus.FAILED.isFinal()).isTrue();
        }

        @Test
        @DisplayName("CANCELLED는 최종 상태이다")
        void cancelledIsFinal() {
            assertThat(PaymentStatus.CANCELLED.isFinal()).isTrue();
        }

        @Test
        @DisplayName("PROCESSING은 최종 상태가 아니다")
        void processingIsNotFinal() {
            assertThat(PaymentStatus.PROCESSING.isFinal()).isFalse();
        }

        @Test
        @DisplayName("COMPLETED는 최종 상태가 아니다")
        void completedIsNotFinal() {
            assertThat(PaymentStatus.COMPLETED.isFinal()).isFalse();
        }
    }
}
