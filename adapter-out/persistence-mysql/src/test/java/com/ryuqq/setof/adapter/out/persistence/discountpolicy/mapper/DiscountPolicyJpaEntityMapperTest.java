package com.ryuqq.setof.adapter.out.persistence.discountpolicy.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountTargetJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import com.ryuqq.setof.domain.discount.vo.DiscountMethod;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * DiscountPolicyJpaEntityMapperTest - 할인 정책 Mapper 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 검증.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("DiscountPolicyJpaEntityMapper 단위 테스트")
class DiscountPolicyJpaEntityMapperTest {

    private DiscountPolicyJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DiscountPolicyJpaEntityMapper();
    }

    // ========================================================================
    // 1. toEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntity 변환 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("신규 RATE 정책 Domain을 Entity로 변환합니다")
        void toEntity_WithNewRatePolicy_ReturnsEntityWithNullId() {
            // given
            DiscountPolicy domain = DiscountFixtures.newRateInstantPolicy();

            // when
            DiscountPolicyJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isNull();
            assertThat(entity.getName()).isEqualTo(domain.nameValue());
            assertThat(entity.getDescription()).isEqualTo(domain.description());
            assertThat(entity.getDiscountMethod())
                    .isEqualTo(DiscountPolicyJpaEntity.DiscountMethod.RATE);
            assertThat(entity.getDiscountRate()).isEqualTo(domain.discountRateValue());
            assertThat(entity.getDiscountAmount()).isNull();
            assertThat(entity.isDiscountCapped()).isEqualTo(domain.isDiscountCapped());
            assertThat(entity.getApplicationType())
                    .isEqualTo(DiscountPolicyJpaEntity.ApplicationType.IMMEDIATE);
            assertThat(entity.getPublisherType())
                    .isEqualTo(DiscountPolicyJpaEntity.PublisherType.ADMIN);
            assertThat(entity.getStackingGroup())
                    .isEqualTo(DiscountPolicyJpaEntity.StackingGroup.PLATFORM_INSTANT);
            assertThat(entity.isActive()).isTrue();
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("신규 FIXED_AMOUNT 셀러 정책 Domain을 Entity로 변환합니다")
        void toEntity_WithNewFixedSellerPolicy_ReturnsEntityWithSellerId() {
            // given
            DiscountPolicy domain = DiscountFixtures.newFixedInstantPolicy();

            // when
            DiscountPolicyJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isNull();
            assertThat(entity.getDiscountMethod())
                    .isEqualTo(DiscountPolicyJpaEntity.DiscountMethod.FIXED_AMOUNT);
            assertThat(entity.getDiscountAmount()).isEqualTo(domain.discountAmountValue());
            assertThat(entity.getDiscountRate()).isNull();
            assertThat(entity.getSellerId()).isEqualTo(domain.sellerIdValue());
            assertThat(entity.getPublisherType())
                    .isEqualTo(DiscountPolicyJpaEntity.PublisherType.SELLER);
            assertThat(entity.getStackingGroup())
                    .isEqualTo(DiscountPolicyJpaEntity.StackingGroup.SELLER_INSTANT);
        }

        @Test
        @DisplayName("기존 RATE 정책 Domain을 Entity로 변환 시 ID가 보존됩니다")
        void toEntity_WithExistingRatePolicy_PreservesId() {
            // given
            DiscountPolicy domain = DiscountFixtures.activeRatePolicy(1L);

            // when
            DiscountPolicyJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("삭제된 정책 Domain을 Entity로 변환 시 deletedAt이 설정됩니다")
        void toEntity_WithDeletedPolicy_SetsDeletedAt() {
            // given
            DiscountPolicy domain = DiscountFixtures.deletedPolicy();

            // when
            DiscountPolicyJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getDeletedAt()).isNotNull();
            assertThat(entity.isActive()).isFalse();
        }

        @Test
        @DisplayName("비활성 정책 Domain을 Entity로 변환합니다")
        void toEntity_WithInactivePolicy_ReturnsInactiveEntity() {
            // given
            DiscountPolicy domain = DiscountFixtures.inactivePolicy();

            // when
            DiscountPolicyJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.isActive()).isFalse();
        }

        @Test
        @DisplayName("쿠폰 타입 정책 Domain을 Entity로 변환합니다")
        void toEntity_WithCouponPolicy_ReturnsCouponTypeEntity() {
            // given
            DiscountPolicy domain = DiscountFixtures.newCouponPolicy();

            // when
            DiscountPolicyJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getApplicationType())
                    .isEqualTo(DiscountPolicyJpaEntity.ApplicationType.COUPON);
            assertThat(entity.getStackingGroup())
                    .isEqualTo(DiscountPolicyJpaEntity.StackingGroup.COUPON);
        }
    }

    // ========================================================================
    // 2. toTargetEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toTargetEntity 변환 테스트")
    class ToTargetEntityTest {

        @Test
        @DisplayName("신규 DiscountTarget을 DiscountTargetJpaEntity로 변환합니다")
        void toTargetEntity_WithNewTarget_ReturnsEntityWithNullId() {
            // given
            DiscountTarget target = DiscountFixtures.newTarget();
            long policyId = 1L;
            Instant now = Instant.now();

            // when
            DiscountTargetJpaEntity entity = mapper.toTargetEntity(target, policyId, now);

            // then
            assertThat(entity.getId()).isNull();
            assertThat(entity.getDiscountPolicyId()).isEqualTo(policyId);
            assertThat(entity.getTargetType())
                    .isEqualTo(DiscountTargetJpaEntity.TargetType.PRODUCT);
            assertThat(entity.getTargetId()).isEqualTo(target.targetId());
            assertThat(entity.isActive()).isTrue();
        }

        @Test
        @DisplayName("기존 DiscountTarget을 변환 시 ID가 보존됩니다")
        void toTargetEntity_WithExistingTarget_PreservesId() {
            // given
            DiscountTarget target = DiscountFixtures.activeTarget(10L);
            long policyId = 1L;
            Instant now = Instant.now();

            // when
            DiscountTargetJpaEntity entity = mapper.toTargetEntity(target, policyId, now);

            // then
            assertThat(entity.getId()).isEqualTo(10L);
        }

        @Test
        @DisplayName("비활성 DiscountTarget을 Entity로 변환합니다")
        void toTargetEntity_WithInactiveTarget_ReturnsInactiveEntity() {
            // given
            DiscountTarget target = DiscountFixtures.inactiveTarget(5L);
            long policyId = 1L;
            Instant now = Instant.now();

            // when
            DiscountTargetJpaEntity entity = mapper.toTargetEntity(target, policyId, now);

            // then
            assertThat(entity.isActive()).isFalse();
        }
    }

    // ========================================================================
    // 3. toDomain 테스트 (Policy + Targets)
    // ========================================================================

    @Nested
    @DisplayName("toDomain 변환 테스트 (DiscountPolicy)")
    class ToDomainTest {

        @Test
        @DisplayName("활성 RATE Entity를 Domain으로 변환합니다")
        void toDomain_WithActiveRateEntity_ReturnsValidDomain() {
            // given
            DiscountPolicyJpaEntity entity = DiscountPolicyJpaEntityFixtures.activeRateEntity();
            List<DiscountTargetJpaEntity> targets = List.of();

            // when
            DiscountPolicy domain = mapper.toDomain(entity, targets);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.nameValue()).isEqualTo(entity.getName());
            assertThat(domain.description()).isEqualTo(entity.getDescription());
            assertThat(domain.discountMethod()).isEqualTo(DiscountMethod.RATE);
            assertThat(domain.discountRateValue()).isEqualTo(entity.getDiscountRate());
            assertThat(domain.discountAmountValue()).isNull();
            assertThat(domain.isActive()).isEqualTo(entity.isActive());
            assertThat(domain.deletedAt()).isNull();
            assertThat(domain.targets()).isEmpty();
        }

        @Test
        @DisplayName("타겟 목록과 함께 Policy Entity를 Domain으로 변환합니다")
        void toDomain_WithTargets_ReturnsValidDomainWithTargets() {
            // given
            DiscountPolicyJpaEntity entity = DiscountPolicyJpaEntityFixtures.activeRateEntity(1L);
            DiscountTargetJpaEntity targetEntity =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(1L);
            List<DiscountTargetJpaEntity> targets = List.of(targetEntity);

            // when
            DiscountPolicy domain = mapper.toDomain(entity, targets);

            // then
            assertThat(domain.targets()).hasSize(1);
            DiscountTarget target = domain.targets().get(0);
            assertThat(target.targetType()).isEqualTo(DiscountTargetType.PRODUCT);
            assertThat(target.targetId()).isEqualTo(targetEntity.getTargetId());
            assertThat(target.isActive()).isTrue();
        }

        @Test
        @DisplayName("삭제된 Policy Entity를 Domain으로 변환합니다")
        void toDomain_WithDeletedEntity_ReturnsDeletedDomain() {
            // given
            DiscountPolicyJpaEntity entity = DiscountPolicyJpaEntityFixtures.deletedEntity();
            List<DiscountTargetJpaEntity> targets = List.of();

            // when
            DiscountPolicy domain = mapper.toDomain(entity, targets);

            // then
            assertThat(domain.deletedAt()).isNotNull();
            assertThat(domain.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("sellerId가 없는 ADMIN 정책 Entity를 Domain으로 변환합니다")
        void toDomain_WithNoSellerId_ReturnsDomainWithNullSellerId() {
            // given
            DiscountPolicyJpaEntity entity = DiscountPolicyJpaEntityFixtures.activeRateEntity();
            List<DiscountTargetJpaEntity> targets = List.of();

            // when
            DiscountPolicy domain = mapper.toDomain(entity, targets);

            // then
            assertThat(domain.sellerIdValue()).isNull();
        }
    }

    // ========================================================================
    // 4. toTargetDomain 테스트
    // ========================================================================

    @Nested
    @DisplayName("toTargetDomain 변환 테스트 (DiscountTarget)")
    class ToTargetDomainTest {

        @Test
        @DisplayName("활성 PRODUCT 타겟 Entity를 Domain으로 변환합니다")
        void toTargetDomain_WithActiveProductTarget_ReturnsValidDomain() {
            // given
            DiscountTargetJpaEntity entity =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(1L);

            // when
            DiscountTarget domain = mapper.toTargetDomain(entity);

            // then
            assertThat(domain.targetType()).isEqualTo(DiscountTargetType.PRODUCT);
            assertThat(domain.targetId()).isEqualTo(entity.getTargetId());
            assertThat(domain.isActive()).isTrue();
        }

        @Test
        @DisplayName("비활성 타겟 Entity를 Domain으로 변환합니다")
        void toTargetDomain_WithInactiveTarget_ReturnsInactiveDomain() {
            // given
            DiscountTargetJpaEntity entity =
                    DiscountTargetJpaEntityFixtures.newInactiveProductTarget(1L);

            // when
            DiscountTarget domain = mapper.toTargetDomain(entity);

            // then
            assertThat(domain.isActive()).isFalse();
        }

        @Test
        @DisplayName("BRAND 타겟 Entity를 Domain으로 변환합니다")
        void toTargetDomain_WithBrandTarget_ReturnsBrandTypeDomain() {
            // given
            DiscountTargetJpaEntity entity =
                    DiscountTargetJpaEntityFixtures.newActiveBrandTarget(1L);

            // when
            DiscountTarget domain = mapper.toTargetDomain(entity);

            // then
            assertThat(domain.targetType()).isEqualTo(DiscountTargetType.BRAND);
        }
    }

    // ========================================================================
    // 5. 양방향 변환 테스트
    // ========================================================================

    @Nested
    @DisplayName("양방향 변환 테스트")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("Policy Domain -> Entity -> Domain 변환 시 데이터가 보존됩니다")
        void bidirectional_PolicyConversion_PreservesData() {
            // given
            DiscountPolicy original = DiscountFixtures.activeRatePolicy(1L);

            // when
            DiscountPolicyJpaEntity entity = mapper.toEntity(original);
            DiscountPolicy converted = mapper.toDomain(entity, List.of());

            // then
            assertThat(converted.idValue()).isEqualTo(original.idValue());
            assertThat(converted.nameValue()).isEqualTo(original.nameValue());
            assertThat(converted.discountMethod()).isEqualTo(original.discountMethod());
            assertThat(converted.discountRateValue()).isEqualTo(original.discountRateValue());
            assertThat(converted.isActive()).isEqualTo(original.isActive());
            assertThat(converted.stackingGroup()).isEqualTo(original.stackingGroup());
        }
    }
}
