package com.ryuqq.setof.domain.member.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.member.exception.InvalidEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Email Value Object 테스트
 *
 * <p>Zero-Tolerance Rules: - Lombok 금지 (Pure Java Record) - 불변성 보장 - RFC 5322 형식 검증 - nullable 허용
 * (선택적 필드)
 */
@DisplayName("Email Value Object")
class EmailTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 이메일로 Email 생성")
        void shouldCreateEmailWithValidFormat() {
            // Given
            String validEmail = "test@example.com";

            // When
            Email email = Email.of(validEmail);

            // Then
            assertNotNull(email);
            assertEquals(validEmail, email.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 이메일 형식으로 생성")
        @ValueSource(
                strings = {
                    "user@domain.com",
                    "user.name@domain.com",
                    "user+tag@domain.com",
                    "user@subdomain.domain.com",
                    "user123@domain123.com",
                    "user_name@domain.co.kr"
                })
        void shouldCreateEmailWithVariousValidFormats(String validEmail) {
            // When
            Email email = Email.of(validEmail);

            // Then
            assertNotNull(email);
            assertEquals(validEmail, email.value());
        }

        @Test
        @DisplayName("null 값으로 Email 생성 허용 (nullable)")
        void shouldAllowNullEmail() {
            // Given
            String nullEmail = null;

            // When
            Email email = Email.of(nullEmail);

            // Then
            assertNotNull(email);
            assertNull(email.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenEmailIsEmpty() {
            // Given
            String emptyEmail = "";

            // When & Then
            assertThrows(InvalidEmailException.class, () -> Email.of(emptyEmail));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenEmailIsBlank() {
            // Given
            String blankEmail = "   ";

            // When & Then
            assertThrows(InvalidEmailException.class, () -> Email.of(blankEmail));
        }

        @ParameterizedTest
        @DisplayName("잘못된 형식으로 생성 시 예외 발생")
        @ValueSource(
                strings = {
                    "invalid", // @ 없음
                    "@domain.com", // 로컬 파트 없음
                    "user@", // 도메인 없음
                    "user@.com", // 도메인 시작이 .
                    "user@domain", // TLD 없음
                    "user@domain.", // 도메인 끝이 .
                    "user name@domain.com", // 공백 포함
                    "user@domain..com" // 연속 점
                })
        void shouldThrowExceptionWhenEmailIsInvalidFormat(String invalidEmail) {
            // When & Then
            assertThrows(InvalidEmailException.class, () -> Email.of(invalidEmail));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 Email은 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            Email email1 = Email.of("test@example.com");
            Email email2 = Email.of("test@example.com");

            // When & Then
            assertEquals(email1, email2);
            assertEquals(email1.hashCode(), email2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 Email은 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            Email email1 = Email.of("test1@example.com");
            Email email2 = Email.of("test2@example.com");

            // When & Then
            assertNotEquals(email1, email2);
        }

        @Test
        @DisplayName("두 null Email은 동등하다")
        void shouldBeEqualWhenBothNull() {
            // Given
            Email email1 = Email.of(null);
            Email email2 = Email.of(null);

            // When & Then
            assertEquals(email1, email2);
            assertEquals(email1.hashCode(), email2.hashCode());
        }
    }

    @Nested
    @DisplayName("유틸리티 메서드 테스트")
    class UtilityMethodTest {

        @Test
        @DisplayName("값이 있으면 hasValue() true 반환")
        void shouldReturnTrueWhenHasValue() {
            // Given
            Email email = Email.of("test@example.com");

            // When & Then
            assertTrue(email.hasValue());
        }

        @Test
        @DisplayName("값이 null이면 hasValue() false 반환")
        void shouldReturnFalseWhenValueIsNull() {
            // Given
            Email email = Email.of(null);

            // When & Then
            assertFalse(email.hasValue());
        }
    }
}
