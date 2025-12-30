package com.ryuqq.setof.domain.discount.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.discount.vo.CostShare;
import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * DiscountPolicy Aggregate 테스트
 *
 * <p>할인 정책 관리에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("DiscountPolicy Aggregate")
class DiscountPolicyTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 1L;

    @Nested
    @DisplayName("forNewRateDiscount() - 신규 정률 할인 정책 생성")
    class ForNewRateDiscount {

        @Test
        @DisplayName("신규 정률 할인 정책을 생성할 수 있다")
        void shouldCreateNewRateDiscountPolicy() {
            // given
            PolicyName policyName = PolicyName.of("신규 회원 10% 할인");
            DiscountRate discountRate = DiscountRate.of(10);
            ValidPeriod validPeriod =
                    ValidPeriod.of(FIXED_TIME, FIXED_TIME.plus(30, ChronoUnit.DAYS));

            // when
            DiscountPolicy policy =
                    DiscountPolicy.forNewRateDiscount(
                            TEST_SELLER_ID,
                            policyName,
                            DiscountGroup.MEMBER,
                            DiscountTargetType.ALL,
                            null,
                            discountRate,
                            MaximumDiscountAmount.of(10000L),
                            MinimumOrderAmount.of(30000L),
                            validPeriod,
                            UsageLimit.perCustomer(1),
                            CostShare.platformOnly(),
                            Priority.defaultPriority(),
                            FIXED_TIME);

            // then
            assertNotNull(policy);
            assertNull(policy.getId());
            assertEquals(TEST_SELLER_ID, policy.getSellerId());
            assertEquals("신규 회원 10% 할인", policy.getPolicyNameValue());
            assertEquals(DiscountGroup.MEMBER, policy.getDiscountGroup());
            assertEquals(DiscountType.RATE, policy.getDiscountType());
            assertTrue(policy.isRateDiscount());
            assertFalse(policy.isFixedDiscount());
            assertTrue(policy.isActive());
            assertFalse(policy.isDeleted());
        }

        @Test
        @DisplayName("정률 할인 시 최대 할인 금액 상한이 적용된다")
        void shouldApplyMaximumDiscountAmountForRateDiscount() {
            // given
            DiscountPolicy policy = createRateDiscountPolicy(10, 5000L); // 10%, 최대 5,000원
            long orderAmount = 100000L; // 10만원 주문 (10% → 10,000원)

            // when
            long discountAmount = policy.calculateDiscountAmount(orderAmount);

            // then
            assertEquals(5000L, discountAmount); // 최대 5,000원 상한 적용
        }
    }

    @Nested
    @DisplayName("forNewFixedDiscount() - 신규 정액 할인 정책 생성")
    class ForNewFixedDiscount {

        @Test
        @DisplayName("신규 정액 할인 정책을 생성할 수 있다")
        void shouldCreateNewFixedDiscountPolicy() {
            // given
            PolicyName policyName = PolicyName.of("3,000원 할인");
            DiscountAmount discountAmount = DiscountAmount.of(3000L);
            ValidPeriod validPeriod =
                    ValidPeriod.of(FIXED_TIME, FIXED_TIME.plus(30, ChronoUnit.DAYS));

            // when
            DiscountPolicy policy =
                    DiscountPolicy.forNewFixedDiscount(
                            TEST_SELLER_ID,
                            policyName,
                            DiscountGroup.PRODUCT,
                            DiscountTargetType.ALL,
                            null,
                            discountAmount,
                            MinimumOrderAmount.noMinimum(),
                            validPeriod,
                            UsageLimit.unlimited(),
                            CostShare.sellerOnly(),
                            Priority.defaultPriority(),
                            FIXED_TIME);

            // then
            assertNotNull(policy);
            assertEquals("3,000원 할인", policy.getPolicyNameValue());
            assertEquals(DiscountGroup.PRODUCT, policy.getDiscountGroup());
            assertEquals(DiscountType.FIXED_PRICE, policy.getDiscountType());
            assertFalse(policy.isRateDiscount());
            assertTrue(policy.isFixedDiscount());
        }

        @Test
        @DisplayName("정액 할인이 주문 금액을 초과하지 않는다")
        void shouldNotExceedOrderAmountForFixedDiscount() {
            // given
            DiscountPolicy policy = createFixedDiscountPolicy(5000L); // 5,000원 할인
            long orderAmount = 3000L; // 3,000원 주문

            // when
            long discountAmount = policy.calculateDiscountAmount(orderAmount);

            // then
            assertEquals(3000L, discountAmount); // 주문 금액만큼만 할인
        }
    }

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("Persistence에서 모든 필드를 복원할 수 있다")
        void shouldReconstitutePolicyFromPersistence() {
            // given
            DiscountPolicyId id = DiscountPolicyId.of(1L);
            Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2024-06-01T00:00:00Z");

            // when
            DiscountPolicy policy =
                    DiscountPolicy.reconstitute(
                            id,
                            TEST_SELLER_ID,
                            PolicyName.of("복원된 정책"),
                            DiscountGroup.PRODUCT,
                            DiscountType.RATE,
                            DiscountTargetType.ALL,
                            null,
                            DiscountRate.of(10),
                            null,
                            MaximumDiscountAmount.unlimited(),
                            MinimumOrderAmount.noMinimum(),
                            ValidPeriod.of(FIXED_TIME, FIXED_TIME.plus(365, ChronoUnit.DAYS)),
                            UsageLimit.unlimited(),
                            CostShare.platformOnly(),
                            Priority.defaultPriority(),
                            true,
                            createdAt,
                            updatedAt,
                            null);

            // then
            assertEquals(1L, policy.getIdValue());
            assertEquals("복원된 정책", policy.getPolicyNameValue());
            assertTrue(policy.isActive());
            assertFalse(policy.isDeleted());
            assertEquals(createdAt, policy.getCreatedAt());
            assertEquals(updatedAt, policy.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("calculateDiscountAmount() - 할인 금액 계산")
    class CalculateDiscountAmount {

        @Test
        @DisplayName("정률 할인을 정확히 계산한다")
        void shouldCalculateRateDiscountCorrectly() {
            // given
            DiscountPolicy policy = createRateDiscountPolicy(15, null); // 15% 할인
            long orderAmount = 50000L;

            // when
            long discountAmount = policy.calculateDiscountAmount(orderAmount);

            // then
            assertEquals(7500L, discountAmount); // 50,000 * 15% = 7,500
        }

        @Test
        @DisplayName("정액 할인을 정확히 계산한다")
        void shouldCalculateFixedDiscountCorrectly() {
            // given
            DiscountPolicy policy = createFixedDiscountPolicy(3000L);
            long orderAmount = 50000L;

            // when
            long discountAmount = policy.calculateDiscountAmount(orderAmount);

            // then
            assertEquals(3000L, discountAmount);
        }

        @Test
        @DisplayName("최소 주문 금액 미달 시 0을 반환한다")
        void shouldReturnZeroWhenMinimumOrderNotMet() {
            // given
            DiscountPolicy policy = createPolicyWithMinimumOrder(30000L);
            long orderAmount = 20000L; // 최소 주문 금액 미달

            // when
            long discountAmount = policy.calculateDiscountAmount(orderAmount);

            // then
            assertEquals(0L, discountAmount);
        }
    }

    @Nested
    @DisplayName("canApply() - 할인 적용 가능 여부")
    class CanApply {

        @Test
        @DisplayName("활성화되고 유효 기간 내이고 최소 주문 금액을 충족하면 적용 가능하다")
        void shouldReturnTrueWhenAllConditionsMet() {
            // given
            DiscountPolicy policy = createActiveValidPolicy();
            long orderAmount = 50000L;

            // when
            boolean canApply = policy.canApply(orderAmount);

            // then
            assertTrue(canApply);
        }

        @Test
        @DisplayName("비활성화된 정책은 적용할 수 없다")
        void shouldReturnFalseWhenInactive() {
            // given
            DiscountPolicy policy = createActiveValidPolicy().deactivate(FIXED_TIME);

            // when
            boolean canApply = policy.canApply(50000L);

            // then
            assertFalse(canApply);
        }

        @Test
        @DisplayName("삭제된 정책은 적용할 수 없다")
        void shouldReturnFalseWhenDeleted() {
            // given
            DiscountPolicy policy = createActiveValidPolicy().delete(FIXED_TIME);

            // when
            boolean canApply = policy.canApply(50000L);

            // then
            assertFalse(canApply);
        }
    }

    @Nested
    @DisplayName("canUse() - 사용 횟수 제한 검증")
    class CanUse {

        @Test
        @DisplayName("사용 횟수 제한 내이면 사용 가능하다")
        void shouldReturnTrueWhenWithinUsageLimit() {
            // given
            DiscountPolicy policy = createPolicyWithUsageLimit(3, 100);

            // when
            boolean canUse = policy.canUse(2, 50); // 고객: 2/3, 전체: 50/100

            // then
            assertTrue(canUse);
        }

        @Test
        @DisplayName("고객별 사용 횟수 초과 시 사용 불가하다")
        void shouldReturnFalseWhenCustomerUsageExceeded() {
            // given
            DiscountPolicy policy = createPolicyWithUsageLimit(3, 100);

            // when
            boolean canUse = policy.canUse(3, 50); // 고객: 3/3 (한도 도달)

            // then
            assertFalse(canUse);
        }

        @Test
        @DisplayName("전체 사용 횟수 초과 시 사용 불가하다")
        void shouldReturnFalseWhenTotalUsageExceeded() {
            // given
            DiscountPolicy policy = createPolicyWithUsageLimit(3, 100);

            // when
            boolean canUse = policy.canUse(0, 100); // 전체: 100/100 (한도 도달)

            // then
            assertFalse(canUse);
        }
    }

    @Nested
    @DisplayName("isApplicableToTarget() - 적용 대상 확인")
    class IsApplicableToTarget {

        @Test
        @DisplayName("전체 대상 정책은 모든 ID에 적용 가능하다")
        void shouldReturnTrueForAllTargetType() {
            // given
            DiscountPolicy policy = createActiveValidPolicy(); // ALL 타입

            // when & then
            assertTrue(policy.isApplicableToTarget(1L));
            assertTrue(policy.isApplicableToTarget(999L));
        }

        @Test
        @DisplayName("특정 대상 정책은 포함된 ID에만 적용 가능하다")
        void shouldReturnTrueOnlyForIncludedTargetIds() {
            // given
            DiscountPolicy policy = createPolicyWithTargetIds(List.of(1L, 2L, 3L));

            // when & then
            assertTrue(policy.isApplicableToTarget(1L));
            assertTrue(policy.isApplicableToTarget(2L));
            assertFalse(policy.isApplicableToTarget(999L));
        }
    }

    @Nested
    @DisplayName("activate() / deactivate() - 활성화/비활성화")
    class ActivateDeactivate {

        @Test
        @DisplayName("비활성화된 정책을 활성화할 수 있다")
        void shouldActivatePolicy() {
            // given
            DiscountPolicy policy = createActiveValidPolicy().deactivate(FIXED_TIME);
            assertFalse(policy.isActive());

            // when
            DiscountPolicy activatedPolicy = policy.activate(FIXED_TIME);

            // then
            assertTrue(activatedPolicy.isActive());
        }

        @Test
        @DisplayName("활성화된 정책을 비활성화할 수 있다")
        void shouldDeactivatePolicy() {
            // given
            DiscountPolicy policy = createActiveValidPolicy();
            assertTrue(policy.isActive());

            // when
            DiscountPolicy deactivatedPolicy = policy.deactivate(FIXED_TIME);

            // then
            assertFalse(deactivatedPolicy.isActive());
        }
    }

    @Nested
    @DisplayName("delete() - 할인 정책 삭제")
    class Delete {

        @Test
        @DisplayName("할인 정책을 소프트 삭제할 수 있다")
        void shouldSoftDeletePolicy() {
            // given
            DiscountPolicy policy = createActiveValidPolicy();
            assertFalse(policy.isDeleted());
            assertTrue(policy.isActive());

            // when
            DiscountPolicy deletedPolicy = policy.delete(FIXED_TIME);

            // then
            assertTrue(deletedPolicy.isDeleted());
            assertFalse(deletedPolicy.isActive()); // 삭제 시 비활성화
            assertNotNull(deletedPolicy.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("changePriority() - 우선순위 변경")
    class ChangePriority {

        @Test
        @DisplayName("우선순위를 변경할 수 있다")
        void shouldChangePriority() {
            // given
            DiscountPolicy policy = createActiveValidPolicy();
            assertEquals(100, policy.getPriorityValue());

            // when
            DiscountPolicy updatedPolicy = policy.changePriority(Priority.of(50), FIXED_TIME);

            // then
            assertEquals(50, updatedPolicy.getPriorityValue());
        }
    }

    @Nested
    @DisplayName("CostShare 계산 - 비용 분담")
    class CostShareCalculation {

        @Test
        @DisplayName("플랫폼 부담 금액을 계산할 수 있다")
        void shouldCalculatePlatformCost() {
            // given
            DiscountPolicy policy = createPolicyWithCostShare(70, 30); // 플랫폼 70%, 셀러 30%
            long discountAmount = 10000L;

            // when
            long platformCost = policy.calculatePlatformCost(discountAmount);
            long sellerCost = policy.calculateSellerCost(discountAmount);

            // then
            assertEquals(7000L, platformCost);
            assertEquals(3000L, sellerCost);
        }
    }

    @Nested
    @DisplayName("changePolicyName() - 정책명 변경")
    class ChangePolicyName {

        @Test
        @DisplayName("정책명을 변경할 수 있다")
        void shouldChangePolicyName() {
            // given
            DiscountPolicy policy = createActiveValidPolicy();
            PolicyName newName = PolicyName.of("변경된 정책명");

            // when
            DiscountPolicy updatedPolicy = policy.changePolicyName(newName, FIXED_TIME);

            // then
            assertEquals("변경된 정책명", updatedPolicy.getPolicyNameValue());
            assertEquals(FIXED_TIME, updatedPolicy.getUpdatedAt());
        }

        @Test
        @DisplayName("정책명 변경은 새로운 인스턴스를 반환한다 (불변성)")
        void shouldReturnNewInstanceWhenChangingPolicyName() {
            // given
            DiscountPolicy policy = createActiveValidPolicy();
            String originalName = policy.getPolicyNameValue();
            PolicyName newName = PolicyName.of("새 정책명");

            // when
            DiscountPolicy updatedPolicy = policy.changePolicyName(newName, FIXED_TIME);

            // then
            assertEquals(originalName, policy.getPolicyNameValue());
            assertEquals("새 정책명", updatedPolicy.getPolicyNameValue());
        }
    }

    @Nested
    @DisplayName("changeMaximumDiscountAmount() - 최대 할인 금액 변경")
    class ChangeMaximumDiscountAmount {

        @Test
        @DisplayName("최대 할인 금액을 변경할 수 있다")
        void shouldChangeMaximumDiscountAmount() {
            // given
            DiscountPolicy policy = createRateDiscountPolicy(10, 5000L);
            MaximumDiscountAmount newMaxAmount = MaximumDiscountAmount.of(10000L);

            // when
            DiscountPolicy updatedPolicy =
                    policy.changeMaximumDiscountAmount(newMaxAmount, FIXED_TIME);

            // then
            assertEquals(10000L, updatedPolicy.getMaximumDiscountAmountValue());
            assertEquals(FIXED_TIME, updatedPolicy.getUpdatedAt());
        }

        @Test
        @DisplayName("최대 할인 금액 변경은 새로운 인스턴스를 반환한다 (불변성)")
        void shouldReturnNewInstanceWhenChangingMaximumDiscountAmount() {
            // given
            DiscountPolicy policy = createRateDiscountPolicy(10, 5000L);
            MaximumDiscountAmount newMaxAmount = MaximumDiscountAmount.of(10000L);

            // when
            DiscountPolicy updatedPolicy =
                    policy.changeMaximumDiscountAmount(newMaxAmount, FIXED_TIME);

            // then
            assertEquals(5000L, policy.getMaximumDiscountAmountValue());
            assertEquals(10000L, updatedPolicy.getMaximumDiscountAmountValue());
        }
    }

    @Nested
    @DisplayName("changeMinimumOrderAmount() - 최소 주문 금액 변경")
    class ChangeMinimumOrderAmount {

        @Test
        @DisplayName("최소 주문 금액을 변경할 수 있다")
        void shouldChangeMinimumOrderAmount() {
            // given
            DiscountPolicy policy = createPolicyWithMinimumOrder(30000L);
            MinimumOrderAmount newMinAmount = MinimumOrderAmount.of(50000L);

            // when
            DiscountPolicy updatedPolicy =
                    policy.changeMinimumOrderAmount(newMinAmount, FIXED_TIME);

            // then
            assertEquals(50000L, updatedPolicy.getMinimumOrderAmountValue());
            assertEquals(FIXED_TIME, updatedPolicy.getUpdatedAt());
        }

        @Test
        @DisplayName("최소 주문 금액 변경은 새로운 인스턴스를 반환한다 (불변성)")
        void shouldReturnNewInstanceWhenChangingMinimumOrderAmount() {
            // given
            DiscountPolicy policy = createPolicyWithMinimumOrder(30000L);
            MinimumOrderAmount newMinAmount = MinimumOrderAmount.of(50000L);

            // when
            DiscountPolicy updatedPolicy =
                    policy.changeMinimumOrderAmount(newMinAmount, FIXED_TIME);

            // then
            assertEquals(30000L, policy.getMinimumOrderAmountValue());
            assertEquals(50000L, updatedPolicy.getMinimumOrderAmountValue());
        }
    }

    @Nested
    @DisplayName("changeUsageLimit() - 사용 횟수 제한 변경")
    class ChangeUsageLimit {

        @Test
        @DisplayName("사용 횟수 제한을 변경할 수 있다")
        void shouldChangeUsageLimit() {
            // given
            DiscountPolicy policy = createPolicyWithUsageLimit(3, 100);
            UsageLimit newUsageLimit = UsageLimit.of(5, 200);

            // when
            DiscountPolicy updatedPolicy = policy.changeUsageLimit(newUsageLimit, FIXED_TIME);

            // then
            assertEquals(5, updatedPolicy.getMaxUsagePerCustomer());
            assertEquals(200, updatedPolicy.getMaxTotalUsage());
            assertEquals(FIXED_TIME, updatedPolicy.getUpdatedAt());
        }

        @Test
        @DisplayName("사용 횟수 제한 변경은 새로운 인스턴스를 반환한다 (불변성)")
        void shouldReturnNewInstanceWhenChangingUsageLimit() {
            // given
            DiscountPolicy policy = createPolicyWithUsageLimit(3, 100);
            UsageLimit newUsageLimit = UsageLimit.of(5, 200);

            // when
            DiscountPolicy updatedPolicy = policy.changeUsageLimit(newUsageLimit, FIXED_TIME);

            // then
            assertEquals(3, policy.getMaxUsagePerCustomer());
            assertEquals(100, policy.getMaxTotalUsage());
            assertEquals(5, updatedPolicy.getMaxUsagePerCustomer());
            assertEquals(200, updatedPolicy.getMaxTotalUsage());
        }
    }

    @Nested
    @DisplayName("changeCostShare() - 비용 분담 비율 변경")
    class ChangeCostShare {

        @Test
        @DisplayName("비용 분담 비율을 변경할 수 있다")
        void shouldChangeCostShare() {
            // given
            DiscountPolicy policy = createPolicyWithCostShare(70, 30);
            CostShare newCostShare = CostShare.of(BigDecimal.valueOf(50), BigDecimal.valueOf(50));

            // when
            DiscountPolicy updatedPolicy = policy.changeCostShare(newCostShare, FIXED_TIME);

            // then
            assertEquals(
                    0, BigDecimal.valueOf(50).compareTo(updatedPolicy.getPlatformCostShareRatio()));
            assertEquals(
                    0, BigDecimal.valueOf(50).compareTo(updatedPolicy.getSellerCostShareRatio()));
            assertEquals(FIXED_TIME, updatedPolicy.getUpdatedAt());
        }

        @Test
        @DisplayName("비용 분담 비율 변경은 새로운 인스턴스를 반환한다 (불변성)")
        void shouldReturnNewInstanceWhenChangingCostShare() {
            // given
            DiscountPolicy policy = createPolicyWithCostShare(70, 30);
            CostShare newCostShare = CostShare.of(BigDecimal.valueOf(50), BigDecimal.valueOf(50));

            // when
            DiscountPolicy updatedPolicy = policy.changeCostShare(newCostShare, FIXED_TIME);

            // then
            assertEquals(0, BigDecimal.valueOf(70).compareTo(policy.getPlatformCostShareRatio()));
            assertEquals(
                    0, BigDecimal.valueOf(50).compareTo(updatedPolicy.getPlatformCostShareRatio()));
        }
    }

    @Nested
    @DisplayName("extendValidPeriod(Instant) - 유효 기간 연장 (Instant 오버로드)")
    class ExtendValidPeriodWithInstant {

        @Test
        @DisplayName("유효 기간 종료일을 연장할 수 있다")
        void shouldExtendValidPeriodEndDate() {
            // given
            DiscountPolicy policy = createActiveValidPolicy();
            Instant originalEndAt = policy.getValidEndAt();
            Instant newEndAt = originalEndAt.plus(30, ChronoUnit.DAYS);

            // when
            DiscountPolicy updatedPolicy = policy.extendValidPeriod(newEndAt, FIXED_TIME);

            // then
            assertEquals(newEndAt, updatedPolicy.getValidEndAt());
            assertEquals(policy.getValidStartAt(), updatedPolicy.getValidStartAt());
            assertEquals(FIXED_TIME, updatedPolicy.getUpdatedAt());
        }

        @Test
        @DisplayName("유효 기간 연장은 새로운 인스턴스를 반환한다 (불변성)")
        void shouldReturnNewInstanceWhenExtendingValidPeriod() {
            // given
            DiscountPolicy policy = createActiveValidPolicy();
            Instant originalEndAt = policy.getValidEndAt();
            Instant newEndAt = originalEndAt.plus(30, ChronoUnit.DAYS);

            // when
            DiscountPolicy updatedPolicy = policy.extendValidPeriod(newEndAt, FIXED_TIME);

            // then
            assertEquals(originalEndAt, policy.getValidEndAt());
            assertEquals(newEndAt, updatedPolicy.getValidEndAt());
        }
    }

    @Nested
    @DisplayName("Law of Demeter Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("getIdValue()는 ID가 없으면 null을 반환한다")
        void shouldReturnNullWhenIdIsNull() {
            // given
            DiscountPolicy policy =
                    DiscountPolicy.forNewRateDiscount(
                            TEST_SELLER_ID,
                            PolicyName.of("테스트"),
                            DiscountGroup.PRODUCT,
                            DiscountTargetType.ALL,
                            null,
                            DiscountRate.of(10),
                            MaximumDiscountAmount.unlimited(),
                            MinimumOrderAmount.noMinimum(),
                            ValidPeriod.of(FIXED_TIME, FIXED_TIME.plus(30, ChronoUnit.DAYS)),
                            UsageLimit.unlimited(),
                            CostShare.platformOnly(),
                            Priority.defaultPriority(),
                            FIXED_TIME);

            // then
            assertNull(policy.getIdValue());
            assertFalse(policy.hasId());
        }

        @Test
        @DisplayName("getValidStartAt()은 유효 기간 시작일을 반환한다")
        void shouldReturnValidStartAt() {
            // given
            Instant startAt = FIXED_TIME;
            Instant endAt = FIXED_TIME.plus(30, ChronoUnit.DAYS);
            DiscountPolicy policy =
                    DiscountPolicy.forNewRateDiscount(
                            TEST_SELLER_ID,
                            PolicyName.of("테스트"),
                            DiscountGroup.PRODUCT,
                            DiscountTargetType.ALL,
                            null,
                            DiscountRate.of(10),
                            MaximumDiscountAmount.unlimited(),
                            MinimumOrderAmount.noMinimum(),
                            ValidPeriod.of(startAt, endAt),
                            UsageLimit.unlimited(),
                            CostShare.platformOnly(),
                            Priority.defaultPriority(),
                            FIXED_TIME);

            // then
            assertEquals(startAt, policy.getValidStartAt());
        }

        @Test
        @DisplayName("getValidEndAt()은 유효 기간 종료일을 반환한다")
        void shouldReturnValidEndAt() {
            // given
            Instant startAt = FIXED_TIME;
            Instant endAt = FIXED_TIME.plus(30, ChronoUnit.DAYS);
            DiscountPolicy policy =
                    DiscountPolicy.forNewRateDiscount(
                            TEST_SELLER_ID,
                            PolicyName.of("테스트"),
                            DiscountGroup.PRODUCT,
                            DiscountTargetType.ALL,
                            null,
                            DiscountRate.of(10),
                            MaximumDiscountAmount.unlimited(),
                            MinimumOrderAmount.noMinimum(),
                            ValidPeriod.of(startAt, endAt),
                            UsageLimit.unlimited(),
                            CostShare.platformOnly(),
                            Priority.defaultPriority(),
                            FIXED_TIME);

            // then
            assertEquals(endAt, policy.getValidEndAt());
        }

        @Test
        @DisplayName("getMaxUsagePerCustomer()는 고객별 최대 사용 횟수를 반환한다")
        void shouldReturnMaxUsagePerCustomer() {
            // given
            DiscountPolicy policy = createPolicyWithUsageLimit(5, 100);

            // then
            assertEquals(5, policy.getMaxUsagePerCustomer());
        }

        @Test
        @DisplayName("getMaxTotalUsage()는 전체 최대 사용 횟수를 반환한다")
        void shouldReturnMaxTotalUsage() {
            // given
            DiscountPolicy policy = createPolicyWithUsageLimit(5, 100);

            // then
            assertEquals(100, policy.getMaxTotalUsage());
        }

        @Test
        @DisplayName("getTargetId()는 첫 번째 대상 ID를 반환한다")
        void shouldReturnFirstTargetId() {
            // given
            DiscountPolicy policy = createPolicyWithTargetIds(List.of(1L, 2L, 3L));

            // then
            assertEquals(1L, policy.getTargetId());
        }

        @Test
        @DisplayName("getTargetId()는 대상이 없으면 null을 반환한다")
        void shouldReturnNullWhenNoTargetIds() {
            // given
            DiscountPolicy policy = createActiveValidPolicy(); // ALL 타입, 대상 없음

            // then
            assertNull(policy.getTargetId());
        }
    }

    // ========== Helper Methods ==========

    private DiscountPolicy createRateDiscountPolicy(int rate, Long maxAmount) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("정률 할인"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                null,
                DiscountRate.of(rate),
                null,
                maxAmount != null
                        ? MaximumDiscountAmount.of(maxAmount)
                        : MaximumDiscountAmount.unlimited(),
                MinimumOrderAmount.noMinimum(),
                ValidPeriod.of(FIXED_TIME, FIXED_TIME.plus(365, ChronoUnit.DAYS)),
                UsageLimit.unlimited(),
                CostShare.platformOnly(),
                Priority.defaultPriority(),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createFixedDiscountPolicy(long amount) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("정액 할인"),
                DiscountGroup.PRODUCT,
                DiscountType.FIXED_PRICE,
                DiscountTargetType.ALL,
                null,
                null,
                DiscountAmount.of(amount),
                MaximumDiscountAmount.unlimited(),
                MinimumOrderAmount.noMinimum(),
                ValidPeriod.of(FIXED_TIME, FIXED_TIME.plus(365, ChronoUnit.DAYS)),
                UsageLimit.unlimited(),
                CostShare.platformOnly(),
                Priority.defaultPriority(),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createPolicyWithMinimumOrder(long minimumAmount) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("최소 주문 정책"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                null,
                DiscountRate.of(10),
                null,
                MaximumDiscountAmount.unlimited(),
                MinimumOrderAmount.of(minimumAmount),
                ValidPeriod.of(FIXED_TIME, FIXED_TIME.plus(365, ChronoUnit.DAYS)),
                UsageLimit.unlimited(),
                CostShare.platformOnly(),
                Priority.defaultPriority(),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createActiveValidPolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("활성 정책"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                null,
                DiscountRate.of(10),
                null,
                MaximumDiscountAmount.unlimited(),
                MinimumOrderAmount.noMinimum(),
                ValidPeriod.of(
                        Instant.now().minus(1, ChronoUnit.DAYS),
                        Instant.now().plus(365, ChronoUnit.DAYS)),
                UsageLimit.unlimited(),
                CostShare.platformOnly(),
                Priority.defaultPriority(),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createPolicyWithUsageLimit(int perCustomer, int total) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("사용 제한 정책"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                null,
                DiscountRate.of(10),
                null,
                MaximumDiscountAmount.unlimited(),
                MinimumOrderAmount.noMinimum(),
                ValidPeriod.of(
                        Instant.now().minus(1, ChronoUnit.DAYS),
                        Instant.now().plus(365, ChronoUnit.DAYS)),
                UsageLimit.of(perCustomer, total, null),
                CostShare.platformOnly(),
                Priority.defaultPriority(),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createPolicyWithTargetIds(List<Long> targetIds) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("특정 대상 정책"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.PRODUCT,
                targetIds,
                DiscountRate.of(10),
                null,
                MaximumDiscountAmount.unlimited(),
                MinimumOrderAmount.noMinimum(),
                ValidPeriod.of(
                        Instant.now().minus(1, ChronoUnit.DAYS),
                        Instant.now().plus(365, ChronoUnit.DAYS)),
                UsageLimit.unlimited(),
                CostShare.platformOnly(),
                Priority.defaultPriority(),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createPolicyWithCostShare(int platformRatio, int sellerRatio) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("비용 분담 정책"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                null,
                DiscountRate.of(10),
                null,
                MaximumDiscountAmount.unlimited(),
                MinimumOrderAmount.noMinimum(),
                ValidPeriod.of(
                        Instant.now().minus(1, ChronoUnit.DAYS),
                        Instant.now().plus(365, ChronoUnit.DAYS)),
                UsageLimit.unlimited(),
                CostShare.of(BigDecimal.valueOf(platformRatio), BigDecimal.valueOf(sellerRatio)),
                Priority.defaultPriority(),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }
}
