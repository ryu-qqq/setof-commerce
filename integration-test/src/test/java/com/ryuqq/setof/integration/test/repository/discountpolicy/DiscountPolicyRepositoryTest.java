package com.ryuqq.setof.integration.test.repository.discountpolicy;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountTargetJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountPolicyJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountTargetJpaRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DiscountPolicy JpaRepository 통합 테스트.
 *
 * <p>JPA 기본 저장/조회 동작을 검증합니다.
 */
@Tag(TestTags.DISCOUNT)
@DisplayName("할인 정책 JPA Repository 테스트")
class DiscountPolicyRepositoryTest extends RepositoryTestBase {

    @Autowired private DiscountPolicyJpaRepository discountPolicyJpaRepository;
    @Autowired private DiscountTargetJpaRepository discountTargetJpaRepository;

    // ========================================================================
    // 1. DiscountPolicyJpaRepository save 테스트
    // ========================================================================

    @Nested
    @DisplayName("DiscountPolicyJpaRepository save 테스트")
    class DiscountPolicySaveTest {

        @Test
        @DisplayName("할인 정책을 저장하고 ID를 반환합니다")
        void save_NewPolicy_AssignsId() {
            // given
            DiscountPolicyJpaEntity entity = DiscountPolicyJpaEntityFixtures.newActiveRateEntity();

            // when
            DiscountPolicyJpaEntity saved = discountPolicyJpaRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getName()).isEqualTo(entity.getName());
        }

        @Test
        @DisplayName("할인 정책을 저장 후 ID로 조회할 수 있습니다")
        void save_NewPolicy_CanFindById() {
            // given
            DiscountPolicyJpaEntity entity = DiscountPolicyJpaEntityFixtures.newActiveRateEntity();

            // when
            DiscountPolicyJpaEntity saved = discountPolicyJpaRepository.save(entity);
            flushAndClear();

            // then
            Optional<DiscountPolicyJpaEntity> found =
                    discountPolicyJpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getName()).isEqualTo(entity.getName());
            assertThat(found.get().getDiscountMethod())
                    .isEqualTo(DiscountPolicyJpaEntity.DiscountMethod.RATE);
            assertThat(found.get().isActive()).isTrue();
        }

        @Test
        @DisplayName("여러 할인 정책을 일괄 저장합니다")
        void saveAll_MultipleEntities_SavesAll() {
            // given
            DiscountPolicyJpaEntity entity1 =
                    DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("정책1");
            DiscountPolicyJpaEntity entity2 =
                    DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("정책2");
            DiscountPolicyJpaEntity entity3 =
                    DiscountPolicyJpaEntityFixtures.newActiveFixedAmountEntity();

            // when
            List<DiscountPolicyJpaEntity> saved =
                    discountPolicyJpaRepository.saveAll(List.of(entity1, entity2, entity3));
            flushAndClear();

            // then
            assertThat(saved).hasSize(3);
            assertThat(saved).allMatch(e -> e.getId() != null);
        }

        @Test
        @DisplayName("FIXED_AMOUNT 할인 정책을 저장합니다")
        void save_FixedAmountPolicy_SavesCorrectly() {
            // given
            DiscountPolicyJpaEntity entity =
                    DiscountPolicyJpaEntityFixtures.newActiveFixedAmountEntity();

            // when
            DiscountPolicyJpaEntity saved = discountPolicyJpaRepository.save(entity);
            flushAndClear();

            // then
            Optional<DiscountPolicyJpaEntity> found =
                    discountPolicyJpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getDiscountMethod())
                    .isEqualTo(DiscountPolicyJpaEntity.DiscountMethod.FIXED_AMOUNT);
            assertThat(found.get().getDiscountAmount()).isNotNull();
            assertThat(found.get().getDiscountRate()).isNull();
        }

        @Test
        @DisplayName("SELLER 발행자 타입의 할인 정책을 저장합니다")
        void save_SellerPolicy_SavesWithSellerId() {
            // given
            DiscountPolicyJpaEntity entity =
                    DiscountPolicyJpaEntityFixtures.newActiveSellerEntity();

            // when
            DiscountPolicyJpaEntity saved = discountPolicyJpaRepository.save(entity);
            flushAndClear();

            // then
            Optional<DiscountPolicyJpaEntity> found =
                    discountPolicyJpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getSellerId()).isNotNull();
            assertThat(found.get().getPublisherType())
                    .isEqualTo(DiscountPolicyJpaEntity.PublisherType.SELLER);
        }

        @Test
        @DisplayName("비활성 할인 정책을 저장합니다")
        void save_InactivePolicy_SavesWithActiveFalse() {
            // given
            DiscountPolicyJpaEntity entity =
                    DiscountPolicyJpaEntityFixtures.newInactiveRateEntity();

            // when
            DiscountPolicyJpaEntity saved = discountPolicyJpaRepository.save(entity);
            flushAndClear();

            // then
            Optional<DiscountPolicyJpaEntity> found =
                    discountPolicyJpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().isActive()).isFalse();
        }
    }

    // ========================================================================
    // 2. DiscountTargetJpaRepository save 테스트
    // ========================================================================

    @Nested
    @DisplayName("DiscountTargetJpaRepository save 테스트")
    class DiscountTargetSaveTest {

        @Test
        @DisplayName("할인 타겟을 저장하고 ID를 반환합니다")
        void save_NewTarget_AssignsId() {
            // given
            DiscountPolicyJpaEntity policy =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            flushAndClear();

            DiscountTargetJpaEntity targetEntity =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(policy.getId());

            // when
            DiscountTargetJpaEntity saved = discountTargetJpaRepository.save(targetEntity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getDiscountPolicyId()).isEqualTo(policy.getId());
            assertThat(saved.isActive()).isTrue();
        }

        @Test
        @DisplayName("여러 타입의 할인 타겟을 일괄 저장합니다")
        void saveAll_MultipleTargets_SavesAll() {
            // given
            DiscountPolicyJpaEntity policy =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            flushAndClear();

            DiscountTargetJpaEntity productTarget =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(policy.getId());
            DiscountTargetJpaEntity sellerTarget =
                    DiscountTargetJpaEntityFixtures.newActiveSellerTarget(policy.getId());
            DiscountTargetJpaEntity brandTarget =
                    DiscountTargetJpaEntityFixtures.newActiveBrandTarget(policy.getId());

            // when
            List<DiscountTargetJpaEntity> saved =
                    discountTargetJpaRepository.saveAll(
                            List.of(productTarget, sellerTarget, brandTarget));
            flushAndClear();

            // then
            assertThat(saved).hasSize(3);
            assertThat(saved).allMatch(e -> e.getId() != null);
        }

        @Test
        @DisplayName("비활성 타겟을 저장합니다")
        void save_InactiveTarget_SavesWithActiveFalse() {
            // given
            DiscountPolicyJpaEntity policy =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            flushAndClear();

            DiscountTargetJpaEntity targetEntity =
                    DiscountTargetJpaEntityFixtures.newInactiveProductTarget(policy.getId());

            // when
            DiscountTargetJpaEntity saved = discountTargetJpaRepository.save(targetEntity);
            flushAndClear();

            // then
            Optional<DiscountTargetJpaEntity> found =
                    discountTargetJpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().isActive()).isFalse();
        }
    }
}
