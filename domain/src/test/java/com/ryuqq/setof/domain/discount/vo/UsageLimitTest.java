package com.ryuqq.setof.domain.discount.vo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.discount.exception.InvalidUsageLimitException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** UsageLimit Value Object 테스트 */
@DisplayName("UsageLimit Value Object")
class UsageLimitTest {

    @Nested
    @DisplayName("생성 검증")
    class Creation {

        @Test
        @DisplayName("유효한 사용 제한을 생성할 수 있다")
        void shouldCreateValidUsageLimit() {
            // when
            UsageLimit limit = UsageLimit.of(3, 100, UsageLimit.ResetPeriod.DAILY);

            // then
            assertTrue(limit.hasResetPeriod());
            assertFalse(limit.isUnlimited());
        }

        @Test
        @DisplayName("무제한 사용 제한을 생성할 수 있다")
        void shouldCreateUnlimitedUsageLimit() {
            // when
            UsageLimit limit = UsageLimit.unlimited();

            // then
            assertTrue(limit.isUnlimited());
            assertFalse(limit.hasResetPeriod());
        }

        @Test
        @DisplayName("고객당 제한만 설정할 수 있다")
        void shouldCreatePerCustomerLimit() {
            // when
            UsageLimit limit = UsageLimit.perCustomer(5);

            // then
            assertFalse(limit.isUnlimited());
            assertTrue(limit.canCustomerUse(4));
            assertFalse(limit.canCustomerUse(5));
        }

        @Test
        @DisplayName("전체 제한만 설정할 수 있다")
        void shouldCreateTotalLimit() {
            // when
            UsageLimit limit = UsageLimit.total(100);

            // then
            assertFalse(limit.isUnlimited());
            assertTrue(limit.hasTotalCapacity(99));
            assertFalse(limit.hasTotalCapacity(100));
        }

        @Test
        @DisplayName("0 이하의 고객당 제한은 예외가 발생한다")
        void shouldThrowExceptionForZeroOrNegativePerCustomerLimit() {
            // when & then
            assertThrows(InvalidUsageLimitException.class, () -> UsageLimit.perCustomer(0));
            assertThrows(InvalidUsageLimitException.class, () -> UsageLimit.perCustomer(-1));
        }

        @Test
        @DisplayName("0 이하의 전체 제한은 예외가 발생한다")
        void shouldThrowExceptionForZeroOrNegativeTotalLimit() {
            // when & then
            assertThrows(InvalidUsageLimitException.class, () -> UsageLimit.total(0));
            assertThrows(InvalidUsageLimitException.class, () -> UsageLimit.total(-1));
        }

        @Test
        @DisplayName("100만을 초과하는 제한은 예외가 발생한다")
        void shouldThrowExceptionForLimitOverMillion() {
            // when & then
            assertThrows(InvalidUsageLimitException.class, () -> UsageLimit.perCustomer(1000001));
            assertThrows(InvalidUsageLimitException.class, () -> UsageLimit.total(1000001));
        }
    }

    @Nested
    @DisplayName("사용 가능 확인")
    class UsageCheck {

        @Test
        @DisplayName("고객이 사용 가능한지 확인할 수 있다")
        void shouldCheckIfCustomerCanUse() {
            // given
            UsageLimit limit = UsageLimit.perCustomer(3);

            // then
            assertTrue(limit.canCustomerUse(0));
            assertTrue(limit.canCustomerUse(2));
            assertFalse(limit.canCustomerUse(3));
            assertFalse(limit.canCustomerUse(4));
        }

        @Test
        @DisplayName("전체 용량이 남았는지 확인할 수 있다")
        void shouldCheckIfTotalCapacityRemains() {
            // given
            UsageLimit limit = UsageLimit.total(100);

            // then
            assertTrue(limit.hasTotalCapacity(0));
            assertTrue(limit.hasTotalCapacity(99));
            assertFalse(limit.hasTotalCapacity(100));
            assertFalse(limit.hasTotalCapacity(101));
        }

        @Test
        @DisplayName("무제한인 경우 항상 사용 가능하다")
        void shouldAlwaysAllowForUnlimited() {
            // given
            UsageLimit limit = UsageLimit.unlimited();

            // then
            assertTrue(limit.canCustomerUse(Integer.MAX_VALUE));
            assertTrue(limit.hasTotalCapacity(Integer.MAX_VALUE));
        }
    }
}
