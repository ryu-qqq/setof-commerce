package com.ryuqq.setof.adapter.out.persistence.shippingpolicy.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.entity.ShippingPolicyJpaEntity;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryCost;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryGuide;
import com.ryuqq.setof.domain.shippingpolicy.vo.DisplayOrder;
import com.ryuqq.setof.domain.shippingpolicy.vo.FreeShippingThreshold;
import com.ryuqq.setof.domain.shippingpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ShippingPolicyPersistenceAdapter 통합 테스트
 *
 * <p>ShippingPolicyPersistencePort 구현체의 저장 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("ShippingPolicyPersistenceAdapter 통합 테스트")
class ShippingPolicyPersistenceAdapterTest extends RepositoryTestSupport {

    @Autowired private ShippingPolicyPersistenceAdapter shippingPolicyPersistenceAdapter;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 1L;

    @Nested
    @DisplayName("persist 메서드")
    class Persist {

        @Test
        @DisplayName("성공 - 새 배송 정책을 저장하고 ID를 반환한다")
        void persist_newShippingPolicy_savesAndReturnsId() {
            // Given
            ShippingPolicy newPolicy = createNewShippingPolicy();

            // When
            ShippingPolicyId savedId = shippingPolicyPersistenceAdapter.persist(newPolicy);
            flushAndClear();

            // Then
            assertThat(savedId).isNotNull();
            assertThat(savedId.value()).isNotNull();

            ShippingPolicyJpaEntity found = find(ShippingPolicyJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getPolicyName()).isEqualTo("기본 배송");
            assertThat(found.getDefaultDeliveryCost()).isEqualTo(3000);
            assertThat(found.getIsDefault()).isTrue();
        }

        @Test
        @DisplayName("성공 - 기존 배송 정책을 수정한다")
        void persist_existingShippingPolicy_updates() {
            // Given
            ShippingPolicyJpaEntity existingEntity =
                    persistAndFlush(
                            ShippingPolicyJpaEntity.of(
                                    null,
                                    TEST_SELLER_ID,
                                    "기존 배송",
                                    3000,
                                    50000,
                                    null,
                                    true,
                                    1,
                                    NOW,
                                    NOW,
                                    null));
            flushAndClear();

            ShippingPolicy updatedPolicy = createShippingPolicyWithId(existingEntity.getId());

            // When
            ShippingPolicyId savedId = shippingPolicyPersistenceAdapter.persist(updatedPolicy);
            flushAndClear();

            // Then
            assertThat(savedId.value()).isEqualTo(existingEntity.getId());
            ShippingPolicyJpaEntity found = find(ShippingPolicyJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
        }

        @Test
        @DisplayName("성공 - 기본 정책이 아닌 배송 정책을 저장한다")
        void persist_nonDefaultShippingPolicy_saves() {
            // Given
            ShippingPolicy nonDefaultPolicy = createNonDefaultShippingPolicy();

            // When
            ShippingPolicyId savedId = shippingPolicyPersistenceAdapter.persist(nonDefaultPolicy);
            flushAndClear();

            // Then
            ShippingPolicyJpaEntity found = find(ShippingPolicyJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getIsDefault()).isFalse();
            assertThat(found.getPolicyName()).isEqualTo("추가 배송");
        }

        @Test
        @DisplayName("성공 - 삭제된 배송 정책을 저장한다 (soft delete)")
        void persist_deletedShippingPolicy_savesSoftDeleted() {
            // Given
            ShippingPolicy deletedPolicy = createDeletedShippingPolicy();

            // When
            ShippingPolicyId savedId = shippingPolicyPersistenceAdapter.persist(deletedPolicy);
            flushAndClear();

            // Then
            ShippingPolicyJpaEntity found = find(ShippingPolicyJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getDeletedAt()).isNotNull();
        }
    }

    // ========== Helper Methods ==========

    private ShippingPolicy createNewShippingPolicy() {
        return ShippingPolicy.create(
                TEST_SELLER_ID,
                PolicyName.of("기본 배송"),
                DeliveryCost.of(3000),
                FreeShippingThreshold.of(50000),
                DeliveryGuide.of("평일 1~2일 내 발송"),
                true,
                DisplayOrder.of(1),
                NOW);
    }

    private ShippingPolicy createShippingPolicyWithId(Long id) {
        return ShippingPolicy.reconstitute(
                ShippingPolicyId.of(id),
                TEST_SELLER_ID,
                PolicyName.of("수정된 배송"),
                DeliveryCost.of(3000),
                FreeShippingThreshold.of(50000),
                DeliveryGuide.of("수정된 배송 안내"),
                true,
                DisplayOrder.of(1),
                NOW,
                NOW,
                null);
    }

    private ShippingPolicy createNonDefaultShippingPolicy() {
        return ShippingPolicy.create(
                TEST_SELLER_ID,
                PolicyName.of("추가 배송"),
                DeliveryCost.of(5000),
                null,
                null,
                false,
                DisplayOrder.of(2),
                NOW);
    }

    private ShippingPolicy createDeletedShippingPolicy() {
        return ShippingPolicy.reconstitute(
                null,
                TEST_SELLER_ID,
                PolicyName.of("삭제된 배송"),
                DeliveryCost.of(3000),
                null,
                null,
                false,
                DisplayOrder.of(3),
                NOW,
                NOW,
                NOW);
    }
}
