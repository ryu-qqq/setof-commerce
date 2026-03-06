package com.ryuqq.setof.integration.test.repository.brand;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.brand.BrandJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.brand.repository.BrandJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.brand.repository.BrandQueryDslRepository;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.query.BrandSortKey;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Brand QueryDSL Repository 통합 테스트.
 *
 * <p>QueryDSL 기반의 복잡한 쿼리 동작을 검증합니다.
 *
 * <ul>
 *   <li>Soft Delete 필터링 (deletedAt IS NULL)
 *   <li>동적 검색 조건 (ConditionBuilder)
 *   <li>정렬 및 페이징
 * </ul>
 */
@Tag(TestTags.BRAND)
@DisplayName("브랜드 QueryDSL Repository 테스트")
class BrandQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private BrandJpaRepository jpaRepository;
    @Autowired private BrandQueryDslRepository queryDslRepository;

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("활성 상태 브랜드 조회 성공")
        void shouldFindActiveEntity() {
            // given
            BrandJpaEntity entity = jpaRepository.save(BrandJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            Optional<BrandJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getBrandName())
                    .isEqualTo(BrandJpaEntityFixtures.DEFAULT_BRAND_NAME);
        }

        @Test
        @DisplayName("삭제된 브랜드는 조회되지 않음 (Soft Delete 필터)")
        void shouldNotFindDeletedEntity() {
            // given
            BrandJpaEntity entity = jpaRepository.save(createDeletedEntity());
            flushAndClear();

            // when
            Optional<BrandJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<BrandJpaEntity> found = queryDslRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByIds 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 브랜드 목록 조회 성공")
        void shouldFindByIds() {
            // given
            BrandJpaEntity entity1 =
                    jpaRepository.save(
                            BrandJpaEntityFixtures.activeEntityWithName("브랜드A", "브랜드A 표시명"));
            BrandJpaEntity entity2 =
                    jpaRepository.save(
                            BrandJpaEntityFixtures.activeEntityWithName("브랜드B", "브랜드B 표시명"));
            BrandJpaEntity entity3 =
                    jpaRepository.save(
                            BrandJpaEntityFixtures.activeEntityWithName("브랜드C", "브랜드C 표시명"));
            flushAndClear();

            // when
            List<BrandJpaEntity> found =
                    queryDslRepository.findByIds(List.of(entity1.getId(), entity3.getId()));

            // then
            assertThat(found).hasSize(2);
            assertThat(found)
                    .extracting(BrandJpaEntity::getBrandName)
                    .containsExactlyInAnyOrder("브랜드A", "브랜드C");
        }

        @Test
        @DisplayName("삭제된 브랜드는 ID 목록 조회에서 제외됨")
        void shouldExcludeDeletedEntities() {
            // given
            BrandJpaEntity activeEntity =
                    jpaRepository.save(
                            BrandJpaEntityFixtures.activeEntityWithName("활성브랜드", "활성브랜드 표시명"));
            BrandJpaEntity deletedEntity = jpaRepository.save(createDeletedEntity());
            flushAndClear();

            // when
            List<BrandJpaEntity> found =
                    queryDslRepository.findByIds(
                            List.of(activeEntity.getId(), deletedEntity.getId()));

            // then
            assertThat(found).hasSize(1);
            assertThat(found.get(0).getBrandName()).isEqualTo("활성브랜드");
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회시 빈 목록 반환")
        void shouldReturnEmptyListWhenEmptyIds() {
            // when
            List<BrandJpaEntity> found = queryDslRepository.findByIds(List.of());

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsById 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("활성 브랜드 존재 확인 - true")
        void shouldReturnTrueForActiveEntity() {
            // given
            BrandJpaEntity entity = jpaRepository.save(BrandJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsById(entity.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("삭제된 브랜드는 존재하지 않음으로 판단 (Soft Delete)")
        void shouldReturnFalseForDeletedEntity() {
            // given
            BrandJpaEntity entity = jpaRepository.save(createDeletedEntity());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsById(entity.getId());

            // then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("존재하지 않는 ID - false")
        void shouldReturnFalseForNonExistentId() {
            // when
            boolean exists = queryDslRepository.existsById(999999L);

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("findByCriteria 테스트")
    class FindByCriteriaTest {

        private BrandJpaEntity activeBrand1;
        private BrandJpaEntity activeBrand2;
        private BrandJpaEntity activeBrand3;
        private BrandJpaEntity inactiveBrand;
        private BrandJpaEntity deletedBrand;

        @BeforeEach
        void setUp() {
            activeBrand1 =
                    jpaRepository.save(
                            BrandJpaEntityFixtures.activeEntityWithName("나이키", "나이키 코리아"));
            activeBrand2 =
                    jpaRepository.save(
                            BrandJpaEntityFixtures.activeEntityWithName("아디다스", "아디다스 코리아"));
            activeBrand3 =
                    jpaRepository.save(BrandJpaEntityFixtures.activeEntityWithName("퓨마", "퓨마 코리아"));
            inactiveBrand = jpaRepository.save(createInactiveEntityWithName("비활성브랜드", "비활성 표시명"));
            deletedBrand = jpaRepository.save(createDeletedEntityWithName("삭제브랜드", "삭제 표시명"));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 조회 - 삭제된 브랜드 제외")
        void shouldFindAllExcludingDeleted() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.defaultOf();

            // when
            List<BrandJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(4);
            assertThat(result)
                    .extracting(BrandJpaEntity::getId)
                    .doesNotContain(deletedBrand.getId());
        }

        @Test
        @DisplayName("표시 중인 브랜드만 조회")
        void shouldFilterByDisplayedStatus() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.displayedOnly();

            // when
            List<BrandJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).allMatch(BrandJpaEntity::isDisplayed);
        }

        @Test
        @DisplayName("브랜드명 검색 - 부분 일치")
        void shouldSearchByBrandName() {
            // given
            QueryContext<BrandSortKey> queryContext =
                    QueryContext.defaultOf(BrandSortKey.CREATED_AT);
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, "다스", queryContext);

            // when
            List<BrandJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getBrandName()).isEqualTo("아디다스");
        }

        @Test
        @DisplayName("브랜드명 검색 - 대소문자 무시")
        void shouldSearchIgnoringCase() {
            // given
            jpaRepository.save(BrandJpaEntityFixtures.activeEntityWithName("ABC브랜드", "ABC 표시명"));
            flushAndClear();

            QueryContext<BrandSortKey> queryContext =
                    QueryContext.defaultOf(BrandSortKey.CREATED_AT);
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, "abc", queryContext);

            // when
            List<BrandJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getBrandName()).isEqualTo("ABC브랜드");
        }

        @Nested
        @DisplayName("정렬 테스트")
        class SortingTest {

            @Test
            @DisplayName("등록일시 내림차순 정렬 (최신순)")
            void shouldSortByCreatedAtDesc() {
                // given
                QueryContext<BrandSortKey> queryContext =
                        QueryContext.of(
                                BrandSortKey.CREATED_AT,
                                SortDirection.DESC,
                                PageRequest.defaultPage(),
                                false);
                BrandSearchCriteria criteria =
                        BrandSearchCriteria.of(true, null, null, queryContext);

                // when
                List<BrandJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(3);
                assertThat(result.get(0).getId()).isGreaterThan(result.get(1).getId());
            }

            @Test
            @DisplayName("브랜드명 오름차순 정렬")
            void shouldSortByBrandNameAsc() {
                // given
                QueryContext<BrandSortKey> queryContext =
                        QueryContext.of(
                                BrandSortKey.BRAND_NAME,
                                SortDirection.ASC,
                                PageRequest.defaultPage(),
                                false);
                BrandSearchCriteria criteria =
                        BrandSearchCriteria.of(true, null, null, queryContext);

                // when
                List<BrandJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(3);
                assertThat(result)
                        .extracting(BrandJpaEntity::getBrandName)
                        .containsExactly("나이키", "아디다스", "퓨마");
            }
        }

        @Nested
        @DisplayName("페이징 테스트")
        class PagingTest {

            @Test
            @DisplayName("첫 페이지 조회")
            void shouldReturnFirstPage() {
                // given
                QueryContext<BrandSortKey> queryContext =
                        QueryContext.of(
                                BrandSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(0, 2),
                                false);
                BrandSearchCriteria criteria =
                        BrandSearchCriteria.of(true, null, null, queryContext);

                // when
                List<BrandJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(2);
            }

            @Test
            @DisplayName("두 번째 페이지 조회")
            void shouldReturnSecondPage() {
                // given
                QueryContext<BrandSortKey> queryContext =
                        QueryContext.of(
                                BrandSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(1, 2),
                                false);
                BrandSearchCriteria criteria =
                        BrandSearchCriteria.of(true, null, null, queryContext);

                // when
                List<BrandJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(1);
            }

            @Test
            @DisplayName("페이지 범위 초과시 빈 목록 반환")
            void shouldReturnEmptyWhenPageExceeds() {
                // given
                QueryContext<BrandSortKey> queryContext =
                        QueryContext.of(
                                BrandSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(10, 20),
                                false);
                BrandSearchCriteria criteria =
                        BrandSearchCriteria.of(true, null, null, queryContext);

                // when
                List<BrandJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("countByCriteria 테스트")
    class CountByCriteriaTest {

        @BeforeEach
        void setUp() {
            jpaRepository.save(BrandJpaEntityFixtures.activeEntityWithName("브랜드1", "표시명1"));
            jpaRepository.save(BrandJpaEntityFixtures.activeEntityWithName("브랜드2", "표시명2"));
            jpaRepository.save(BrandJpaEntityFixtures.activeEntityWithName("브랜드3", "표시명3"));
            jpaRepository.save(createInactiveEntityWithName("비활성브랜드", "비활성 표시명"));
            jpaRepository.save(createDeletedEntityWithName("삭제브랜드", "삭제 표시명"));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 카운트 - 삭제된 브랜드 제외")
        void shouldCountAllExcludingDeleted() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.defaultOf();

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(4);
        }

        @Test
        @DisplayName("표시 중인 브랜드 카운트")
        void shouldCountByDisplayedStatus() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.displayedOnly();

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("검색 조건 카운트")
        void shouldCountBySearchCondition() {
            // given
            QueryContext<BrandSortKey> queryContext =
                    QueryContext.defaultOf(BrandSortKey.CREATED_AT);
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, "브랜드", queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            // 브랜드1, 브랜드2, 브랜드3, 비활성브랜드 = 4개 (삭제브랜드는 제외)
            assertThat(count).isEqualTo(4);
        }

        @Test
        @DisplayName("조건에 맞는 데이터 없을 시 0 반환")
        void shouldReturnZeroWhenNoMatch() {
            // given
            QueryContext<BrandSortKey> queryContext =
                    QueryContext.defaultOf(BrandSortKey.CREATED_AT);
            BrandSearchCriteria criteria =
                    BrandSearchCriteria.of(null, null, "존재하지않는브랜드", queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isZero();
        }
    }

    @Nested
    @DisplayName("findAllDisplayed 테스트")
    class FindAllDisplayedTest {

        @Test
        @DisplayName("노출 중인 브랜드만 조회")
        void shouldFindOnlyDisplayedBrands() {
            // given
            jpaRepository.save(BrandJpaEntityFixtures.activeEntityWithName("노출브랜드1", "노출 표시명1"));
            jpaRepository.save(BrandJpaEntityFixtures.activeEntityWithName("노출브랜드2", "노출 표시명2"));
            jpaRepository.save(createInactiveEntityWithName("비노출브랜드", "비노출 표시명"));
            jpaRepository.save(createDeletedEntityWithName("삭제브랜드", "삭제 표시명"));
            flushAndClear();

            // when
            List<BrandJpaEntity> result = queryDslRepository.findAllDisplayed();

            // then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(BrandJpaEntity::isDisplayed);
        }

        @Test
        @DisplayName("표시순서로 정렬되어 반환")
        void shouldReturnSortedByDisplayOrder() {
            // given
            jpaRepository.save(createEntityWithDisplayOrder("세번째", 3));
            jpaRepository.save(createEntityWithDisplayOrder("첫번째", 1));
            jpaRepository.save(createEntityWithDisplayOrder("두번째", 2));
            flushAndClear();

            // when
            List<BrandJpaEntity> result = queryDslRepository.findAllDisplayed();

            // then
            assertThat(result).hasSize(3);
            assertThat(result)
                    .extracting(BrandJpaEntity::getBrandName)
                    .containsExactly("첫번째", "두번째", "세번째");
        }
    }

    // ========================================================================
    // Helper Methods
    // ========================================================================

    private BrandJpaEntity createDeletedEntity() {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                null,
                BrandJpaEntityFixtures.DEFAULT_BRAND_NAME,
                BrandJpaEntityFixtures.DEFAULT_ICON_URL,
                BrandJpaEntityFixtures.DEFAULT_DISPLAY_NAME,
                BrandJpaEntityFixtures.DEFAULT_DISPLAY_ORDER,
                false,
                now,
                now,
                now);
    }

    private BrandJpaEntity createDeletedEntityWithName(String brandName, String displayName) {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                null,
                brandName,
                BrandJpaEntityFixtures.DEFAULT_ICON_URL,
                displayName,
                BrandJpaEntityFixtures.DEFAULT_DISPLAY_ORDER,
                false,
                now,
                now,
                now);
    }

    private BrandJpaEntity createInactiveEntityWithName(String brandName, String displayName) {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                null,
                brandName,
                BrandJpaEntityFixtures.DEFAULT_ICON_URL,
                displayName,
                BrandJpaEntityFixtures.DEFAULT_DISPLAY_ORDER,
                false,
                now,
                now,
                null);
    }

    private BrandJpaEntity createEntityWithDisplayOrder(String brandName, int displayOrder) {
        Instant now = Instant.now();
        return BrandJpaEntity.create(
                null,
                brandName,
                BrandJpaEntityFixtures.DEFAULT_ICON_URL,
                brandName + " 표시명",
                displayOrder,
                true,
                now,
                now,
                null);
    }
}
