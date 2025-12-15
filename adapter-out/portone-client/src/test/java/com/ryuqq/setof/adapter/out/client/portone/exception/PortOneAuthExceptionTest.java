package com.ryuqq.setof.adapter.out.client.portone.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.adapter.out.client.portone.dto.auth.PortOneErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PortOneAuthException")
class PortOneAuthExceptionTest {

    @Nested
    @DisplayName("생성자")
    class ConstructorTest {

        @Test
        @DisplayName("메시지만 있는 생성자")
        void shouldCreateWithMessageOnly() {
            // When
            PortOneAuthException exception = new PortOneAuthException("Test error");

            // Then
            assertEquals("Test error", exception.getMessage());
            assertNull(exception.getErrorType());
        }

        @Test
        @DisplayName("메시지와 원인이 있는 생성자")
        void shouldCreateWithMessageAndCause() {
            // Given
            RuntimeException cause = new RuntimeException("Original error");

            // When
            PortOneAuthException exception = new PortOneAuthException("Test error", cause);

            // Then
            assertEquals("Test error", exception.getMessage());
            assertEquals(cause, exception.getCause());
            assertNull(exception.getErrorType());
        }

        @Test
        @DisplayName("ErrorResponse 기반 생성자")
        void shouldCreateFromErrorResponse() {
            // Given
            PortOneErrorResponse errorResponse =
                    new PortOneErrorResponse("INVALID_REQUEST", "Invalid parameter");

            // When
            PortOneAuthException exception = new PortOneAuthException(errorResponse);

            // Then
            assertTrue(exception.getMessage().contains("INVALID_REQUEST"));
            assertTrue(exception.getMessage().contains("Invalid parameter"));
            assertEquals("INVALID_REQUEST", exception.getErrorType());
        }

        @Test
        @DisplayName("null ErrorResponse 기반 생성자")
        void shouldCreateFromNullErrorResponse() {
            // When
            PortOneAuthException exception = new PortOneAuthException((PortOneErrorResponse) null);

            // Then
            assertTrue(exception.getMessage().contains("unknown error"));
            assertNull(exception.getErrorType());
        }

        @Test
        @DisplayName("errorType과 메시지 생성자")
        void shouldCreateWithErrorTypeAndMessage() {
            // When
            PortOneAuthException exception =
                    new PortOneAuthException("UNAUTHORIZED", "Auth failed");

            // Then
            assertEquals("Auth failed", exception.getMessage());
            assertEquals("UNAUTHORIZED", exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("에러 타입 검사")
    class ErrorTypeCheckTest {

        @Test
        @DisplayName("INVALID_REQUEST 타입 확인")
        void shouldDetectInvalidRequest() {
            // Given
            PortOneAuthException exception = PortOneAuthException.invalidRequest("Bad request");

            // Then
            assertTrue(exception.isInvalidRequest());
            assertFalse(exception.isUnauthorized());
        }

        @Test
        @DisplayName("UNAUTHORIZED 타입 확인")
        void shouldDetectUnauthorized() {
            // Given
            PortOneAuthException exception = PortOneAuthException.unauthorized("Auth failed");

            // Then
            assertFalse(exception.isInvalidRequest());
            assertTrue(exception.isUnauthorized());
        }

        @Test
        @DisplayName("null 에러 타입은 false")
        void shouldReturnFalseForNullErrorType() {
            // Given
            PortOneAuthException exception = new PortOneAuthException("Test error");

            // Then
            assertFalse(exception.isInvalidRequest());
            assertFalse(exception.isUnauthorized());
        }
    }

    @Nested
    @DisplayName("정적 팩토리 메서드")
    class StaticFactoryMethodTest {

        @Test
        @DisplayName("invalidRequest 팩토리 메서드")
        void shouldCreateInvalidRequestException() {
            // When
            PortOneAuthException exception = PortOneAuthException.invalidRequest("Invalid param");

            // Then
            assertNotNull(exception);
            assertEquals("Invalid param", exception.getMessage());
            assertEquals(PortOneErrorResponse.INVALID_REQUEST, exception.getErrorType());
        }

        @Test
        @DisplayName("unauthorized 팩토리 메서드")
        void shouldCreateUnauthorizedException() {
            // When
            PortOneAuthException exception = PortOneAuthException.unauthorized("Token expired");

            // Then
            assertNotNull(exception);
            assertEquals("Token expired", exception.getMessage());
            assertEquals(PortOneErrorResponse.UNAUTHORIZED, exception.getErrorType());
        }
    }
}
