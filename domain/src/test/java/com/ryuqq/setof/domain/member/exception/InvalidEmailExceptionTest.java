package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** InvalidEmailException 테스트 */
@DisplayName("InvalidEmailException")
class InvalidEmailExceptionTest {

    @Test
    @DisplayName("잘못된 값으로 생성")
    void shouldHaveMessageWithInvalidValue() {
        // Given
        String invalidValue = "invalid-email";

        // When
        InvalidEmailException exception = new InvalidEmailException(invalidValue);

        // Then
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(invalidValue));
    }

    @Test
    @DisplayName("잘못된 값과 사유로 생성 시 상세 메시지")
    void shouldHaveMessageWithInvalidValueAndReason() {
        // Given
        String invalidValue = "invalid-email";
        String reason = "RFC 5322 형식이어야 합니다.";

        // When
        InvalidEmailException exception = new InvalidEmailException(invalidValue, reason);

        // Then
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(invalidValue));
        assertTrue(exception.getMessage().contains(reason));
    }
}
