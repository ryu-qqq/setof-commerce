package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InvalidRefreshTokenException")
class InvalidRefreshTokenExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성")
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidRefreshTokenException exception = new InvalidRefreshTokenException();
        assertEquals("유효하지 않은 Refresh Token입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("커스텀 메시지로 예외 생성")
    void shouldCreateExceptionWithCustomMessage() {
        InvalidRefreshTokenException exception =
                new InvalidRefreshTokenException("Refresh Token이 만료되었습니다.");
        assertEquals("Refresh Token이 만료되었습니다.", exception.getMessage());
    }
}
