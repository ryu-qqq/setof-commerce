package com.ryuqq.setof.domain.bank.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.bank.exception.InvalidBankCodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * BankCode Value Object 테스트
 *
 * <p>Zero-Tolerance Rules: - Lombok 금지 (Pure Java Record) - 불변성 보장 - 3자리 숫자 형식 검증
 */
@DisplayName("BankCode Value Object")
class BankCodeTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 은행 코드로 BankCode 생성")
        void shouldCreateBankCodeWithValidFormat() {
            // Given
            String validCode = "004";

            // When
            BankCode bankCode = BankCode.of(validCode);

            // Then
            assertNotNull(bankCode);
            assertEquals(validCode, bankCode.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 은행 코드로 생성")
        @ValueSource(
                strings = {"004", "088", "020", "011", "023", "039", "031", "032", "002", "003"})
        void shouldCreateBankCodeWithVariousValidFormats(String validCode) {
            // When
            BankCode bankCode = BankCode.of(validCode);

            // Then
            assertNotNull(bankCode);
            assertEquals(validCode, bankCode.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenCodeIsNull() {
            // Given
            String nullCode = null;

            // When & Then
            assertThrows(InvalidBankCodeException.class, () -> BankCode.of(nullCode));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenCodeIsEmpty() {
            // Given
            String emptyCode = "";

            // When & Then
            assertThrows(InvalidBankCodeException.class, () -> BankCode.of(emptyCode));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenCodeIsBlank() {
            // Given
            String blankCode = "   ";

            // When & Then
            assertThrows(InvalidBankCodeException.class, () -> BankCode.of(blankCode));
        }

        @ParameterizedTest
        @DisplayName("잘못된 길이로 생성 시 예외 발생")
        @ValueSource(strings = {"01", "1", "0004", "12345"})
        void shouldThrowExceptionWhenCodeHasInvalidLength(String invalidCode) {
            // When & Then
            assertThrows(InvalidBankCodeException.class, () -> BankCode.of(invalidCode));
        }

        @ParameterizedTest
        @DisplayName("숫자가 아닌 문자 포함 시 예외 발생")
        @ValueSource(strings = {"00A", "ABC", "0a4", "1 2"})
        void shouldThrowExceptionWhenCodeContainsNonDigit(String invalidCode) {
            // When & Then
            assertThrows(InvalidBankCodeException.class, () -> BankCode.of(invalidCode));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 BankCode는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            BankCode code1 = BankCode.of("004");
            BankCode code2 = BankCode.of("004");

            // When & Then
            assertEquals(code1, code2);
            assertEquals(code1.hashCode(), code2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 BankCode는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            BankCode code1 = BankCode.of("004");
            BankCode code2 = BankCode.of("088");

            // When & Then
            assertNotEquals(code1, code2);
        }
    }
}
