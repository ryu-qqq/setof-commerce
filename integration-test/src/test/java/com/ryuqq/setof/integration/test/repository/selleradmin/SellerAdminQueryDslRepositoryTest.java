package com.ryuqq.setof.integration.test.repository.selleradmin;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.selleradmin.SellerAdminJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.selleradmin.entity.SellerAdminJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.selleradmin.repository.SellerAdminJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.selleradmin.repository.SellerAdminQueryDslRepository;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.selleradmin.query.SellerAdminSearchCriteria;
import com.ryuqq.setof.domain.selleradmin.query.SellerAdminSearchField;
import com.ryuqq.setof.domain.selleradmin.query.SellerAdminSortKey;
import com.ryuqq.setof.domain.selleradmin.vo.SellerAdminStatus;
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
 * SellerAdmin QueryDSL Repository 통합 테스트.
 *
 * <p>QueryDSL 기반의 복잡한 쿼리 동작을 검증합니다.
 *
 * <ul>
 *   <li>Soft Delete 필터링 (deletedAt IS NULL)
 *   <li>동적 검색 조건 (ConditionBuilder)
 *   <li>정렬 및 페이징
 *   <li>비즈니스 조건 (existsByLoginId 등)
 * </ul>
 */
@Tag(TestTags.SELLER_ADMIN)
@DisplayName("셀러 관리자 QueryDSL Repository 테스트")
class SellerAdminQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private SellerAdminJpaRepository jpaRepository;
    @Autowired private SellerAdminQueryDslRepository queryDslRepository;

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("활성 상태 셀러 관리자 조회 성공")
        void shouldFindActiveEntity() {
            // given
            SellerAdminJpaEntity entity =
                    jpaRepository.save(SellerAdminJpaEntityFixtures.activeEntity());
            flushAndClear();

            // when
            Optional<SellerAdminJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getLoginId())
                    .isEqualTo(SellerAdminJpaEntityFixtures.DEFAULT_LOGIN_ID);
        }

        @Test
        @DisplayName("삭제된 셀러 관리자는 조회되지 않음 (Soft Delete 필터)")
        void shouldNotFindDeletedEntity() {
            // given
            SellerAdminJpaEntity entity =
                    jpaRepository.save(
                            SellerAdminJpaEntityFixtures.deletedEntity(
                                    "01956f4a-dead-7d8e-9f0a-1b2c3d4e5f60",
                                    "deleted-find@test.com"));
            flushAndClear();

            // when
            Optional<SellerAdminJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<SellerAdminJpaEntity> found =
                    queryDslRepository.findById("non-existing-id-12345");

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findBySellerIdAndId 테스트")
    class FindBySellerIdAndIdTest {

        @Test
        @DisplayName("셀러 ID와 관리자 ID로 조회 성공")
        void shouldFindBySellerIdAndId() {
            // given
            SellerAdminJpaEntity entity =
                    jpaRepository.save(SellerAdminJpaEntityFixtures.activeEntity());
            flushAndClear();

            // when
            Optional<SellerAdminJpaEntity> found =
                    queryDslRepository.findBySellerIdAndId(
                            SellerAdminJpaEntityFixtures.DEFAULT_SELLER_ID, entity.getId());

            // then
            assertThat(found).isPresent();
        }

        @Test
        @DisplayName("셀러 ID가 다르면 조회되지 않음")
        void shouldNotFindWithDifferentSellerId() {
            // given
            SellerAdminJpaEntity entity =
                    jpaRepository.save(SellerAdminJpaEntityFixtures.activeEntity());
            flushAndClear();

            // when
            Optional<SellerAdminJpaEntity> found =
                    queryDslRepository.findBySellerIdAndId(999L, entity.getId());

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findBySellerIdAndIdAndStatuses 테스트")
    class FindBySellerIdAndIdAndStatusesTest {

        @Test
        @DisplayName("상태 필터와 함께 조회 성공")
        void shouldFindWithStatusFilter() {
            // given
            SellerAdminJpaEntity entity =
                    jpaRepository.save(SellerAdminJpaEntityFixtures.activeEntity());
            flushAndClear();

            // when
            Optional<SellerAdminJpaEntity> found =
                    queryDslRepository.findBySellerIdAndIdAndStatuses(
                            SellerAdminJpaEntityFixtures.DEFAULT_SELLER_ID,
                            entity.getId(),
                            List.of(SellerAdminStatus.ACTIVE));

            // then
            assertThat(found).isPresent();
        }

        @Test
        @DisplayName("상태가 맞지 않으면 조회되지 않음")
        void shouldNotFindWithWrongStatus() {
            // given
            SellerAdminJpaEntity entity =
                    jpaRepository.save(SellerAdminJpaEntityFixtures.activeEntity());
            flushAndClear();

            // when
            Optional<SellerAdminJpaEntity> found =
                    queryDslRepository.findBySellerIdAndIdAndStatuses(
                            SellerAdminJpaEntityFixtures.DEFAULT_SELLER_ID,
                            entity.getId(),
                            List.of(SellerAdminStatus.SUSPENDED));

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByIdAndStatuses 테스트")
    class FindByIdAndStatusesTest {

        @Test
        @DisplayName("ID와 상태로 조회 성공")
        void shouldFindByIdAndStatuses() {
            // given
            SellerAdminJpaEntity entity =
                    jpaRepository.save(SellerAdminJpaEntityFixtures.activeEntity());
            flushAndClear();

            // when
            Optional<SellerAdminJpaEntity> found =
                    queryDslRepository.findByIdAndStatuses(
                            entity.getId(),
                            List.of(SellerAdminStatus.ACTIVE, SellerAdminStatus.PENDING_APPROVAL));

            // then
            assertThat(found).isPresent();
        }
    }

    @Nested
    @DisplayName("existsByLoginId 테스트")
    class ExistsByLoginIdTest {

        @Test
        @DisplayName("존재하는 로그인 ID 확인 - true")
        void shouldReturnTrueForExistingLoginId() {
            // given
            jpaRepository.save(SellerAdminJpaEntityFixtures.activeEntity());
            flushAndClear();

            // when
            boolean exists =
                    queryDslRepository.existsByLoginId(
                            SellerAdminJpaEntityFixtures.DEFAULT_LOGIN_ID);

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("삭제된 관리자의 로그인 ID는 존재하지 않음으로 판단")
        void shouldReturnFalseForDeletedAdminLoginId() {
            // given
            jpaRepository.save(
                    SellerAdminJpaEntityFixtures.deletedEntity(
                            "01956f4a-del1-7d8e-9f0a-1b2c3d4e5f60", "deleted-exists@test.com"));
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsByLoginId("deleted-exists@test.com");

            // then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("존재하지 않는 로그인 ID - false")
        void shouldReturnFalseForNonExistentLoginId() {
            // when
            boolean exists = queryDslRepository.existsByLoginId("non-existing@test.com");

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("findByCriteria 테스트")
    class FindByCriteriaTest {

        private SellerAdminJpaEntity activeAdmin1;
        private SellerAdminJpaEntity activeAdmin2;
        private SellerAdminJpaEntity activeAdmin3;
        private SellerAdminJpaEntity pendingAdmin;
        private SellerAdminJpaEntity deletedAdmin;

        @BeforeEach
        void setUp() {
            activeAdmin1 =
                    jpaRepository.save(
                            SellerAdminJpaEntityFixtures.newEntityWithNameAndLoginId(
                                    "01956f4a-0001-7d8e-9f0a-1b2c3d4e5f60",
                                    "홍길동",
                                    "hong@test.com"));
            activeAdmin2 =
                    jpaRepository.save(
                            SellerAdminJpaEntityFixtures.newEntityWithNameAndLoginId(
                                    "01956f4a-0002-7d8e-9f0a-1b2c3d4e5f60", "김철수", "kim@test.com"));
            activeAdmin3 =
                    jpaRepository.save(
                            SellerAdminJpaEntityFixtures.newEntityWithNameAndLoginId(
                                    "01956f4a-0003-7d8e-9f0a-1b2c3d4e5f60", "이영희", "lee@test.com"));
            pendingAdmin =
                    jpaRepository.save(
                            SellerAdminJpaEntityFixtures.pendingApprovalEntity(
                                    "01956f4a-0004-7d8e-9f0a-1b2c3d4e5f60",
                                    "pending-criteria@test.com"));
            deletedAdmin =
                    jpaRepository.save(
                            SellerAdminJpaEntityFixtures.deletedEntity(
                                    "01956f4a-0005-7d8e-9f0a-1b2c3d4e5f60",
                                    "deleted-criteria@test.com"));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 조회 - 삭제된 관리자 제외")
        void shouldFindAllExcludingDeleted() {
            // given
            SellerAdminSearchCriteria criteria = SellerAdminSearchCriteria.defaultCriteria();

            // when
            List<SellerAdminJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(4);
            assertThat(result)
                    .extracting(SellerAdminJpaEntity::getId)
                    .doesNotContain(deletedAdmin.getId());
        }

        @Test
        @DisplayName("승인 대기 상태만 조회")
        void shouldFilterByPendingStatus() {
            // given
            SellerAdminSearchCriteria criteria = SellerAdminSearchCriteria.pendingOnly();

            // when
            List<SellerAdminJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo(SellerAdminStatus.PENDING_APPROVAL);
        }

        @Test
        @DisplayName("활성 상태만 조회")
        void shouldFilterByActiveStatus() {
            // given
            QueryContext<SellerAdminSortKey> queryContext =
                    QueryContext.defaultOf(SellerAdminSortKey.CREATED_AT);
            SellerAdminSearchCriteria criteria =
                    SellerAdminSearchCriteria.of(
                            null,
                            List.of(SellerAdminStatus.ACTIVE),
                            null,
                            null,
                            null,
                            queryContext);

            // when
            List<SellerAdminJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).allMatch(e -> e.getStatus() == SellerAdminStatus.ACTIVE);
        }

        @Test
        @DisplayName("이름으로 검색")
        void shouldSearchByName() {
            // given
            QueryContext<SellerAdminSortKey> queryContext =
                    QueryContext.defaultOf(SellerAdminSortKey.CREATED_AT);
            SellerAdminSearchCriteria criteria =
                    SellerAdminSearchCriteria.of(
                            null,
                            List.of(),
                            SellerAdminSearchField.NAME,
                            "홍길동",
                            null,
                            queryContext);

            // when
            List<SellerAdminJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("로그인 ID로 검색")
        void shouldSearchByLoginId() {
            // given
            QueryContext<SellerAdminSortKey> queryContext =
                    QueryContext.defaultOf(SellerAdminSortKey.CREATED_AT);
            SellerAdminSearchCriteria criteria =
                    SellerAdminSearchCriteria.of(
                            null,
                            List.of(),
                            SellerAdminSearchField.LOGIN_ID,
                            "kim",
                            null,
                            queryContext);

            // when
            List<SellerAdminJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getLoginId()).contains("kim");
        }

        @Test
        @DisplayName("통합 검색 - 필드 미지정 시 이름/로그인ID 모두 검색")
        void shouldSearchAllFieldsWhenNoFieldSpecified() {
            // given
            QueryContext<SellerAdminSortKey> queryContext =
                    QueryContext.defaultOf(SellerAdminSortKey.CREATED_AT);
            SellerAdminSearchCriteria criteria =
                    SellerAdminSearchCriteria.of(null, List.of(), null, "hong", null, queryContext);

            // when
            List<SellerAdminJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
        }

        @Nested
        @DisplayName("정렬 테스트")
        class SortingTest {

            @Test
            @DisplayName("등록일시 내림차순 정렬 (최신순)")
            void shouldSortByCreatedAtDesc() {
                // given
                QueryContext<SellerAdminSortKey> queryContext =
                        QueryContext.of(
                                SellerAdminSortKey.CREATED_AT,
                                SortDirection.DESC,
                                PageRequest.defaultPage(),
                                false);
                SellerAdminSearchCriteria criteria =
                        SellerAdminSearchCriteria.of(
                                null,
                                List.of(SellerAdminStatus.ACTIVE),
                                null,
                                null,
                                null,
                                queryContext);

                // when
                List<SellerAdminJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(3);
            }

            @Test
            @DisplayName("등록일시 오름차순 정렬 (오래된순)")
            void shouldSortByCreatedAtAsc() {
                // given
                QueryContext<SellerAdminSortKey> queryContext =
                        QueryContext.of(
                                SellerAdminSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.defaultPage(),
                                false);
                SellerAdminSearchCriteria criteria =
                        SellerAdminSearchCriteria.of(
                                null,
                                List.of(SellerAdminStatus.ACTIVE),
                                null,
                                null,
                                null,
                                queryContext);

                // when
                List<SellerAdminJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(3);
            }
        }

        @Nested
        @DisplayName("페이징 테스트")
        class PagingTest {

            @Test
            @DisplayName("첫 페이지 조회")
            void shouldReturnFirstPage() {
                // given
                QueryContext<SellerAdminSortKey> queryContext =
                        QueryContext.of(
                                SellerAdminSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(0, 2),
                                false);
                SellerAdminSearchCriteria criteria =
                        SellerAdminSearchCriteria.of(
                                null,
                                List.of(SellerAdminStatus.ACTIVE),
                                null,
                                null,
                                null,
                                queryContext);

                // when
                List<SellerAdminJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(2);
            }

            @Test
            @DisplayName("두 번째 페이지 조회")
            void shouldReturnSecondPage() {
                // given
                QueryContext<SellerAdminSortKey> queryContext =
                        QueryContext.of(
                                SellerAdminSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(1, 2),
                                false);
                SellerAdminSearchCriteria criteria =
                        SellerAdminSearchCriteria.of(
                                null,
                                List.of(SellerAdminStatus.ACTIVE),
                                null,
                                null,
                                null,
                                queryContext);

                // when
                List<SellerAdminJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(1);
            }

            @Test
            @DisplayName("페이지 범위 초과시 빈 목록 반환")
            void shouldReturnEmptyWhenPageExceeds() {
                // given
                QueryContext<SellerAdminSortKey> queryContext =
                        QueryContext.of(
                                SellerAdminSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(10, 20),
                                false);
                SellerAdminSearchCriteria criteria =
                        SellerAdminSearchCriteria.of(
                                null,
                                List.of(SellerAdminStatus.ACTIVE),
                                null,
                                null,
                                null,
                                queryContext);

                // when
                List<SellerAdminJpaEntity> result = queryDslRepository.findByCriteria(criteria);

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
            jpaRepository.save(
                    SellerAdminJpaEntityFixtures.newActiveEntityWithLoginId(
                            "01956f4a-c001-7d8e-9f0a-1b2c3d4e5f60", "count1@test.com"));
            jpaRepository.save(
                    SellerAdminJpaEntityFixtures.newActiveEntityWithLoginId(
                            "01956f4a-c002-7d8e-9f0a-1b2c3d4e5f60", "count2@test.com"));
            jpaRepository.save(
                    SellerAdminJpaEntityFixtures.newActiveEntityWithLoginId(
                            "01956f4a-c003-7d8e-9f0a-1b2c3d4e5f60", "count3@test.com"));
            jpaRepository.save(
                    SellerAdminJpaEntityFixtures.pendingApprovalEntity(
                            "01956f4a-c004-7d8e-9f0a-1b2c3d4e5f60", "count-pending@test.com"));
            jpaRepository.save(
                    SellerAdminJpaEntityFixtures.deletedEntity(
                            "01956f4a-c005-7d8e-9f0a-1b2c3d4e5f60", "count-deleted@test.com"));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 카운트 - 삭제된 관리자 제외")
        void shouldCountAllExcludingDeleted() {
            // given
            SellerAdminSearchCriteria criteria = SellerAdminSearchCriteria.defaultCriteria();

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(4);
        }

        @Test
        @DisplayName("활성 상태 필터 카운트")
        void shouldCountByActiveStatus() {
            // given
            QueryContext<SellerAdminSortKey> queryContext =
                    QueryContext.defaultOf(SellerAdminSortKey.CREATED_AT);
            SellerAdminSearchCriteria criteria =
                    SellerAdminSearchCriteria.of(
                            null,
                            List.of(SellerAdminStatus.ACTIVE),
                            null,
                            null,
                            null,
                            queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("승인 대기 상태 카운트")
        void shouldCountByPendingStatus() {
            // given
            SellerAdminSearchCriteria criteria = SellerAdminSearchCriteria.pendingOnly();

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(1);
        }

        @Test
        @DisplayName("검색 조건 카운트")
        void shouldCountBySearchCondition() {
            // given
            QueryContext<SellerAdminSortKey> queryContext =
                    QueryContext.defaultOf(SellerAdminSortKey.CREATED_AT);
            SellerAdminSearchCriteria criteria =
                    SellerAdminSearchCriteria.of(
                            null,
                            List.of(),
                            SellerAdminSearchField.LOGIN_ID,
                            "count",
                            null,
                            queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            // count1, count2, count3, count-pending = 4 (count-deleted는 제외)
            assertThat(count).isEqualTo(4);
        }

        @Test
        @DisplayName("조건에 맞는 데이터 없을 시 0 반환")
        void shouldReturnZeroWhenNoMatch() {
            // given
            QueryContext<SellerAdminSortKey> queryContext =
                    QueryContext.defaultOf(SellerAdminSortKey.CREATED_AT);
            SellerAdminSearchCriteria criteria =
                    SellerAdminSearchCriteria.of(
                            null,
                            List.of(),
                            SellerAdminSearchField.LOGIN_ID,
                            "nonexistent",
                            null,
                            queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isZero();
        }
    }
}
