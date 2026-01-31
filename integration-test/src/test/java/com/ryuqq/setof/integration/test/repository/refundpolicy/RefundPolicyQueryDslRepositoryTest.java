package com.ryuqq.setof.integration.test.repository.refundpolicy;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.refundpolicy.RefundPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.entity.RefundPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.repository.RefundPolicyJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.repository.RefundPolicyQueryDslRepository;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.refundpolicy.query.RefundPolicySearchCriteria;
import com.ryuqq.setof.domain.refundpolicy.query.RefundPolicySortKey;
import com.ryuqq.setof.domain.seller.id.SellerId;
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
 * RefundPolicy QueryDSL Repository 통합 테스트.
 *
 * <p>QueryDSL 기반의 복잡한 쿼리 동작을 검증합니다. Soft Delete 필터링, 정렬, 페이징을 테스트합니다.
 */
@Tag(TestTags.REFUND_POLICY)
@DisplayName("환불정책 QueryDSL Repository 테스트")
class RefundPolicyQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private RefundPolicyJpaRepository jpaRepository;
    @Autowired private RefundPolicyQueryDslRepository queryDslRepository;

    private static final Long SELLER_ID = 100L;

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 환불정책 조회 성공")
        void shouldFindById() {
            // given
            RefundPolicyJpaEntity entity =
                    jpaRepository.save(RefundPolicyJpaEntityFixtures.newActiveEntity(SELLER_ID));
            flushAndClear();

            // when
            Optional<RefundPolicyJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getSellerId()).isEqualTo(SELLER_ID);
        }

        @Test
        @DisplayName("삭제된 환불정책은 조회되지 않음")
        void shouldNotFindDeletedPolicy() {
            // given
            RefundPolicyJpaEntity entity =
                    jpaRepository.save(RefundPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();

            // when
            Optional<RefundPolicyJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<RefundPolicyJpaEntity> found = queryDslRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByIds 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 환불정책 목록 조회 성공")
        void shouldFindByIds() {
            // given
            RefundPolicyJpaEntity entity1 =
                    jpaRepository.save(RefundPolicyJpaEntityFixtures.newActiveEntity(SELLER_ID));
            RefundPolicyJpaEntity entity2 =
                    jpaRepository.save(
                            RefundPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    SELLER_ID, "빠른환불정책"));
            RefundPolicyJpaEntity entity3 =
                    jpaRepository.save(
                            RefundPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    SELLER_ID, "프리미엄정책"));
            flushAndClear();

            // when
            List<RefundPolicyJpaEntity> found =
                    queryDslRepository.findByIds(List.of(entity1.getId(), entity3.getId()));

            // then
            assertThat(found).hasSize(2);
            assertThat(found)
                    .extracting(RefundPolicyJpaEntity::getId)
                    .containsExactlyInAnyOrder(entity1.getId(), entity3.getId());
        }

        @Test
        @DisplayName("삭제된 정책은 조회되지 않음")
        void shouldNotFindDeletedPolicies() {
            // given
            RefundPolicyJpaEntity activeEntity =
                    jpaRepository.save(RefundPolicyJpaEntityFixtures.newActiveEntity(SELLER_ID));
            RefundPolicyJpaEntity deletedEntity =
                    jpaRepository.save(RefundPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();

            // when
            List<RefundPolicyJpaEntity> found =
                    queryDslRepository.findByIds(
                            List.of(activeEntity.getId(), deletedEntity.getId()));

            // then
            assertThat(found).hasSize(1);
            assertThat(found.get(0).getId()).isEqualTo(activeEntity.getId());
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회시 빈 목록 반환")
        void shouldReturnEmptyListWhenEmptyIds() {
            // when
            List<RefundPolicyJpaEntity> found = queryDslRepository.findByIds(List.of());

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findDefaultBySellerId 테스트")
    class FindDefaultBySellerIdTest {

        @Test
        @DisplayName("셀러의 기본 환불정책 조회 성공")
        void shouldFindDefaultPolicy() {
            // given
            jpaRepository.save(RefundPolicyJpaEntityFixtures.newDefaultEntity(SELLER_ID));
            jpaRepository.save(
                    RefundPolicyJpaEntityFixtures.newActiveEntityWithName(SELLER_ID, "추가정책"));
            flushAndClear();

            // when
            Optional<RefundPolicyJpaEntity> found =
                    queryDslRepository.findDefaultBySellerId(SELLER_ID);

            // then
            assertThat(found).isPresent();
            assertThat(found.get().isDefaultPolicy()).isTrue();
        }

        @Test
        @DisplayName("기본 정책이 삭제된 경우 조회되지 않음")
        void shouldNotFindDeletedDefaultPolicy() {
            // given
            jpaRepository.save(RefundPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();

            // when
            Optional<RefundPolicyJpaEntity> found =
                    queryDslRepository.findDefaultBySellerId(SELLER_ID);

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("기본 정책이 없는 셀러 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNoDefaultPolicy() {
            // given
            jpaRepository.save(
                    RefundPolicyJpaEntityFixtures.newActiveEntityWithName(SELLER_ID, "추가정책만"));
            flushAndClear();

            // when
            Optional<RefundPolicyJpaEntity> found =
                    queryDslRepository.findDefaultBySellerId(SELLER_ID);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findBySellerIdAndId 테스트")
    class FindBySellerIdAndIdTest {

        @Test
        @DisplayName("셀러 ID와 정책 ID로 조회 성공")
        void shouldFindBySellerIdAndId() {
            // given
            RefundPolicyJpaEntity entity =
                    jpaRepository.save(RefundPolicyJpaEntityFixtures.newActiveEntity(SELLER_ID));
            flushAndClear();

            // when
            Optional<RefundPolicyJpaEntity> found =
                    queryDslRepository.findBySellerIdAndId(SELLER_ID, entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getId()).isEqualTo(entity.getId());
        }

        @Test
        @DisplayName("다른 셀러의 정책은 조회되지 않음")
        void shouldNotFindOtherSellerPolicy() {
            // given
            RefundPolicyJpaEntity entity =
                    jpaRepository.save(RefundPolicyJpaEntityFixtures.newActiveEntity(SELLER_ID));
            flushAndClear();

            // when
            Optional<RefundPolicyJpaEntity> found =
                    queryDslRepository.findBySellerIdAndId(999L, entity.getId());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("삭제된 정책은 조회되지 않음")
        void shouldNotFindDeletedPolicy() {
            // given
            RefundPolicyJpaEntity entity =
                    jpaRepository.save(RefundPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();

            // when
            Optional<RefundPolicyJpaEntity> found =
                    queryDslRepository.findBySellerIdAndId(SELLER_ID, entity.getId());

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByCriteria 테스트")
    class FindByCriteriaTest {

        private RefundPolicyJpaEntity activePolicy1;
        private RefundPolicyJpaEntity activePolicy2;
        private RefundPolicyJpaEntity activePolicy3;
        private RefundPolicyJpaEntity inactivePolicy;
        private RefundPolicyJpaEntity deletedPolicy;

        @BeforeEach
        void setUp() {
            activePolicy1 =
                    jpaRepository.save(
                            RefundPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    SELLER_ID, "기본환불"));
            activePolicy2 =
                    jpaRepository.save(
                            RefundPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    SELLER_ID, "빠른환불"));
            activePolicy3 =
                    jpaRepository.save(
                            RefundPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    SELLER_ID, "프리미엄환불"));
            inactivePolicy =
                    jpaRepository.save(RefundPolicyJpaEntityFixtures.newInactiveEntity(SELLER_ID));
            deletedPolicy =
                    jpaRepository.save(RefundPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();
        }

        @Test
        @DisplayName("셀러별 전체 조회 - 삭제된 항목 제외")
        void shouldFindAllExcludingDeleted() {
            // given
            RefundPolicySearchCriteria criteria =
                    RefundPolicySearchCriteria.defaultCriteria(SellerId.of(SELLER_ID));

            // when
            List<RefundPolicyJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(4);
            assertThat(result)
                    .extracting(RefundPolicyJpaEntity::getId)
                    .doesNotContain(deletedPolicy.getId());
        }

        @Test
        @DisplayName("다른 셀러의 정책은 조회되지 않음")
        void shouldNotFindOtherSellerPolicies() {
            // given
            Long otherSellerId = 999L;
            jpaRepository.save(RefundPolicyJpaEntityFixtures.newActiveEntity(otherSellerId));
            flushAndClear();

            RefundPolicySearchCriteria criteria =
                    RefundPolicySearchCriteria.defaultCriteria(SellerId.of(SELLER_ID));

            // when
            List<RefundPolicyJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(4);
            assertThat(result).allMatch(e -> e.getSellerId().equals(SELLER_ID));
        }

        @Nested
        @DisplayName("정렬 테스트")
        class SortingTest {

            @Test
            @DisplayName("정책명 오름차순 정렬")
            void shouldSortByPolicyNameAsc() {
                // given
                QueryContext<RefundPolicySortKey> queryContext =
                        QueryContext.of(
                                RefundPolicySortKey.POLICY_NAME,
                                SortDirection.ASC,
                                PageRequest.defaultPage(),
                                false);
                RefundPolicySearchCriteria criteria =
                        RefundPolicySearchCriteria.of(SellerId.of(SELLER_ID), queryContext);

                // when
                List<RefundPolicyJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(4);
                // 정책명으로 정렬 확인
                for (int i = 0; i < result.size() - 1; i++) {
                    assertThat(result.get(i).getPolicyName())
                            .isLessThanOrEqualTo(result.get(i + 1).getPolicyName());
                }
            }

            @Test
            @DisplayName("등록일시 내림차순 정렬")
            void shouldSortByCreatedAtDesc() {
                // given
                QueryContext<RefundPolicySortKey> queryContext =
                        QueryContext.of(
                                RefundPolicySortKey.CREATED_AT,
                                SortDirection.DESC,
                                PageRequest.defaultPage(),
                                false);
                RefundPolicySearchCriteria criteria =
                        RefundPolicySearchCriteria.of(SellerId.of(SELLER_ID), queryContext);

                // when
                List<RefundPolicyJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(4);
                for (int i = 0; i < result.size() - 1; i++) {
                    assertThat(result.get(i).getCreatedAt())
                            .isAfterOrEqualTo(result.get(i + 1).getCreatedAt());
                }
            }
        }

        @Nested
        @DisplayName("페이징 테스트")
        class PagingTest {

            @Test
            @DisplayName("첫 페이지 조회")
            void shouldReturnFirstPage() {
                // given
                QueryContext<RefundPolicySortKey> queryContext =
                        QueryContext.of(
                                RefundPolicySortKey.CREATED_AT,
                                SortDirection.DESC,
                                PageRequest.of(0, 2),
                                false);
                RefundPolicySearchCriteria criteria =
                        RefundPolicySearchCriteria.of(SellerId.of(SELLER_ID), queryContext);

                // when
                List<RefundPolicyJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(2);
            }

            @Test
            @DisplayName("두 번째 페이지 조회")
            void shouldReturnSecondPage() {
                // given
                QueryContext<RefundPolicySortKey> queryContext =
                        QueryContext.of(
                                RefundPolicySortKey.CREATED_AT,
                                SortDirection.DESC,
                                PageRequest.of(1, 2),
                                false);
                RefundPolicySearchCriteria criteria =
                        RefundPolicySearchCriteria.of(SellerId.of(SELLER_ID), queryContext);

                // when
                List<RefundPolicyJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(2);
            }
        }
    }

    @Nested
    @DisplayName("countByCriteria 테스트")
    class CountByCriteriaTest {

        @BeforeEach
        void setUp() {
            jpaRepository.save(
                    RefundPolicyJpaEntityFixtures.newActiveEntityWithName(SELLER_ID, "정책1"));
            jpaRepository.save(
                    RefundPolicyJpaEntityFixtures.newActiveEntityWithName(SELLER_ID, "정책2"));
            jpaRepository.save(
                    RefundPolicyJpaEntityFixtures.newActiveEntityWithName(SELLER_ID, "정책3"));
            jpaRepository.save(RefundPolicyJpaEntityFixtures.newInactiveEntity(SELLER_ID));
            jpaRepository.save(RefundPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 카운트 - 삭제된 항목 제외")
        void shouldCountAllExcludingDeleted() {
            // given
            RefundPolicySearchCriteria criteria =
                    RefundPolicySearchCriteria.defaultCriteria(SellerId.of(SELLER_ID));

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(4);
        }

        @Test
        @DisplayName("다른 셀러의 정책은 카운트되지 않음")
        void shouldNotCountOtherSellerPolicies() {
            // given
            Long otherSellerId = 999L;
            jpaRepository.save(RefundPolicyJpaEntityFixtures.newActiveEntity(otherSellerId));
            flushAndClear();

            RefundPolicySearchCriteria criteria =
                    RefundPolicySearchCriteria.defaultCriteria(SellerId.of(SELLER_ID));

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(4);
        }
    }

    @Nested
    @DisplayName("countActiveBySellerId 테스트")
    class CountActiveBySellerIdTest {

        @BeforeEach
        void setUp() {
            jpaRepository.save(RefundPolicyJpaEntityFixtures.newActiveEntity(SELLER_ID));
            jpaRepository.save(
                    RefundPolicyJpaEntityFixtures.newActiveEntityWithName(SELLER_ID, "정책2"));
            jpaRepository.save(RefundPolicyJpaEntityFixtures.newInactiveEntity(SELLER_ID));
            jpaRepository.save(RefundPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();
        }

        @Test
        @DisplayName("활성 정책 개수 조회")
        void shouldCountActivePolicies() {
            // when
            long count = queryDslRepository.countActiveBySellerId(SELLER_ID);

            // then
            assertThat(count).isEqualTo(2);
        }

        @Test
        @DisplayName("활성 정책이 없는 셀러")
        void shouldReturnZeroWhenNoActivePolicies() {
            // given
            Long otherSellerId = 999L;
            jpaRepository.save(RefundPolicyJpaEntityFixtures.newInactiveEntity(otherSellerId));
            flushAndClear();

            // when
            long count = queryDslRepository.countActiveBySellerId(otherSellerId);

            // then
            assertThat(count).isEqualTo(0);
        }
    }
}
