package com.ryuqq.setof.integration.test.repository.shippingpolicy;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.ShippingPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.entity.ShippingPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.repository.ShippingPolicyJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.repository.ShippingPolicyQueryDslRepository;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.shippingpolicy.query.ShippingPolicySearchCriteria;
import com.ryuqq.setof.domain.shippingpolicy.query.ShippingPolicySortKey;
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
 * ShippingPolicy QueryDSL Repository 통합 테스트.
 *
 * <p>QueryDSL 기반의 복잡한 쿼리 동작을 검증합니다. Soft Delete 필터링, 정렬, 페이징을 테스트합니다.
 */
@Tag(TestTags.SHIPPING_POLICY)
@DisplayName("배송정책 QueryDSL Repository 테스트")
class ShippingPolicyQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private ShippingPolicyJpaRepository jpaRepository;
    @Autowired private ShippingPolicyQueryDslRepository queryDslRepository;

    private static final Long SELLER_ID = 100L;

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 배송정책 조회 성공")
        void shouldFindById() {
            // given
            ShippingPolicyJpaEntity entity =
                    jpaRepository.save(ShippingPolicyJpaEntityFixtures.newActiveEntity(SELLER_ID));
            flushAndClear();

            // when
            Optional<ShippingPolicyJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getSellerId()).isEqualTo(SELLER_ID);
        }

        @Test
        @DisplayName("삭제된 배송정책은 조회되지 않음")
        void shouldNotFindDeletedPolicy() {
            // given
            ShippingPolicyJpaEntity entity =
                    jpaRepository.save(ShippingPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();

            // when
            Optional<ShippingPolicyJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<ShippingPolicyJpaEntity> found = queryDslRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByIds 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 배송정책 목록 조회 성공")
        void shouldFindByIds() {
            // given
            ShippingPolicyJpaEntity entity1 =
                    jpaRepository.save(ShippingPolicyJpaEntityFixtures.newActiveEntity(SELLER_ID));
            ShippingPolicyJpaEntity entity2 =
                    jpaRepository.save(
                            ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    SELLER_ID, "빠른배송정책"));
            ShippingPolicyJpaEntity entity3 =
                    jpaRepository.save(
                            ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    SELLER_ID, "프리미엄정책"));
            flushAndClear();

            // when
            List<ShippingPolicyJpaEntity> found =
                    queryDslRepository.findByIds(List.of(entity1.getId(), entity3.getId()));

            // then
            assertThat(found).hasSize(2);
            assertThat(found)
                    .extracting(ShippingPolicyJpaEntity::getId)
                    .containsExactlyInAnyOrder(entity1.getId(), entity3.getId());
        }

        @Test
        @DisplayName("삭제된 정책은 조회되지 않음")
        void shouldNotFindDeletedPolicies() {
            // given
            ShippingPolicyJpaEntity activeEntity =
                    jpaRepository.save(ShippingPolicyJpaEntityFixtures.newActiveEntity(SELLER_ID));
            ShippingPolicyJpaEntity deletedEntity =
                    jpaRepository.save(ShippingPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();

            // when
            List<ShippingPolicyJpaEntity> found =
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
            List<ShippingPolicyJpaEntity> found = queryDslRepository.findByIds(List.of());

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findDefaultBySellerId 테스트")
    class FindDefaultBySellerIdTest {

        @Test
        @DisplayName("셀러의 기본 배송정책 조회 성공")
        void shouldFindDefaultPolicy() {
            // given
            jpaRepository.save(ShippingPolicyJpaEntityFixtures.newDefaultEntity(SELLER_ID));
            jpaRepository.save(
                    ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(SELLER_ID, "추가정책"));
            flushAndClear();

            // when
            Optional<ShippingPolicyJpaEntity> found =
                    queryDslRepository.findDefaultBySellerId(SELLER_ID);

            // then
            assertThat(found).isPresent();
            assertThat(found.get().isDefaultPolicy()).isTrue();
        }

        @Test
        @DisplayName("기본 정책이 삭제된 경우 조회되지 않음")
        void shouldNotFindDeletedDefaultPolicy() {
            // given
            ShippingPolicyJpaEntity deletedDefault =
                    jpaRepository.save(ShippingPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();

            // when
            Optional<ShippingPolicyJpaEntity> found =
                    queryDslRepository.findDefaultBySellerId(SELLER_ID);

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("기본 정책이 없는 셀러 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNoDefaultPolicy() {
            // given
            jpaRepository.save(
                    ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(SELLER_ID, "추가정책만"));
            flushAndClear();

            // when
            Optional<ShippingPolicyJpaEntity> found =
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
            ShippingPolicyJpaEntity entity =
                    jpaRepository.save(ShippingPolicyJpaEntityFixtures.newActiveEntity(SELLER_ID));
            flushAndClear();

            // when
            Optional<ShippingPolicyJpaEntity> found =
                    queryDslRepository.findBySellerIdAndId(SELLER_ID, entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getId()).isEqualTo(entity.getId());
        }

        @Test
        @DisplayName("다른 셀러의 정책은 조회되지 않음")
        void shouldNotFindOtherSellerPolicy() {
            // given
            ShippingPolicyJpaEntity entity =
                    jpaRepository.save(ShippingPolicyJpaEntityFixtures.newActiveEntity(SELLER_ID));
            flushAndClear();

            // when
            Optional<ShippingPolicyJpaEntity> found =
                    queryDslRepository.findBySellerIdAndId(999L, entity.getId());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("삭제된 정책은 조회되지 않음")
        void shouldNotFindDeletedPolicy() {
            // given
            ShippingPolicyJpaEntity entity =
                    jpaRepository.save(ShippingPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();

            // when
            Optional<ShippingPolicyJpaEntity> found =
                    queryDslRepository.findBySellerIdAndId(SELLER_ID, entity.getId());

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByCriteria 테스트")
    class FindByCriteriaTest {

        private ShippingPolicyJpaEntity activePolicy1;
        private ShippingPolicyJpaEntity activePolicy2;
        private ShippingPolicyJpaEntity activePolicy3;
        private ShippingPolicyJpaEntity inactivePolicy;
        private ShippingPolicyJpaEntity deletedPolicy;

        @BeforeEach
        void setUp() {
            activePolicy1 =
                    jpaRepository.save(
                            ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    SELLER_ID, "기본배송"));
            activePolicy2 =
                    jpaRepository.save(
                            ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    SELLER_ID, "빠른배송"));
            activePolicy3 =
                    jpaRepository.save(
                            ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    SELLER_ID, "프리미엄배송"));
            inactivePolicy =
                    jpaRepository.save(
                            ShippingPolicyJpaEntityFixtures.newInactiveEntity(SELLER_ID));
            deletedPolicy =
                    jpaRepository.save(ShippingPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();
        }

        @Test
        @DisplayName("셀러별 전체 조회 - 삭제된 항목 제외")
        void shouldFindAllExcludingDeleted() {
            // given
            ShippingPolicySearchCriteria criteria =
                    ShippingPolicySearchCriteria.defaultCriteria(SellerId.of(SELLER_ID));

            // when
            List<ShippingPolicyJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(4);
            assertThat(result)
                    .extracting(ShippingPolicyJpaEntity::getId)
                    .doesNotContain(deletedPolicy.getId());
        }

        @Test
        @DisplayName("다른 셀러의 정책은 조회되지 않음")
        void shouldNotFindOtherSellerPolicies() {
            // given
            Long otherSellerId = 999L;
            jpaRepository.save(ShippingPolicyJpaEntityFixtures.newActiveEntity(otherSellerId));
            flushAndClear();

            ShippingPolicySearchCriteria criteria =
                    ShippingPolicySearchCriteria.defaultCriteria(SellerId.of(SELLER_ID));

            // when
            List<ShippingPolicyJpaEntity> result = queryDslRepository.findByCriteria(criteria);

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
                QueryContext<ShippingPolicySortKey> queryContext =
                        QueryContext.of(
                                ShippingPolicySortKey.POLICY_NAME,
                                SortDirection.ASC,
                                PageRequest.defaultPage(),
                                false);
                ShippingPolicySearchCriteria criteria =
                        ShippingPolicySearchCriteria.of(SellerId.of(SELLER_ID), queryContext);

                // when
                List<ShippingPolicyJpaEntity> result = queryDslRepository.findByCriteria(criteria);

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
                QueryContext<ShippingPolicySortKey> queryContext =
                        QueryContext.of(
                                ShippingPolicySortKey.CREATED_AT,
                                SortDirection.DESC,
                                PageRequest.defaultPage(),
                                false);
                ShippingPolicySearchCriteria criteria =
                        ShippingPolicySearchCriteria.of(SellerId.of(SELLER_ID), queryContext);

                // when
                List<ShippingPolicyJpaEntity> result = queryDslRepository.findByCriteria(criteria);

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
                QueryContext<ShippingPolicySortKey> queryContext =
                        QueryContext.of(
                                ShippingPolicySortKey.CREATED_AT,
                                SortDirection.DESC,
                                PageRequest.of(0, 2),
                                false);
                ShippingPolicySearchCriteria criteria =
                        ShippingPolicySearchCriteria.of(SellerId.of(SELLER_ID), queryContext);

                // when
                List<ShippingPolicyJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(2);
            }

            @Test
            @DisplayName("두 번째 페이지 조회")
            void shouldReturnSecondPage() {
                // given
                QueryContext<ShippingPolicySortKey> queryContext =
                        QueryContext.of(
                                ShippingPolicySortKey.CREATED_AT,
                                SortDirection.DESC,
                                PageRequest.of(1, 2),
                                false);
                ShippingPolicySearchCriteria criteria =
                        ShippingPolicySearchCriteria.of(SellerId.of(SELLER_ID), queryContext);

                // when
                List<ShippingPolicyJpaEntity> result = queryDslRepository.findByCriteria(criteria);

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
                    ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(SELLER_ID, "정책1"));
            jpaRepository.save(
                    ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(SELLER_ID, "정책2"));
            jpaRepository.save(
                    ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(SELLER_ID, "정책3"));
            jpaRepository.save(ShippingPolicyJpaEntityFixtures.newInactiveEntity(SELLER_ID));
            jpaRepository.save(ShippingPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 카운트 - 삭제된 항목 제외")
        void shouldCountAllExcludingDeleted() {
            // given
            ShippingPolicySearchCriteria criteria =
                    ShippingPolicySearchCriteria.defaultCriteria(SellerId.of(SELLER_ID));

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
            jpaRepository.save(ShippingPolicyJpaEntityFixtures.newActiveEntity(otherSellerId));
            flushAndClear();

            ShippingPolicySearchCriteria criteria =
                    ShippingPolicySearchCriteria.defaultCriteria(SellerId.of(SELLER_ID));

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
            jpaRepository.save(ShippingPolicyJpaEntityFixtures.newActiveEntity(SELLER_ID));
            jpaRepository.save(
                    ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(SELLER_ID, "정책2"));
            jpaRepository.save(ShippingPolicyJpaEntityFixtures.newInactiveEntity(SELLER_ID));
            jpaRepository.save(ShippingPolicyJpaEntityFixtures.newDeletedEntity(SELLER_ID));
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
            jpaRepository.save(ShippingPolicyJpaEntityFixtures.newInactiveEntity(otherSellerId));
            flushAndClear();

            // when
            long count = queryDslRepository.countActiveBySellerId(otherSellerId);

            // then
            assertThat(count).isEqualTo(0);
        }
    }
}
