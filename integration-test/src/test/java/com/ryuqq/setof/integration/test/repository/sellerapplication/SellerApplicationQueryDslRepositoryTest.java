package com.ryuqq.setof.integration.test.repository.sellerapplication;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.sellerapplication.SellerApplicationJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.sellerapplication.entity.SellerApplicationJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.sellerapplication.entity.SellerApplicationJpaEntity.ApplicationStatusJpaValue;
import com.ryuqq.setof.adapter.out.persistence.sellerapplication.repository.SellerApplicationJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.sellerapplication.repository.SellerApplicationQueryDslRepository;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.sellerapplication.query.SellerApplicationSearchCriteria;
import com.ryuqq.setof.domain.sellerapplication.query.SellerApplicationSearchField;
import com.ryuqq.setof.domain.sellerapplication.query.SellerApplicationSortKey;
import com.ryuqq.setof.domain.sellerapplication.vo.ApplicationStatus;
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
 * SellerApplication QueryDSL Repository 통합 테스트.
 *
 * <p>QueryDSL 기반의 복잡한 쿼리 동작을 검증합니다.
 */
@Tag(TestTags.SELLER_APPLICATION)
@DisplayName("입점신청 QueryDSL Repository 테스트")
class SellerApplicationQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private SellerApplicationJpaRepository jpaRepository;
    @Autowired private SellerApplicationQueryDslRepository queryDslRepository;

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 입점신청 조회 성공")
        void shouldFindById() {
            // given
            SellerApplicationJpaEntity entity =
                    jpaRepository.save(SellerApplicationJpaEntityFixtures.pendingEntity());
            flushAndClear();

            // when
            Optional<SellerApplicationJpaEntity> found =
                    queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getSellerName())
                    .isEqualTo(SellerApplicationJpaEntityFixtures.DEFAULT_SELLER_NAME);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<SellerApplicationJpaEntity> found = queryDslRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsById 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 ID 확인 - true")
        void shouldReturnTrueForExistingId() {
            // given
            SellerApplicationJpaEntity entity =
                    jpaRepository.save(SellerApplicationJpaEntityFixtures.pendingEntity());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsById(entity.getId());

            // then
            assertThat(exists).isTrue();
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
    @DisplayName("existsPendingByRegistrationNumber 테스트")
    class ExistsPendingByRegistrationNumberTest {

        @Test
        @DisplayName("대기 중인 신청이 있으면 true")
        void shouldReturnTrueForPendingApplication() {
            // given
            jpaRepository.save(
                    SellerApplicationJpaEntityFixtures.pendingEntityWithRegistrationNumber(
                            "111-22-33333"));
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsPendingByRegistrationNumber("111-22-33333");

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("승인된 신청만 있으면 false")
        void shouldReturnFalseForApprovedApplication() {
            // given
            jpaRepository.save(SellerApplicationJpaEntityFixtures.approvedEntity(1L));
            flushAndClear();

            // when
            boolean exists =
                    queryDslRepository.existsPendingByRegistrationNumber(
                            SellerApplicationJpaEntityFixtures.DEFAULT_REGISTRATION_NUMBER);

            // then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("존재하지 않는 사업자번호 - false")
        void shouldReturnFalseForNonExistentNumber() {
            // when
            boolean exists = queryDslRepository.existsPendingByRegistrationNumber("000-00-00000");

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("findByCriteria 테스트")
    class FindByCriteriaTest {

        private SellerApplicationJpaEntity pendingApp1;
        private SellerApplicationJpaEntity pendingApp2;
        private SellerApplicationJpaEntity approvedApp;
        private SellerApplicationJpaEntity rejectedApp;

        @BeforeEach
        void setUp() {
            pendingApp1 =
                    jpaRepository.save(
                            SellerApplicationJpaEntityFixtures.pendingEntityWithCustomInfo(
                                    "ABC셀러", "111-11-11111"));
            pendingApp2 =
                    jpaRepository.save(
                            SellerApplicationJpaEntityFixtures.pendingEntityWithCustomInfo(
                                    "XYZ셀러", "222-22-22222"));
            approvedApp = jpaRepository.save(SellerApplicationJpaEntityFixtures.approvedEntity(1L));
            rejectedApp = jpaRepository.save(SellerApplicationJpaEntityFixtures.rejectedEntity());
            flushAndClear();
        }

        @Test
        @DisplayName("전체 조회")
        void shouldFindAll() {
            // given
            SellerApplicationSearchCriteria criteria =
                    SellerApplicationSearchCriteria.defaultCriteria();

            // when
            List<SellerApplicationJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(4);
        }

        @Test
        @DisplayName("상태 필터 - PENDING만 조회")
        void shouldFilterByPendingStatus() {
            // given
            SellerApplicationSearchCriteria criteria =
                    SellerApplicationSearchCriteria.pendingOnly();

            // when
            List<SellerApplicationJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(e -> e.getStatus() == ApplicationStatusJpaValue.PENDING);
        }

        @Test
        @DisplayName("상태 필터 - APPROVED만 조회")
        void shouldFilterByApprovedStatus() {
            // given
            QueryContext<SellerApplicationSortKey> queryContext =
                    QueryContext.defaultOf(SellerApplicationSortKey.APPLIED_AT);
            SellerApplicationSearchCriteria criteria =
                    SellerApplicationSearchCriteria.of(
                            ApplicationStatus.APPROVED, null, null, queryContext);

            // when
            List<SellerApplicationJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo(ApplicationStatusJpaValue.APPROVED);
        }

        @Test
        @DisplayName("회사명 검색")
        void shouldSearchByCompanyName() {
            // given
            QueryContext<SellerApplicationSortKey> queryContext =
                    QueryContext.defaultOf(SellerApplicationSortKey.APPLIED_AT);
            SellerApplicationSearchCriteria criteria =
                    SellerApplicationSearchCriteria.of(
                            null,
                            SellerApplicationSearchField.COMPANY_NAME,
                            "테스트컴퍼니",
                            queryContext);

            // when
            List<SellerApplicationJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            // 모든 fixtures가 DEFAULT_COMPANY_NAME("테스트컴퍼니")을 사용
            assertThat(result).hasSize(4);
        }

        @Nested
        @DisplayName("정렬 테스트")
        class SortingTest {

            @Test
            @DisplayName("신청일시 내림차순 정렬")
            void shouldSortByAppliedAtDesc() {
                // given
                QueryContext<SellerApplicationSortKey> queryContext =
                        QueryContext.of(
                                SellerApplicationSortKey.APPLIED_AT,
                                SortDirection.DESC,
                                PageRequest.defaultPage(),
                                false);
                SellerApplicationSearchCriteria criteria =
                        SellerApplicationSearchCriteria.of(null, null, null, queryContext);

                // when
                List<SellerApplicationJpaEntity> result =
                        queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(4);
                // 최신 신청이 먼저 나와야 함
                for (int i = 0; i < result.size() - 1; i++) {
                    assertThat(result.get(i).getAppliedAt())
                            .isAfterOrEqualTo(result.get(i + 1).getAppliedAt());
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
                QueryContext<SellerApplicationSortKey> queryContext =
                        QueryContext.of(
                                SellerApplicationSortKey.APPLIED_AT,
                                SortDirection.DESC,
                                PageRequest.of(0, 2),
                                false);
                SellerApplicationSearchCriteria criteria =
                        SellerApplicationSearchCriteria.of(null, null, null, queryContext);

                // when
                List<SellerApplicationJpaEntity> result =
                        queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(2);
            }

            @Test
            @DisplayName("두 번째 페이지 조회")
            void shouldReturnSecondPage() {
                // given
                QueryContext<SellerApplicationSortKey> queryContext =
                        QueryContext.of(
                                SellerApplicationSortKey.APPLIED_AT,
                                SortDirection.DESC,
                                PageRequest.of(1, 2),
                                false);
                SellerApplicationSearchCriteria criteria =
                        SellerApplicationSearchCriteria.of(null, null, null, queryContext);

                // when
                List<SellerApplicationJpaEntity> result =
                        queryDslRepository.findByCriteria(criteria);

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
                    SellerApplicationJpaEntityFixtures.pendingEntityWithCustomInfo(
                            "셀러1", "111-11-11111"));
            jpaRepository.save(
                    SellerApplicationJpaEntityFixtures.pendingEntityWithCustomInfo(
                            "셀러2", "222-22-22222"));
            jpaRepository.save(
                    SellerApplicationJpaEntityFixtures.pendingEntityWithCustomInfo(
                            "셀러3", "333-33-33333"));
            jpaRepository.save(SellerApplicationJpaEntityFixtures.approvedEntity(1L));
            jpaRepository.save(SellerApplicationJpaEntityFixtures.rejectedEntity());
            flushAndClear();
        }

        @Test
        @DisplayName("전체 카운트")
        void shouldCountAll() {
            // given
            SellerApplicationSearchCriteria criteria =
                    SellerApplicationSearchCriteria.defaultCriteria();

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(5);
        }

        @Test
        @DisplayName("상태 필터 카운트 - PENDING")
        void shouldCountByPendingStatus() {
            // given
            SellerApplicationSearchCriteria criteria =
                    SellerApplicationSearchCriteria.pendingOnly();

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("상태 필터 카운트 - REJECTED")
        void shouldCountByRejectedStatus() {
            // given
            QueryContext<SellerApplicationSortKey> queryContext =
                    QueryContext.defaultOf(SellerApplicationSortKey.APPLIED_AT);
            SellerApplicationSearchCriteria criteria =
                    SellerApplicationSearchCriteria.of(
                            ApplicationStatus.REJECTED, null, null, queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(1);
        }
    }
}
