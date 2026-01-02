package com.ryuqq.setof.adapter.out.persistence.discount.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountGroupType;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountTargetTypeEnum;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountTypeEnum;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
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
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DiscountPolicyPersistenceAdapter 통합 테스트
 *
 * <p>DiscountPolicyPersistencePort 구현체의 저장 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("DiscountPolicyPersistenceAdapter 통합 테스트")
class DiscountPolicyPersistenceAdapterTest extends RepositoryTestSupport {

    @Autowired private DiscountPolicyPersistenceAdapter discountPolicyPersistenceAdapter;

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FUTURE_TIME = Instant.parse("2025-12-31T23:59:59Z");
    private static final Long TEST_SELLER_ID = 1L;

    @Nested
    @DisplayName("persist 메서드 - 신규 저장")
    class PersistNew {

        @Test
        @DisplayName("성공 - 새 정률 할인 정책을 저장하고 ID를 반환한다")
        void persist_newRateDiscountPolicy_savesAndReturnsId() {
            // Given
            DiscountPolicy newPolicy = createNewRateDiscountPolicy();

            // When
            DiscountPolicyId savedId = discountPolicyPersistenceAdapter.persist(newPolicy);
            flushAndClear();

            // Then
            assertThat(savedId).isNotNull();
            assertThat(savedId.value()).isNotNull();

            DiscountPolicyJpaEntity found = find(DiscountPolicyJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getPolicyName()).isEqualTo("여름 시즌 할인");
            assertThat(found.getSellerId()).isEqualTo(TEST_SELLER_ID);
            assertThat(found.getDiscountGroup()).isEqualTo(DiscountGroupType.PRODUCT);
            assertThat(found.getDiscountType()).isEqualTo(DiscountTypeEnum.RATE);
            assertThat(found.getDiscountRate()).isEqualByComparingTo(new BigDecimal("10"));
            assertThat(found.getIsActive()).isTrue();
        }

        @Test
        @DisplayName("성공 - 새 정액 할인 정책을 저장하고 ID를 반환한다")
        void persist_newFixedDiscountPolicy_savesAndReturnsId() {
            // Given
            DiscountPolicy newPolicy = createNewFixedDiscountPolicy();

            // When
            DiscountPolicyId savedId = discountPolicyPersistenceAdapter.persist(newPolicy);
            flushAndClear();

            // Then
            assertThat(savedId).isNotNull();
            assertThat(savedId.value()).isNotNull();

            DiscountPolicyJpaEntity found = find(DiscountPolicyJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getPolicyName()).isEqualTo("정액 할인 이벤트");
            assertThat(found.getDiscountType()).isEqualTo(DiscountTypeEnum.FIXED_PRICE);
            assertThat(found.getDiscountAmount()).isEqualTo(5000L);
        }

        @Test
        @DisplayName("성공 - 특정 대상 ID가 있는 정책을 저장한다")
        void persist_policyWithTargetIds_saves() {
            // Given
            DiscountPolicy policyWithTargets = createPolicyWithTargetIds();

            // When
            DiscountPolicyId savedId = discountPolicyPersistenceAdapter.persist(policyWithTargets);
            flushAndClear();

            // Then
            DiscountPolicyJpaEntity found = find(DiscountPolicyJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getTargetType()).isEqualTo(DiscountTargetTypeEnum.PRODUCT);
            assertThat(found.getTargetIds()).isEqualTo("100,200,300");
        }

        @Test
        @DisplayName("성공 - 비활성 상태로 정책을 업데이트한다")
        void persist_inactivePolicy_updates() {
            // Given - 먼저 활성 정책 저장
            DiscountPolicy activePolicy = createNewRateDiscountPolicy();
            DiscountPolicyId savedId = discountPolicyPersistenceAdapter.persist(activePolicy);
            flushAndClear();

            // When - 비활성으로 업데이트
            DiscountPolicy inactivePolicy = createInactivePolicy(savedId.value());
            discountPolicyPersistenceAdapter.persist(inactivePolicy);
            flushAndClear();

            // Then
            DiscountPolicyJpaEntity found = find(DiscountPolicyJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getIsActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("persist 메서드 - 수정")
    class PersistUpdate {

        @Test
        @DisplayName("성공 - 기존 정책을 수정한다")
        void persist_existingPolicy_updates() {
            // Given
            DiscountPolicyJpaEntity existingEntity = persistAndFlush(createExistingEntity());
            flushAndClear();

            DiscountPolicy updatedPolicy = createUpdatedPolicy(existingEntity.getId());

            // When
            discountPolicyPersistenceAdapter.persist(updatedPolicy);
            flushAndClear();

            // Then
            DiscountPolicyJpaEntity found =
                    find(DiscountPolicyJpaEntity.class, existingEntity.getId());
            assertThat(found).isNotNull();
            assertThat(found.getPolicyName()).isEqualTo("수정된 정책명");
            assertThat(found.getMaximumDiscountAmount()).isEqualTo(20000L);
        }

        @Test
        @DisplayName("성공 - 활성화 상태를 변경한다")
        void persist_changeActiveStatus_updates() {
            // Given
            DiscountPolicyJpaEntity existingEntity = persistAndFlush(createExistingEntity());
            flushAndClear();

            DiscountPolicy deactivatedPolicy = createDeactivatedPolicy(existingEntity.getId());

            // When
            discountPolicyPersistenceAdapter.persist(deactivatedPolicy);
            flushAndClear();

            // Then
            DiscountPolicyJpaEntity found =
                    find(DiscountPolicyJpaEntity.class, existingEntity.getId());
            assertThat(found).isNotNull();
            assertThat(found.getIsActive()).isFalse();
        }

        @Test
        @DisplayName("성공 - 삭제 상태로 변경한다 (Soft Delete)")
        void persist_softDelete_updates() {
            // Given
            DiscountPolicyJpaEntity existingEntity = persistAndFlush(createExistingEntity());
            flushAndClear();

            DiscountPolicy deletedPolicy = createDeletedPolicy(existingEntity.getId());

            // When
            discountPolicyPersistenceAdapter.persist(deletedPolicy);
            flushAndClear();

            // Then
            DiscountPolicyJpaEntity found =
                    find(DiscountPolicyJpaEntity.class, existingEntity.getId());
            assertThat(found).isNotNull();
            assertThat(found.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("성공 - 유효 기간을 연장한다")
        void persist_extendValidPeriod_updates() {
            // Given
            DiscountPolicyJpaEntity existingEntity = persistAndFlush(createExistingEntity());
            flushAndClear();

            Instant newEndAt = Instant.parse("2026-12-31T23:59:59Z");
            DiscountPolicy extendedPolicy =
                    createPolicyWithExtendedPeriod(existingEntity.getId(), newEndAt);

            // When
            discountPolicyPersistenceAdapter.persist(extendedPolicy);
            flushAndClear();

            // Then
            DiscountPolicyJpaEntity found =
                    find(DiscountPolicyJpaEntity.class, existingEntity.getId());
            assertThat(found).isNotNull();
            assertThat(found.getValidEndAt()).isEqualTo(newEndAt);
        }

        @Test
        @DisplayName("성공 - 사용 횟수 제한을 변경한다")
        void persist_changeUsageLimit_updates() {
            // Given
            DiscountPolicyJpaEntity existingEntity = persistAndFlush(createExistingEntity());
            flushAndClear();

            DiscountPolicy updatedPolicy = createPolicyWithNewUsageLimit(existingEntity.getId());

            // When
            discountPolicyPersistenceAdapter.persist(updatedPolicy);
            flushAndClear();

            // Then
            DiscountPolicyJpaEntity found =
                    find(DiscountPolicyJpaEntity.class, existingEntity.getId());
            assertThat(found).isNotNull();
            assertThat(found.getMaxUsagePerCustomer()).isEqualTo(5);
            assertThat(found.getMaxTotalUsage()).isEqualTo(5000);
        }
    }

    // ========== Helper Methods - Domain Objects ==========

    private DiscountPolicy createNewRateDiscountPolicy() {
        return DiscountPolicy.forNewRateDiscount(
                TEST_SELLER_ID,
                PolicyName.of("여름 시즌 할인"),
                DiscountGroup.PRODUCT,
                DiscountTargetType.ALL,
                List.of(),
                DiscountRate.of(10),
                MaximumDiscountAmount.of(10000L),
                MinimumOrderAmount.of(30000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.of(1, 1000),
                CostShare.of(new BigDecimal("50"), new BigDecimal("50")),
                Priority.of(100),
                FIXED_TIME);
    }

    private DiscountPolicy createNewFixedDiscountPolicy() {
        return DiscountPolicy.forNewFixedDiscount(
                TEST_SELLER_ID,
                PolicyName.of("정액 할인 이벤트"),
                DiscountGroup.MEMBER,
                DiscountTargetType.ALL,
                List.of(),
                DiscountAmount.of(5000L),
                MinimumOrderAmount.of(50000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.of(2, 500),
                CostShare.of(new BigDecimal("60"), new BigDecimal("40")),
                Priority.of(200),
                FIXED_TIME);
    }

    private DiscountPolicy createPolicyWithTargetIds() {
        return DiscountPolicy.forNewRateDiscount(
                TEST_SELLER_ID,
                PolicyName.of("상품별 할인"),
                DiscountGroup.PRODUCT,
                DiscountTargetType.PRODUCT,
                List.of(100L, 200L, 300L),
                DiscountRate.of(15),
                MaximumDiscountAmount.of(20000L),
                MinimumOrderAmount.of(10000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.unlimited(),
                CostShare.of(new BigDecimal("30"), new BigDecimal("70")),
                Priority.of(50),
                FIXED_TIME);
    }

    private DiscountPolicy createInactivePolicy(Long id) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(id),
                TEST_SELLER_ID,
                PolicyName.of("비활성 정책"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                List.of(),
                DiscountRate.of(10),
                null,
                MaximumDiscountAmount.of(10000L),
                MinimumOrderAmount.of(30000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.of(1, 1000),
                CostShare.of(new BigDecimal("50"), new BigDecimal("50")),
                Priority.of(100),
                false,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createUpdatedPolicy(Long id) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(id),
                TEST_SELLER_ID,
                PolicyName.of("수정된 정책명"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                List.of(),
                DiscountRate.of(10),
                null,
                MaximumDiscountAmount.of(20000L),
                MinimumOrderAmount.of(30000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.of(1, 1000),
                CostShare.of(new BigDecimal("50"), new BigDecimal("50")),
                Priority.of(100),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createDeactivatedPolicy(Long id) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(id),
                TEST_SELLER_ID,
                PolicyName.of("기존 정책"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                List.of(),
                DiscountRate.of(10),
                null,
                MaximumDiscountAmount.of(10000L),
                MinimumOrderAmount.of(30000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.of(1, 1000),
                CostShare.of(new BigDecimal("50"), new BigDecimal("50")),
                Priority.of(100),
                false,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createDeletedPolicy(Long id) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(id),
                TEST_SELLER_ID,
                PolicyName.of("삭제된 정책"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                List.of(),
                DiscountRate.of(10),
                null,
                MaximumDiscountAmount.of(10000L),
                MinimumOrderAmount.of(30000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.of(1, 1000),
                CostShare.of(new BigDecimal("50"), new BigDecimal("50")),
                Priority.of(100),
                false,
                FIXED_TIME,
                FIXED_TIME,
                FIXED_TIME);
    }

    private DiscountPolicy createPolicyWithExtendedPeriod(Long id, Instant newEndAt) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(id),
                TEST_SELLER_ID,
                PolicyName.of("기존 정책"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                List.of(),
                DiscountRate.of(10),
                null,
                MaximumDiscountAmount.of(10000L),
                MinimumOrderAmount.of(30000L),
                ValidPeriod.of(FIXED_TIME, newEndAt),
                UsageLimit.of(1, 1000),
                CostShare.of(new BigDecimal("50"), new BigDecimal("50")),
                Priority.of(100),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createPolicyWithNewUsageLimit(Long id) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(id),
                TEST_SELLER_ID,
                PolicyName.of("기존 정책"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                List.of(),
                DiscountRate.of(10),
                null,
                MaximumDiscountAmount.of(10000L),
                MinimumOrderAmount.of(30000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.of(5, 5000),
                CostShare.of(new BigDecimal("50"), new BigDecimal("50")),
                Priority.of(100),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    // ========== Helper Methods - Entity Objects ==========

    private DiscountPolicyJpaEntity createExistingEntity() {
        return DiscountPolicyJpaEntity.of(
                null,
                TEST_SELLER_ID,
                "기존 정책",
                DiscountGroupType.PRODUCT,
                DiscountTypeEnum.RATE,
                DiscountTargetTypeEnum.ALL,
                null,
                new BigDecimal("10"),
                null,
                10000L,
                30000L,
                FIXED_TIME,
                FUTURE_TIME,
                1,
                1000,
                new BigDecimal("50"),
                new BigDecimal("50"),
                100,
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }
}
