package com.ryuqq.setof.adapter.out.persistence.refundpolicy.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.entity.RefundPolicyJpaEntity;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundDeliveryCost;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundGuide;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPeriodDays;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.refundpolicy.vo.ReturnAddress;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RefundPolicyPersistenceAdapter 통합 테스트
 *
 * <p>RefundPolicyPersistencePort 구현체의 저장 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("RefundPolicyPersistenceAdapter 통합 테스트")
class RefundPolicyPersistenceAdapterTest extends RepositoryTestSupport {

    @Autowired private RefundPolicyPersistenceAdapter refundPolicyPersistenceAdapter;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 1L;

    @Nested
    @DisplayName("persist 메서드")
    class Persist {

        @Test
        @DisplayName("성공 - 새 환불 정책을 저장하고 ID를 반환한다")
        void persist_newRefundPolicy_savesAndReturnsId() {
            // Given
            RefundPolicy newPolicy = createNewRefundPolicy();

            // When
            RefundPolicyId savedId = refundPolicyPersistenceAdapter.persist(newPolicy);
            flushAndClear();

            // Then
            assertThat(savedId).isNotNull();
            assertThat(savedId.value()).isNotNull();

            RefundPolicyJpaEntity found = find(RefundPolicyJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getPolicyName()).isEqualTo("기본 환불");
            assertThat(found.getReturnAddressLine1()).isEqualTo("서울시 강남구 테헤란로 123");
            assertThat(found.getIsDefault()).isTrue();
        }

        @Test
        @DisplayName("성공 - 기존 환불 정책을 수정한다")
        void persist_existingRefundPolicy_updates() {
            // Given
            RefundPolicyJpaEntity existingEntity =
                    persistAndFlush(
                            RefundPolicyJpaEntity.of(
                                    null,
                                    TEST_SELLER_ID,
                                    "기존 환불",
                                    "서울시 강남구 테헤란로 123",
                                    null,
                                    "06234",
                                    7,
                                    3000,
                                    null,
                                    true,
                                    1,
                                    NOW,
                                    NOW,
                                    null));
            flushAndClear();

            RefundPolicy updatedPolicy = createRefundPolicyWithId(existingEntity.getId());

            // When
            RefundPolicyId savedId = refundPolicyPersistenceAdapter.persist(updatedPolicy);
            flushAndClear();

            // Then
            assertThat(savedId.value()).isEqualTo(existingEntity.getId());
            RefundPolicyJpaEntity found = find(RefundPolicyJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
        }

        @Test
        @DisplayName("성공 - 기본 정책이 아닌 환불 정책을 저장한다")
        void persist_nonDefaultRefundPolicy_saves() {
            // Given
            RefundPolicy nonDefaultPolicy = createNonDefaultRefundPolicy();

            // When
            RefundPolicyId savedId = refundPolicyPersistenceAdapter.persist(nonDefaultPolicy);
            flushAndClear();

            // Then
            RefundPolicyJpaEntity found = find(RefundPolicyJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getIsDefault()).isFalse();
            assertThat(found.getPolicyName()).isEqualTo("추가 환불");
        }

        @Test
        @DisplayName("성공 - 삭제된 환불 정책을 저장한다 (soft delete)")
        void persist_deletedRefundPolicy_savesSoftDeleted() {
            // Given
            RefundPolicy deletedPolicy = createDeletedRefundPolicy();

            // When
            RefundPolicyId savedId = refundPolicyPersistenceAdapter.persist(deletedPolicy);
            flushAndClear();

            // Then
            RefundPolicyJpaEntity found = find(RefundPolicyJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getDeletedAt()).isNotNull();
        }
    }

    // ========== Helper Methods ==========

    private RefundPolicy createNewRefundPolicy() {
        return RefundPolicy.create(
                TEST_SELLER_ID,
                PolicyName.of("기본 환불"),
                ReturnAddress.of("서울시 강남구 테헤란로 123", "101동 1001호", "06234"),
                RefundPeriodDays.of(7),
                RefundDeliveryCost.of(3000),
                RefundGuide.of("상품 수령 후 7일 이내 환불 가능"),
                true,
                1,
                NOW);
    }

    private RefundPolicy createRefundPolicyWithId(Long id) {
        return RefundPolicy.reconstitute(
                RefundPolicyId.of(id),
                TEST_SELLER_ID,
                PolicyName.of("수정된 환불"),
                ReturnAddress.of("서울시 강남구 테헤란로 123", "101동 1001호", "06234"),
                RefundPeriodDays.of(7),
                RefundDeliveryCost.of(3000),
                RefundGuide.of("수정된 환불 안내"),
                true,
                1,
                NOW,
                NOW,
                null);
    }

    private RefundPolicy createNonDefaultRefundPolicy() {
        return RefundPolicy.create(
                TEST_SELLER_ID,
                PolicyName.of("추가 환불"),
                ReturnAddress.of("서울시 서초구 서초대로 456", null, "06789"),
                RefundPeriodDays.of(14),
                RefundDeliveryCost.of(5000),
                null,
                false,
                2,
                NOW);
    }

    private RefundPolicy createDeletedRefundPolicy() {
        return RefundPolicy.reconstitute(
                null,
                TEST_SELLER_ID,
                PolicyName.of("삭제된 환불"),
                ReturnAddress.of("서울시 마포구 마포대로 789", null, "04001"),
                RefundPeriodDays.of(7),
                RefundDeliveryCost.of(3000),
                null,
                false,
                3,
                NOW,
                NOW,
                NOW);
    }
}
