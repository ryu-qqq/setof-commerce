package com.ryuqq.setof.domain.core.member.vo;

import com.ryuqq.setof.domain.core.member.exception.InvalidMemberIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MemberId Value Object 테스트
 *
 * Zero-Tolerance Rules:
 * - Lombok 금지 (Pure Java Record)
 * - 불변성 보장
 * - Long > 0 검증
 */
@DisplayName("MemberId Value Object")
class MemberIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 값으로 MemberId 생성 - Long > 0")
        void shouldCreateMemberIdWithValidValue() {
            // Given
            Long validId = 1L;

            // When
            MemberId memberId = MemberId.of(validId);

            // Then
            assertNotNull(memberId);
            assertEquals(validId, memberId.value());
        }

        @Test
        @DisplayName("큰 양수 값으로 MemberId 생성")
        void shouldCreateMemberIdWithLargeValue() {
            // Given
            Long largeId = Long.MAX_VALUE;

            // When
            MemberId memberId = MemberId.of(largeId);

            // Then
            assertNotNull(memberId);
            assertEquals(largeId, memberId.value());
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
            InvalidMemberIdException exception = assertThrows(
                InvalidMemberIdException.class,
                () -> MemberId.of(nullId)
            );

            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("0 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsZero() {
            // Given
            Long zeroId = 0L;

            // When & Then
            InvalidMemberIdException exception = assertThrows(
                InvalidMemberIdException.class,
                () -> MemberId.of(zeroId)
            );

            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("음수 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsNegative() {
            // Given
            Long negativeId = -1L;

            // When & Then
            InvalidMemberIdException exception = assertThrows(
                InvalidMemberIdException.class,
                () -> MemberId.of(negativeId)
            );

            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("Long.MIN_VALUE로 생성 시 예외 발생")
        void shouldThrowExceptionWhenIdIsMinValue() {
            // Given
            Long minId = Long.MIN_VALUE;

            // When & Then
            assertThrows(
                InvalidMemberIdException.class,
                () -> MemberId.of(minId)
            );
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 MemberId는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            MemberId memberId1 = MemberId.of(1L);
            MemberId memberId2 = MemberId.of(1L);

            // When & Then
            assertEquals(memberId1, memberId2);
            assertEquals(memberId1.hashCode(), memberId2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 MemberId는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            MemberId memberId1 = MemberId.of(1L);
            MemberId memberId2 = MemberId.of(2L);

            // When & Then
            assertNotEquals(memberId1, memberId2);
        }
    }
}
