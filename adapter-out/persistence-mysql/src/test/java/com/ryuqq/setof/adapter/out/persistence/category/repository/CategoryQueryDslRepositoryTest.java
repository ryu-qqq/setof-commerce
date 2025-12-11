package com.ryuqq.setof.adapter.out.persistence.category.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.common.JpaSliceTestSupport;
import com.ryuqq.setof.domain.category.query.criteria.CategorySearchCriteria;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * CategoryQueryDslRepository Slice 테스트
 *
 * <p>QueryDSL 기반 Category 조회 쿼리를 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("CategoryQueryDslRepository Slice 테스트")
@Import(CategoryQueryDslRepository.class)
class CategoryQueryDslRepositoryTest extends JpaSliceTestSupport {

    @Autowired private CategoryQueryDslRepository categoryQueryDslRepository;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("findById 메서드")
    class FindById {

        private CategoryJpaEntity savedCategory;

        @BeforeEach
        void setUp() {
            savedCategory =
                    persistAndFlush(
                            CategoryJpaEntity.of(
                                    null, "FASHION", "패션", null, 0, "/1/", 1, false, "ACTIVE", NOW,
                                    NOW));
        }

        @Test
        @DisplayName("성공 - ID로 Category를 조회한다")
        void findById_existingId_returnsCategory() {
            // When
            Optional<CategoryJpaEntity> result =
                    categoryQueryDslRepository.findById(savedCategory.getId());

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getCode()).isEqualTo("FASHION");
            assertThat(result.get().getNameKo()).isEqualTo("패션");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_nonExistingId_returnsEmpty() {
            // When
            Optional<CategoryJpaEntity> result = categoryQueryDslRepository.findById(9999L);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByCode 메서드")
    class FindByCode {

        @BeforeEach
        void setUp() {
            persistAndFlush(
                    CategoryJpaEntity.of(
                            null, "FASHION", "패션", null, 0, "/1/", 1, false, "ACTIVE", NOW, NOW));
            persistAndFlush(
                    CategoryJpaEntity.of(
                            null,
                            "ELECTRONICS",
                            "전자제품",
                            null,
                            0,
                            "/2/",
                            2,
                            false,
                            "ACTIVE",
                            NOW,
                            NOW));
        }

        @Test
        @DisplayName("성공 - 카테고리 코드로 Category를 조회한다")
        void findByCode_existingCode_returnsCategory() {
            // When
            Optional<CategoryJpaEntity> result =
                    categoryQueryDslRepository.findByCode("ELECTRONICS");

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getNameKo()).isEqualTo("전자제품");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 코드로 조회 시 빈 Optional 반환")
        void findByCode_nonExistingCode_returnsEmpty() {
            // When
            Optional<CategoryJpaEntity> result =
                    categoryQueryDslRepository.findByCode("NON_EXISTENT");

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByParentId 메서드")
    class FindByParentId {

        private CategoryJpaEntity parentCategory;

        @BeforeEach
        void setUp() {
            parentCategory =
                    persistAndFlush(
                            CategoryJpaEntity.of(
                                    null, "FASHION", "패션", null, 0, "/1/", 1, false, "ACTIVE", NOW,
                                    NOW));
            flushAndClear();

            Long parentId = parentCategory.getId();
            persistAndFlush(
                    CategoryJpaEntity.of(
                            null,
                            "CLOTHING",
                            "의류",
                            parentId,
                            1,
                            "/1/2/",
                            2,
                            false,
                            "ACTIVE",
                            NOW,
                            NOW));
            persistAndFlush(
                    CategoryJpaEntity.of(
                            null, "SHOES", "신발", parentId, 1, "/1/3/", 1, false, "ACTIVE", NOW,
                            NOW));
            persistAndFlush(
                    CategoryJpaEntity.of(
                            null,
                            "INACTIVE_SUB",
                            "비활성하위",
                            parentId,
                            1,
                            "/1/4/",
                            3,
                            false,
                            "INACTIVE",
                            NOW,
                            NOW));
        }

        @Test
        @DisplayName("성공 - 부모 ID로 활성 하위 카테고리 목록을 조회한다")
        void findByParentId_existingParentId_returnsActiveChildren() {
            // When
            List<CategoryJpaEntity> result =
                    categoryQueryDslRepository.findByParentId(parentCategory.getId());

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(c -> "ACTIVE".equals(c.getStatus()));
        }

        @Test
        @DisplayName("성공 - sortOrder 순으로 정렬된다")
        void findByParentId_orderedBySortOrder() {
            // When
            List<CategoryJpaEntity> result =
                    categoryQueryDslRepository.findByParentId(parentCategory.getId());

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getCode()).isEqualTo("SHOES");
            assertThat(result.get(1).getCode()).isEqualTo("CLOTHING");
        }

        @Test
        @DisplayName("성공 - 하위 카테고리가 없는 경우 빈 목록 반환")
        void findByParentId_noChildren_returnsEmptyList() {
            // When
            List<CategoryJpaEntity> result = categoryQueryDslRepository.findByParentId(9999L);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllActive 메서드")
    class FindAllActive {

        @BeforeEach
        void setUp() {
            CategoryJpaEntity parent =
                    persistAndFlush(
                            CategoryJpaEntity.of(
                                    null, "FASHION", "패션", null, 0, "/1/", 2, false, "ACTIVE", NOW,
                                    NOW));
            flushAndClear();

            Long parentId = parent.getId();
            persistAndFlush(
                    CategoryJpaEntity.of(
                            null,
                            "CLOTHING",
                            "의류",
                            parentId,
                            1,
                            "/1/2/",
                            1,
                            false,
                            "ACTIVE",
                            NOW,
                            NOW));
            persistAndFlush(
                    CategoryJpaEntity.of(
                            null,
                            "INACTIVE",
                            "비활성",
                            null,
                            0,
                            "/3/",
                            3,
                            false,
                            "INACTIVE",
                            NOW,
                            NOW));
        }

        @Test
        @DisplayName("성공 - 활성화된 Category 목록만 조회한다")
        void findAllActive_returnsOnlyActiveCategories() {
            // When
            List<CategoryJpaEntity> result = categoryQueryDslRepository.findAllActive();

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(c -> "ACTIVE".equals(c.getStatus()));
        }

        @Test
        @DisplayName("성공 - depth, sortOrder 순으로 정렬된다")
        void findAllActive_orderedByDepthAndSortOrder() {
            // When
            List<CategoryJpaEntity> result = categoryQueryDslRepository.findAllActive();

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getDepth()).isEqualTo(0);
            assertThat(result.get(1).getDepth()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("findByCondition 메서드")
    class FindByCondition {

        private CategoryJpaEntity rootCategory;

        @BeforeEach
        void setUp() {
            rootCategory =
                    persistAndFlush(
                            CategoryJpaEntity.of(
                                    null, "FASHION", "패션", null, 0, "/1/", 1, false, "ACTIVE", NOW,
                                    NOW));
            flushAndClear();

            Long rootId = rootCategory.getId();
            CategoryJpaEntity middle =
                    persistAndFlush(
                            CategoryJpaEntity.of(
                                    null,
                                    "CLOTHING",
                                    "의류",
                                    rootId,
                                    1,
                                    "/1/2/",
                                    1,
                                    false,
                                    "ACTIVE",
                                    NOW,
                                    NOW));
            flushAndClear();

            Long middleId = middle.getId();
            persistAndFlush(
                    CategoryJpaEntity.of(
                            null, "TOPS", "상의", middleId, 2, "/1/2/3/", 1, true, "ACTIVE", NOW,
                            NOW));
            persistAndFlush(
                    CategoryJpaEntity.of(
                            null, "BOTTOMS", "하의", middleId, 2, "/1/2/4/", 2, true, "ACTIVE", NOW,
                            NOW));
            persistAndFlush(
                    CategoryJpaEntity.of(
                            null,
                            "INACTIVE_CAT",
                            "비활성카테고리",
                            null,
                            0,
                            "/5/",
                            5,
                            true,
                            "INACTIVE",
                            NOW,
                            NOW));
        }

        @Test
        @DisplayName("성공 - 최상위 카테고리 조건으로 조회한다")
        void findByCondition_rootCategories_success() {
            // Given
            CategorySearchCriteria criteria = CategorySearchCriteria.forRootCategories();

            // When
            List<CategoryJpaEntity> result = categoryQueryDslRepository.findByCondition(criteria);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCode()).isEqualTo("FASHION");
        }

        @Test
        @DisplayName("성공 - 부모 ID로 하위 카테고리 조회")
        void findByCondition_byParentId_success() {
            // Given
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.forChildren(rootCategory.getId());

            // When
            List<CategoryJpaEntity> result = categoryQueryDslRepository.findByCondition(criteria);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCode()).isEqualTo("CLOTHING");
        }

        @Test
        @DisplayName("성공 - 특정 depth로 조회")
        void findByCondition_byDepth_success() {
            // Given
            CategorySearchCriteria criteria = CategorySearchCriteria.of(null, 2, "ACTIVE", false);

            // When
            List<CategoryJpaEntity> result = categoryQueryDslRepository.findByCondition(criteria);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(c -> c.getDepth() == 2);
        }

        @Test
        @DisplayName("성공 - 비활성 포함하여 조회")
        void findByCondition_includeInactive_success() {
            // Given
            CategorySearchCriteria criteria = CategorySearchCriteria.of(null, null, null, true);

            // When
            List<CategoryJpaEntity> result = categoryQueryDslRepository.findByCondition(criteria);

            // Then
            assertThat(result).hasSize(5);
        }

        @Test
        @DisplayName("성공 - 활성 전체 조회")
        void findByCondition_allActive_success() {
            // Given
            CategorySearchCriteria criteria = CategorySearchCriteria.forAllActive();

            // When
            List<CategoryJpaEntity> result = categoryQueryDslRepository.findByCondition(criteria);

            // Then
            assertThat(result).hasSize(4);
            assertThat(result).allMatch(c -> "ACTIVE".equals(c.getStatus()));
        }
    }

    @Nested
    @DisplayName("findByIds 메서드")
    class FindByIds {

        private List<Long> savedIds;

        @BeforeEach
        void setUp() {
            CategoryJpaEntity cat1 =
                    persistAndFlush(
                            CategoryJpaEntity.of(
                                    null, "FASHION", "패션", null, 0, "/1/", 1, false, "ACTIVE", NOW,
                                    NOW));
            CategoryJpaEntity cat2 =
                    persistAndFlush(
                            CategoryJpaEntity.of(
                                    null,
                                    "CLOTHING",
                                    "의류",
                                    cat1.getId(),
                                    1,
                                    "/1/2/",
                                    1,
                                    false,
                                    "ACTIVE",
                                    NOW,
                                    NOW));
            CategoryJpaEntity cat3 =
                    persistAndFlush(
                            CategoryJpaEntity.of(
                                    null,
                                    "TOPS",
                                    "상의",
                                    cat2.getId(),
                                    2,
                                    "/1/2/3/",
                                    1,
                                    true,
                                    "ACTIVE",
                                    NOW,
                                    NOW));

            savedIds = List.of(cat1.getId(), cat2.getId(), cat3.getId());
        }

        @Test
        @DisplayName("성공 - ID 목록으로 Category를 조회한다")
        void findByIds_existingIds_returnsCategories() {
            // When
            List<CategoryJpaEntity> result = categoryQueryDslRepository.findByIds(savedIds);

            // Then
            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("성공 - depth 순으로 정렬된다")
        void findByIds_orderedByDepth() {
            // When
            List<CategoryJpaEntity> result = categoryQueryDslRepository.findByIds(savedIds);

            // Then
            assertThat(result.get(0).getDepth()).isEqualTo(0);
            assertThat(result.get(1).getDepth()).isEqualTo(1);
            assertThat(result.get(2).getDepth()).isEqualTo(2);
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID 포함 시 존재하는 것만 반환")
        void findByIds_partiallyExisting_returnsExistingOnly() {
            // Given
            List<Long> mixedIds = List.of(savedIds.get(0), 9999L);

            // When
            List<CategoryJpaEntity> result = categoryQueryDslRepository.findByIds(mixedIds);

            // Then
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("성공 - 빈 목록 조회 시 빈 결과 반환")
        void findByIds_emptyList_returnsEmpty() {
            // When
            List<CategoryJpaEntity> result = categoryQueryDslRepository.findByIds(List.of());

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsById 메서드")
    class ExistsById {

        private CategoryJpaEntity savedCategory;

        @BeforeEach
        void setUp() {
            savedCategory =
                    persistAndFlush(
                            CategoryJpaEntity.of(
                                    null, "FASHION", "패션", null, 0, "/1/", 1, false, "ACTIVE", NOW,
                                    NOW));
        }

        @Test
        @DisplayName("성공 - 존재하는 ID인 경우 true 반환")
        void existsById_existingId_returnsTrue() {
            // When
            boolean result = categoryQueryDslRepository.existsById(savedCategory.getId());

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID인 경우 false 반환")
        void existsById_nonExistingId_returnsFalse() {
            // When
            boolean result = categoryQueryDslRepository.existsById(9999L);

            // Then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("existsActiveById 메서드")
    class ExistsActiveById {

        private CategoryJpaEntity activeCategory;
        private CategoryJpaEntity inactiveCategory;

        @BeforeEach
        void setUp() {
            activeCategory =
                    persistAndFlush(
                            CategoryJpaEntity.of(
                                    null, "FASHION", "패션", null, 0, "/1/", 1, false, "ACTIVE", NOW,
                                    NOW));
            inactiveCategory =
                    persistAndFlush(
                            CategoryJpaEntity.of(
                                    null,
                                    "INACTIVE",
                                    "비활성",
                                    null,
                                    0,
                                    "/2/",
                                    2,
                                    true,
                                    "INACTIVE",
                                    NOW,
                                    NOW));
        }

        @Test
        @DisplayName("성공 - 활성 Category ID인 경우 true 반환")
        void existsActiveById_activeCategoryId_returnsTrue() {
            // When
            boolean result = categoryQueryDslRepository.existsActiveById(activeCategory.getId());

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("성공 - 비활성 Category ID인 경우 false 반환")
        void existsActiveById_inactiveCategoryId_returnsFalse() {
            // When
            boolean result = categoryQueryDslRepository.existsActiveById(inactiveCategory.getId());

            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID인 경우 false 반환")
        void existsActiveById_nonExistingId_returnsFalse() {
            // When
            boolean result = categoryQueryDslRepository.existsActiveById(9999L);

            // Then
            assertThat(result).isFalse();
        }
    }
}
