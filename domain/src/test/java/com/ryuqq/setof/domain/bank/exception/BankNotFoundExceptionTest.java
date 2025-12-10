package com.ryuqq.setof.domain.bank.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * BankNotFoundException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("BankNotFoundException 단위 테스트")
class BankNotFoundExceptionTest {

    @Nested
    @DisplayName("bankId 생성자")
    class BankIdConstructor {

        @Test
        @DisplayName("성공 - bankId로 예외를 생성한다")
        void shouldCreateWithBankId() {
            // Given
            Long bankId = 1L;

            // When
            BankNotFoundException exception = new BankNotFoundException(bankId);

            // Then
            assertThat(exception.getMessage()).contains("은행을 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getErrorCode()).isEqualTo(BankErrorCode.BANK_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("bankCode 생성자")
    class BankCodeConstructor {

        @Test
        @DisplayName("성공 - bankCode로 예외를 생성한다")
        void shouldCreateWithBankCode() {
            // Given
            String bankCode = "004";

            // When
            BankNotFoundException exception = new BankNotFoundException(bankCode);

            // Then
            assertThat(exception.getMessage()).contains("은행을 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("004");
        }
    }
}
