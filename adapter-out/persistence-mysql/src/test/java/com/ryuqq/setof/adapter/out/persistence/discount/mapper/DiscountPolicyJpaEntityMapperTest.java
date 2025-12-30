package com.ryuqq.setof.adapter.out.persistence.discount.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.MapperTestSupport;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * DiscountPolicyJpaEntityMapper 단위 테스트
 *
 * <p>DiscountPolicy Domain <-> DiscountPolicyJpaEntity 간의 변환 로직을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("DiscountPolicyJpaEntityMapper 단위 테스트")
class DiscountPolicyJpaEntityMapperTest extends MapperTestSupport {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FUTURE_TIME = Instant.parse("2025-12-31T23:59:59Z");
    private static final Long TEST_SELLER_ID = 1L;
    private static final Long TEST_POLICY_ID = 1L;

    private DiscountPolicyJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DiscountPolicyJpaEntityMapper();
    }

    @Nested
    @DisplayName("toEntity 메서드")
    class ToEntity {

        @Test
        @DisplayName("성공 - 정률 할인 정책을 Entity로 변환한다")
        void toEntity_rateDiscount_success() {
            // Given
            DiscountPolicy rateDiscount = createRateDiscountPolicy();

            // When
            DiscountPolicyJpaEntity entity = mapper.toEntity(rateDiscount);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(rateDiscount.getIdValue());
            assertThat(entity.getSellerId()).isEqualTo(rateDiscount.getSellerId());
            assertThat(entity.getPolicyName()).isEqualTo(rateDiscount.getPolicyNameValue());
            assertThat(entity.getDiscountGroup()).isEqualTo(DiscountGroupType.PRODUCT);
            assertThat(entity.getDiscountType()).isEqualTo(DiscountTypeEnum.RATE);
            assertThat(entity.getTargetType()).isEqualTo(DiscountTargetTypeEnum.ALL);
            assertThat(entity.getDiscountRate()).isEqualByComparingTo(new BigDecimal("10"));
            assertThat(entity.getDiscountAmount()).isNull();
            assertThat(entity.getMaximumDiscountAmount()).isEqualTo(10000L);
            assertThat(entity.getMinimumOrderAmount()).isEqualTo(30000L);
            assertThat(entity.getValidStartAt()).isEqualTo(FIXED_TIME);
            assertThat(entity.getValidEndAt()).isEqualTo(FUTURE_TIME);
            assertThat(entity.getMaxUsagePerCustomer()).isEqualTo(1);
            assertThat(entity.getMaxTotalUsage()).isEqualTo(1000);
            assertThat(entity.getPlatformCostShareRatio())
                    .isEqualByComparingTo(new BigDecimal("50"));
            assertThat(entity.getSellerCostShareRatio()).isEqualByComparingTo(new BigDecimal("50"));
            assertThat(entity.getPriority()).isEqualTo(100);
            assertThat(entity.getIsActive()).isTrue();
        }

        @Test
        @DisplayName("성공 - 정액 할인 정책을 Entity로 변환한다")
        void toEntity_fixedDiscount_success() {
            // Given
            DiscountPolicy fixedDiscount = createFixedDiscountPolicy();

            // When
            DiscountPolicyJpaEntity entity = mapper.toEntity(fixedDiscount);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getDiscountType()).isEqualTo(DiscountTypeEnum.FIXED_PRICE);
            assertThat(entity.getDiscountGroup()).isEqualTo(DiscountGroupType.MEMBER);
            assertThat(entity.getDiscountAmount()).isEqualTo(5000L);
            assertThat(entity.getDiscountRate()).isNull();
        }

        @Test
        @DisplayName("성공 - 특정 대상 ID가 있는 정책을 Entity로 변환한다")
        void toEntity_withTargetIds_success() {
            // Given
            DiscountPolicy policyWithTargets = createPolicyWithTargetIds();

            // When
            DiscountPolicyJpaEntity entity = mapper.toEntity(policyWithTargets);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getTargetType()).isEqualTo(DiscountTargetTypeEnum.PRODUCT);
            assertThat(entity.getTargetIds()).isEqualTo("100,200,300");
        }

        @Test
        @DisplayName("성공 - 삭제된 정책을 Entity로 변환한다")
        void toEntity_deletedPolicy_success() {
            // Given
            DiscountPolicy deletedPolicy = createDeletedPolicy();

            // When
            DiscountPolicyJpaEntity entity = mapper.toEntity(deletedPolicy);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("성공 - 비활성화된 정책을 Entity로 변환한다")
        void toEntity_inactivePolicy_success() {
            // Given
            DiscountPolicy inactivePolicy = createInactivePolicy();

            // When
            DiscountPolicyJpaEntity entity = mapper.toEntity(inactivePolicy);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getIsActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("toDomain 메서드")
    class ToDomain {

        @Test
        @DisplayName("성공 - 정률 할인 Entity를 Domain으로 변환한다")
        void toDomain_rateDiscount_success() {
            // Given
            DiscountPolicyJpaEntity entity = createRateDiscountEntity();

            // When
            DiscountPolicy domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getIdValue()).isEqualTo(entity.getId());
            assertThat(domain.getSellerId()).isEqualTo(entity.getSellerId());
            assertThat(domain.getPolicyNameValue()).isEqualTo(entity.getPolicyName());
            assertThat(domain.getDiscountGroup()).isEqualTo(DiscountGroup.PRODUCT);
            assertThat(domain.getDiscountType()).isEqualTo(DiscountType.RATE);
            assertThat(domain.getTargetType()).isEqualTo(DiscountTargetType.ALL);
            assertThat(domain.getDiscountRateValue()).isEqualByComparingTo(new BigDecimal("10"));
            assertThat(domain.getDiscountAmountValue()).isNull();
            assertThat(domain.getMaximumDiscountAmountValue()).isEqualTo(10000L);
            assertThat(domain.getMinimumOrderAmountValue()).isEqualTo(30000L);
            assertThat(domain.getValidStartAt()).isEqualTo(FIXED_TIME);
            assertThat(domain.getValidEndAt()).isEqualTo(FUTURE_TIME);
            assertThat(domain.getMaxUsagePerCustomer()).isEqualTo(1);
            assertThat(domain.getMaxTotalUsage()).isEqualTo(1000);
            assertThat(domain.getPlatformCostShareRatio())
                    .isEqualByComparingTo(new BigDecimal("50"));
            assertThat(domain.getSellerCostShareRatio()).isEqualByComparingTo(new BigDecimal("50"));
            assertThat(domain.getPriorityValue()).isEqualTo(100);
            assertThat(domain.isActive()).isTrue();
        }

        @Test
        @DisplayName("성공 - 정액 할인 Entity를 Domain으로 변환한다")
        void toDomain_fixedDiscount_success() {
            // Given
            DiscountPolicyJpaEntity entity = createFixedDiscountEntity();

            // When
            DiscountPolicy domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getDiscountType()).isEqualTo(DiscountType.FIXED_PRICE);
            assertThat(domain.getDiscountGroup()).isEqualTo(DiscountGroup.MEMBER);
            assertThat(domain.getDiscountAmountValue()).isEqualTo(5000L);
            assertThat(domain.getDiscountRateValue()).isNull();
        }

        @Test
        @DisplayName("성공 - 특정 대상 ID가 있는 Entity를 Domain으로 변환한다")
        void toDomain_withTargetIds_success() {
            // Given
            DiscountPolicyJpaEntity entity = createEntityWithTargetIds();

            // When
            DiscountPolicy domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getTargetType()).isEqualTo(DiscountTargetType.PRODUCT);
            assertThat(domain.getTargetIds()).containsExactly(100L, 200L, 300L);
        }

        @Test
        @DisplayName("성공 - 대상 ID가 없는 Entity를 Domain으로 변환한다")
        void toDomain_withoutTargetIds_success() {
            // Given
            DiscountPolicyJpaEntity entity = createRateDiscountEntity();

            // When
            DiscountPolicy domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getTargetIds()).isEmpty();
        }

        @Test
        @DisplayName("성공 - 삭제된 Entity를 Domain으로 변환한다")
        void toDomain_deletedEntity_success() {
            // Given
            DiscountPolicyJpaEntity entity = createDeletedEntity();

            // When
            DiscountPolicy domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getDeletedAt()).isNotNull();
            assertThat(domain.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("성공 - 최대 할인 금액이 없는 Entity를 Domain으로 변환한다 (Unlimited)")
        void toDomain_withoutMaxDiscountAmount_success() {
            // Given
            DiscountPolicyJpaEntity entity = createEntityWithoutMaxDiscount();

            // When
            DiscountPolicy domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getMaximumDiscountAmount().hasLimit()).isFalse();
        }

        @Test
        @DisplayName("성공 - 최소 주문 금액이 없는 Entity를 Domain으로 변환한다 (NoMinimum)")
        void toDomain_withoutMinOrderAmount_success() {
            // Given
            DiscountPolicyJpaEntity entity = createEntityWithoutMinOrder();

            // When
            DiscountPolicy domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getMinimumOrderAmountValue()).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("양방향 변환 검증")
    class RoundTrip {

        @Test
        @DisplayName("성공 - Domain -> Entity -> Domain 변환 시 정률 할인 데이터가 보존된다")
        void roundTrip_domainToEntityToDomain_rateDiscount_preservesData() {
            // Given
            DiscountPolicy original = createRateDiscountPolicy();

            // When
            DiscountPolicyJpaEntity entity = mapper.toEntity(original);
            DiscountPolicy converted = mapper.toDomain(entity);

            // Then
            assertThat(converted.getIdValue()).isEqualTo(original.getIdValue());
            assertThat(converted.getSellerId()).isEqualTo(original.getSellerId());
            assertThat(converted.getPolicyNameValue()).isEqualTo(original.getPolicyNameValue());
            assertThat(converted.getDiscountGroup()).isEqualTo(original.getDiscountGroup());
            assertThat(converted.getDiscountType()).isEqualTo(original.getDiscountType());
            assertThat(converted.getTargetType()).isEqualTo(original.getTargetType());
            assertThat(converted.getDiscountRateValue()).isEqualTo(original.getDiscountRateValue());
            assertThat(converted.getMaximumDiscountAmountValue())
                    .isEqualTo(original.getMaximumDiscountAmountValue());
            assertThat(converted.getMinimumOrderAmountValue())
                    .isEqualTo(original.getMinimumOrderAmountValue());
            assertThat(converted.getValidStartAt()).isEqualTo(original.getValidStartAt());
            assertThat(converted.getValidEndAt()).isEqualTo(original.getValidEndAt());
            assertThat(converted.getMaxUsagePerCustomer())
                    .isEqualTo(original.getMaxUsagePerCustomer());
            assertThat(converted.getMaxTotalUsage()).isEqualTo(original.getMaxTotalUsage());
            assertThat(converted.getPlatformCostShareRatio())
                    .isEqualTo(original.getPlatformCostShareRatio());
            assertThat(converted.getSellerCostShareRatio())
                    .isEqualTo(original.getSellerCostShareRatio());
            assertThat(converted.getPriorityValue()).isEqualTo(original.getPriorityValue());
            assertThat(converted.isActive()).isEqualTo(original.isActive());
        }

        @Test
        @DisplayName("성공 - Domain -> Entity -> Domain 변환 시 정액 할인 데이터가 보존된다")
        void roundTrip_domainToEntityToDomain_fixedDiscount_preservesData() {
            // Given
            DiscountPolicy original = createFixedDiscountPolicy();

            // When
            DiscountPolicyJpaEntity entity = mapper.toEntity(original);
            DiscountPolicy converted = mapper.toDomain(entity);

            // Then
            assertThat(converted.getDiscountType()).isEqualTo(DiscountType.FIXED_PRICE);
            assertThat(converted.getDiscountAmountValue())
                    .isEqualTo(original.getDiscountAmountValue());
        }

        @Test
        @DisplayName("성공 - Entity -> Domain -> Entity 변환 시 데이터가 보존된다")
        void roundTrip_entityToDomainToEntity_preservesData() {
            // Given
            DiscountPolicyJpaEntity original = createRateDiscountEntity();

            // When
            DiscountPolicy domain = mapper.toDomain(original);
            DiscountPolicyJpaEntity converted = mapper.toEntity(domain);

            // Then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getSellerId()).isEqualTo(original.getSellerId());
            assertThat(converted.getPolicyName()).isEqualTo(original.getPolicyName());
            assertThat(converted.getDiscountGroup()).isEqualTo(original.getDiscountGroup());
            assertThat(converted.getDiscountType()).isEqualTo(original.getDiscountType());
            assertThat(converted.getTargetType()).isEqualTo(original.getTargetType());
            assertThat(converted.getDiscountRate())
                    .isEqualByComparingTo(original.getDiscountRate());
            assertThat(converted.getMaximumDiscountAmount())
                    .isEqualTo(original.getMaximumDiscountAmount());
            assertThat(converted.getMinimumOrderAmount())
                    .isEqualTo(original.getMinimumOrderAmount());
            assertThat(converted.getValidStartAt()).isEqualTo(original.getValidStartAt());
            assertThat(converted.getValidEndAt()).isEqualTo(original.getValidEndAt());
            assertThat(converted.getMaxUsagePerCustomer())
                    .isEqualTo(original.getMaxUsagePerCustomer());
            assertThat(converted.getMaxTotalUsage()).isEqualTo(original.getMaxTotalUsage());
            assertThat(converted.getPlatformCostShareRatio())
                    .isEqualByComparingTo(original.getPlatformCostShareRatio());
            assertThat(converted.getSellerCostShareRatio())
                    .isEqualByComparingTo(original.getSellerCostShareRatio());
            assertThat(converted.getPriority()).isEqualTo(original.getPriority());
            assertThat(converted.getIsActive()).isEqualTo(original.getIsActive());
        }

        @Test
        @DisplayName("성공 - 대상 ID 목록이 변환 시 보존된다")
        void roundTrip_targetIds_preserved() {
            // Given
            DiscountPolicy original = createPolicyWithTargetIds();

            // When
            DiscountPolicyJpaEntity entity = mapper.toEntity(original);
            DiscountPolicy converted = mapper.toDomain(entity);

            // Then
            assertThat(converted.getTargetIds()).isEqualTo(original.getTargetIds());
        }
    }

    // ========== Helper Methods - Domain Objects ==========

    private DiscountPolicy createRateDiscountPolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(TEST_POLICY_ID),
                TEST_SELLER_ID,
                PolicyName.of("여름 시즌 할인"),
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
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createFixedDiscountPolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(2L),
                TEST_SELLER_ID,
                PolicyName.of("정액 할인 이벤트"),
                DiscountGroup.MEMBER,
                DiscountType.FIXED_PRICE,
                DiscountTargetType.ALL,
                List.of(),
                null,
                DiscountAmount.of(5000L),
                null,
                MinimumOrderAmount.of(50000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.of(2, 500),
                CostShare.of(new BigDecimal("60"), new BigDecimal("40")),
                Priority.of(200),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createPolicyWithTargetIds() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(3L),
                TEST_SELLER_ID,
                PolicyName.of("상품별 할인"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.PRODUCT,
                List.of(100L, 200L, 300L),
                DiscountRate.of(15),
                null,
                MaximumDiscountAmount.of(20000L),
                MinimumOrderAmount.of(10000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.unlimited(),
                CostShare.of(new BigDecimal("30"), new BigDecimal("70")),
                Priority.of(50),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createDeletedPolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(4L),
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

    private DiscountPolicy createInactivePolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(5L),
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

    // ========== Helper Methods - Entity Objects ==========

    private DiscountPolicyJpaEntity createRateDiscountEntity() {
        return DiscountPolicyJpaEntity.of(
                TEST_POLICY_ID,
                TEST_SELLER_ID,
                "여름 시즌 할인",
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

    private DiscountPolicyJpaEntity createFixedDiscountEntity() {
        return DiscountPolicyJpaEntity.of(
                2L,
                TEST_SELLER_ID,
                "정액 할인 이벤트",
                DiscountGroupType.MEMBER,
                DiscountTypeEnum.FIXED_PRICE,
                DiscountTargetTypeEnum.ALL,
                null,
                null,
                5000L,
                null,
                50000L,
                FIXED_TIME,
                FUTURE_TIME,
                2,
                500,
                new BigDecimal("60"),
                new BigDecimal("40"),
                200,
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicyJpaEntity createEntityWithTargetIds() {
        return DiscountPolicyJpaEntity.of(
                3L,
                TEST_SELLER_ID,
                "상품별 할인",
                DiscountGroupType.PRODUCT,
                DiscountTypeEnum.RATE,
                DiscountTargetTypeEnum.PRODUCT,
                "100,200,300",
                new BigDecimal("15"),
                null,
                20000L,
                10000L,
                FIXED_TIME,
                FUTURE_TIME,
                null,
                null,
                new BigDecimal("30"),
                new BigDecimal("70"),
                50,
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicyJpaEntity createDeletedEntity() {
        return DiscountPolicyJpaEntity.of(
                4L,
                TEST_SELLER_ID,
                "삭제된 정책",
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
                false,
                FIXED_TIME,
                FIXED_TIME,
                FIXED_TIME);
    }

    private DiscountPolicyJpaEntity createEntityWithoutMaxDiscount() {
        return DiscountPolicyJpaEntity.of(
                5L,
                TEST_SELLER_ID,
                "무제한 할인",
                DiscountGroupType.PRODUCT,
                DiscountTypeEnum.RATE,
                DiscountTargetTypeEnum.ALL,
                null,
                new BigDecimal("10"),
                null,
                null,
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

    private DiscountPolicyJpaEntity createEntityWithoutMinOrder() {
        return DiscountPolicyJpaEntity.of(
                6L,
                TEST_SELLER_ID,
                "조건없는 할인",
                DiscountGroupType.PRODUCT,
                DiscountTypeEnum.RATE,
                DiscountTargetTypeEnum.ALL,
                null,
                new BigDecimal("10"),
                null,
                10000L,
                null,
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
