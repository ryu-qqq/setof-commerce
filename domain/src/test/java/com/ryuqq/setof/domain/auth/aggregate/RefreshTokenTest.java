package com.ryuqq.setof.domain.auth.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RefreshToken")
class RefreshTokenTest {

    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String TOKEN_VALUE = "refresh-token-123";
    private static final long EXPIRES_IN_SECONDS = 604800L; // 7일
    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("forNew - 새 RefreshToken 생성 (권장)")
    class ForNewTest {

        @Test
        @DisplayName("forNew로 RefreshToken 생성 성공")
        void shouldCreateRefreshTokenWithForNew() {
            // When
            RefreshToken refreshToken =
                    RefreshToken.forNew(MEMBER_ID, TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW);

            // Then
            assertNotNull(refreshToken);
            assertEquals(MEMBER_ID, refreshToken.getMemberId());
            assertEquals(TOKEN_VALUE, refreshToken.getTokenValue());
            assertEquals(EXPIRES_IN_SECONDS, refreshToken.getExpiresInSeconds());
            assertEquals(NOW, refreshToken.getCreatedAt());
        }

        @Test
        @DisplayName("forNew로 만료 시간 계산 확인")
        void shouldCalculateExpirationCorrectlyWithForNew() {
            // When
            RefreshToken refreshToken =
                    RefreshToken.forNew(MEMBER_ID, TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW);

            // Then
            Instant expectedExpiration = NOW.plusSeconds(EXPIRES_IN_SECONDS);
            assertEquals(expectedExpiration, refreshToken.getExpiresAt());
        }

        @Test
        @DisplayName("forNew로 memberId가 null이면 예외 발생")
        void shouldThrowExceptionWhenMemberIdIsNullWithForNew() {
            // When & Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> RefreshToken.forNew(null, TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW));
        }

        @Test
        @DisplayName("forNew로 memberId가 빈 문자열이면 예외 발생")
        void shouldThrowExceptionWhenMemberIdIsEmptyWithForNew() {
            // When & Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> RefreshToken.forNew("", TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW));
        }
    }

    @Nested
    @DisplayName("create - 기존 메서드 (하위 호환)")
    class CreateTest {

        @Test
        @DisplayName("RefreshToken 생성 성공")
        void shouldCreateRefreshToken() {
            // When
            RefreshToken refreshToken =
                    RefreshToken.create(MEMBER_ID, TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW);

            // Then
            assertNotNull(refreshToken);
            assertEquals(MEMBER_ID, refreshToken.getMemberId());
            assertEquals(TOKEN_VALUE, refreshToken.getTokenValue());
            assertEquals(EXPIRES_IN_SECONDS, refreshToken.getExpiresInSeconds());
            assertEquals(NOW, refreshToken.getCreatedAt());
        }

        @Test
        @DisplayName("만료 시간 계산 확인")
        void shouldCalculateExpirationCorrectly() {
            // When
            RefreshToken refreshToken =
                    RefreshToken.create(MEMBER_ID, TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW);

            // Then
            Instant expectedExpiration = NOW.plusSeconds(EXPIRES_IN_SECONDS);
            assertEquals(expectedExpiration, refreshToken.getExpiresAt());
        }
    }

    @Nested
    @DisplayName("create 검증")
    class CreateValidationTest {

        @Test
        @DisplayName("memberId가 null이면 예외 발생")
        void shouldThrowExceptionWhenMemberIdIsNull() {
            // When & Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> RefreshToken.create(null, TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW));
        }

        @Test
        @DisplayName("memberId가 빈 문자열이면 예외 발생")
        void shouldThrowExceptionWhenMemberIdIsEmpty() {
            // When & Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> RefreshToken.create("", TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW));
        }

        @Test
        @DisplayName("memberId가 공백 문자열이면 예외 발생")
        void shouldThrowExceptionWhenMemberIdIsBlank() {
            // When & Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> RefreshToken.create("   ", TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW));
        }
    }

    @Nested
    @DisplayName("reconstitute")
    class ReconstituteTest {

        @Test
        @DisplayName("기존 데이터로 RefreshToken 재구성")
        void shouldReconstituteRefreshToken() {
            // Given
            Instant expiresAt = NOW.plusSeconds(EXPIRES_IN_SECONDS);

            // When
            RefreshToken refreshToken =
                    RefreshToken.reconstitute(MEMBER_ID, TOKEN_VALUE, expiresAt, NOW);

            // Then
            assertNotNull(refreshToken);
            assertEquals(MEMBER_ID, refreshToken.getMemberId());
            assertEquals(TOKEN_VALUE, refreshToken.getTokenValue());
            assertEquals(expiresAt, refreshToken.getExpiresAt());
            assertEquals(NOW, refreshToken.getCreatedAt());
        }

        @Test
        @DisplayName("재구성 시 expiresInSeconds 계산 확인")
        void shouldCalculateExpiresInSecondsOnReconstitute() {
            // Given
            Instant expiresAt = NOW.plusSeconds(EXPIRES_IN_SECONDS);

            // When
            RefreshToken refreshToken =
                    RefreshToken.reconstitute(MEMBER_ID, TOKEN_VALUE, expiresAt, NOW);

            // Then
            assertEquals(EXPIRES_IN_SECONDS, refreshToken.getExpiresInSeconds());
        }
    }

    @Nested
    @DisplayName("isExpired")
    class IsExpiredTest {

        @Test
        @DisplayName("만료되지 않은 토큰은 false 반환")
        void shouldReturnFalseWhenNotExpired() {
            // Given
            RefreshToken refreshToken =
                    RefreshToken.create(MEMBER_ID, TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW);
            Instant beforeExpiration = NOW.plusSeconds(EXPIRES_IN_SECONDS - 1);

            // When
            boolean result = refreshToken.isExpired(beforeExpiration);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("만료된 토큰은 true 반환")
        void shouldReturnTrueWhenExpired() {
            // Given
            RefreshToken refreshToken =
                    RefreshToken.create(MEMBER_ID, TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW);
            Instant afterExpiration = NOW.plusSeconds(EXPIRES_IN_SECONDS + 1);

            // When
            boolean result = refreshToken.isExpired(afterExpiration);

            // Then
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("toCacheKey")
    class ToCacheKeyTest {

        @Test
        @DisplayName("캐시 키 생성 확인")
        void shouldCreateCacheKey() {
            // Given
            RefreshToken refreshToken =
                    RefreshToken.create(MEMBER_ID, TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW);

            // When
            RefreshTokenCacheKey cacheKey = refreshToken.toCacheKey();

            // Then
            assertNotNull(cacheKey);
            assertEquals("cache:refresh-token:" + TOKEN_VALUE, cacheKey.value());
        }
    }
}
