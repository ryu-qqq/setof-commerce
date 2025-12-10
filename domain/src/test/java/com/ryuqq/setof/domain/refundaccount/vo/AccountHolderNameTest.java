package com.ryuqq.setof.domain.refundaccount.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.refundaccount.exception.InvalidAccountHolderNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * AccountHolderName Value Object 테스트
 *
 * <p>예금주명 검증 로직을 테스트합니다.
 */
@DisplayName("AccountHolderName Value Object")
class AccountHolderNameTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 예금주명으로 AccountHolderName 생성")
        void shouldCreateAccountHolderNameWithValidValue() {
            // Given
            String validName = "홍길동";

            // When
            AccountHolderName accountHolderName = AccountHolderName.of(validName);

            // Then
            assertNotNull(accountHolderName);
            assertEquals(validName, accountHolderName.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 예금주명으로 생성")
        @ValueSource(strings = {"홍길동", "김철수", "John Doe", "가", "가나다라마바사아자차카타파하"})
        void shouldCreateAccountHolderNameWithVariousValidFormats(String validName) {
            // When
            AccountHolderName accountHolderName = AccountHolderName.of(validName);

            // Then
            assertNotNull(accountHolderName);
            assertEquals(validName, accountHolderName.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsNull() {
            // When & Then
            assertThrows(InvalidAccountHolderNameException.class, () -> AccountHolderName.of(null));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsEmpty() {
            // When & Then
            assertThrows(InvalidAccountHolderNameException.class, () -> AccountHolderName.of(""));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsBlank() {
            // When & Then
            assertThrows(InvalidAccountHolderNameException.class, () -> AccountHolderName.of("   "));
        }

        @Test
        @DisplayName("20자를 초과하는 예금주명으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameExceedsMaxLength() {
            // Given
            String longName = "가".repeat(21);

            // When & Then
            assertThrows(InvalidAccountHolderNameException.class, () -> AccountHolderName.of(longName));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 AccountHolderName은 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            AccountHolderName name1 = AccountHolderName.of("홍길동");
            AccountHolderName name2 = AccountHolderName.of("홍길동");

            // When & Then
            assertEquals(name1, name2);
            assertEquals(name1.hashCode(), name2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 AccountHolderName은 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            AccountHolderName name1 = AccountHolderName.of("홍길동");
            AccountHolderName name2 = AccountHolderName.of("김철수");

            // When & Then
            assertNotEquals(name1, name2);
        }
    }
}
