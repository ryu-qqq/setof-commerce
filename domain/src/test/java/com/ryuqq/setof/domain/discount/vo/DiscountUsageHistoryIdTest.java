package com.ryuqq.setof.domain.discount.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.discount.exception.InvalidDiscountUsageHistoryIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * DiscountUsageHistoryId Value Object 테스트
 *
 * <p>할인 사용 히스토리 ID의 생성 및 검증 로직을 테스트합니다.
 */
@DisplayName("DiscountUsageHistoryId Value Object")
class DiscountUsageHistoryIdTest {

    @Nested
    @DisplayName("생성 검증")
    class Creation {

        @Test
        @DisplayName("유효한 ID를 생성할 수 있다")
        void shouldCreateValidId() {
            // when
            DiscountUsageHistoryId id = DiscountUsageHistoryId.of(1L);

            // then
            assertEquals(1L, id.value());
        }

        @Test
        @DisplayName("큰 ID 값도 생성할 수 있다")
        void shouldCreateLargeId() {
            // given
            Long largeValue = 9_999_999_999L;

            // when
            DiscountUsageHistoryId id = DiscountUsageHistoryId.of(largeValue);

            // then
            assertEquals(largeValue, id.value());
        }
    }

    @Nested
    @DisplayName("검증 실패")
    class ValidationFailure {

        @Test
        @DisplayName("null 값으로 생성 시 예외가 발생한다")
        void shouldThrowExceptionForNullValue() {
            // when & then
            assertThrows(
                    InvalidDiscountUsageHistoryIdException.class,
                    () -> DiscountUsageHistoryId.of(null));
        }

        @Test
        @DisplayName("0 값으로 생성 시 예외가 발생한다")
        void shouldThrowExceptionForZeroValue() {
            // when & then
            assertThrows(
                    InvalidDiscountUsageHistoryIdException.class,
                    () -> DiscountUsageHistoryId.of(0L));
        }

        @Test
        @DisplayName("음수 값으로 생성 시 예외가 발생한다")
        void shouldThrowExceptionForNegativeValue() {
            // when & then
            assertThrows(
                    InvalidDiscountUsageHistoryIdException.class,
                    () -> DiscountUsageHistoryId.of(-1L));
        }
    }

    @Nested
    @DisplayName("동등성")
    class Equality {

        @Test
        @DisplayName("같은 값을 가진 ID는 동등하다")
        void shouldBeEqualForSameValue() {
            // given
            DiscountUsageHistoryId id1 = DiscountUsageHistoryId.of(100L);
            DiscountUsageHistoryId id2 = DiscountUsageHistoryId.of(100L);

            // then
            assertEquals(id1, id2);
            assertEquals(id1.hashCode(), id2.hashCode());
        }
    }
}
