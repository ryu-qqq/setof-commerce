package com.ryuqq.setof.domain.bank.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * InvalidBankIdException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("InvalidBankIdException 단위 테스트")
class InvalidBankIdExceptionTest {

    @Test
    @DisplayName("성공 - 유효하지 않은 ID로 예외를 생성한다")
    void shouldCreateWithInvalidId() {
        // Given
        Long invalidId = -1L;

        // When
        InvalidBankIdException exception = new InvalidBankIdException(invalidId);

        // Then
        assertThat(exception.getMessage()).contains("유효하지 않은 은행 ID");
        assertThat(exception.getMessage()).contains("-1");
        assertThat(exception.getErrorCode()).isEqualTo(BankErrorCode.INVALID_BANK_ID);
    }

    @Test
    @DisplayName("성공 - null ID로 예외를 생성한다")
    void shouldCreateWithNullId() {
        // When
        InvalidBankIdException exception = new InvalidBankIdException(null);

        // Then
        assertThat(exception.getMessage()).contains("유효하지 않은 은행 ID");
        assertThat(exception.getMessage()).contains("null");
    }
}
