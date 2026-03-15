package com.ryuqq.setof.adapter.out.persistence.discountpolicy.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountTargetJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.mapper.DiscountPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountTargetQueryDslRepository;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * DiscountTargetQueryAdapterTest - 할인 적용 대상 Query Adapter 단위 테스트.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("DiscountTargetQueryAdapter 단위 테스트")
class DiscountTargetQueryAdapterTest {

    @Mock private DiscountTargetQueryDslRepository queryDslRepository;

    @Mock private DiscountPolicyJpaEntityMapper mapper;

    @InjectMocks private DiscountTargetQueryAdapter queryAdapter;

    // ========================================================================
    // 1. findByPolicyId 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByPolicyId 메서드 테스트")
    class FindByPolicyIdTest {

        @Test
        @DisplayName("정책 ID로 타겟 목록을 반환합니다")
        void findByPolicyId_WithValidPolicyId_ReturnsTargetList() {
            // given
            long policyId = 1L;
            DiscountTargetJpaEntity entity =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(policyId);
            DiscountTarget domain = DiscountFixtures.activeTarget(1L);

            given(queryDslRepository.findByPolicyId(policyId)).willReturn(List.of(entity));
            given(mapper.toTargetDomain(entity)).willReturn(domain);

            // when
            List<DiscountTarget> result = queryAdapter.findByPolicyId(policyId);

            // then
            assertThat(result).hasSize(1);
            assertThat(result).contains(domain);
            then(queryDslRepository).should().findByPolicyId(policyId);
        }

        @Test
        @DisplayName("정책에 타겟이 없으면 빈 목록을 반환합니다")
        void findByPolicyId_WithNoTargets_ReturnsEmptyList() {
            // given
            long policyId = 1L;
            given(queryDslRepository.findByPolicyId(policyId)).willReturn(List.of());

            // when
            List<DiscountTarget> result = queryAdapter.findByPolicyId(policyId);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 2. findByPolicyIds 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByPolicyIds 메서드 테스트")
    class FindByPolicyIdsTest {

        @Test
        @DisplayName("여러 정책 ID로 타겟 목록을 반환합니다")
        void findByPolicyIds_WithMultiplePolicyIds_ReturnsAllTargets() {
            // given
            List<Long> policyIds = List.of(1L, 2L);
            DiscountTargetJpaEntity entity1 =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(1L);
            DiscountTargetJpaEntity entity2 =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(2L);
            DiscountTarget domain1 = DiscountFixtures.activeTarget(1L);
            DiscountTarget domain2 = DiscountFixtures.activeTarget(2L);

            given(queryDslRepository.findByPolicyIds(policyIds))
                    .willReturn(List.of(entity1, entity2));
            given(mapper.toTargetDomain(entity1)).willReturn(domain1);
            given(mapper.toTargetDomain(entity2)).willReturn(domain2);

            // when
            List<DiscountTarget> result = queryAdapter.findByPolicyIds(policyIds);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).contains(domain1, domain2);
        }

        @Test
        @DisplayName("빈 정책 ID 목록으로 조회 시 빈 목록을 반환합니다")
        void findByPolicyIds_WithEmptyPolicyIds_ReturnsEmptyList() {
            // given
            List<Long> emptyIds = List.of();
            given(queryDslRepository.findByPolicyIds(emptyIds)).willReturn(List.of());

            // when
            List<DiscountTarget> result = queryAdapter.findByPolicyIds(emptyIds);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 3. countByPolicyId 테스트
    // ========================================================================

    @Nested
    @DisplayName("countByPolicyId 메서드 테스트")
    class CountByPolicyIdTest {

        @Test
        @DisplayName("정책 ID로 활성 타겟 수를 반환합니다")
        void countByPolicyId_WithValidPolicyId_ReturnsCount() {
            // given
            long policyId = 1L;
            given(queryDslRepository.countByPolicyId(policyId)).willReturn(3L);

            // when
            long result = queryAdapter.countByPolicyId(policyId);

            // then
            assertThat(result).isEqualTo(3L);
            then(queryDslRepository).should().countByPolicyId(policyId);
        }

        @Test
        @DisplayName("타겟이 없으면 0을 반환합니다")
        void countByPolicyId_WithNoTargets_ReturnsZero() {
            // given
            long policyId = 1L;
            given(queryDslRepository.countByPolicyId(policyId)).willReturn(0L);

            // when
            long result = queryAdapter.countByPolicyId(policyId);

            // then
            assertThat(result).isZero();
        }
    }
}
