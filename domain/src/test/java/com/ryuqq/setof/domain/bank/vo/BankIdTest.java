package com.ryuqq.setof.domain.bank.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.bank.exception.InvalidBankIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * BankId Value Object 테스트
 *
 * <p>Zero-Tolerance Rules: - Lombok 금지 (Pure Java Record) - 불변성 보장 - 양수 ID 값 검증
 */
@DisplayName("BankId Value Object")
class BankIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 ID로 BankId 생성")
        void shouldCreateBankIdWithValidValue() {
            // Given
            Long validId = 1L;

            // When
            BankId bankId = BankId.of(validId);

            // Then
            assertNotNull(bankId);
            assertEquals(validId, bankId.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 ID로 생성")
        @ValueSource(longs = {1L, 10L, 100L, 1000L, Long.MAX_VALUE})
        void shouldCreateBankIdWithVariousValidValues(Long validId) {
            // When
            BankId bankId = BankId.of(validId);

            // Then
            assertNotNull(bankId);
            assertEquals(validId, bankId.value());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsNull() {
            // Given
            Long nullId = null;

            // When & Then
            assertThrows(InvalidBankIdException.class, () -> BankId.of(nullId));
        }

        @Test
        @DisplayName("0 이하의 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsZero() {
            // Given
            Long zeroId = 0L;

            // When & Then
            assertThrows(InvalidBankIdException.class, () -> BankId.of(zeroId));
        }

        @Test
        @DisplayName("음수 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsNegative() {
            // Given
            Long negativeId = -1L;

            // When & Then
            assertThrows(InvalidBankIdException.class, () -> BankId.of(negativeId));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 BankId는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            BankId id1 = BankId.of(1L);
            BankId id2 = BankId.of(1L);

            // When & Then
            assertEquals(id1, id2);
            assertEquals(id1.hashCode(), id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 BankId는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            BankId id1 = BankId.of(1L);
            BankId id2 = BankId.of(2L);

            // When & Then
            assertNotEquals(id1, id2);
        }
    }
}
