package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** InvalidPhoneNumberException 테스트 */
@DisplayName("InvalidPhoneNumberException")
class InvalidPhoneNumberExceptionTest {

    @Test
    @DisplayName("기본 생성자 사용 시 기본 메시지 포함")
    void shouldHaveDefaultMessage() {
        // When
        InvalidPhoneNumberException exception = new InvalidPhoneNumberException();

        // Then
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("010"));
    }

    @Test
    @DisplayName("커스텀 메시지로 생성")
    void shouldHaveCustomMessage() {
        // Given
        String customMessage = "테스트 에러 메시지";

        // When
        InvalidPhoneNumberException exception = new InvalidPhoneNumberException(customMessage);

        // Then
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    @DisplayName("잘못된 값과 사유로 생성 시 상세 메시지")
    void shouldHaveMessageWithInvalidValueAndReason() {
        // Given
        String invalidValue = "01112345678";
        String reason = "010으로 시작해야 합니다.";

        // When
        InvalidPhoneNumberException exception =
                new InvalidPhoneNumberException(invalidValue, reason);

        // Then
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(invalidValue));
        assertTrue(exception.getMessage().contains(reason));
    }
}
