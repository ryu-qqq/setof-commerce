package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** InvalidEmailException 테스트 */
@DisplayName("InvalidEmailException")
class InvalidEmailExceptionTest {

    @Test
    @DisplayName("기본 생성자 사용 시 기본 메시지 포함")
    void shouldHaveDefaultMessage() {
        // When
        InvalidEmailException exception = new InvalidEmailException();

        // Then
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("이메일"));
    }

    @Test
    @DisplayName("커스텀 메시지로 생성")
    void shouldHaveCustomMessage() {
        // Given
        String customMessage = "테스트 에러 메시지";

        // When
        InvalidEmailException exception = new InvalidEmailException(customMessage);

        // Then
        assertEquals(customMessage, exception.getMessage());
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
