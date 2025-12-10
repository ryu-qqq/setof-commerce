package com.ryuqq.setof.domain.bank.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * InvalidBankCodeException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("InvalidBankCodeException 단위 테스트")
class InvalidBankCodeExceptionTest {

    @Test
    @DisplayName("성공 - 유효하지 않은 은행 코드로 예외를 생성한다")
    void shouldCreateWithInvalidBankCode() {
        // Given
        String invalidBankCode = "12";

        // When
        InvalidBankCodeException exception = new InvalidBankCodeException(invalidBankCode);

        // Then
        assertThat(exception.getMessage()).contains("유효하지 않은 은행 코드");
        assertThat(exception.getMessage()).contains("12");
        assertThat(exception.getErrorCode()).isEqualTo(BankErrorCode.INVALID_BANK_CODE);
    }

    @Test
    @DisplayName("성공 - null 은행 코드로 예외를 생성한다")
    void shouldCreateWithNullBankCode() {
        // When
        InvalidBankCodeException exception = new InvalidBankCodeException(null);

        // Then
        assertThat(exception.getMessage()).contains("유효하지 않은 은행 코드");
        assertThat(exception.getMessage()).contains("null");
    }
}
