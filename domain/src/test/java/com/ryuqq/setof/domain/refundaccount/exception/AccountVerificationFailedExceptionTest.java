package com.ryuqq.setof.domain.refundaccount.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * AccountVerificationFailedException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("AccountVerificationFailedException 단위 테스트")
class AccountVerificationFailedExceptionTest {

    @Nested
    @DisplayName("세 개 파라미터 생성자")
    class ThreeParameterConstructor {

        @Test
        @DisplayName("성공 - bankCode, accountNumber, reason으로 예외를 생성한다")
        void shouldCreateWithAllParams() {
            // Given
            String bankCode = "004";
            String accountNumber = "1234567890123";
            String reason = "계좌번호 불일치";

            // When
            AccountVerificationFailedException exception =
                    new AccountVerificationFailedException(bankCode, accountNumber, reason);

            // Then
            assertThat(exception.getMessage()).contains("계좌 검증에 실패했습니다");
            assertThat(exception.getMessage()).contains("004");
            assertThat(exception.getMessage()).contains("계좌번호 불일치");
            assertThat(exception.getErrorCode()).isEqualTo(RefundAccountErrorCode.ACCOUNT_VERIFICATION_FAILED);
        }
    }

    @Nested
    @DisplayName("두 개 파라미터 생성자")
    class TwoParameterConstructor {

        @Test
        @DisplayName("성공 - bankCode와 accountNumber로 예외를 생성한다")
        void shouldCreateWithBankCodeAndAccountNumber() {
            // Given
            String bankCode = "004";
            String accountNumber = "1234567890123";

            // When
            AccountVerificationFailedException exception =
                    new AccountVerificationFailedException(bankCode, accountNumber);

            // Then
            assertThat(exception.getMessage()).contains("계좌 검증에 실패했습니다");
            assertThat(exception.getMessage()).contains("004");
        }

        @Test
        @DisplayName("성공 - 짧은 계좌번호는 마스킹된다")
        void shouldMaskShortAccountNumber() {
            // Given
            String bankCode = "004";
            String shortAccountNumber = "12345678";

            // When
            AccountVerificationFailedException exception =
                    new AccountVerificationFailedException(bankCode, shortAccountNumber);

            // Then
            assertThat(exception).isNotNull();
        }

        @Test
        @DisplayName("성공 - null 계좌번호는 마스킹된다")
        void shouldHandleNullAccountNumber() {
            // Given
            String bankCode = "004";

            // When
            AccountVerificationFailedException exception =
                    new AccountVerificationFailedException(bankCode, null);

            // Then
            assertThat(exception).isNotNull();
        }
    }
}
