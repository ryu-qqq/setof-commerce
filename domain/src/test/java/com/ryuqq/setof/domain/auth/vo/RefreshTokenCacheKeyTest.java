package com.ryuqq.setof.domain.auth.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RefreshTokenCacheKey")
class RefreshTokenCacheKeyTest {

    private static final String VALID_TOKEN = "refresh-token-123";
    private static final String EXPECTED_PREFIX = "cache:refresh-token:";

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 토큰으로 캐시 키 생성")
        void shouldCreateCacheKeyWithValidToken() {
            // When
            RefreshTokenCacheKey cacheKey = RefreshTokenCacheKey.of(VALID_TOKEN);

            // Then
            assertEquals(VALID_TOKEN, cacheKey.token());
        }

        @Test
        @DisplayName("of 정적 팩토리 메서드 동작 확인")
        void shouldCreateCacheKeyUsingOfMethod() {
            // When
            RefreshTokenCacheKey cacheKey = RefreshTokenCacheKey.of(VALID_TOKEN);

            // Then
            assertEquals(VALID_TOKEN, cacheKey.token());
        }
    }

    @Nested
    @DisplayName("value() 메서드 테스트")
    class ValueTest {

        @Test
        @DisplayName("캐시 키 값에 prefix가 포함됨")
        void shouldContainPrefixInValue() {
            // When
            RefreshTokenCacheKey cacheKey = RefreshTokenCacheKey.of(VALID_TOKEN);

            // Then
            assertTrue(cacheKey.value().startsWith(EXPECTED_PREFIX));
        }

        @Test
        @DisplayName("캐시 키 값 형식 확인")
        void shouldReturnCorrectValueFormat() {
            // When
            RefreshTokenCacheKey cacheKey = RefreshTokenCacheKey.of(VALID_TOKEN);

            // Then
            assertEquals(EXPECTED_PREFIX + VALID_TOKEN, cacheKey.value());
        }
    }

    @Nested
    @DisplayName("검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("null 토큰으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenTokenIsNull() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> RefreshTokenCacheKey.of(null));
        }

        @Test
        @DisplayName("빈 문자열 토큰으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenTokenIsEmpty() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> RefreshTokenCacheKey.of(""));
        }

        @Test
        @DisplayName("공백 문자열 토큰으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenTokenIsBlank() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> RefreshTokenCacheKey.of("   "));
        }
    }
}
