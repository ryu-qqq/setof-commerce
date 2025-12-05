package com.ryuqq.setof.domain.core.member.vo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.core.member.exception.InvalidPasswordException;
import com.ryuqq.setof.domain.core.member.exception.PasswordPolicyViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Password Value Object 테스트
 *
 * <p>검증 대상:
 *
 * <ul>
 *   <li>BCrypt 해시값 저장 (이미 해시된 값 저장)
 *   <li>NotBlank 검증
 *   <li>Static Factory 메서드
 * </ul>
 */
@DisplayName("Password Value Object")
class PasswordTest {

    @Nested
    @DisplayName("생성 테스트")
    class Creation {

        @Test
        @DisplayName("유효한 BCrypt 해시값으로 Password 생성 성공")
        void shouldCreatePasswordWithValidHash() {
            // Given - BCrypt 해시값 형식 ($2a$... 또는 $2b$...)
            String bcryptHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

            // When
            Password password = Password.of(bcryptHash);

            // Then
            assertNotNull(password);
            assertEquals(bcryptHash, password.value());
        }

        @Test
        @DisplayName("일반 문자열도 Password로 저장 가능 (해시 형식 강제 안함)")
        void shouldCreatePasswordWithAnyNonBlankString() {
            // Given - 해시가 아닌 일반 문자열도 저장 가능 (Application Layer에서 해시화 책임)
            String rawOrHash = "someHashedValue";

            // When
            Password password = Password.of(rawOrHash);

            // Then
            assertNotNull(password);
            assertEquals(rawOrHash, password.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailure {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenPasswordIsNull() {
            // When & Then
            InvalidPasswordException exception =
                    assertThrows(InvalidPasswordException.class, () -> Password.of(null));

            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenPasswordIsEmpty() {
            // When & Then
            InvalidPasswordException exception =
                    assertThrows(InvalidPasswordException.class, () -> Password.of(""));

            assertNotNull(exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"   ", "\t", "\n", "  \t\n  "})
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenPasswordIsBlank(String blankValue) {
            // When & Then
            assertThrows(InvalidPasswordException.class, () -> Password.of(blankValue));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class Equality {

        @Test
        @DisplayName("같은 해시값을 가진 Password는 동등")
        void shouldBeEqualWhenSameHash() {
            // Given
            String hash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

            // When
            Password password1 = Password.of(hash);
            Password password2 = Password.of(hash);

            // Then
            assertEquals(password1, password2);
            assertEquals(password1.hashCode(), password2.hashCode());
        }

        @Test
        @DisplayName("다른 해시값을 가진 Password는 다름")
        void shouldNotBeEqualWhenDifferentHash() {
            // Given
            String hash1 = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
            String hash2 = "$2a$10$dDJ3SW6W7PwQA/WVqDu.aOjA8/VTHcHqXgK6H3v.CkT2T7QqbG.kK";

            // When
            Password password1 = Password.of(hash1);
            Password password2 = Password.of(hash2);

            // Then
            assertNotEquals(password1, password2);
        }
    }

    @Nested
    @DisplayName("비밀번호 정책 검증 테스트")
    class PolicyValidation {

        @ParameterizedTest
        @ValueSource(
                strings = {
                    "Password1!", // 최소 요구사항 충족
                    "Abcd1234@", // 영문 대소문자 + 숫자 + 특수문자
                    "StrongPass99#", // 긴 비밀번호
                    "MyP@ssw0rd!" // 복잡한 비밀번호
                })
        @DisplayName("유효한 비밀번호 정책 통과")
        void shouldValidatePasswordPolicy(String rawPassword) {
            // When & Then - 예외 없이 통과
            assertDoesNotThrow(() -> Password.validatePolicy(rawPassword));
        }

        @Test
        @DisplayName("8자 미만 비밀번호는 정책 위반")
        void shouldThrowExceptionWhenPasswordTooShort() {
            // Given
            String shortPassword = "Pass1!"; // 6자

            // When & Then
            PasswordPolicyViolationException exception =
                    assertThrows(
                            PasswordPolicyViolationException.class,
                            () -> Password.validatePolicy(shortPassword));

            assertNotNull(exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(
                strings = {
                    "password1!", // 대문자 없음
                    "PASSWORD1!", // 소문자 없음
                    "Password!!", // 숫자 없음
                    "Password12" // 특수문자 없음
                })
        @DisplayName("영문 대소문자/숫자/특수문자 누락 시 정책 위반")
        void shouldThrowExceptionWhenPasswordPolicyViolated(String invalidPassword) {
            // When & Then
            assertThrows(
                    PasswordPolicyViolationException.class,
                    () -> Password.validatePolicy(invalidPassword));
        }

        @Test
        @DisplayName("null 비밀번호는 정책 위반")
        void shouldThrowExceptionWhenRawPasswordIsNull() {
            // When & Then
            assertThrows(
                    PasswordPolicyViolationException.class, () -> Password.validatePolicy(null));
        }

        @Test
        @DisplayName("빈 문자열 비밀번호는 정책 위반")
        void shouldThrowExceptionWhenRawPasswordIsEmpty() {
            // When & Then
            assertThrows(PasswordPolicyViolationException.class, () -> Password.validatePolicy(""));
        }
    }
}
