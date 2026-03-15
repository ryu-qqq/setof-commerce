package com.ryuqq.setof.integration.test.repository.discountpolicy;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountTargetJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountPolicyJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountPolicyQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountTargetJpaRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DiscountPolicy QueryDSL Repository 통합 테스트.
 *
 * <p>QueryDSL 기반의 복잡한 쿼리 동작을 검증합니다. Soft Delete 필터링, 타겟 조인, 페이징을 테스트합니다.
 */
@Tag(TestTags.DISCOUNT)
@DisplayName("할인 정책 QueryDSL Repository 테스트")
class DiscountPolicyQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private DiscountPolicyJpaRepository jpaRepository;
    @Autowired private DiscountTargetJpaRepository targetJpaRepository;
    @Autowired private DiscountPolicyQueryDslRepository queryDslRepository;

    // ========================================================================
    // 1. findById 테스트
    // ========================================================================

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 할인 정책 조회 성공")
        void shouldFindById() {
            // given
            DiscountPolicyJpaEntity entity =
                    jpaRepository.save(DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            flushAndClear();

            // when
            Optional<DiscountPolicyJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getId()).isEqualTo(entity.getId());
            assertThat(found.get().isActive()).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<DiscountPolicyJpaEntity> found = queryDslRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    // ========================================================================
    // 2. findAllByIds 테스트
    // ========================================================================

    @Nested
    @DisplayName("findAllByIds 테스트")
    class FindAllByIdsTest {

        @Test
        @DisplayName("ID 목록으로 할인 정책 목록 조회 성공")
        void shouldFindAllByIds() {
            // given
            DiscountPolicyJpaEntity entity1 =
                    jpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("정책1"));
            DiscountPolicyJpaEntity entity2 =
                    jpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("정책2"));
            DiscountPolicyJpaEntity entity3 =
                    jpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("정책3"));
            flushAndClear();

            // when
            List<DiscountPolicyJpaEntity> found =
                    queryDslRepository.findAllByIds(List.of(entity1.getId(), entity3.getId()));

            // then
            assertThat(found).hasSize(2);
            assertThat(found)
                    .extracting(DiscountPolicyJpaEntity::getId)
                    .containsExactlyInAnyOrder(entity1.getId(), entity3.getId());
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회시 빈 목록 반환")
        void shouldReturnEmptyListWhenEmptyIds() {
            // when
            List<DiscountPolicyJpaEntity> found = queryDslRepository.findAllByIds(List.of());

            // then
            assertThat(found).isEmpty();
        }
    }

    // ========================================================================
    // 3. findActivePolicyIdsByTarget 테스트
    // ========================================================================

    @Nested
    @DisplayName("findActivePolicyIdsByTarget 테스트")
    class FindActivePolicyIdsByTargetTest {

        private DiscountPolicyJpaEntity activePolicy;
        private DiscountPolicyJpaEntity inactivePolicy;
        private static final long TARGET_ID = 101L;

        @BeforeEach
        void setUp() {
            activePolicy =
                    jpaRepository.save(DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            inactivePolicy =
                    jpaRepository.save(DiscountPolicyJpaEntityFixtures.newInactiveRateEntity());

            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(
                            activePolicy.getId(), TARGET_ID));
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(
                            inactivePolicy.getId(), TARGET_ID));
            flushAndClear();
        }

        @Test
        @DisplayName("활성 정책의 타겟 ID만 조회합니다")
        void shouldReturnOnlyActivePolicyIds() {
            // when
            List<Long> policyIds =
                    queryDslRepository.findActivePolicyIdsByTarget(
                            DiscountTargetJpaEntity.TargetType.PRODUCT,
                            TARGET_ID,
                            java.time.Instant.now());

            // then
            assertThat(policyIds).contains(activePolicy.getId());
            assertThat(policyIds).doesNotContain(inactivePolicy.getId());
        }

        @Test
        @DisplayName("다른 타겟 ID로 조회 시 빈 목록을 반환합니다")
        void shouldReturnEmptyForDifferentTargetId() {
            // when
            List<Long> policyIds =
                    queryDslRepository.findActivePolicyIdsByTarget(
                            DiscountTargetJpaEntity.TargetType.PRODUCT,
                            9999L,
                            java.time.Instant.now());

            // then
            assertThat(policyIds).isEmpty();
        }

        @Test
        @DisplayName("타겟 타입이 다른 경우 조회되지 않습니다")
        void shouldNotReturnForDifferentTargetType() {
            // when
            List<Long> policyIds =
                    queryDslRepository.findActivePolicyIdsByTarget(
                            DiscountTargetJpaEntity.TargetType.BRAND,
                            TARGET_ID,
                            java.time.Instant.now());

            // then
            assertThat(policyIds).isEmpty();
        }
    }

    // ========================================================================
    // 4. findByCriteria 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByCriteria 테스트")
    class FindByCriteriaTest {

        private DiscountPolicyJpaEntity activePolicy1;
        private DiscountPolicyJpaEntity activePolicy2;
        private DiscountPolicyJpaEntity activePolicy3;
        private DiscountPolicyJpaEntity inactivePolicy;

        @BeforeEach
        void setUp() {
            activePolicy1 =
                    jpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("할인정책A"));
            activePolicy2 =
                    jpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("할인정책B"));
            activePolicy3 =
                    jpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("할인정책C"));
            inactivePolicy =
                    jpaRepository.save(DiscountPolicyJpaEntityFixtures.newInactiveRateEntity());
            flushAndClear();
        }

        @Test
        @DisplayName("삭제되지 않은 모든 정책 조회 (activeOnly=false)")
        void shouldFindAllExcludingDeleted() {
            // when
            List<DiscountPolicyJpaEntity> result =
                    queryDslRepository.findByCriteria(
                            null,
                            null,
                            null,
                            null,
                            false,
                            com.ryuqq.setof.domain.discount.query.DiscountPolicySortKey.CREATED_AT,
                            false,
                            0,
                            100);

            // then
            assertThat(result).hasSizeGreaterThanOrEqualTo(4);
        }

        @Test
        @DisplayName("활성 정책만 조회 (activeOnly=true)")
        void shouldFindOnlyActivePolicies() {
            // when
            List<DiscountPolicyJpaEntity> result =
                    queryDslRepository.findByCriteria(
                            null,
                            null,
                            null,
                            null,
                            true,
                            com.ryuqq.setof.domain.discount.query.DiscountPolicySortKey.CREATED_AT,
                            false,
                            0,
                            100);

            // then
            assertThat(result).allMatch(DiscountPolicyJpaEntity::isActive);
            assertThat(result)
                    .extracting(DiscountPolicyJpaEntity::getId)
                    .doesNotContain(inactivePolicy.getId());
        }

        @Test
        @DisplayName("ADMIN 발행자 타입 필터")
        void shouldFilterByPublisherType() {
            // given
            jpaRepository.save(DiscountPolicyJpaEntityFixtures.newActiveSellerEntity());
            flushAndClear();

            // when
            List<DiscountPolicyJpaEntity> result =
                    queryDslRepository.findByCriteria(
                            null,
                            DiscountPolicyJpaEntity.PublisherType.ADMIN,
                            null,
                            null,
                            false,
                            com.ryuqq.setof.domain.discount.query.DiscountPolicySortKey.CREATED_AT,
                            false,
                            0,
                            100);

            // then
            assertThat(result)
                    .allMatch(
                            e ->
                                    e.getPublisherType()
                                            == DiscountPolicyJpaEntity.PublisherType.ADMIN);
        }

        @Nested
        @DisplayName("정렬 테스트")
        class SortingTest {

            @Test
            @DisplayName("등록일시 내림차순 정렬")
            void shouldSortByCreatedAtDesc() {
                // when
                List<DiscountPolicyJpaEntity> result =
                        queryDslRepository.findByCriteria(
                                null,
                                null,
                                null,
                                null,
                                false,
                                com.ryuqq.setof.domain.discount.query.DiscountPolicySortKey
                                        .CREATED_AT,
                                false,
                                0,
                                100);

                // then
                assertThat(result).isNotEmpty();
                for (int i = 0; i < result.size() - 1; i++) {
                    assertThat(result.get(i).getCreatedAt())
                            .isAfterOrEqualTo(result.get(i + 1).getCreatedAt());
                }
            }

            @Test
            @DisplayName("이름 오름차순 정렬")
            void shouldSortByNameAsc() {
                // when
                List<DiscountPolicyJpaEntity> result =
                        queryDslRepository.findByCriteria(
                                null,
                                null,
                                null,
                                null,
                                false,
                                com.ryuqq.setof.domain.discount.query.DiscountPolicySortKey.NAME,
                                true,
                                0,
                                100);

                // then
                assertThat(result).isNotEmpty();
                for (int i = 0; i < result.size() - 1; i++) {
                    assertThat(result.get(i).getName())
                            .isLessThanOrEqualTo(result.get(i + 1).getName());
                }
            }
        }

        @Nested
        @DisplayName("페이징 테스트")
        class PagingTest {

            @Test
            @DisplayName("첫 페이지 조회")
            void shouldReturnFirstPage() {
                // when
                List<DiscountPolicyJpaEntity> result =
                        queryDslRepository.findByCriteria(
                                null,
                                null,
                                null,
                                null,
                                false,
                                com.ryuqq.setof.domain.discount.query.DiscountPolicySortKey
                                        .CREATED_AT,
                                false,
                                0,
                                2);

                // then
                assertThat(result).hasSize(2);
            }

            @Test
            @DisplayName("두 번째 페이지 조회")
            void shouldReturnSecondPage() {
                // when
                List<DiscountPolicyJpaEntity> result =
                        queryDslRepository.findByCriteria(
                                null,
                                null,
                                null,
                                null,
                                false,
                                com.ryuqq.setof.domain.discount.query.DiscountPolicySortKey
                                        .CREATED_AT,
                                false,
                                2,
                                2);

                // then
                assertThat(result).hasSizeLessThanOrEqualTo(2);
            }
        }
    }

    // ========================================================================
    // 5. countByCriteria 테스트
    // ========================================================================

    @Nested
    @DisplayName("countByCriteria 테스트")
    class CountByCriteriaTest {

        @BeforeEach
        void setUp() {
            jpaRepository.save(
                    DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("카운트정책1"));
            jpaRepository.save(
                    DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("카운트정책2"));
            jpaRepository.save(DiscountPolicyJpaEntityFixtures.newInactiveRateEntity());
            flushAndClear();
        }

        @Test
        @DisplayName("활성 정책 카운트 조회")
        void shouldCountActivePolicies() {
            // when
            long count = queryDslRepository.countByCriteria(null, null, null, null, true);

            // then
            assertThat(count).isGreaterThanOrEqualTo(2);
        }

        @Test
        @DisplayName("전체 정책 카운트 조회 (삭제 제외)")
        void shouldCountAllPoliciesExcludingDeleted() {
            // when
            long count = queryDslRepository.countByCriteria(null, null, null, null, false);

            // then
            assertThat(count).isGreaterThanOrEqualTo(3);
        }
    }
}
