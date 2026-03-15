package com.ryuqq.setof.adapter.out.persistence.discountpolicy.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountTargetJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.mapper.DiscountPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountPolicyQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountTargetQueryDslRepository;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySearchCriteria;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySortKey;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * DiscountPolicyQueryAdapterTest - 할인 정책 Query Adapter 단위 테스트.
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
@DisplayName("DiscountPolicyQueryAdapter 단위 테스트")
class DiscountPolicyQueryAdapterTest {

    @Mock private DiscountPolicyQueryDslRepository queryDslRepository;

    @Mock private DiscountTargetQueryDslRepository targetQueryDslRepository;

    @Mock private DiscountPolicyJpaEntityMapper mapper;

    @InjectMocks private DiscountPolicyQueryAdapter queryAdapter;

    // ========================================================================
    // 1. findActiveByTarget 테스트
    // ========================================================================

    @Nested
    @DisplayName("findActiveByTarget 메서드 테스트")
    class FindActiveByTargetTest {

        @Test
        @DisplayName("타겟에 적용 가능한 활성 정책 목록을 반환합니다")
        void findActiveByTarget_WithValidTarget_ReturnsPolicies() {
            // given
            DiscountTargetType targetType = DiscountTargetType.PRODUCT;
            long targetId = 100L;
            List<Long> policyIds = List.of(1L, 2L);

            DiscountPolicyJpaEntity entity1 = DiscountPolicyJpaEntityFixtures.activeRateEntity(1L);
            DiscountPolicyJpaEntity entity2 = DiscountPolicyJpaEntityFixtures.activeRateEntity(2L);
            DiscountTargetJpaEntity targetEntity =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(1L);

            DiscountPolicy domain1 = DiscountFixtures.activeRatePolicy(1L);
            DiscountPolicy domain2 = DiscountFixtures.activeRatePolicy(2L);

            given(
                            queryDslRepository.findActivePolicyIdsByTarget(
                                    ArgumentMatchers.eq(DiscountTargetJpaEntity.TargetType.PRODUCT),
                                    ArgumentMatchers.eq(targetId),
                                    ArgumentMatchers.any()))
                    .willReturn(policyIds);
            given(queryDslRepository.findAllByIds(policyIds)).willReturn(List.of(entity1, entity2));
            given(targetQueryDslRepository.findByPolicyIds(policyIds))
                    .willReturn(List.of(targetEntity));
            given(mapper.toDomain(ArgumentMatchers.eq(entity1), ArgumentMatchers.any()))
                    .willReturn(domain1);
            given(mapper.toDomain(ArgumentMatchers.eq(entity2), ArgumentMatchers.any()))
                    .willReturn(domain2);

            // when
            List<DiscountPolicy> result = queryAdapter.findActiveByTarget(targetType, targetId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).contains(domain1, domain2);
        }

        @Test
        @DisplayName("적용 가능한 정책 ID가 없으면 빈 목록을 반환합니다")
        void findActiveByTarget_WithNoPolicyIds_ReturnsEmptyList() {
            // given
            DiscountTargetType targetType = DiscountTargetType.PRODUCT;
            long targetId = 100L;

            given(
                            queryDslRepository.findActivePolicyIdsByTarget(
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.eq(targetId),
                                    ArgumentMatchers.any()))
                    .willReturn(List.of());

            // when
            List<DiscountPolicy> result = queryAdapter.findActiveByTarget(targetType, targetId);

            // then
            assertThat(result).isEmpty();
            then(queryDslRepository)
                    .should(org.mockito.Mockito.never())
                    .findAllByIds(ArgumentMatchers.any());
        }
    }

    // ========================================================================
    // 2. findById 테스트
    // ========================================================================

    @Nested
    @DisplayName("findById 메서드 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회 시 Domain을 반환합니다")
        void findById_WithExistingId_ReturnsDomain() {
            // given
            long id = 1L;
            DiscountPolicyJpaEntity entity = DiscountPolicyJpaEntityFixtures.activeRateEntity(id);
            DiscountTargetJpaEntity targetEntity =
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(id);
            DiscountPolicy domain = DiscountFixtures.activeRatePolicy(id);

            given(queryDslRepository.findById(id)).willReturn(Optional.of(entity));
            given(targetQueryDslRepository.findByPolicyId(id)).willReturn(List.of(targetEntity));
            given(mapper.toDomain(ArgumentMatchers.eq(entity), ArgumentMatchers.any()))
                    .willReturn(domain);

            // when
            Optional<DiscountPolicy> result = queryAdapter.findById(id);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
            then(queryDslRepository).should().findById(id);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void findById_WithNonExistingId_ReturnsEmpty() {
            // given
            long id = 999L;
            given(queryDslRepository.findById(id)).willReturn(Optional.empty());

            // when
            Optional<DiscountPolicy> result = queryAdapter.findById(id);

            // then
            assertThat(result).isEmpty();
            then(queryDslRepository).should().findById(id);
        }
    }

    // ========================================================================
    // 3. findByCriteria 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByCriteria 메서드 테스트")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 조회 시 Domain 목록을 반환합니다")
        void findByCriteria_WithValidCriteria_ReturnsDomainList() {
            // given
            DiscountPolicySearchCriteria criteria =
                    DiscountPolicySearchCriteria.of(
                            CommonVoFixtures.defaultSellerId(),
                            null,
                            null,
                            null,
                            true,
                            QueryContext.of(
                                    DiscountPolicySortKey.CREATED_AT,
                                    SortDirection.DESC,
                                    PageRequest.of(0, 10)));

            DiscountPolicyJpaEntity entity = DiscountPolicyJpaEntityFixtures.activeRateEntity(1L);
            DiscountPolicy domain = DiscountFixtures.activeRatePolicy(1L);

            given(
                            queryDslRepository.findByCriteria(
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.anyBoolean(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.anyBoolean(),
                                    ArgumentMatchers.anyLong(),
                                    ArgumentMatchers.anyInt()))
                    .willReturn(List.of(entity));
            given(targetQueryDslRepository.findByPolicyIds(List.of(1L))).willReturn(List.of());
            given(mapper.toDomain(ArgumentMatchers.eq(entity), ArgumentMatchers.any()))
                    .willReturn(domain);

            // when
            List<DiscountPolicy> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result).contains(domain);
        }

        @Test
        @DisplayName("결과가 없으면 빈 목록을 반환합니다")
        void findByCriteria_WithNoCriteria_ReturnsEmptyList() {
            // given
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();

            given(
                            queryDslRepository.findByCriteria(
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.anyBoolean(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.anyBoolean(),
                                    ArgumentMatchers.anyLong(),
                                    ArgumentMatchers.anyInt()))
                    .willReturn(List.of());

            // when
            List<DiscountPolicy> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 4. countByCriteria 테스트
    // ========================================================================

    @Nested
    @DisplayName("countByCriteria 메서드 테스트")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 카운트를 반환합니다")
        void countByCriteria_WithValidCriteria_ReturnsCount() {
            // given
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();

            given(
                            queryDslRepository.countByCriteria(
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.anyBoolean()))
                    .willReturn(5L);

            // when
            long result = queryAdapter.countByCriteria(criteria);

            // then
            assertThat(result).isEqualTo(5L);
        }

        @Test
        @DisplayName("결과가 없으면 0을 반환합니다")
        void countByCriteria_WithNoResults_ReturnsZero() {
            // given
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();

            given(
                            queryDslRepository.countByCriteria(
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.any(),
                                    ArgumentMatchers.anyBoolean()))
                    .willReturn(0L);

            // when
            long result = queryAdapter.countByCriteria(criteria);

            // then
            assertThat(result).isZero();
        }
    }
}
