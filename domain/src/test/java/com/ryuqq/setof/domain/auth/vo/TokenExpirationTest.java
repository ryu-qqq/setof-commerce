package com.ryuqq.setof.domain.auth.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TokenExpiration")
class TokenExpirationTest {

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");
    private static final long EXPIRES_IN_SECONDS = 604800L; // 7일

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("of 팩토리 메서드로 TokenExpiration 생성")
        void shouldCreateTokenExpirationUsingOf() {
            // When
            TokenExpiration expiration = TokenExpiration.of(NOW, EXPIRES_IN_SECONDS);

            // Then
            assertEquals(NOW.plusSeconds(EXPIRES_IN_SECONDS), expiration.expiresAt());
            assertEquals(EXPIRES_IN_SECONDS, expiration.expiresInSeconds());
        }

        @Test
        @DisplayName("생성자로 직접 생성")
        void shouldCreateTokenExpirationUsingConstructor() {
            // Given
            Instant expiresAt = NOW.plusSeconds(EXPIRES_IN_SECONDS);

            // When
            TokenExpiration expiration = new TokenExpiration(expiresAt, EXPIRES_IN_SECONDS);

            // Then
            assertEquals(expiresAt, expiration.expiresAt());
            assertEquals(EXPIRES_IN_SECONDS, expiration.expiresInSeconds());
        }
    }

    @Nested
    @DisplayName("만료 여부 테스트")
    class IsExpiredTest {

        @Test
        @DisplayName("만료 시간 이전이면 false 반환")
        void shouldReturnFalseWhenNotExpired() {
            // Given
            TokenExpiration expiration = TokenExpiration.of(NOW, EXPIRES_IN_SECONDS);
            Instant beforeExpiration = NOW.plusSeconds(EXPIRES_IN_SECONDS - 1);

            // When
            boolean result = expiration.isExpired(beforeExpiration);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("만료 시간 이후이면 true 반환")
        void shouldReturnTrueWhenExpired() {
            // Given
            TokenExpiration expiration = TokenExpiration.of(NOW, EXPIRES_IN_SECONDS);
            Instant afterExpiration = NOW.plusSeconds(EXPIRES_IN_SECONDS + 1);

            // When
            boolean result = expiration.isExpired(afterExpiration);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("정확히 만료 시간이면 false 반환 (isAfter 사용)")
        void shouldReturnFalseWhenExactlyAtExpiration() {
            // Given
            TokenExpiration expiration = TokenExpiration.of(NOW, EXPIRES_IN_SECONDS);
            Instant exactExpiration = NOW.plusSeconds(EXPIRES_IN_SECONDS);

            // When
            boolean result = expiration.isExpired(exactExpiration);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("expiresAt이 null이면 예외 발생")
        void shouldThrowExceptionWhenExpiresAtIsNull() {
            // When & Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new TokenExpiration(null, EXPIRES_IN_SECONDS));
        }

        @Test
        @DisplayName("expiresInSeconds가 0이면 예외 발생")
        void shouldThrowExceptionWhenExpiresInSecondsIsZero() {
            // Given
            Instant expiresAt = NOW.plusSeconds(1);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> new TokenExpiration(expiresAt, 0));
        }

        @Test
        @DisplayName("expiresInSeconds가 음수이면 예외 발생")
        void shouldThrowExceptionWhenExpiresInSecondsIsNegative() {
            // Given
            Instant expiresAt = NOW.plusSeconds(1);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> new TokenExpiration(expiresAt, -1));
        }
    }
}
