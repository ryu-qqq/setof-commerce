package com.ryuqq.setof.domain.discount.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.CostShare;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountRate;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.DiscountType;
import com.ryuqq.setof.domain.discount.vo.MaximumDiscountAmount;
import com.ryuqq.setof.domain.discount.vo.MinimumOrderAmount;
import com.ryuqq.setof.domain.discount.vo.PolicyName;
import com.ryuqq.setof.domain.discount.vo.Priority;
import com.ryuqq.setof.domain.discount.vo.UsageLimit;
import com.ryuqq.setof.domain.discount.vo.ValidPeriod;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** DiscountCalculator Domain Service 테스트 */
@DisplayName("DiscountCalculator Domain Service")
class DiscountCalculatorTest {

    private DiscountCalculator calculator;
    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 1L;

    @BeforeEach
    void setUp() {
        calculator = new DiscountCalculator();
    }

    @Nested
    @DisplayName("calculateTotalDiscount() - 총 할인 금액 계산")
    class CalculateTotalDiscount {

        @Test
        @DisplayName("단일 정책의 할인 금액을 계산할 수 있다")
        void shouldCalculateSinglePolicyDiscount() {
            // given
            DiscountPolicy policy = createProductDiscount(10, 1); // 10%, 우선순위 1
            long orderAmount = 50000L;

            // when
            long totalDiscount = calculator.calculateTotalDiscount(List.of(policy), orderAmount);

            // then
            assertEquals(5000L, totalDiscount); // 50,000 * 10% = 5,000
        }

        @Test
        @DisplayName("같은 그룹 내 정책 중 우선순위가 높은 것만 적용된다")
        void shouldApplyHighestPriorityPolicyWithinSameGroup() {
            // given
            DiscountPolicy lowPriority = createProductDiscount(5, 100); // 5%, 우선순위 100 (낮음)
            DiscountPolicy highPriority = createProductDiscount(10, 1); // 10%, 우선순위 1 (높음)
            long orderAmount = 50000L;

            // when
            long totalDiscount =
                    calculator.calculateTotalDiscount(
                            List.of(lowPriority, highPriority), orderAmount);

            // then
            assertEquals(5000L, totalDiscount); // 우선순위 1의 10% 할인만 적용
        }

        @Test
        @DisplayName("서로 다른 그룹의 정책은 중첩 적용된다")
        void shouldStackDiscountsAcrossDifferentGroups() {
            // given
            DiscountPolicy productDiscount =
                    createPolicyWithGroup(DiscountGroup.PRODUCT, 10, 1); // 상품 10%
            DiscountPolicy memberDiscount =
                    createPolicyWithGroup(DiscountGroup.MEMBER, 5, 1); // 회원 5%
            long orderAmount = 100000L;

            // when
            long totalDiscount =
                    calculator.calculateTotalDiscount(
                            List.of(productDiscount, memberDiscount), orderAmount);

            // then
            // 상품 할인: 100,000 * 10% = 10,000
            // 회원 할인: (100,000 - 10,000) * 5% = 4,500
            assertEquals(14500L, totalDiscount);
        }

        @Test
        @DisplayName("할인 금액이 주문 금액을 초과하지 않는다")
        void shouldNotExceedOrderAmount() {
            // given
            DiscountPolicy bigDiscount1 =
                    createPolicyWithGroup(DiscountGroup.PRODUCT, 60, 1); // 60%
            DiscountPolicy bigDiscount2 = createPolicyWithGroup(DiscountGroup.MEMBER, 60, 1); // 60%
            long orderAmount = 10000L;

            // when
            long totalDiscount =
                    calculator.calculateTotalDiscount(
                            List.of(bigDiscount1, bigDiscount2), orderAmount);

            // then
            // PRODUCT 할인: 10,000 * 60% = 6,000
            // 남은 금액: 4,000
            // MEMBER 할인: 4,000 * 60% = 2,400
            // 총 할인: 8,400 (순차 적용으로 주문 금액 초과 불가)
            assertEquals(8400L, totalDiscount);
        }

        @Test
        @DisplayName("빈 정책 목록은 0을 반환한다")
        void shouldReturnZeroForEmptyPolicies() {
            // when
            long totalDiscount = calculator.calculateTotalDiscount(List.of(), 50000L);

            // then
            assertEquals(0L, totalDiscount);
        }
    }

    @Nested
    @DisplayName("calculateDiscountsByGroup() - 그룹별 할인 금액 계산")
    class CalculateDiscountsByGroup {

        @Test
        @DisplayName("그룹별 할인 금액을 분리하여 계산할 수 있다")
        void shouldCalculateDiscountsByGroup() {
            // given
            DiscountPolicy productDiscount =
                    createPolicyWithGroup(DiscountGroup.PRODUCT, 10, 1); // 상품 10%
            DiscountPolicy memberDiscount =
                    createPolicyWithGroup(DiscountGroup.MEMBER, 5, 1); // 회원 5%
            DiscountPolicy paymentDiscount =
                    createPolicyWithGroup(DiscountGroup.PAYMENT, 2, 1); // 결제 2%
            long orderAmount = 100000L;

            // when
            Map<DiscountGroup, Long> discounts =
                    calculator.calculateDiscountsByGroup(
                            List.of(productDiscount, memberDiscount, paymentDiscount), orderAmount);

            // then
            assertEquals(10000L, discounts.get(DiscountGroup.PRODUCT)); // 100,000 * 10%
            assertEquals(4500L, discounts.get(DiscountGroup.MEMBER)); // 90,000 * 5%
            assertEquals(1710L, discounts.get(DiscountGroup.PAYMENT)); // 85,500 * 2%
        }
    }

    @Nested
    @DisplayName("sortByPriority() - 우선순위 정렬")
    class SortByPriority {

        @Test
        @DisplayName("우선순위순으로 정렬할 수 있다")
        void shouldSortByPriority() {
            // given
            DiscountPolicy priority100 = createProductDiscount(10, 100);
            DiscountPolicy priority1 = createProductDiscount(10, 1);
            DiscountPolicy priority50 = createProductDiscount(10, 50);

            // when
            List<DiscountPolicy> sorted =
                    calculator.sortByPriority(List.of(priority100, priority1, priority50));

            // then
            assertEquals(1, sorted.get(0).getPriorityValue());
            assertEquals(50, sorted.get(1).getPriorityValue());
            assertEquals(100, sorted.get(2).getPriorityValue());
        }
    }

    // ========== Helper Methods ==========

    private DiscountPolicy createProductDiscount(int rate, int priority) {
        return createPolicyWithGroup(DiscountGroup.PRODUCT, rate, priority);
    }

    private DiscountPolicy createPolicyWithGroup(DiscountGroup group, int rate, int priority) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of((long) priority),
                TEST_SELLER_ID,
                PolicyName.of("테스트 정책 " + priority),
                group,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                null,
                DiscountRate.of(rate),
                null,
                MaximumDiscountAmount.unlimited(),
                MinimumOrderAmount.noMinimum(),
                ValidPeriod.of(
                        Instant.now().minus(1, ChronoUnit.DAYS),
                        Instant.now().plus(365, ChronoUnit.DAYS)),
                UsageLimit.unlimited(),
                CostShare.platformOnly(),
                Priority.of(priority),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }
}
