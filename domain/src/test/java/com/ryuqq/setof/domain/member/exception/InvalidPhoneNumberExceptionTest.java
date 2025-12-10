package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** InvalidPhoneNumberException 테스트 */
@DisplayName("InvalidPhoneNumberException")
class InvalidPhoneNumberExceptionTest {

    @Test
    @DisplayName("잘못된 값으로 생성")
    void shouldHaveMessageWithInvalidValue() {
        // Given
        String invalidValue = "01112345678";

        // When
        InvalidPhoneNumberException exception = new InvalidPhoneNumberException(invalidValue);

        // Then
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(invalidValue));
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
