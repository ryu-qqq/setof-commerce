package com.ryuqq.setof.integration.test.repository.commoncode;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.commoncode.CommonCodeJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.commoncode.entity.CommonCodeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.commoncode.repository.CommonCodeJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.commoncode.repository.CommonCodeQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.CommonCodeTypeJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.entity.CommonCodeTypeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.repository.CommonCodeTypeJpaRepository;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.commoncode.query.CommonCodeSearchCriteria;
import com.ryuqq.setof.domain.commoncode.query.CommonCodeSortKey;
import com.ryuqq.setof.domain.commoncodetype.id.CommonCodeTypeId;
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
 * CommonCode QueryDSL Repository 통합 테스트.
 *
 * <p>QueryDSL 기반의 복잡한 쿼리 동작을 검증합니다.
 *
 * <ul>
 *   <li>Soft Delete 필터링 (deletedAt IS NULL)
 *   <li>타입 ID별 조회
 *   <li>동적 검색 조건 (ConditionBuilder)
 *   <li>정렬 및 페이징
 * </ul>
 */
@Tag(TestTags.COMMON_CODE)
@DisplayName("공통코드 QueryDSL Repository 테스트")
class CommonCodeQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private CommonCodeJpaRepository jpaRepository;
    @Autowired private CommonCodeQueryDslRepository queryDslRepository;
    @Autowired private CommonCodeTypeJpaRepository typeJpaRepository;

    private CommonCodeTypeJpaEntity codeType;

    @BeforeEach
    void setUpCodeType() {
        codeType =
                typeJpaRepository.save(
                        CommonCodeTypeJpaEntityFixtures.newEntityWithCode(
                                "PAYMENT_METHOD", "결제수단"));
        flushAndClear();
    }

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("활성 상태 공통코드 조회 성공")
        void shouldFindActiveEntity() {
            // given
            CommonCodeJpaEntity entity =
                    jpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                                    codeType.getId(), "CREDIT_CARD", "신용카드"));
            flushAndClear();

            // when
            Optional<CommonCodeJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getCode()).isEqualTo("CREDIT_CARD");
        }

        @Test
        @DisplayName("삭제된 공통코드는 조회되지 않음 (Soft Delete 필터)")
        void shouldNotFindDeletedEntity() {
            // given
            CommonCodeJpaEntity entity = jpaRepository.save(createDeletedEntity(codeType.getId()));
            flushAndClear();

            // when
            Optional<CommonCodeJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<CommonCodeJpaEntity> found = queryDslRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByIds 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 공통코드 목록 조회 성공")
        void shouldFindByIds() {
            // given
            CommonCodeJpaEntity entity1 =
                    jpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                                    codeType.getId(), "CODE_1", "코드1"));
            CommonCodeJpaEntity entity2 =
                    jpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                                    codeType.getId(), "CODE_2", "코드2"));
            CommonCodeJpaEntity entity3 =
                    jpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                                    codeType.getId(), "CODE_3", "코드3"));
            flushAndClear();

            // when
            List<CommonCodeJpaEntity> found =
                    queryDslRepository.findByIds(List.of(entity1.getId(), entity3.getId()));

            // then
            assertThat(found).hasSize(2);
            assertThat(found)
                    .extracting(CommonCodeJpaEntity::getCode)
                    .containsExactlyInAnyOrder("CODE_1", "CODE_3");
        }

        @Test
        @DisplayName("삭제된 공통코드는 ID 목록 조회에서 제외됨")
        void shouldExcludeDeletedEntities() {
            // given
            CommonCodeJpaEntity activeEntity =
                    jpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                                    codeType.getId(), "ACTIVE_CODE", "활성코드"));
            CommonCodeJpaEntity deletedEntity =
                    jpaRepository.save(createDeletedEntity(codeType.getId()));
            flushAndClear();

            // when
            List<CommonCodeJpaEntity> found =
                    queryDslRepository.findByIds(
                            List.of(activeEntity.getId(), deletedEntity.getId()));

            // then
            assertThat(found).hasSize(1);
            assertThat(found.get(0).getCode()).isEqualTo("ACTIVE_CODE");
        }
    }

    @Nested
    @DisplayName("existsByCommonCodeTypeIdAndCode 테스트")
    class ExistsByCommonCodeTypeIdAndCodeTest {

        @Test
        @DisplayName("존재하는 타입ID + 코드 조합 확인 - true")
        void shouldReturnTrueForExistingCombination() {
            // given
            jpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            codeType.getId(), "UNIQUE_CODE", "유니크코드"));
            flushAndClear();

            // when
            boolean exists =
                    queryDslRepository.existsByCommonCodeTypeIdAndCode(
                            codeType.getId(), "UNIQUE_CODE");

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("삭제된 공통코드는 존재하지 않음으로 판단")
        void shouldReturnFalseForDeletedEntity() {
            // given
            jpaRepository.save(createDeletedEntity(codeType.getId()));
            flushAndClear();

            // when
            boolean exists =
                    queryDslRepository.existsByCommonCodeTypeIdAndCode(
                            codeType.getId(), "DELETED_CODE");

            // then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("존재하지 않는 코드 - false")
        void shouldReturnFalseForNonExistentCode() {
            // when
            boolean exists =
                    queryDslRepository.existsByCommonCodeTypeIdAndCode(
                            codeType.getId(), "NON_EXISTENT");

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("existsActiveByCommonCodeTypeId 테스트")
    class ExistsActiveByCommonCodeTypeIdTest {

        @Test
        @DisplayName("활성화된 공통코드 존재 - true")
        void shouldReturnTrueWhenActiveCodeExists() {
            // given
            jpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            codeType.getId(), "ACTIVE_CODE", "활성코드"));
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsActiveByCommonCodeTypeId(codeType.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("비활성화된 공통코드만 있으면 - false")
        void shouldReturnFalseWhenOnlyInactiveCodeExists() {
            // given
            jpaRepository.save(createInactiveEntity(codeType.getId()));
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsActiveByCommonCodeTypeId(codeType.getId());

            // then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("공통코드가 없으면 - false")
        void shouldReturnFalseWhenNoCodeExists() {
            // when
            boolean exists = queryDslRepository.existsActiveByCommonCodeTypeId(codeType.getId());

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("findByCriteria 테스트")
    class FindByCriteriaTest {

        private CommonCodeJpaEntity activeCode1;
        private CommonCodeJpaEntity activeCode2;
        private CommonCodeJpaEntity activeCode3;
        private CommonCodeJpaEntity inactiveCode;
        private CommonCodeJpaEntity deletedCode;

        @BeforeEach
        void setUp() {
            activeCode1 =
                    jpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                                    codeType.getId(), "CREDIT_CARD", "신용카드"));
            activeCode2 =
                    jpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                                    codeType.getId(), "DEBIT_CARD", "체크카드"));
            activeCode3 =
                    jpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                                    codeType.getId(), "BANK_TRANSFER", "계좌이체"));
            inactiveCode = jpaRepository.save(createInactiveEntity(codeType.getId()));
            deletedCode = jpaRepository.save(createDeletedEntity(codeType.getId()));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 조회 - 삭제된 공통코드 제외")
        void shouldFindAllExcludingDeleted() {
            // given
            CommonCodeSearchCriteria criteria =
                    CommonCodeSearchCriteria.defaultOf(CommonCodeTypeId.of(codeType.getId()));

            // when
            List<CommonCodeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(4);
            assertThat(result)
                    .extracting(CommonCodeJpaEntity::getId)
                    .doesNotContain(deletedCode.getId());
        }

        @Test
        @DisplayName("활성 상태 필터 - 활성 공통코드만 조회")
        void shouldFilterByActiveStatus() {
            // given
            CommonCodeSearchCriteria criteria =
                    CommonCodeSearchCriteria.activeOnly(CommonCodeTypeId.of(codeType.getId()));

            // when
            List<CommonCodeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).allMatch(CommonCodeJpaEntity::isActive);
        }

        @Test
        @DisplayName("코드 검색 - 부분 일치 (대문자 변환)")
        void shouldSearchByCode() {
            // given
            QueryContext<CommonCodeSortKey> queryContext =
                    QueryContext.defaultOf(CommonCodeSortKey.CREATED_AT);
            CommonCodeSearchCriteria criteria =
                    CommonCodeSearchCriteria.of(
                            CommonCodeTypeId.of(codeType.getId()), null, "card", queryContext);

            // when
            List<CommonCodeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            // CREDIT_CARD, DEBIT_CARD = 2개
            assertThat(result).hasSize(2);
            assertThat(result)
                    .extracting(CommonCodeJpaEntity::getCode)
                    .allMatch(code -> code.contains("CARD"));
        }

        @Nested
        @DisplayName("정렬 테스트")
        class SortingTest {

            @Test
            @DisplayName("코드 오름차순 정렬")
            void shouldSortByCodeAsc() {
                // given
                QueryContext<CommonCodeSortKey> queryContext =
                        QueryContext.of(
                                CommonCodeSortKey.CODE,
                                SortDirection.ASC,
                                PageRequest.defaultPage(),
                                false);
                CommonCodeSearchCriteria criteria =
                        CommonCodeSearchCriteria.of(
                                CommonCodeTypeId.of(codeType.getId()), true, null, queryContext);

                // when
                List<CommonCodeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(3);
                assertThat(result)
                        .extracting(CommonCodeJpaEntity::getCode)
                        .containsExactly("BANK_TRANSFER", "CREDIT_CARD", "DEBIT_CARD");
            }
        }

        @Nested
        @DisplayName("페이징 테스트")
        class PagingTest {

            @Test
            @DisplayName("첫 페이지 조회")
            void shouldReturnFirstPage() {
                // given
                QueryContext<CommonCodeSortKey> queryContext =
                        QueryContext.of(
                                CommonCodeSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(0, 2),
                                false);
                CommonCodeSearchCriteria criteria =
                        CommonCodeSearchCriteria.of(
                                CommonCodeTypeId.of(codeType.getId()), true, null, queryContext);

                // when
                List<CommonCodeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(2);
            }

            @Test
            @DisplayName("두 번째 페이지 조회")
            void shouldReturnSecondPage() {
                // given
                QueryContext<CommonCodeSortKey> queryContext =
                        QueryContext.of(
                                CommonCodeSortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(1, 2),
                                false);
                CommonCodeSearchCriteria criteria =
                        CommonCodeSearchCriteria.of(
                                CommonCodeTypeId.of(codeType.getId()), true, null, queryContext);

                // when
                List<CommonCodeJpaEntity> result = queryDslRepository.findByCriteria(criteria);

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
            jpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            codeType.getId(), "CODE_1", "코드1"));
            jpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            codeType.getId(), "CODE_2", "코드2"));
            jpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            codeType.getId(), "CODE_3", "코드3"));
            jpaRepository.save(createInactiveEntity(codeType.getId()));
            jpaRepository.save(createDeletedEntity(codeType.getId()));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 카운트 - 삭제된 공통코드 제외")
        void shouldCountAllExcludingDeleted() {
            // given
            CommonCodeSearchCriteria criteria =
                    CommonCodeSearchCriteria.defaultOf(CommonCodeTypeId.of(codeType.getId()));

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(4);
        }

        @Test
        @DisplayName("활성 상태 필터 카운트")
        void shouldCountByActiveStatus() {
            // given
            CommonCodeSearchCriteria criteria =
                    CommonCodeSearchCriteria.activeOnly(CommonCodeTypeId.of(codeType.getId()));

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("조건에 맞는 데이터 없을 시 0 반환")
        void shouldReturnZeroWhenNoMatch() {
            // given
            QueryContext<CommonCodeSortKey> queryContext =
                    QueryContext.defaultOf(CommonCodeSortKey.CREATED_AT);
            CommonCodeSearchCriteria criteria =
                    CommonCodeSearchCriteria.of(
                            CommonCodeTypeId.of(codeType.getId()),
                            null,
                            "NON_EXISTENT",
                            queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isZero();
        }
    }

    private CommonCodeJpaEntity createInactiveEntity(Long typeId) {
        Instant now = Instant.now();
        return CommonCodeJpaEntity.create(
                null, typeId, "INACTIVE_CODE", "비활성코드", 99, false, now, now, null);
    }

    private CommonCodeJpaEntity createDeletedEntity(Long typeId) {
        Instant now = Instant.now();
        return CommonCodeJpaEntity.create(
                null, typeId, "DELETED_CODE", "삭제된코드", 99, false, now, now, now);
    }
}
