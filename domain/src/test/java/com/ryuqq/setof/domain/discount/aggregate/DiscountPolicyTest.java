package com.ryuqq.setof.domain.discount.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.exception.InsufficientBudgetException;
import com.ryuqq.setof.domain.discount.exception.InvalidDiscountConfigException;
import com.ryuqq.setof.domain.discount.id.DiscountTargetId;
import com.ryuqq.setof.domain.discount.vo.ApplicationType;
import com.ryuqq.setof.domain.discount.vo.DiscountMethod;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyName;
import com.ryuqq.setof.domain.discount.vo.DiscountRate;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DiscountPolicy Aggregate 테스트")
class DiscountPolicyTest {

    @Nested
    @DisplayName("forNew() - 신규 할인 정책 생성")
    class ForNewTest {

        @Test
        @DisplayName("RATE 즉시할인 정책을 생성한다")
        void createRateInstantPolicy() {
            var policy = DiscountFixtures.newRateInstantPolicy();

            assertThat(policy.isNew()).isTrue();
            assertThat(policy.nameValue()).isEqualTo(DiscountFixtures.DEFAULT_POLICY_NAME);
            assertThat(policy.discountMethod()).isEqualTo(DiscountMethod.RATE);
            assertThat(policy.discountRateValue())
                    .isEqualTo(DiscountFixtures.DEFAULT_DISCOUNT_RATE);
            assertThat(policy.discountAmountValue()).isNull();
            assertThat(policy.applicationType()).isEqualTo(ApplicationType.INSTANT);
            assertThat(policy.stackingGroup()).isEqualTo(StackingGroup.PLATFORM_INSTANT);
            assertThat(policy.isActive()).isTrue();
            assertThat(policy.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("FIXED_AMOUNT 셀러 즉시할인 정책을 생성한다")
        void createFixedInstantPolicy() {
            var policy = DiscountFixtures.newFixedInstantPolicy();

            assertThat(policy.isNew()).isTrue();
            assertThat(policy.discountMethod()).isEqualTo(DiscountMethod.FIXED_AMOUNT);
            assertThat(policy.discountAmountValue())
                    .isEqualTo(DiscountFixtures.DEFAULT_FIXED_AMOUNT);
            assertThat(policy.discountRateValue()).isNull();
            assertThat(policy.stackingGroup()).isEqualTo(StackingGroup.SELLER_INSTANT);
            assertThat(policy.sellerIdValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("COUPON 타입 정책을 생성한다")
        void createCouponPolicy() {
            var policy = DiscountFixtures.newCouponPolicy();

            assertThat(policy.applicationType()).isEqualTo(ApplicationType.COUPON);
            assertThat(policy.stackingGroup()).isEqualTo(StackingGroup.COUPON);
        }

        @Test
        @DisplayName("RATE 타입에 discountRate 없으면 예외가 발생한다")
        void throwForRateWithoutRate() {
            assertThatThrownBy(
                            () ->
                                    DiscountPolicy.forNew(
                                            DiscountFixtures.defaultPolicyName(),
                                            null,
                                            DiscountMethod.RATE,
                                            null,
                                            null,
                                            null,
                                            false,
                                            null,
                                            ApplicationType.INSTANT,
                                            com.ryuqq.setof.domain.discount.vo.PublisherType.ADMIN,
                                            null,
                                            StackingGroup.PLATFORM_INSTANT,
                                            DiscountFixtures.defaultPriority(),
                                            DiscountFixtures.defaultActivePeriod(),
                                            DiscountFixtures.defaultBudget(),
                                            CommonVoFixtures.now()))
                    .isInstanceOf(InvalidDiscountConfigException.class);
        }

        @Test
        @DisplayName("RATE 타입에 discountAmount 설정하면 예외가 발생한다")
        void throwForRateWithAmount() {
            assertThatThrownBy(
                            () ->
                                    DiscountPolicy.forNew(
                                            DiscountFixtures.defaultPolicyName(),
                                            null,
                                            DiscountMethod.RATE,
                                            DiscountRate.of(10.0),
                                            Money.of(5000),
                                            null,
                                            false,
                                            null,
                                            ApplicationType.INSTANT,
                                            com.ryuqq.setof.domain.discount.vo.PublisherType.ADMIN,
                                            null,
                                            StackingGroup.PLATFORM_INSTANT,
                                            DiscountFixtures.defaultPriority(),
                                            DiscountFixtures.defaultActivePeriod(),
                                            DiscountFixtures.defaultBudget(),
                                            CommonVoFixtures.now()))
                    .isInstanceOf(InvalidDiscountConfigException.class);
        }

        @Test
        @DisplayName("FIXED_AMOUNT 타입에 discountAmount 없으면 예외가 발생한다")
        void throwForFixedWithoutAmount() {
            assertThatThrownBy(
                            () ->
                                    DiscountPolicy.forNew(
                                            DiscountFixtures.defaultPolicyName(),
                                            null,
                                            DiscountMethod.FIXED_AMOUNT,
                                            null,
                                            null,
                                            null,
                                            false,
                                            null,
                                            ApplicationType.INSTANT,
                                            com.ryuqq.setof.domain.discount.vo.PublisherType.ADMIN,
                                            null,
                                            StackingGroup.SELLER_INSTANT,
                                            DiscountFixtures.defaultPriority(),
                                            DiscountFixtures.defaultActivePeriod(),
                                            DiscountFixtures.defaultBudget(),
                                            CommonVoFixtures.now()))
                    .isInstanceOf(InvalidDiscountConfigException.class);
        }

        @Test
        @DisplayName("COUPON 타입이 COUPON이 아닌 스태킹 그룹이면 예외가 발생한다")
        void throwForCouponWithNonCouponGroup() {
            assertThatThrownBy(
                            () ->
                                    DiscountPolicy.forNew(
                                            DiscountFixtures.defaultPolicyName(),
                                            null,
                                            DiscountMethod.RATE,
                                            DiscountRate.of(10.0),
                                            null,
                                            null,
                                            false,
                                            null,
                                            ApplicationType.COUPON,
                                            com.ryuqq.setof.domain.discount.vo.PublisherType.ADMIN,
                                            null,
                                            StackingGroup.PLATFORM_INSTANT,
                                            DiscountFixtures.defaultPriority(),
                                            DiscountFixtures.defaultActivePeriod(),
                                            DiscountFixtures.defaultBudget(),
                                            CommonVoFixtures.now()))
                    .isInstanceOf(InvalidDiscountConfigException.class);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 상태의 정책을 복원한다")
        void reconstituteActivePolicy() {
            var policy = DiscountFixtures.activeRatePolicy();

            assertThat(policy.isNew()).isFalse();
            assertThat(policy.idValue()).isEqualTo(1L);
            assertThat(policy.isActive()).isTrue();
            assertThat(policy.isDeleted()).isFalse();
            assertThat(policy.deletedAt()).isNull();
        }

        @Test
        @DisplayName("삭제된 정책을 복원한다")
        void reconstituteDeletedPolicy() {
            var policy = DiscountFixtures.deletedPolicy();

            assertThat(policy.isDeleted()).isTrue();
            assertThat(policy.deletedAt()).isNotNull();
            assertThat(policy.isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("update() - 정책 수정")
    class UpdateTest {

        @Test
        @DisplayName("정책 정보를 수정한다")
        void updatePolicy() {
            var policy = DiscountFixtures.activeRatePolicy();
            var updateData = DiscountFixtures.rateUpdateData();
            Instant now = CommonVoFixtures.now();

            policy.update(updateData, now);

            assertThat(policy.nameValue()).isEqualTo("수정된정책명");
            assertThat(policy.description()).isEqualTo("수정된 설명");
            assertThat(policy.discountRateValue()).isEqualTo(15.0);
            assertThat(policy.priorityValue()).isEqualTo(70);
            assertThat(policy.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("잘못된 설정으로 수정하면 예외가 발생한다")
        void throwForInvalidUpdate() {
            var policy = DiscountFixtures.activeRatePolicy();
            var invalidData =
                    DiscountPolicyUpdateData.of(
                            DiscountPolicyName.of("수정"),
                            null,
                            DiscountMethod.RATE,
                            null,
                            null,
                            null,
                            false,
                            null,
                            DiscountFixtures.defaultPriority(),
                            DiscountFixtures.defaultActivePeriod(),
                            DiscountFixtures.defaultBudget());

            assertThatThrownBy(() -> policy.update(invalidData, CommonVoFixtures.now()))
                    .isInstanceOf(InvalidDiscountConfigException.class);
        }
    }

    @Nested
    @DisplayName("activate() / deactivate() - 활성 상태 변경")
    class ActivationTest {

        @Test
        @DisplayName("비활성 정책을 활성화한다")
        void activatePolicy() {
            var policy = DiscountFixtures.inactivePolicy();
            Instant now = CommonVoFixtures.now();

            policy.activate(now);

            assertThat(policy.isActive()).isTrue();
            assertThat(policy.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("활성 정책을 비활성화한다")
        void deactivatePolicy() {
            var policy = DiscountFixtures.activeRatePolicy();
            Instant now = CommonVoFixtures.now();

            policy.deactivate(now);

            assertThat(policy.isActive()).isFalse();
            assertThat(policy.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("delete() - 삭제 (Soft Delete)")
    class DeletionTest {

        @Test
        @DisplayName("정책을 삭제한다")
        void deletePolicy() {
            var policy = DiscountFixtures.activeRatePolicy();
            Instant now = CommonVoFixtures.now();

            policy.delete(now);

            assertThat(policy.isDeleted()).isTrue();
            assertThat(policy.deletedAt()).isEqualTo(now);
            assertThat(policy.isActive()).isFalse();
            assertThat(policy.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("Target 관리 테스트")
    class TargetManagementTest {

        @Test
        @DisplayName("할인 대상을 추가한다")
        void addTarget() {
            var policy = DiscountFixtures.activeRatePolicy();
            Instant now = CommonVoFixtures.now();

            policy.addTarget(DiscountTargetType.PRODUCT, 100L, now);

            assertThat(policy.targets()).hasSize(1);
            assertThat(policy.hasTarget(DiscountTargetType.PRODUCT, 100L)).isTrue();
            assertThat(policy.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("중복 대상 추가 시 무시한다")
        void ignoreDuplicateTarget() {
            var policy = DiscountFixtures.activeRatePolicy();
            Instant now = CommonVoFixtures.now();

            policy.addTarget(DiscountTargetType.PRODUCT, 100L, now);
            policy.addTarget(DiscountTargetType.PRODUCT, 100L, now);

            assertThat(policy.targets()).hasSize(1);
        }

        @Test
        @DisplayName("다른 대상 유형은 별도로 추가된다")
        void addDifferentTargetTypes() {
            var policy = DiscountFixtures.activeRatePolicy();
            Instant now = CommonVoFixtures.now();

            policy.addTarget(DiscountTargetType.PRODUCT, 100L, now);
            policy.addTarget(DiscountTargetType.CATEGORY, 200L, now);

            assertThat(policy.targets()).hasSize(2);
            assertThat(policy.hasTarget(DiscountTargetType.PRODUCT, 100L)).isTrue();
            assertThat(policy.hasTarget(DiscountTargetType.CATEGORY, 200L)).isTrue();
        }

        @Test
        @DisplayName("할인 대상을 제거한다")
        void removeTarget() {
            var policy = DiscountFixtures.activeRatePolicy();
            Instant now = CommonVoFixtures.now();
            policy.addTarget(DiscountTargetType.PRODUCT, 100L, now);

            DiscountTargetId targetId = policy.targets().get(0).id();
            policy.removeTarget(targetId, now);

            assertThat(policy.activeTargets()).isEmpty();
        }
    }

    @Nested
    @DisplayName("calculateDiscountAmount() - 할인 계산")
    class CalculateDiscountTest {

        @Test
        @DisplayName("RATE 할인을 계산한다 (10% of 100000 = 10000)")
        void calculateRateDiscount() {
            var policy = DiscountFixtures.activeRatePolicy();

            Money discount = policy.calculateDiscountAmount(Money.of(100000));

            assertThat(discount).isEqualTo(Money.of(10000));
        }

        @Test
        @DisplayName("RATE 할인에 maxDiscountAmount 한도가 적용된다")
        void rateDiscountCappedByMaxAmount() {
            var policy = DiscountFixtures.activeRatePolicy();

            Money discount = policy.calculateDiscountAmount(Money.of(1000000));

            assertThat(discount).isEqualTo(Money.of(DiscountFixtures.DEFAULT_MAX_DISCOUNT_AMOUNT));
        }

        @Test
        @DisplayName("FIXED_AMOUNT 할인을 계산한다")
        void calculateFixedDiscount() {
            var policy = DiscountFixtures.activeFixedPolicy(1L);

            Money discount = policy.calculateDiscountAmount(Money.of(100000));

            assertThat(discount).isEqualTo(Money.of(DiscountFixtures.DEFAULT_FIXED_AMOUNT));
        }

        @Test
        @DisplayName("FIXED_AMOUNT 할인이 기준가를 초과하지 않는다")
        void fixedDiscountDoesNotExceedBase() {
            var policy = DiscountFixtures.activeFixedPolicy(1L);

            Money discount = policy.calculateDiscountAmount(Money.of(3000));

            assertThat(discount).isEqualTo(Money.of(3000));
        }

        @Test
        @DisplayName("기준가가 0이면 할인 금액은 0이다")
        void zeroBasePriceReturnsZero() {
            var policy = DiscountFixtures.activeRatePolicy();

            Money discount = policy.calculateDiscountAmount(Money.zero());

            assertThat(discount).isEqualTo(Money.zero());
        }

        @Test
        @DisplayName("기준가가 null이면 할인 금액은 0이다")
        void nullBasePriceReturnsZero() {
            var policy = DiscountFixtures.activeRatePolicy();

            Money discount = policy.calculateDiscountAmount(null);

            assertThat(discount).isEqualTo(Money.zero());
        }
    }

    @Nested
    @DisplayName("Budget 관리 테스트")
    class BudgetManagementTest {

        @Test
        @DisplayName("예산을 소진한다")
        void consumeBudget() {
            var policy = DiscountFixtures.activeRatePolicy();
            Instant now = CommonVoFixtures.now();

            policy.consumeBudget(Money.of(10000), now);

            assertThat(policy.budget().usedBudget()).isEqualTo(Money.of(10000));
            assertThat(policy.hasSufficientBudget(Money.of(990000))).isTrue();
        }

        @Test
        @DisplayName("예산 부족 시 예외가 발생한다")
        void throwForInsufficientBudget() {
            var policy = DiscountFixtures.activeRatePolicy();

            assertThatThrownBy(
                            () ->
                                    policy.consumeBudget(
                                            Money.of(DiscountFixtures.DEFAULT_TOTAL_BUDGET + 1),
                                            CommonVoFixtures.now()))
                    .isInstanceOf(InsufficientBudgetException.class);
        }

        @Test
        @DisplayName("hasSufficientBudget()는 잔여 예산을 확인한다")
        void hasSufficientBudget() {
            var policy = DiscountFixtures.activeRatePolicy();

            assertThat(policy.hasSufficientBudget(Money.of(500000))).isTrue();
            assertThat(
                            policy.hasSufficientBudget(
                                    Money.of(DiscountFixtures.DEFAULT_TOTAL_BUDGET + 1)))
                    .isFalse();
        }
    }

    @Nested
    @DisplayName("isApplicableAt() - 적용 가능성 확인")
    class ApplicabilityTest {

        @Test
        @DisplayName("활성 + 기간 내 + 미삭제 + 예산 잔여이면 적용 가능하다")
        void applicableWhenAllConditionsMet() {
            var policy = DiscountFixtures.activeRatePolicy();
            assertThat(policy.isApplicableAt(Instant.now())).isTrue();
        }

        @Test
        @DisplayName("비활성이면 적용 불가하다")
        void notApplicableWhenInactive() {
            var policy = DiscountFixtures.inactivePolicy();
            assertThat(policy.isApplicableAt(Instant.now())).isFalse();
        }

        @Test
        @DisplayName("삭제되면 적용 불가하다")
        void notApplicableWhenDeleted() {
            var policy = DiscountFixtures.deletedPolicy();
            assertThat(policy.isApplicableAt(Instant.now())).isFalse();
        }

        @Test
        @DisplayName("예산 소진되면 적용 불가하다")
        void notApplicableWhenBudgetExhausted() {
            var policy = DiscountFixtures.exhaustedBudgetPolicy();
            assertThat(policy.isApplicableAt(Instant.now())).isFalse();
        }
    }

    @Nested
    @DisplayName("meetsMinimumOrder() - 최소 주문금액 확인")
    class MinimumOrderTest {

        @Test
        @DisplayName("주문금액이 최소 금액 이상이면 true를 반환한다")
        void returnsTrueForSufficientOrder() {
            var policy = DiscountFixtures.activeRatePolicy();
            assertThat(
                            policy.meetsMinimumOrder(
                                    Money.of(DiscountFixtures.DEFAULT_MINIMUM_ORDER_AMOUNT)))
                    .isTrue();
        }

        @Test
        @DisplayName("주문금액이 최소 금액 미만이면 false를 반환한다")
        void returnsFalseForInsufficientOrder() {
            var policy = DiscountFixtures.activeRatePolicy();
            assertThat(
                            policy.meetsMinimumOrder(
                                    Money.of(DiscountFixtures.DEFAULT_MINIMUM_ORDER_AMOUNT - 1)))
                    .isFalse();
        }

        @Test
        @DisplayName("minimumOrderAmount가 null이면 항상 true를 반환한다")
        void returnsTrueWhenNoMinimum() {
            var policy = DiscountFixtures.activeFixedPolicy(1L);
            assertThat(policy.meetsMinimumOrder(Money.of(1))).isTrue();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("id()는 DiscountPolicyId를 반환한다")
        void returnsId() {
            var policy = DiscountFixtures.activeRatePolicy();
            assertThat(policy.id()).isNotNull();
            assertThat(policy.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("name()은 DiscountPolicyName을 반환한다")
        void returnsName() {
            var policy = DiscountFixtures.activeRatePolicy();
            assertThat(policy.name()).isNotNull();
            assertThat(policy.nameValue()).isEqualTo(DiscountFixtures.DEFAULT_POLICY_NAME);
        }

        @Test
        @DisplayName("deletionStatus()는 DeletionStatus를 반환한다")
        void returnsDeletionStatus() {
            var policy = DiscountFixtures.activeRatePolicy();
            assertThat(policy.deletionStatus()).isNotNull();
            assertThat(policy.deletionStatus().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("targets()는 불변 리스트를 반환한다")
        void returnsUnmodifiableTargets() {
            var policy = DiscountFixtures.activeRatePolicy();
            assertThatThrownBy(() -> policy.targets().add(DiscountFixtures.newTarget()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("createdAt()은 생성 시각을 반환한다")
        void returnsCreatedAt() {
            var policy = DiscountFixtures.activeRatePolicy();
            assertThat(policy.createdAt()).isNotNull();
        }
    }
}
