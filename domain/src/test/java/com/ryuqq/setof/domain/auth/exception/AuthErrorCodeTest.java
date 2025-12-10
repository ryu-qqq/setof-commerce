package com.ryuqq.setof.domain.auth.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AuthErrorCode")
class AuthErrorCodeTest {

    @Nested
    @DisplayName("INVALID_REFRESH_TOKEN")
    class InvalidRefreshTokenTest {

        @Test
        @DisplayName("에러 코드 확인")
        void shouldHaveCorrectCode() {
            // Then
            assertEquals("AUTH-001", AuthErrorCode.INVALID_REFRESH_TOKEN.getCode());
        }

        @Test
        @DisplayName("HTTP 상태 코드 확인")
        void shouldHaveCorrectHttpStatus() {
            // Then
            assertEquals(401, AuthErrorCode.INVALID_REFRESH_TOKEN.getHttpStatus());
        }

        @Test
        @DisplayName("메시지 확인")
        void shouldHaveCorrectMessage() {
            // Then
            assertNotNull(AuthErrorCode.INVALID_REFRESH_TOKEN.getMessage());
        }
    }

    @Nested
    @DisplayName("EXPIRED_REFRESH_TOKEN")
    class ExpiredRefreshTokenTest {

        @Test
        @DisplayName("에러 코드 확인")
        void shouldHaveCorrectCode() {
            // Then
            assertEquals("AUTH-002", AuthErrorCode.EXPIRED_REFRESH_TOKEN.getCode());
        }

        @Test
        @DisplayName("HTTP 상태 코드 확인")
        void shouldHaveCorrectHttpStatus() {
            // Then
            assertEquals(401, AuthErrorCode.EXPIRED_REFRESH_TOKEN.getHttpStatus());
        }
    }

    @Nested
    @DisplayName("INVALID_ACCESS_TOKEN")
    class InvalidAccessTokenTest {

        @Test
        @DisplayName("에러 코드 확인")
        void shouldHaveCorrectCode() {
            // Then
            assertEquals("AUTH-003", AuthErrorCode.INVALID_ACCESS_TOKEN.getCode());
        }

        @Test
        @DisplayName("HTTP 상태 코드 확인")
        void shouldHaveCorrectHttpStatus() {
            // Then
            assertEquals(401, AuthErrorCode.INVALID_ACCESS_TOKEN.getHttpStatus());
        }
    }

    @Nested
    @DisplayName("EXPIRED_ACCESS_TOKEN")
    class ExpiredAccessTokenTest {

        @Test
        @DisplayName("에러 코드 확인")
        void shouldHaveCorrectCode() {
            // Then
            assertEquals("AUTH-004", AuthErrorCode.EXPIRED_ACCESS_TOKEN.getCode());
        }

        @Test
        @DisplayName("HTTP 상태 코드 확인")
        void shouldHaveCorrectHttpStatus() {
            // Then
            assertEquals(401, AuthErrorCode.EXPIRED_ACCESS_TOKEN.getHttpStatus());
        }
    }

    @Nested
    @DisplayName("Enum 열거값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("4개의 에러 코드가 정의됨")
        void shouldHaveFourErrorCodes() {
            // Then
            assertEquals(4, AuthErrorCode.values().length);
        }
    }
}
