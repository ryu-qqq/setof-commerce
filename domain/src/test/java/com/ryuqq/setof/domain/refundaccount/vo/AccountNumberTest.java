package com.ryuqq.setof.domain.refundaccount.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.refundaccount.exception.InvalidAccountNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * AccountNumber Value Object 테스트
 *
 * <p>계좌번호 검증과 마스킹/정규화 기능을 테스트합니다.
 */
@DisplayName("AccountNumber Value Object")
class AccountNumberTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 계좌번호로 AccountNumber 생성")
        void shouldCreateAccountNumberWithValidValue() {
            // Given
            String validAccountNumber = "1234567890123";

            // When
            AccountNumber accountNumber = AccountNumber.of(validAccountNumber);

            // Then
            assertNotNull(accountNumber);
            assertEquals(validAccountNumber, accountNumber.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 계좌번호로 생성")
        @ValueSource(strings = {"12345678", "123456789012", "12345678901234567890", "123-456-789012"})
        void shouldCreateAccountNumberWithVariousValidFormats(String validAccountNumber) {
            // When
            AccountNumber accountNumber = AccountNumber.of(validAccountNumber);

            // Then
            assertNotNull(accountNumber);
            assertEquals(validAccountNumber, accountNumber.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenAccountNumberIsNull() {
            // When & Then
            assertThrows(InvalidAccountNumberException.class, () -> AccountNumber.of(null));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenAccountNumberIsEmpty() {
            // When & Then
            assertThrows(InvalidAccountNumberException.class, () -> AccountNumber.of(""));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenAccountNumberIsBlank() {
            // When & Then
            assertThrows(InvalidAccountNumberException.class, () -> AccountNumber.of("   "));
        }

        @Test
        @DisplayName("8자리 미만의 계좌번호로 생성 시 예외 발생")
        void shouldThrowExceptionWhenAccountNumberIsTooShort() {
            // When & Then
            assertThrows(InvalidAccountNumberException.class, () -> AccountNumber.of("1234567"));
        }

        @Test
        @DisplayName("30자리 초과 계좌번호로 생성 시 예외 발생")
        void shouldThrowExceptionWhenAccountNumberIsTooLong() {
            // Given
            String longAccountNumber = "1".repeat(31);

            // When & Then
            assertThrows(InvalidAccountNumberException.class, () -> AccountNumber.of(longAccountNumber));
        }

        @ParameterizedTest
        @DisplayName("정규화 후 8자리 미만이면 예외 발생")
        @ValueSource(strings = {"123-456", "12-34-56", "1a2b3c4d"})
        void shouldThrowExceptionWhenNormalizedAccountNumberIsTooShort(String shortAccountNumber) {
            // When & Then
            assertThrows(
                    InvalidAccountNumberException.class, () -> AccountNumber.of(shortAccountNumber));
        }
    }

    @Nested
    @DisplayName("유틸리티 메서드 테스트")
    class UtilityMethodTest {

        @Test
        @DisplayName("normalized()는 숫자만 반환한다")
        void shouldReturnNormalizedAccountNumber() {
            // Given
            AccountNumber accountNumber = AccountNumber.of("123-456-789012");

            // When
            String normalized = accountNumber.normalized();

            // Then
            assertEquals("123456789012", normalized);
        }

        @Test
        @DisplayName("masked()는 앞 4자리와 뒤 4자리만 표시한다 (9자리 이상)")
        void shouldReturnMaskedAccountNumberForLong() {
            // Given
            AccountNumber accountNumber = AccountNumber.of("1234567890123");

            // When
            String masked = accountNumber.masked();

            // Then
            assertEquals("1234****0123", masked);
            assertTrue(masked.contains("****"));
        }

        @Test
        @DisplayName("masked()는 8자리 이하일 때 앞 2자리와 뒤 2자리만 표시한다")
        void shouldReturnMaskedAccountNumberForShort() {
            // Given
            AccountNumber accountNumber = AccountNumber.of("12345678");

            // When
            String masked = accountNumber.masked();

            // Then
            assertEquals("12****78", masked);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 AccountNumber는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            AccountNumber number1 = AccountNumber.of("1234567890123");
            AccountNumber number2 = AccountNumber.of("1234567890123");

            // When & Then
            assertEquals(number1, number2);
            assertEquals(number1.hashCode(), number2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 AccountNumber는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            AccountNumber number1 = AccountNumber.of("1234567890123");
            AccountNumber number2 = AccountNumber.of("9876543210987");

            // When & Then
            assertNotEquals(number1, number2);
        }
    }
}
