package com.ryuqq.setof.domain.refundaccount.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("RefundAccountException 단위 테스트")
class RefundAccountExceptionTest {

    @Nested
    @DisplayName("RefundAccountException 기본 생성 테스트")
    class BaseExceptionCreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            RefundAccountException exception =
                    new RefundAccountException(RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("환불 계좌를 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("RFA-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            RefundAccountException exception =
                    new RefundAccountException(
                            RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND, "ID 999 환불 계좌 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 999 환불 계좌 없음");
            assertThat(exception.code()).isEqualTo("RFA-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            RefundAccountException exception =
                    new RefundAccountException(
                            RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("RFA-001");
        }

        @Test
        @DisplayName("RefundAccountException은 DomainException을 상속한다")
        void extendsDomainException() {
            // given
            RefundAccountException exception =
                    new RefundAccountException(RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("RefundAccountNotFoundException 테스트")
    class NotFoundExceptionTest {

        @Test
        @DisplayName("기본 생성자로 RefundAccountNotFoundException을 생성한다")
        void createWithDefaultConstructor() {
            // when
            RefundAccountNotFoundException exception = new RefundAccountNotFoundException();

            // then
            assertThat(exception.code()).isEqualTo("RFA-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("환불 계좌를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("ID를 포함한 메시지로 RefundAccountNotFoundException을 생성한다")
        void createWithId() {
            // when
            RefundAccountNotFoundException exception = new RefundAccountNotFoundException(42L);

            // then
            assertThat(exception.code()).isEqualTo("RFA-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("ID가 42인 환불 계좌를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("RefundAccountNotFoundException은 RefundAccountException을 상속한다")
        void extendsRefundAccountException() {
            // given
            RefundAccountNotFoundException exception = new RefundAccountNotFoundException();

            // then
            assertThat(exception).isInstanceOf(RefundAccountException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("AccountVerificationFailedException 테스트")
    class VerificationFailedExceptionTest {

        @Test
        @DisplayName("기본 생성자로 AccountVerificationFailedException을 생성한다")
        void createWithDefaultConstructor() {
            // when
            AccountVerificationFailedException exception = new AccountVerificationFailedException();

            // then
            assertThat(exception.code()).isEqualTo("RFA-002");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("계좌 실명 검증에 실패했습니다");
        }

        @Test
        @DisplayName("은행명과 계좌번호를 포함한 메시지로 AccountVerificationFailedException을 생성한다")
        void createWithBankNameAndAccountNumber() {
            // when
            AccountVerificationFailedException exception =
                    new AccountVerificationFailedException("국민은행", "123456789012");

            // then
            assertThat(exception.code()).isEqualTo("RFA-002");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("계좌 실명 검증 실패: 국민은행 123456789012");
        }

        @Test
        @DisplayName("AccountVerificationFailedException은 RefundAccountException을 상속한다")
        void extendsRefundAccountException() {
            // given
            AccountVerificationFailedException exception = new AccountVerificationFailedException();

            // then
            assertThat(exception).isInstanceOf(RefundAccountException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
