package com.ryuqq.setof.domain.refundpolicy.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("RefundPolicyException 테스트")
class RefundPolicyExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            RefundPolicyException exception =
                    new RefundPolicyException(RefundPolicyErrorCode.REFUND_POLICY_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("환불 정책을 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("RFP-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            RefundPolicyException exception =
                    new RefundPolicyException(
                            RefundPolicyErrorCode.REFUND_POLICY_NOT_FOUND, "ID 456 환불 정책 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 456 환불 정책 없음");
            assertThat(exception.code()).isEqualTo("RFP-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            RefundPolicyException exception =
                    new RefundPolicyException(RefundPolicyErrorCode.REFUND_POLICY_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("RFP-001");
        }
    }

    @Nested
    @DisplayName("구체적 예외 클래스 테스트")
    class ConcreteExceptionTest {

        @Test
        @DisplayName("RefundPolicyNotFoundException을 생성한다")
        void createRefundPolicyNotFoundException() {
            // when
            RefundPolicyNotFoundException exception = new RefundPolicyNotFoundException();

            // then
            assertThat(exception.code()).isEqualTo("RFP-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("환불 정책을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("RefundPolicyInactiveException을 생성한다")
        void createRefundPolicyInactiveException() {
            // when
            RefundPolicyInactiveException exception = new RefundPolicyInactiveException();

            // then
            assertThat(exception.code()).isEqualTo("RFP-002");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("비활성화된 환불 정책입니다");
        }

        @Test
        @DisplayName("ReturnPeriodExpiredException을 생성한다")
        void createReturnPeriodExpiredException() {
            // when
            ReturnPeriodExpiredException exception = new ReturnPeriodExpiredException();

            // then
            assertThat(exception.code()).isEqualTo("RFP-007");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("반품 가능 기간이 만료되었습니다");
        }

        @Test
        @DisplayName("ExchangePeriodExpiredException을 생성한다")
        void createExchangePeriodExpiredException() {
            // when
            ExchangePeriodExpiredException exception = new ExchangePeriodExpiredException();

            // then
            assertThat(exception.code()).isEqualTo("RFP-008");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("교환 가능 기간이 만료되었습니다");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("DomainException을 상속한다")
        void extendsDomainException() {
            // given
            RefundPolicyException exception = new RefundPolicyNotFoundException();

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
