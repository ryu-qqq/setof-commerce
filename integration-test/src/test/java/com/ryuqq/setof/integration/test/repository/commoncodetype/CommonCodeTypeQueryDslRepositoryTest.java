package com.ryuqq.setof.integration.test.repository.commoncodetype;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.commoncodetype.CommonCodeTypeJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.entity.CommonCodeTypeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.repository.CommonCodeTypeJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.repository.CommonCodeTypeQueryDslRepository;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.commoncodetype.query.CommonCodeTypeSearchCriteria;
import com.ryuqq.setof.domain.commoncodetype.query.CommonCodeTypeSortKey;
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
 * CommonCodeType QueryDSL Repository 통합 테스트.
 *
 * <p>QueryDSL 기반의 복잡한 쿼리 동작을 검증합니다.
 */
@Tag(TestTags.COMMON_CODE)
@DisplayName("공통코드타입 QueryDSL Repository 테스트")
class CommonCodeTypeQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private CommonCodeTypeJpaRepository jpaRepository;
    @Autowired private CommonCodeTypeQueryDslRepository queryDslRepository;

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 공통코드타입 조회 성공")
        void shouldFindById() {
            // given
            CommonCodeTypeJpaEntity entity =
                    jpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode(
                                    "TEST_CODE", "테스트코드"));
            flushAndClear();

            // when
            Optional<CommonCodeTypeJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getCode()).isEqualTo("TEST_CODE");
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<CommonCodeTypeJpaEntity> found = queryDslRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByIds 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 공통코드타입 목록 조회 성공")
        void shouldFindByIds() {
            // given
            CommonCodeTypeJpaEntity entity1 =
                    jpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode("CODE_1", "코드1"));
            CommonCodeTypeJpaEntity entity2 =
                    jpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode("CODE_2", "코드2"));
            CommonCodeTypeJpaEntity entity3 =
                    jpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode("CODE_3", "코드3"));
            flushAndClear();

            // when
            List<CommonCodeTypeJpaEntity> found =
                    queryDslRepository.findByIds(List.of(entity1.getId(), entity3.getId()));

            // then
            assertThat(found).hasSize(2);
            assertThat(found)
                    .extracting(CommonCodeTypeJpaEntity::getCode)
                    .containsExactlyInAnyOrder("CODE_1", "CODE_3");
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회시 빈 목록 반환")
        void shouldReturnEmptyListWhenEmptyIds() {
            // when
            List<CommonCodeTypeJpaEntity> found = queryDslRepository.findByIds(List.of());

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsByCode 테스트")
    class ExistsByCodeTest {

        @Test
        @DisplayName("존재하는 코드 확인 - true")
        void shouldReturnTrueForExistingCode() {
            // given
            jpaRepository.save(
                    CommonCodeTypeJpaEntityFixtures.newEntityWithCode("UNIQUE_CODE", "유니크코드"));
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsByCode("UNIQUE_CODE");

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 코드 - false")
        void shouldReturnFalseForNonExistentCode() {
            // when
            boolean exists = queryDslRepository.existsByCode("NON_EXISTENT_CODE");

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("existsByDisplayOrderExcludingId 테스트")
    class ExistsByDisplayOrderExcludingIdTest {

        @Test
        @DisplayName("다른 항목이 같은 표시순서를 사용중이면 true")
        void shouldReturnTrueWhenOtherHasSameDisplayOrder() {
            // given
            CommonCodeTypeJpaEntity existing =
                    jpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode("EXISTING", "기존"));
            CommonCodeTypeJpaEntity current =
                    jpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode("CURRENT", "현재"));
            flushAndClear();

            // when
            boolean exists =
                    queryDslRepository.existsByDisplayOrderExcludingId(
                            existing.getDisplayOrder(), current.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("자기 자신의 표시순서는 제외하고 검사")
        void shouldExcludeSelfWhenCheckingDisplayOrder() {
            // given
            CommonCodeTypeJpaEntity entity =
                    jpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode("ONLY_ONE", "유일"));
            flushAndClear();

            // when
            boolean exists =
                    queryDslRepository.existsByDisplayOrderExcludingId(
                            entity.getDisplayOrder(), entity.getId());

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("findByCriteria 테스트")
    class FindByCriteriaTest {

        private CommonCodeTypeJpaEntity activeType1;
        private CommonCodeTypeJpaEntity activeType2;
        private CommonCodeTypeJpaEntity activeType3;
        private CommonCodeTypeJpaEntity inactiveType;
        private CommonCodeTypeJpaEntity deletedType;

        @BeforeEach
        void setUp() {
            activeType1 =
                    jpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode(
                                    "PAYMENT_METHOD", "결제수단"));
            activeType2 =
                    jpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode(
                                    "ORDER_STATUS", "주문상태"));
            activeType3 =
                    jpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode(
                                    "DELIVERY_STATUS", "배송상태"));
            inactiveType = jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newInactiveEntity());
            deletedType = jpaRepository.save(createDeletedEntity());
            flushAndClear();
        }

        private CommonCodeTypeJpaEntity createDeletedEntity() {
            java.time.Instant now = java.time.Instant.now();
            return CommonCodeTypeJpaEntity.create(
                    null, "DELETED_CODE", "삭제된타입", "설명", 99, false, now, now, now);
        }

        @Test
        @DisplayName("전체 조회 - 삭제된 항목 제외")
        void shouldFindAllExcludingDeleted() {
            // given
            CommonCodeTypeSearchCriteria criteria = CommonCodeTypeSearchCriteria.defaultCriteria();

            // when
            List<CommonCodeTypeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(4);
            assertThat(result)
                    .extracting(CommonCodeTypeJpaEntity::getId)
                    .doesNotContain(deletedType.getId());
        }

        @Test
        @DisplayName("활성 상태 필터 - 활성 항목만 조회")
        void shouldFilterByActiveStatus() {
            // given
            CommonCodeTypeSearchCriteria criteria = CommonCodeTypeSearchCriteria.activeOnly();

            // when
            List<CommonCodeTypeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).allMatch(CommonCodeTypeJpaEntity::isActive);
        }

        @Test
        @DisplayName("키워드 검색 - 부분 일치")
        void shouldSearchByKeyword() {
            // given
            QueryContext<CommonCodeTypeSortKey> queryContext =
                    QueryContext.defaultOf(CommonCodeTypeSortKey.CREATED_AT);
            CommonCodeTypeSearchCriteria criteria =
                    CommonCodeTypeSearchCriteria.of(null, "상태", queryContext);

            // when
            List<CommonCodeTypeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            // 주문상태, 배송상태 = 2개
            assertThat(result).hasSize(2);
            assertThat(result)
                    .extracting(CommonCodeTypeJpaEntity::getName)
                    .allMatch(name -> name.contains("상태"));
        }

        @Test
        @DisplayName("includeDeleted 옵션 - 삭제된 항목 포함 조회")
        void shouldIncludeDeletedWhenOptionSet() {
            // given
            QueryContext<CommonCodeTypeSortKey> queryContext =
                    QueryContext.of(
                            CommonCodeTypeSortKey.CREATED_AT,
                            SortDirection.DESC,
                            PageRequest.defaultPage(),
                            true);
            CommonCodeTypeSearchCriteria criteria =
                    CommonCodeTypeSearchCriteria.of(null, null, queryContext);

            // when
            List<CommonCodeTypeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(5);
            assertThat(result)
                    .extracting(CommonCodeTypeJpaEntity::getId)
                    .contains(deletedType.getId());
        }

        @Nested
        @DisplayName("정렬 테스트")
        class SortingTest {

            @Test
            @DisplayName("코드 오름차순 정렬")
            void shouldSortByCodeAsc() {
                // given
                QueryContext<CommonCodeTypeSortKey> queryContext =
                        QueryContext.of(
                                CommonCodeTypeSortKey.CODE,
                                SortDirection.ASC,
                                PageRequest.defaultPage(),
                                false);
                CommonCodeTypeSearchCriteria criteria =
                        CommonCodeTypeSearchCriteria.of(true, null, queryContext);

                // when
                List<CommonCodeTypeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(3);
                assertThat(result)
                        .extracting(CommonCodeTypeJpaEntity::getCode)
                        .containsExactly("DELIVERY_STATUS", "ORDER_STATUS", "PAYMENT_METHOD");
            }
        }

        @Nested
        @DisplayName("페이징 테스트")
        class PagingTest {

            @Test
            @DisplayName("첫 페이지 조회")
            void shouldReturnFirstPage() {
                // given
                QueryContext<CommonCodeTypeSortKey> queryContext =
                        QueryContext.of(
                                CommonCodeTypeSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(0, 2),
                                false);
                CommonCodeTypeSearchCriteria criteria =
                        CommonCodeTypeSearchCriteria.of(true, null, queryContext);

                // when
                List<CommonCodeTypeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(2);
            }

            @Test
            @DisplayName("두 번째 페이지 조회")
            void shouldReturnSecondPage() {
                // given
                QueryContext<CommonCodeTypeSortKey> queryContext =
                        QueryContext.of(
                                CommonCodeTypeSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(1, 2),
                                false);
                CommonCodeTypeSearchCriteria criteria =
                        CommonCodeTypeSearchCriteria.of(true, null, queryContext);

                // when
                List<CommonCodeTypeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(1);
            }
        }
    }

    @Nested
    @DisplayName("countByCriteria 테스트")
    class CountByCriteriaTest {

        @BeforeEach
        void setUp() {
            jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntityWithCode("TYPE_1", "타입1"));
            jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntityWithCode("TYPE_2", "타입2"));
            jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntityWithCode("TYPE_3", "타입3"));
            jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newInactiveEntity());

            java.time.Instant now = java.time.Instant.now();
            jpaRepository.save(
                    CommonCodeTypeJpaEntity.create(
                            null, "DELETED", "삭제됨", "설명", 99, false, now, now, now));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 카운트 - 삭제된 항목 제외")
        void shouldCountAllExcludingDeleted() {
            // given
            CommonCodeTypeSearchCriteria criteria = CommonCodeTypeSearchCriteria.defaultCriteria();

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(4);
        }

        @Test
        @DisplayName("활성 상태 필터 카운트")
        void shouldCountByActiveStatus() {
            // given
            CommonCodeTypeSearchCriteria criteria = CommonCodeTypeSearchCriteria.activeOnly();

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("includeDeleted 옵션 카운트")
        void shouldCountIncludingDeletedWhenOptionSet() {
            // given
            QueryContext<CommonCodeTypeSortKey> queryContext =
                    QueryContext.of(
                            CommonCodeTypeSortKey.CREATED_AT,
                            SortDirection.DESC,
                            PageRequest.defaultPage(),
                            true);
            CommonCodeTypeSearchCriteria criteria =
                    CommonCodeTypeSearchCriteria.of(null, null, queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(5);
        }
    }
}
