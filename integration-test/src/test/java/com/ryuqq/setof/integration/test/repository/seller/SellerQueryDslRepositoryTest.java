package com.ryuqq.setof.integration.test.repository.seller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.seller.SellerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerQueryDslRepository;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import com.ryuqq.setof.domain.seller.query.SellerSearchField;
import com.ryuqq.setof.domain.seller.query.SellerSortKey;
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
 * Seller QueryDSL Repository 통합 테스트.
 *
 * <p>QueryDSL 기반의 복잡한 쿼리 동작을 검증합니다.
 *
 * <ul>
 *   <li>Soft Delete 필터링 (deletedAt IS NULL)
 *   <li>동적 검색 조건 (ConditionBuilder)
 *   <li>정렬 및 페이징
 *   <li>비즈니스 조건 (existsBySellerNameExcluding 등)
 * </ul>
 */
@Tag(TestTags.SELLER)
@DisplayName("셀러 QueryDSL Repository 테스트")
class SellerQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private SellerJpaRepository jpaRepository;
    @Autowired private SellerQueryDslRepository queryDslRepository;

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("활성 상태 셀러 조회 성공")
        void shouldFindActiveEntity() {
            // given
            SellerJpaEntity entity = jpaRepository.save(SellerJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            Optional<SellerJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getSellerName())
                    .isEqualTo(SellerJpaEntityFixtures.DEFAULT_SELLER_NAME);
        }

        @Test
        @DisplayName("삭제된 셀러는 조회되지 않음 (Soft Delete 필터)")
        void shouldNotFindDeletedEntity() {
            // given
            SellerJpaEntity entity = jpaRepository.save(SellerJpaEntityFixtures.newDeletedEntity());
            flushAndClear();

            // when
            Optional<SellerJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<SellerJpaEntity> found = queryDslRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByIds 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 셀러 목록 조회 성공")
        void shouldFindByIds() {
            // given
            SellerJpaEntity entity1 =
                    jpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("셀러1", "스토어1"));
            SellerJpaEntity entity2 =
                    jpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("셀러2", "스토어2"));
            SellerJpaEntity entity3 =
                    jpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("셀러3", "스토어3"));
            flushAndClear();

            // when
            List<SellerJpaEntity> found =
                    queryDslRepository.findByIds(List.of(entity1.getId(), entity3.getId()));

            // then
            assertThat(found).hasSize(2);
            assertThat(found)
                    .extracting(SellerJpaEntity::getSellerName)
                    .containsExactlyInAnyOrder("셀러1", "셀러3");
        }

        @Test
        @DisplayName("삭제된 셀러는 ID 목록 조회에서 제외됨")
        void shouldExcludeDeletedEntities() {
            // given
            SellerJpaEntity activeEntity =
                    jpaRepository.save(
                            SellerJpaEntityFixtures.activeEntityWithName("활성셀러", "활성스토어"));
            SellerJpaEntity deletedEntity =
                    jpaRepository.save(SellerJpaEntityFixtures.newDeletedEntity());
            flushAndClear();

            // when
            List<SellerJpaEntity> found =
                    queryDslRepository.findByIds(
                            List.of(activeEntity.getId(), deletedEntity.getId()));

            // then
            assertThat(found).hasSize(1);
            assertThat(found.get(0).getSellerName()).isEqualTo("활성셀러");
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회시 빈 목록 반환")
        void shouldReturnEmptyListWhenEmptyIds() {
            // when
            List<SellerJpaEntity> found = queryDslRepository.findByIds(List.of());

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsById 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("활성 셀러 존재 확인 - true")
        void shouldReturnTrueForActiveEntity() {
            // given
            SellerJpaEntity entity = jpaRepository.save(SellerJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsById(entity.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("삭제된 셀러는 존재하지 않음으로 판단 (Soft Delete)")
        void shouldReturnFalseForDeletedEntity() {
            // given
            SellerJpaEntity entity = jpaRepository.save(SellerJpaEntityFixtures.newDeletedEntity());
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
    @DisplayName("existsBySellerName 테스트")
    class ExistsBySellerNameTest {

        @Test
        @DisplayName("존재하는 셀러명 확인 - true")
        void shouldReturnTrueForExistingName() {
            // given
            jpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("고유한셀러명", "스토어명"));
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsBySellerName("고유한셀러명");

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("삭제된 셀러의 이름은 존재하지 않음으로 판단")
        void shouldReturnFalseForDeletedSellerName() {
            // given
            jpaRepository.save(SellerJpaEntityFixtures.newDeletedEntity());
            flushAndClear();

            // when
            boolean exists =
                    queryDslRepository.existsBySellerName(
                            SellerJpaEntityFixtures.DEFAULT_SELLER_NAME);

            // then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("존재하지 않는 셀러명 - false")
        void shouldReturnFalseForNonExistentName() {
            // when
            boolean exists = queryDslRepository.existsBySellerName("존재하지않는셀러");

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("existsBySellerNameExcluding 테스트")
    class ExistsBySellerNameExcludingTest {

        @Test
        @DisplayName("다른 셀러가 같은 이름을 사용중이면 true")
        void shouldReturnTrueWhenOtherSellerHasSameName() {
            // given
            SellerJpaEntity existingSeller =
                    jpaRepository.save(
                            SellerJpaEntityFixtures.activeEntityWithName("중복셀러명", "스토어1"));
            SellerJpaEntity currentSeller =
                    jpaRepository.save(
                            SellerJpaEntityFixtures.activeEntityWithName("현재셀러", "스토어2"));
            flushAndClear();

            // when
            boolean exists =
                    queryDslRepository.existsBySellerNameExcluding("중복셀러명", currentSeller.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("자기 자신의 이름은 제외하고 검사")
        void shouldExcludeSelfWhenCheckingName() {
            // given
            SellerJpaEntity seller =
                    jpaRepository.save(
                            SellerJpaEntityFixtures.activeEntityWithName("유일한셀러", "스토어"));
            flushAndClear();

            // when
            boolean exists =
                    queryDslRepository.existsBySellerNameExcluding("유일한셀러", seller.getId());

            // then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("삭제된 셀러의 이름은 중복 검사에서 제외")
        void shouldExcludeDeletedSellerFromDuplicateCheck() {
            // given
            jpaRepository.save(SellerJpaEntityFixtures.newDeletedEntity());
            SellerJpaEntity currentSeller =
                    jpaRepository.save(
                            SellerJpaEntityFixtures.activeEntityWithName("새로운셀러", "스토어"));
            flushAndClear();

            // when
            boolean exists =
                    queryDslRepository.existsBySellerNameExcluding(
                            SellerJpaEntityFixtures.DEFAULT_SELLER_NAME, currentSeller.getId());

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("findByCriteria 테스트")
    class FindByCriteriaTest {

        private SellerJpaEntity activeSeller1;
        private SellerJpaEntity activeSeller2;
        private SellerJpaEntity activeSeller3;
        private SellerJpaEntity inactiveSeller;
        private SellerJpaEntity deletedSeller;

        @BeforeEach
        void setUp() {
            activeSeller1 =
                    jpaRepository.save(
                            SellerJpaEntityFixtures.activeEntityWithName("ABC마켓", "ABC스토어"));
            activeSeller2 =
                    jpaRepository.save(
                            SellerJpaEntityFixtures.activeEntityWithName("XYZ마켓", "XYZ스토어"));
            activeSeller3 =
                    jpaRepository.save(
                            SellerJpaEntityFixtures.activeEntityWithName("테스트마켓", "테스트스토어"));
            inactiveSeller =
                    jpaRepository.save(
                            SellerJpaEntityFixtures.inactiveEntityWithName("비활성마켓", "비활성스토어"));
            deletedSeller =
                    jpaRepository.save(
                            SellerJpaEntityFixtures.deletedEntityWithName("삭제마켓", "삭제스토어"));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 조회 - 삭제된 셀러 제외")
        void shouldFindAllExcludingDeleted() {
            // given
            SellerSearchCriteria criteria = SellerSearchCriteria.defaultCriteria();

            // when
            List<SellerJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(4);
            assertThat(result)
                    .extracting(SellerJpaEntity::getId)
                    .doesNotContain(deletedSeller.getId());
        }

        @Test
        @DisplayName("활성 상태 필터 - 활성 셀러만 조회")
        void shouldFilterByActiveStatus() {
            // given
            SellerSearchCriteria criteria = SellerSearchCriteria.activeOnly();

            // when
            List<SellerJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).allMatch(SellerJpaEntity::isActive);
        }

        @Test
        @DisplayName("비활성 상태 필터 - 비활성 셀러만 조회")
        void shouldFilterByInactiveStatus() {
            // given
            QueryContext<SellerSortKey> queryContext =
                    QueryContext.defaultOf(SellerSortKey.CREATED_AT);
            SellerSearchCriteria criteria =
                    SellerSearchCriteria.of(false, null, null, queryContext);

            // when
            List<SellerJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(inactiveSeller.getId());
        }

        @Test
        @DisplayName("셀러명 검색 - 부분 일치")
        void shouldSearchBySellerName() {
            // given
            QueryContext<SellerSortKey> queryContext =
                    QueryContext.defaultOf(SellerSortKey.CREATED_AT);
            SellerSearchCriteria criteria =
                    SellerSearchCriteria.of(
                            null, SellerSearchField.SELLER_NAME, "마켓", queryContext);

            // when
            List<SellerJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            // ABC마켓, XYZ마켓, 테스트마켓, 비활성마켓 = 4개 (삭제마켓은 제외)
            assertThat(result).hasSize(4);
            assertThat(result)
                    .extracting(SellerJpaEntity::getSellerName)
                    .allMatch(name -> name.contains("마켓"));
        }

        @Test
        @DisplayName("셀러명 검색 - 대소문자 무시")
        void shouldSearchIgnoringCase() {
            // given
            QueryContext<SellerSortKey> queryContext =
                    QueryContext.defaultOf(SellerSortKey.CREATED_AT);
            SellerSearchCriteria criteria =
                    SellerSearchCriteria.of(
                            null, SellerSearchField.SELLER_NAME, "abc", queryContext);

            // when
            List<SellerJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getSellerName()).isEqualTo("ABC마켓");
        }

        @Test
        @DisplayName("includeDeleted 옵션 - 삭제된 항목 포함 조회")
        void shouldIncludeDeletedWhenOptionSet() {
            // given
            QueryContext<SellerSortKey> queryContext =
                    QueryContext.of(
                            SellerSortKey.CREATED_AT,
                            SortDirection.DESC,
                            PageRequest.defaultPage(),
                            true);
            SellerSearchCriteria criteria = SellerSearchCriteria.of(null, null, null, queryContext);

            // when
            List<SellerJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(5);
            assertThat(result).extracting(SellerJpaEntity::getId).contains(deletedSeller.getId());
        }

        @Nested
        @DisplayName("정렬 테스트")
        class SortingTest {

            @Test
            @DisplayName("등록일시 내림차순 정렬 (최신순)")
            void shouldSortByCreatedAtDesc() {
                // given
                QueryContext<SellerSortKey> queryContext =
                        QueryContext.of(
                                SellerSortKey.CREATED_AT,
                                SortDirection.DESC,
                                PageRequest.defaultPage(),
                                false);
                SellerSearchCriteria criteria =
                        SellerSearchCriteria.of(true, null, null, queryContext);

                // when
                List<SellerJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(3);
                // 나중에 생성된 것이 먼저 나와야 함
                assertThat(result.get(0).getId()).isGreaterThan(result.get(1).getId());
            }

            @Test
            @DisplayName("등록일시 오름차순 정렬 (오래된순)")
            void shouldSortByCreatedAtAsc() {
                // given
                QueryContext<SellerSortKey> queryContext =
                        QueryContext.of(
                                SellerSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.defaultPage(),
                                false);
                SellerSearchCriteria criteria =
                        SellerSearchCriteria.of(true, null, null, queryContext);

                // when
                List<SellerJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(3);
                // 먼저 생성된 것이 먼저 나와야 함
                assertThat(result.get(0).getId()).isLessThan(result.get(1).getId());
            }

            @Test
            @DisplayName("셀러명 오름차순 정렬")
            void shouldSortBySellerNameAsc() {
                // given
                QueryContext<SellerSortKey> queryContext =
                        QueryContext.of(
                                SellerSortKey.SELLER_NAME,
                                SortDirection.ASC,
                                PageRequest.defaultPage(),
                                false);
                SellerSearchCriteria criteria =
                        SellerSearchCriteria.of(true, null, null, queryContext);

                // when
                List<SellerJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(3);
                assertThat(result)
                        .extracting(SellerJpaEntity::getSellerName)
                        .containsExactly("ABC마켓", "XYZ마켓", "테스트마켓");
            }
        }

        @Nested
        @DisplayName("페이징 테스트")
        class PagingTest {

            @Test
            @DisplayName("첫 페이지 조회")
            void shouldReturnFirstPage() {
                // given
                QueryContext<SellerSortKey> queryContext =
                        QueryContext.of(
                                SellerSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(0, 2),
                                false);
                SellerSearchCriteria criteria =
                        SellerSearchCriteria.of(true, null, null, queryContext);

                // when
                List<SellerJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(2);
            }

            @Test
            @DisplayName("두 번째 페이지 조회")
            void shouldReturnSecondPage() {
                // given
                QueryContext<SellerSortKey> queryContext =
                        QueryContext.of(
                                SellerSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(1, 2),
                                false);
                SellerSearchCriteria criteria =
                        SellerSearchCriteria.of(true, null, null, queryContext);

                // when
                List<SellerJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(1);
            }

            @Test
            @DisplayName("페이지 범위 초과시 빈 목록 반환")
            void shouldReturnEmptyWhenPageExceeds() {
                // given
                QueryContext<SellerSortKey> queryContext =
                        QueryContext.of(
                                SellerSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(10, 20),
                                false);
                SellerSearchCriteria criteria =
                        SellerSearchCriteria.of(true, null, null, queryContext);

                // when
                List<SellerJpaEntity> result = queryDslRepository.findByCriteria(criteria);

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
            jpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("셀러1", "스토어1"));
            jpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("셀러2", "스토어2"));
            jpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("셀러3", "스토어3"));
            jpaRepository.save(SellerJpaEntityFixtures.inactiveEntityWithName("비활성셀러", "비활성스토어"));
            jpaRepository.save(SellerJpaEntityFixtures.deletedEntityWithName("삭제셀러", "삭제스토어"));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 카운트 - 삭제된 셀러 제외")
        void shouldCountAllExcludingDeleted() {
            // given
            SellerSearchCriteria criteria = SellerSearchCriteria.defaultCriteria();

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(4);
        }

        @Test
        @DisplayName("활성 상태 필터 카운트")
        void shouldCountByActiveStatus() {
            // given
            SellerSearchCriteria criteria = SellerSearchCriteria.activeOnly();

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("검색 조건 카운트")
        void shouldCountBySearchCondition() {
            // given
            QueryContext<SellerSortKey> queryContext =
                    QueryContext.defaultOf(SellerSortKey.CREATED_AT);
            SellerSearchCriteria criteria =
                    SellerSearchCriteria.of(
                            null, SellerSearchField.SELLER_NAME, "셀러", queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            // 셀러1, 셀러2, 셀러3, 비활성셀러 = 4개 (삭제셀러는 제외)
            assertThat(count).isEqualTo(4);
        }

        @Test
        @DisplayName("includeDeleted 옵션 카운트")
        void shouldCountIncludingDeletedWhenOptionSet() {
            // given
            QueryContext<SellerSortKey> queryContext =
                    QueryContext.of(
                            SellerSortKey.CREATED_AT,
                            SortDirection.DESC,
                            PageRequest.defaultPage(),
                            true);
            SellerSearchCriteria criteria = SellerSearchCriteria.of(null, null, null, queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(5);
        }

        @Test
        @DisplayName("조건에 맞는 데이터 없을 시 0 반환")
        void shouldReturnZeroWhenNoMatch() {
            // given
            QueryContext<SellerSortKey> queryContext =
                    QueryContext.defaultOf(SellerSortKey.CREATED_AT);
            SellerSearchCriteria criteria =
                    SellerSearchCriteria.of(
                            null, SellerSearchField.SELLER_NAME, "존재하지않는", queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isZero();
        }
    }
}
