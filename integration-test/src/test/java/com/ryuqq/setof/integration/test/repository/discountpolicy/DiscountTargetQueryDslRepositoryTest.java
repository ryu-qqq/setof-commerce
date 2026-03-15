package com.ryuqq.setof.integration.test.repository.discountpolicy;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountTargetJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountPolicyJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountTargetJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountTargetQueryDslRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DiscountTarget QueryDSL Repository 통합 테스트.
 *
 * <p>QueryDSL 기반의 할인 타겟 조회 동작을 검증합니다.
 */
@Tag(TestTags.DISCOUNT)
@DisplayName("할인 타겟 QueryDSL Repository 테스트")
class DiscountTargetQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private DiscountPolicyJpaRepository policyJpaRepository;
    @Autowired private DiscountTargetJpaRepository targetJpaRepository;
    @Autowired private DiscountTargetQueryDslRepository queryDslRepository;

    private DiscountPolicyJpaEntity savedPolicy;

    @BeforeEach
    void setUpPolicy() {
        savedPolicy =
                policyJpaRepository.save(DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
        flushAndClear();
    }

    // ========================================================================
    // 1. findByPolicyId 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByPolicyId 테스트")
    class FindByPolicyIdTest {

        @Test
        @DisplayName("정책 ID로 타겟 목록 조회 성공")
        void shouldFindByPolicyId() {
            // given
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(savedPolicy.getId()));
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveBrandTarget(savedPolicy.getId()));
            flushAndClear();

            // when
            List<DiscountTargetJpaEntity> found =
                    queryDslRepository.findByPolicyId(savedPolicy.getId());

            // then
            assertThat(found).hasSize(2);
            assertThat(found).allMatch(t -> t.getDiscountPolicyId() == savedPolicy.getId());
        }

        @Test
        @DisplayName("타겟이 없는 정책 ID로 조회시 빈 목록 반환")
        void shouldReturnEmptyWhenNoTargets() {
            // when
            List<DiscountTargetJpaEntity> found = queryDslRepository.findByPolicyId(999999L);

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("비활성 타겟도 함께 조회됩니다")
        void shouldIncludeInactiveTargets() {
            // given
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(savedPolicy.getId()));
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newInactiveProductTarget(savedPolicy.getId()));
            flushAndClear();

            // when
            List<DiscountTargetJpaEntity> found =
                    queryDslRepository.findByPolicyId(savedPolicy.getId());

            // then
            assertThat(found).hasSize(2);
        }
    }

    // ========================================================================
    // 2. findByPolicyIds 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByPolicyIds 테스트")
    class FindByPolicyIdsTest {

        @Test
        @DisplayName("여러 정책 ID로 타겟 목록 조회 성공")
        void shouldFindByPolicyIds() {
            // given
            DiscountPolicyJpaEntity policy2 =
                    policyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("다른정책"));

            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(savedPolicy.getId()));
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveBrandTarget(policy2.getId()));
            flushAndClear();

            // when
            List<DiscountTargetJpaEntity> found =
                    queryDslRepository.findByPolicyIds(
                            List.of(savedPolicy.getId(), policy2.getId()));

            // then
            assertThat(found).hasSize(2);
            assertThat(found)
                    .extracting(DiscountTargetJpaEntity::getDiscountPolicyId)
                    .containsExactlyInAnyOrder(savedPolicy.getId(), policy2.getId());
        }

        @Test
        @DisplayName("빈 정책 ID 목록으로 조회시 빈 목록 반환")
        void shouldReturnEmptyWhenEmptyPolicyIds() {
            // when
            List<DiscountTargetJpaEntity> found = queryDslRepository.findByPolicyIds(List.of());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("null 정책 ID 목록으로 조회시 빈 목록 반환")
        void shouldReturnEmptyWhenNullPolicyIds() {
            // when
            List<DiscountTargetJpaEntity> found = queryDslRepository.findByPolicyIds(null);

            // then
            assertThat(found).isEmpty();
        }
    }

    // ========================================================================
    // 3. countByPolicyId 테스트
    // ========================================================================

    @Nested
    @DisplayName("countByPolicyId 테스트")
    class CountByPolicyIdTest {

        @Test
        @DisplayName("정책 ID별 활성 타겟 수 조회")
        void shouldCountActiveTargets() {
            // given
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(savedPolicy.getId()));
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveBrandTarget(savedPolicy.getId()));
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newInactiveProductTarget(savedPolicy.getId()));
            flushAndClear();

            // when
            long count = queryDslRepository.countByPolicyId(savedPolicy.getId());

            // then
            assertThat(count).isEqualTo(2);
        }

        @Test
        @DisplayName("타겟이 없는 정책의 카운트는 0입니다")
        void shouldReturnZeroWhenNoTargets() {
            // when
            long count = queryDslRepository.countByPolicyId(999999L);

            // then
            assertThat(count).isZero();
        }

        @Test
        @DisplayName("비활성 타겟은 카운트에 포함되지 않습니다")
        void shouldNotCountInactiveTargets() {
            // given
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newInactiveProductTarget(savedPolicy.getId()));
            flushAndClear();

            // when
            long count = queryDslRepository.countByPolicyId(savedPolicy.getId());

            // then
            assertThat(count).isZero();
        }
    }

    // ========================================================================
    // 4. findByTargetTypeAndTargetId 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByTargetTypeAndTargetId 테스트")
    class FindByTargetTypeAndTargetIdTest {

        private static final long TARGET_ID = 101L;

        @Test
        @DisplayName("타겟 타입과 ID로 활성 타겟 조회 성공")
        void shouldFindByTargetTypeAndTargetId() {
            // given
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(
                            savedPolicy.getId(), TARGET_ID));
            flushAndClear();

            // when
            List<DiscountTargetJpaEntity> found =
                    queryDslRepository.findByTargetTypeAndTargetId(
                            DiscountTargetJpaEntity.TargetType.PRODUCT, TARGET_ID);

            // then
            assertThat(found).hasSize(1);
            assertThat(found.get(0).getTargetType())
                    .isEqualTo(DiscountTargetJpaEntity.TargetType.PRODUCT);
            assertThat(found.get(0).getTargetId()).isEqualTo(TARGET_ID);
        }

        @Test
        @DisplayName("비활성 타겟은 조회되지 않습니다")
        void shouldNotFindInactiveTargets() {
            // given
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newInactiveProductTarget(savedPolicy.getId()));
            flushAndClear();

            // when
            List<DiscountTargetJpaEntity> found =
                    queryDslRepository.findByTargetTypeAndTargetId(
                            DiscountTargetJpaEntity.TargetType.PRODUCT,
                            DiscountTargetJpaEntityFixtures.DEFAULT_TARGET_ID);

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("다른 타겟 타입으로 조회시 조회되지 않습니다")
        void shouldNotFindWithDifferentTargetType() {
            // given
            targetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(
                            savedPolicy.getId(), TARGET_ID));
            flushAndClear();

            // when
            List<DiscountTargetJpaEntity> found =
                    queryDslRepository.findByTargetTypeAndTargetId(
                            DiscountTargetJpaEntity.TargetType.BRAND, TARGET_ID);

            // then
            assertThat(found).isEmpty();
        }
    }
}
