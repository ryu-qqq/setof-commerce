package com.ryuqq.setof.domain.bank.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * InvalidBankNameException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("InvalidBankNameException 단위 테스트")
class InvalidBankNameExceptionTest {

    @Test
    @DisplayName("성공 - 유효하지 않은 은행 이름으로 예외를 생성한다")
    void shouldCreateWithInvalidBankName() {
        // Given
        String invalidBankName = "";

        // When
        InvalidBankNameException exception = new InvalidBankNameException(invalidBankName);

        // Then
        assertThat(exception.getMessage()).contains("유효하지 않은 은행 이름");
        assertThat(exception.getErrorCode()).isEqualTo(BankErrorCode.INVALID_BANK_NAME);
    }

    @Test
    @DisplayName("성공 - null 은행 이름으로 예외를 생성한다")
    void shouldCreateWithNullBankName() {
        // When
        InvalidBankNameException exception = new InvalidBankNameException(null);

        // Then
        assertThat(exception.getMessage()).contains("유효하지 않은 은행 이름");
        assertThat(exception.getMessage()).contains("null");
    }
}
