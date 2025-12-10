package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InvalidRefreshTokenException")
class InvalidRefreshTokenExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성")
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidRefreshTokenException exception = new InvalidRefreshTokenException();
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("memberId로 예외 생성")
    void shouldCreateExceptionWithMemberId() {
        UUID memberId = UUID.randomUUID();
        InvalidRefreshTokenException exception = new InvalidRefreshTokenException(memberId);
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("사유로 예외 생성")
    void shouldCreateExceptionWithReason() {
        InvalidRefreshTokenException exception =
                new InvalidRefreshTokenException("Refresh Token이 만료되었습니다.");
        assertNotNull(exception.getMessage());
    }
}
