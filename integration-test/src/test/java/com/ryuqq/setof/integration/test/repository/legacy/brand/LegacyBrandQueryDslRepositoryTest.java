package com.ryuqq.setof.integration.test.repository.legacy.brand;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.query.BrandSortKey;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import com.ryuqq.setof.storage.legacy.brand.LegacyBrandEntityFixtures;
import com.ryuqq.setof.storage.legacy.brand.entity.LegacyBrandEntity;
import com.ryuqq.setof.storage.legacy.brand.repository.LegacyBrandJpaRepository;
import com.ryuqq.setof.storage.legacy.brand.repository.LegacyBrandQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Legacy Brand QueryDSL Repository 통합 테스트.
 *
 * <p>레거시 DB 스키마 기반의 QueryDSL 쿼리 동작을 검증합니다.
 *
 * <ul>
 *   <li>Soft Delete 필터링 (deleteYn = 'N')
 *   <li>동적 검색 조건
 *   <li>정렬 및 페이징
 * </ul>
 */
@Tag(TestTags.LEGACY)
@Tag(TestTags.BRAND)
@DisplayName("레거시 브랜드 QueryDSL Repository 테스트")
class LegacyBrandQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private LegacyBrandJpaRepository jpaRepository;
    @Autowired private LegacyBrandQueryDslRepository queryDslRepository;

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("활성 상태 브랜드 조회 성공")
        void shouldFindActiveEntity() {
            // given
            LegacyBrandEntity entity =
                    jpaRepository.save(LegacyBrandEntityFixtures.builder().id(null).build());
            flushAndClear();

            // when
            Optional<LegacyBrandEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getBrandName())
                    .isEqualTo(LegacyBrandEntityFixtures.DEFAULT_BRAND_NAME);
        }

        @Test
        @DisplayName("삭제된 브랜드는 조회되지 않음 (deleteYn = 'Y')")
        void shouldNotFindDeletedEntity() {
            // given
            LegacyBrandEntity entity =
                    jpaRepository.save(
                            LegacyBrandEntityFixtures.builder().id(null).deleteYn("Y").build());
            flushAndClear();

            // when
            Optional<LegacyBrandEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<LegacyBrandEntity> found = queryDslRepository.findById(999999L);

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
            LegacyBrandEntity entity1 =
                    jpaRepository.save(
                            LegacyBrandEntityFixtures.builder().id(null).brandName("브랜드A").build());
            LegacyBrandEntity entity2 =
                    jpaRepository.save(
                            LegacyBrandEntityFixtures.builder().id(null).brandName("브랜드B").build());
            LegacyBrandEntity entity3 =
                    jpaRepository.save(
                            LegacyBrandEntityFixtures.builder().id(null).brandName("브랜드C").build());
            flushAndClear();

            // when
            List<LegacyBrandEntity> found =
                    queryDslRepository.findByIds(List.of(entity1.getId(), entity3.getId()));

            // then
            assertThat(found).hasSize(2);
            assertThat(found)
                    .extracting(LegacyBrandEntity::getBrandName)
                    .containsExactlyInAnyOrder("브랜드A", "브랜드C");
        }

        @Test
        @DisplayName("삭제된 브랜드는 ID 목록 조회에서 제외됨")
        void shouldExcludeDeletedEntities() {
            // given
            LegacyBrandEntity activeEntity =
                    jpaRepository.save(
                            LegacyBrandEntityFixtures.builder()
                                    .id(null)
                                    .brandName("활성브랜드")
                                    .build());
            LegacyBrandEntity deletedEntity =
                    jpaRepository.save(
                            LegacyBrandEntityFixtures.builder()
                                    .id(null)
                                    .brandName("삭제브랜드")
                                    .deleteYn("Y")
                                    .build());
            flushAndClear();

            // when
            List<LegacyBrandEntity> found =
                    queryDslRepository.findByIds(
                            List.of(activeEntity.getId(), deletedEntity.getId()));

            // then
            assertThat(found).hasSize(1);
            assertThat(found.get(0).getBrandName()).isEqualTo("활성브랜드");
        }
    }

    @Nested
    @DisplayName("existsById 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("활성 브랜드 존재 확인 - true")
        void shouldReturnTrueForActiveEntity() {
            // given
            LegacyBrandEntity entity =
                    jpaRepository.save(LegacyBrandEntityFixtures.builder().id(null).build());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsById(entity.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("삭제된 브랜드는 존재하지 않음으로 판단")
        void shouldReturnFalseForDeletedEntity() {
            // given
            LegacyBrandEntity entity =
                    jpaRepository.save(
                            LegacyBrandEntityFixtures.builder().id(null).deleteYn("Y").build());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsById(entity.getId());

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("findByCriteria 테스트")
    class FindByCriteriaTest {

        private LegacyBrandEntity activeBrand1;
        private LegacyBrandEntity activeBrand2;
        private LegacyBrandEntity inactiveBrand;
        private LegacyBrandEntity deletedBrand;

        @BeforeEach
        void setUp() {
            activeBrand1 =
                    jpaRepository.save(
                            LegacyBrandEntityFixtures.builder()
                                    .id(null)
                                    .brandName("나이키")
                                    .displayKoreanName("나이키 코리아")
                                    .displayOrder(1)
                                    .build());
            activeBrand2 =
                    jpaRepository.save(
                            LegacyBrandEntityFixtures.builder()
                                    .id(null)
                                    .brandName("아디다스")
                                    .displayKoreanName("아디다스 코리아")
                                    .displayOrder(2)
                                    .build());
            inactiveBrand =
                    jpaRepository.save(
                            LegacyBrandEntityFixtures.builder()
                                    .id(null)
                                    .brandName("비활성브랜드")
                                    .displayYn("N")
                                    .displayOrder(3)
                                    .build());
            deletedBrand =
                    jpaRepository.save(
                            LegacyBrandEntityFixtures.builder()
                                    .id(null)
                                    .brandName("삭제브랜드")
                                    .deleteYn("Y")
                                    .displayOrder(4)
                                    .build());
            flushAndClear();
        }

        @Test
        @DisplayName("전체 조회 - 삭제된 브랜드 제외")
        void shouldFindAllExcludingDeleted() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.defaultOf();

            // when
            List<LegacyBrandEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(3);
            assertThat(result)
                    .extracting(LegacyBrandEntity::getId)
                    .doesNotContain(deletedBrand.getId());
        }

        @Test
        @DisplayName("표시 중인 브랜드만 조회 (displayYn = 'Y')")
        void shouldFilterByDisplayedStatus() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.displayedOnly();

            // when
            List<LegacyBrandEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(LegacyBrandEntity::isDisplayed);
        }

        @Test
        @DisplayName("브랜드명 검색 - 부분 일치")
        void shouldSearchByBrandName() {
            // given
            BrandSearchCriteria criteria =
                    BrandSearchCriteria.of(
                            null, null, "다스", QueryContext.defaultOf(BrandSortKey.defaultKey()));

            // when
            List<LegacyBrandEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getBrandName()).isEqualTo("아디다스");
        }
    }

    @Nested
    @DisplayName("countByCriteria 테스트")
    class CountByCriteriaTest {

        @BeforeEach
        void setUp() {
            jpaRepository.save(
                    LegacyBrandEntityFixtures.builder().id(null).brandName("브랜드1").build());
            jpaRepository.save(
                    LegacyBrandEntityFixtures.builder().id(null).brandName("브랜드2").build());
            jpaRepository.save(
                    LegacyBrandEntityFixtures.builder()
                            .id(null)
                            .brandName("비활성브랜드")
                            .displayYn("N")
                            .build());
            jpaRepository.save(
                    LegacyBrandEntityFixtures.builder()
                            .id(null)
                            .brandName("삭제브랜드")
                            .deleteYn("Y")
                            .build());
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
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("표시 중인 브랜드 카운트")
        void shouldCountByDisplayedStatus() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.displayedOnly();

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("findAllDisplayed 테스트")
    class FindAllDisplayedTest {

        @Test
        @DisplayName("노출 중인 브랜드만 조회 (displayYn = 'Y', deleteYn = 'N')")
        void shouldFindOnlyDisplayedBrands() {
            // given
            jpaRepository.save(
                    LegacyBrandEntityFixtures.builder()
                            .id(null)
                            .brandName("노출브랜드1")
                            .displayOrder(1)
                            .build());
            jpaRepository.save(
                    LegacyBrandEntityFixtures.builder()
                            .id(null)
                            .brandName("노출브랜드2")
                            .displayOrder(2)
                            .build());
            jpaRepository.save(
                    LegacyBrandEntityFixtures.builder()
                            .id(null)
                            .brandName("비노출브랜드")
                            .displayYn("N")
                            .displayOrder(3)
                            .build());
            jpaRepository.save(
                    LegacyBrandEntityFixtures.builder()
                            .id(null)
                            .brandName("삭제브랜드")
                            .deleteYn("Y")
                            .displayOrder(4)
                            .build());
            flushAndClear();

            // when
            List<LegacyBrandEntity> result = queryDslRepository.findAllDisplayed();

            // then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(LegacyBrandEntity::isDisplayed);
        }

        @Test
        @DisplayName("표시순서로 정렬되어 반환")
        void shouldReturnSortedByDisplayOrder() {
            // given
            jpaRepository.save(
                    LegacyBrandEntityFixtures.builder()
                            .id(null)
                            .brandName("세번째")
                            .displayOrder(3)
                            .build());
            jpaRepository.save(
                    LegacyBrandEntityFixtures.builder()
                            .id(null)
                            .brandName("첫번째")
                            .displayOrder(1)
                            .build());
            jpaRepository.save(
                    LegacyBrandEntityFixtures.builder()
                            .id(null)
                            .brandName("두번째")
                            .displayOrder(2)
                            .build());
            flushAndClear();

            // when
            List<LegacyBrandEntity> result = queryDslRepository.findAllDisplayed();

            // then
            assertThat(result).hasSize(3);
            assertThat(result)
                    .extracting(LegacyBrandEntity::getBrandName)
                    .containsExactly("첫번째", "두번째", "세번째");
        }
    }
}
