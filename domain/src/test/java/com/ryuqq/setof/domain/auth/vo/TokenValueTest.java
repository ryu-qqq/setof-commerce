package com.ryuqq.setof.domain.auth.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TokenValue")
class TokenValueTest {

    private static final String VALID_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjg5MDAwMDAwfQ.xxx";

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 토큰 값으로 TokenValue 생성")
        void shouldCreateTokenValueWithValidValue() {
            // When
            TokenValue tokenValue = TokenValue.of(VALID_TOKEN);

            // Then
            assertEquals(VALID_TOKEN, tokenValue.value());
        }

        @Test
        @DisplayName("of 정적 팩토리 메서드 동작 확인")
        void shouldCreateTokenValueUsingOfMethod() {
            // When
            TokenValue tokenValue = TokenValue.of(VALID_TOKEN);

            // Then
            assertEquals(VALID_TOKEN, tokenValue.value());
        }
    }

    @Nested
    @DisplayName("검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenValueIsNull() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> TokenValue.of(null));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenValueIsEmpty() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> TokenValue.of(""));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenValueIsBlank() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> TokenValue.of("   "));
        }

        @Test
        @DisplayName("512자 초과 토큰 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenValueExceedsMaxLength() {
            // Given
            String tooLongToken = "a".repeat(513);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> TokenValue.of(tooLongToken));
        }

        @Test
        @DisplayName("512자 이하 토큰 값은 정상 생성")
        void shouldCreateTokenValueWithMaxLengthValue() {
            // Given
            String maxLengthToken = "a".repeat(512);

            // When
            TokenValue tokenValue = TokenValue.of(maxLengthToken);

            // Then
            assertEquals(maxLengthToken, tokenValue.value());
        }
    }
}
