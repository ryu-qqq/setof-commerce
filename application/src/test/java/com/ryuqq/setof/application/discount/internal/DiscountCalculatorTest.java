package com.ryuqq.setof.application.discount.internal;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountedPrice;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DiscountCalculator 단위 테스트")
class DiscountCalculatorTest {

    private DiscountCalculator sut;

    @BeforeEach
    void setUp() {
        sut = new DiscountCalculator();
    }

    @Nested
    @DisplayName("할인 정책이 없는 경우")
    class NoPoliciesTest {

        @Test
        @DisplayName("null 정책 목록이면 할인 없는 결과를 반환한다")
        void calculate_NullPolicies_ReturnsNoDiscount() {
            DiscountedPrice result = sut.calculate(Money.of(100000), Money.of(100000), null);

            assertThat(result.salePrice()).isEqualTo(Money.of(100000));
            assertThat(result.totalDiscountRate()).isZero();
            assertThat(result.hasDiscount()).isFalse();
        }

        @Test
        @DisplayName("빈 정책 목록이면 할인 없는 결과를 반환한다")
        void calculate_EmptyPolicies_ReturnsNoDiscount() {
            DiscountedPrice result =
                    sut.calculate(Money.of(100000), Money.of(100000), Collections.emptyList());

            assertThat(result.salePrice()).isEqualTo(Money.of(100000));
            assertThat(result.hasDiscount()).isFalse();
        }
    }

    @Nested
    @DisplayName("단일 정책 적용")
    class SinglePolicyTest {

        @Test
        @DisplayName("단일 RATE 정책을 적용한다 (10% of 100000 = 10000)")
        void calculate_SingleRatePolicy_AppliesDiscount() {
            DiscountPolicy policy =
                    DiscountFixtures.activePolicyWithPriority(
                            1L, 50, StackingGroup.PLATFORM_INSTANT);

            DiscountedPrice result =
                    sut.calculate(Money.of(100000), Money.of(100000), List.of(policy));

            assertThat(result.salePrice()).isEqualTo(Money.of(90000));
            assertThat(result.totalDiscountRate()).isEqualTo(10);
            assertThat(result.appliedDiscounts()).hasSize(1);
            assertThat(result.appliedDiscounts().get(0).amount()).isEqualTo(Money.of(10000));
        }

        @Test
        @DisplayName("단일 FIXED 정책을 적용한다")
        void calculate_SingleFixedPolicy_AppliesDiscount() {
            DiscountPolicy policy =
                    DiscountFixtures.fixedAmountPolicy(1L, 5000, StackingGroup.SELLER_INSTANT);

            DiscountedPrice result =
                    sut.calculate(Money.of(100000), Money.of(100000), List.of(policy));

            assertThat(result.salePrice()).isEqualTo(Money.of(95000));
            assertThat(result.totalDiscountRate()).isEqualTo(5);
            assertThat(result.totalDiscountAmount()).isEqualTo(Money.of(5000));
        }
    }

    @Nested
    @DisplayName("같은 그룹 내 배타적 적용 (우선순위)")
    class SameGroupExclusiveTest {

        @Test
        @DisplayName("같은 그룹에서 우선순위가 높은 정책만 적용된다")
        void calculate_SameGroup_OnlyHighestPriorityApplied() {
            DiscountPolicy highPriority =
                    DiscountFixtures.activePolicyWithPriority(
                            1L, 80, StackingGroup.PLATFORM_INSTANT);
            DiscountPolicy lowPriority =
                    DiscountFixtures.activePolicyWithPriority(
                            2L, 20, StackingGroup.PLATFORM_INSTANT);

            DiscountedPrice result =
                    sut.calculate(
                            Money.of(100000), Money.of(100000), List.of(lowPriority, highPriority));

            assertThat(result.appliedDiscounts()).hasSize(1);
            assertThat(result.appliedDiscounts().get(0).discountPolicyId().value()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("다른 그룹 간 순차 적용 (스태킹)")
    class CrossGroupStackingTest {

        @Test
        @DisplayName("SELLER_INSTANT → PLATFORM_INSTANT 순서로 순차 적용한다")
        void calculate_TwoGroups_StacksSequentially() {
            DiscountPolicy sellerPolicy =
                    DiscountFixtures.fixedAmountPolicy(1L, 5000, StackingGroup.SELLER_INSTANT);
            DiscountPolicy platformPolicy =
                    DiscountFixtures.fixedAmountPolicy(2L, 3000, StackingGroup.PLATFORM_INSTANT);

            DiscountedPrice result =
                    sut.calculate(
                            Money.of(100000),
                            Money.of(100000),
                            List.of(platformPolicy, sellerPolicy));

            assertThat(result.salePrice()).isEqualTo(Money.of(92000));
            assertThat(result.appliedDiscounts()).hasSize(2);
            assertThat(result.totalDiscountAmount()).isEqualTo(Money.of(8000));
        }

        @Test
        @DisplayName("3개 그룹 모두 순차 적용한다 (SELLER → PLATFORM → COUPON)")
        void calculate_ThreeGroups_StacksAllSequentially() {
            DiscountPolicy sellerPolicy =
                    DiscountFixtures.fixedAmountPolicy(1L, 5000, StackingGroup.SELLER_INSTANT);
            DiscountPolicy platformPolicy =
                    DiscountFixtures.fixedAmountPolicy(2L, 3000, StackingGroup.PLATFORM_INSTANT);
            DiscountPolicy couponPolicy =
                    DiscountFixtures.fixedAmountPolicy(3L, 2000, StackingGroup.COUPON);

            DiscountedPrice result =
                    sut.calculate(
                            Money.of(100000),
                            Money.of(100000),
                            List.of(couponPolicy, platformPolicy, sellerPolicy));

            // 100000 - 5000 = 95000 - 3000 = 92000 - 2000 = 90000
            assertThat(result.salePrice()).isEqualTo(Money.of(90000));
            assertThat(result.appliedDiscounts()).hasSize(3);
            assertThat(result.totalDiscountRate()).isEqualTo(10);
        }
    }

    @Nested
    @DisplayName("할인 한도 및 음수 가격 방지")
    class LimitsTest {

        @Test
        @DisplayName("할인이 현재가를 초과하지 않는다")
        void calculate_DiscountExceedsCurrentPrice_CappsAtCurrentPrice() {
            DiscountPolicy bigDiscount =
                    DiscountFixtures.fixedAmountPolicy(1L, 200000, StackingGroup.SELLER_INSTANT);

            DiscountedPrice result =
                    sut.calculate(Money.of(100000), Money.of(100000), List.of(bigDiscount));

            assertThat(result.salePrice()).isEqualTo(Money.zero());
            assertThat(result.totalDiscountAmount()).isEqualTo(Money.of(100000));
        }

        @Test
        @DisplayName("순차 적용 시 남은 가격을 기준으로 계산한다")
        void calculate_SequentialApplication_UsesRunningPrice() {
            // 100000 → SELLER: -80000 = 20000 → PLATFORM: -3000 = 17000
            DiscountPolicy sellerPolicy =
                    DiscountFixtures.fixedAmountPolicy(1L, 80000, StackingGroup.SELLER_INSTANT);
            DiscountPolicy platformPolicy =
                    DiscountFixtures.fixedAmountPolicy(2L, 3000, StackingGroup.PLATFORM_INSTANT);

            DiscountedPrice result =
                    sut.calculate(
                            Money.of(100000),
                            Money.of(100000),
                            List.of(sellerPolicy, platformPolicy));

            assertThat(result.salePrice()).isEqualTo(Money.of(17000));
        }
    }

    @Nested
    @DisplayName("shareRatio 계산")
    class ShareRatioTest {

        @Test
        @DisplayName("단일 정책의 shareRatio는 1.0이다")
        void calculate_SinglePolicy_ShareRatioIsOne() {
            DiscountPolicy policy =
                    DiscountFixtures.fixedAmountPolicy(1L, 5000, StackingGroup.SELLER_INSTANT);

            DiscountedPrice result =
                    sut.calculate(Money.of(100000), Money.of(100000), List.of(policy));

            assertThat(result.appliedDiscounts().get(0).shareRatio()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("복수 정책의 shareRatio 합은 1.0이다")
        void calculate_MultiplePolicies_ShareRatiosSumToOne() {
            DiscountPolicy sellerPolicy =
                    DiscountFixtures.fixedAmountPolicy(1L, 5000, StackingGroup.SELLER_INSTANT);
            DiscountPolicy platformPolicy =
                    DiscountFixtures.fixedAmountPolicy(2L, 5000, StackingGroup.PLATFORM_INSTANT);

            DiscountedPrice result =
                    sut.calculate(
                            Money.of(100000),
                            Money.of(100000),
                            List.of(sellerPolicy, platformPolicy));

            double totalRatio =
                    result.appliedDiscounts().stream().mapToDouble(d -> d.shareRatio()).sum();
            assertThat(totalRatio).isCloseTo(1.0, org.assertj.core.data.Offset.offset(0.01));
        }
    }

    @Nested
    @DisplayName("regularPrice와 totalDiscountRate 관계")
    class DiscountRateCalculationTest {

        @Test
        @DisplayName("정가 대비 할인율을 계산한다")
        void calculate_FixedDiscount_CalculatesRateFromRegularPrice() {
            DiscountPolicy policy =
                    DiscountFixtures.fixedAmountPolicy(1L, 20000, StackingGroup.SELLER_INSTANT);

            DiscountedPrice result =
                    sut.calculate(Money.of(100000), Money.of(100000), List.of(policy));

            assertThat(result.totalDiscountRate()).isEqualTo(20);
        }

        @Test
        @DisplayName("currentPrice와 regularPrice가 다를 때 정가 기준으로 할인율을 계산한다")
        void calculate_DifferentPrices_DiscountRateBasedOnRegularPrice() {
            DiscountPolicy policy =
                    DiscountFixtures.fixedAmountPolicy(1L, 5000, StackingGroup.PLATFORM_INSTANT);

            // regularPrice 100000, currentPrice 80000 → 5000 할인 → salePrice=75000
            // totalDiscountRate = (100000 - 75000) / 100000 * 100 = 25%
            DiscountedPrice result =
                    sut.calculate(Money.of(100000), Money.of(80000), List.of(policy));

            assertThat(result.salePrice()).isEqualTo(Money.of(75000));
            assertThat(result.totalDiscountRate()).isEqualTo(25);
        }
    }
}
