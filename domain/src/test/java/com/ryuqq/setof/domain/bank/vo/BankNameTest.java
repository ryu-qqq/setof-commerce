package com.ryuqq.setof.domain.bank.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.bank.exception.InvalidBankNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * BankName Value Object 테스트
 *
 * <p>Zero-Tolerance Rules: - Lombok 금지 (Pure Java Record) - 불변성 보장 - 은행명 길이 검증 (1~50자)
 */
@DisplayName("BankName Value Object")
class BankNameTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 은행명으로 BankName 생성")
        void shouldCreateBankNameWithValidFormat() {
            // Given
            String validName = "KB국민은행";

            // When
            BankName bankName = BankName.of(validName);

            // Then
            assertNotNull(bankName);
            assertEquals(validName, bankName.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 은행명으로 생성")
        @ValueSource(strings = {"KB국민은행", "신한은행", "우리은행", "하나은행", "농협은행", "카카오뱅크", "토스뱅크", "케이뱅크"})
        void shouldCreateBankNameWithVariousValidFormats(String validName) {
            // When
            BankName bankName = BankName.of(validName);

            // Then
            assertNotNull(bankName);
            assertEquals(validName, bankName.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsNull() {
            // Given
            String nullName = null;

            // When & Then
            assertThrows(InvalidBankNameException.class, () -> BankName.of(nullName));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsEmpty() {
            // Given
            String emptyName = "";

            // When & Then
            assertThrows(InvalidBankNameException.class, () -> BankName.of(emptyName));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsBlank() {
            // Given
            String blankName = "   ";

            // When & Then
            assertThrows(InvalidBankNameException.class, () -> BankName.of(blankName));
        }

        @Test
        @DisplayName("30자를 초과하는 은행명으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameExceedsMaxLength() {
            // Given
            String longName = "가".repeat(31);

            // When & Then
            assertThrows(InvalidBankNameException.class, () -> BankName.of(longName));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 BankName은 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            BankName name1 = BankName.of("KB국민은행");
            BankName name2 = BankName.of("KB국민은행");

            // When & Then
            assertEquals(name1, name2);
            assertEquals(name1.hashCode(), name2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 BankName은 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            BankName name1 = BankName.of("KB국민은행");
            BankName name2 = BankName.of("신한은행");

            // When & Then
            assertNotEquals(name1, name2);
        }
    }
}
