package com.ryuqq.setof.domain.auth.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("InvalidRefreshTokenException")
class InvalidRefreshTokenExceptionTest {

    @Nested
    @DisplayName("기본 생성자 테스트")
    class DefaultConstructorTest {

        @Test
        @DisplayName("기본 에러 코드와 메시지로 생성")
        void shouldCreateWithDefaultErrorCodeAndMessage() {
            // When
            InvalidRefreshTokenException exception = new InvalidRefreshTokenException();

            // Then
            assertNotNull(exception);
            assertEquals("AUTH-001", exception.code());
            assertEquals("유효하지 않은 Refresh Token입니다.", exception.getMessage());
        }

        @Test
        @DisplayName("기본 HTTP 상태 코드 확인")
        void shouldHaveDefaultHttpStatus() {
            // When
            InvalidRefreshTokenException exception = new InvalidRefreshTokenException();

            // Then
            assertEquals(401, exception.httpStatus());
        }
    }

    @Nested
    @DisplayName("메시지 생성자 테스트")
    class MessageConstructorTest {

        @Test
        @DisplayName("커스텀 메시지로 생성")
        void shouldCreateWithCustomMessage() {
            // Given
            String customMessage = "토큰이 만료되었습니다.";

            // When
            InvalidRefreshTokenException exception =
                    new InvalidRefreshTokenException(customMessage);

            // Then
            assertNotNull(exception);
            assertEquals("AUTH-001", exception.code());
            assertEquals(customMessage, exception.getMessage());
        }

        @Test
        @DisplayName("커스텀 메시지로 생성해도 HTTP 상태 코드 유지")
        void shouldKeepHttpStatusWithCustomMessage() {
            // Given
            String customMessage = "토큰이 만료되었습니다.";

            // When
            InvalidRefreshTokenException exception =
                    new InvalidRefreshTokenException(customMessage);

            // Then
            assertEquals(401, exception.httpStatus());
        }
    }
}
