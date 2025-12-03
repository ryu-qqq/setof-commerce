package com.ryuqq.setof.domain.core.member.vo;

import static org.junit.jupiter.api.Assertions.*;

import com.ryuqq.setof.domain.core.member.exception.InvalidPhoneNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * PhoneNumber Value Object 테스트
 *
 * <p>Zero-Tolerance Rules: - Lombok 금지 (Pure Java Record) - 불변성 보장 - 010[0-9]{8} 정규식 검증
 */
@DisplayName("PhoneNumber Value Object")
class PhoneNumberTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 핸드폰 번호로 PhoneNumber 생성 - 01012345678")
        void shouldCreatePhoneNumberWithValidFormat() {
            // Given
            String validPhoneNumber = "01012345678";

            // When
            PhoneNumber phoneNumber = PhoneNumber.of(validPhoneNumber);

            // Then
            assertNotNull(phoneNumber);
            assertEquals(validPhoneNumber, phoneNumber.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 핸드폰 번호로 생성")
        @ValueSource(strings = {"01000000000", "01099999999", "01012345678", "01087654321"})
        void shouldCreatePhoneNumberWithVariousValidFormats(String validPhoneNumber) {
            // When
            PhoneNumber phoneNumber = PhoneNumber.of(validPhoneNumber);

            // Then
            assertNotNull(phoneNumber);
            assertEquals(validPhoneNumber, phoneNumber.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenPhoneNumberIsNull() {
            // Given
            String nullPhoneNumber = null;

            // When & Then
            InvalidPhoneNumberException exception =
                    assertThrows(
                            InvalidPhoneNumberException.class,
                            () -> PhoneNumber.of(nullPhoneNumber));

            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenPhoneNumberIsEmpty() {
            // Given
            String emptyPhoneNumber = "";

            // When & Then
            assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of(emptyPhoneNumber));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenPhoneNumberIsBlank() {
            // Given
            String blankPhoneNumber = "   ";

            // When & Then
            assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of(blankPhoneNumber));
        }

        @ParameterizedTest
        @DisplayName("잘못된 형식으로 생성 시 예외 발생")
        @ValueSource(
                strings = {
                    "0101234567", // 10자리 (11자리 필요)
                    "010123456789", // 12자리 (11자리 필요)
                    "01112345678", // 011로 시작 (010 필요)
                    "02012345678", // 020로 시작 (010 필요)
                    "010-1234-5678", // 하이픈 포함
                    "010 1234 5678", // 공백 포함
                    "0101234567a", // 문자 포함
                    "phone12345678" // 문자로 시작
                })
        void shouldThrowExceptionWhenPhoneNumberIsInvalidFormat(String invalidPhoneNumber) {
            // When & Then
            assertThrows(
                    InvalidPhoneNumberException.class, () -> PhoneNumber.of(invalidPhoneNumber));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 PhoneNumber는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            PhoneNumber phoneNumber1 = PhoneNumber.of("01012345678");
            PhoneNumber phoneNumber2 = PhoneNumber.of("01012345678");

            // When & Then
            assertEquals(phoneNumber1, phoneNumber2);
            assertEquals(phoneNumber1.hashCode(), phoneNumber2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 PhoneNumber는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            PhoneNumber phoneNumber1 = PhoneNumber.of("01012345678");
            PhoneNumber phoneNumber2 = PhoneNumber.of("01087654321");

            // When & Then
            assertNotEquals(phoneNumber1, phoneNumber2);
        }
    }
}
