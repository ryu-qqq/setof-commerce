package com.ryuqq.setof.adapter.out.persistence.category.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.query.criteria.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.vo.CategoryCode;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CategoryQueryAdapter 통합 테스트
 *
 * <p>CategoryQueryPort 구현체의 Domain 변환 및 조회 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("CategoryQueryAdapter 통합 테스트")
class CategoryQueryAdapterTest extends RepositoryTestSupport {

    @Autowired private CategoryQueryAdapter categoryQueryAdapter;

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
        @DisplayName("성공 - ID로 Category 도메인을 조회한다")
        void findById_existingId_returnsCategoryDomain() {
            // When
            Optional<Category> result =
                    categoryQueryAdapter.findById(CategoryId.of(savedCategory.getId()));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getCodeValue()).isEqualTo("FASHION");
            assertThat(result.get().getNameKoValue()).isEqualTo("패션");
            assertThat(result.get().getDepthValue()).isEqualTo(0);
            assertThat(result.get().isLeaf()).isFalse();
            assertThat(result.get().getStatusValue()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_nonExistingId_returnsEmpty() {
            // When
            Optional<Category> result = categoryQueryAdapter.findById(CategoryId.of(9999L));

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
        @DisplayName("성공 - 카테고리 코드로 Category 도메인을 조회한다")
        void findByCode_existingCode_returnsCategoryDomain() {
            // When
            Optional<Category> result =
                    categoryQueryAdapter.findByCode(CategoryCode.of("ELECTRONICS"));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getNameKoValue()).isEqualTo("전자제품");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 코드로 조회 시 빈 Optional 반환")
        void findByCode_nonExistingCode_returnsEmpty() {
            // When
            Optional<Category> result =
                    categoryQueryAdapter.findByCode(CategoryCode.of("NON_EXISTENT"));

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
        @DisplayName("성공 - 부모 ID로 활성 하위 Category 도메인 목록을 조회한다")
        void findByParentId_existingParentId_returnsActiveCategoryDomains() {
            // When
            List<Category> result =
                    categoryQueryAdapter.findByParentId(CategoryId.of(parentCategory.getId()));

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(c -> "ACTIVE".equals(c.getStatusValue()));
        }

        @Test
        @DisplayName("성공 - sortOrder 순으로 정렬된 도메인 목록 반환")
        void findByParentId_orderedBySortOrder() {
            // When
            List<Category> result =
                    categoryQueryAdapter.findByParentId(CategoryId.of(parentCategory.getId()));

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getCodeValue()).isEqualTo("SHOES");
            assertThat(result.get(1).getCodeValue()).isEqualTo("CLOTHING");
        }

        @Test
        @DisplayName("성공 - 하위 카테고리가 없는 경우 빈 목록 반환")
        void findByParentId_noChildren_returnsEmptyList() {
            // When
            List<Category> result = categoryQueryAdapter.findByParentId(CategoryId.of(9999L));

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
        @DisplayName("성공 - 활성화된 Category 도메인 목록만 조회한다")
        void findAllActive_returnsOnlyActiveCategoryDomains() {
            // When
            List<Category> result = categoryQueryAdapter.findAllActive();

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(c -> "ACTIVE".equals(c.getStatusValue()));
        }

        @Test
        @DisplayName("성공 - depth, sortOrder 순으로 정렬된 도메인 목록 반환")
        void findAllActive_orderedByDepthAndSortOrder() {
            // When
            List<Category> result = categoryQueryAdapter.findAllActive();

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getDepthValue()).isEqualTo(0);
            assertThat(result.get(1).getDepthValue()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("findByCriteria 메서드")
    class FindByCriteria {

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
        void findByCriteria_rootCategories_success() {
            // Given
            CategorySearchCriteria criteria = CategorySearchCriteria.forRootCategories();

            // When
            List<Category> result = categoryQueryAdapter.findByCriteria(criteria);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCodeValue()).isEqualTo("FASHION");
        }

        @Test
        @DisplayName("성공 - 부모 ID로 하위 카테고리 조회")
        void findByCriteria_byParentId_success() {
            // Given
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.forChildren(rootCategory.getId());

            // When
            List<Category> result = categoryQueryAdapter.findByCriteria(criteria);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCodeValue()).isEqualTo("CLOTHING");
        }

        @Test
        @DisplayName("성공 - 활성 전체 조회")
        void findByCriteria_allActive_success() {
            // Given
            CategorySearchCriteria criteria = CategorySearchCriteria.forAllActive();

            // When
            List<Category> result = categoryQueryAdapter.findByCriteria(criteria);

            // Then
            assertThat(result).hasSize(4);
            assertThat(result).allMatch(c -> "ACTIVE".equals(c.getStatusValue()));
        }

        @Test
        @DisplayName("성공 - 특정 depth로 조회")
        void findByCriteria_byDepth_success() {
            // Given
            CategorySearchCriteria criteria = CategorySearchCriteria.of(null, 2, "ACTIVE", false);

            // When
            List<Category> result = categoryQueryAdapter.findByCriteria(criteria);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(c -> c.getDepthValue() == 2);
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
            flushAndClear();

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
            flushAndClear();

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
        @DisplayName("성공 - ID 목록으로 Category 도메인을 조회한다")
        void findByIds_existingIds_returnsCategoryDomains() {
            // When
            List<Category> result = categoryQueryAdapter.findByIds(savedIds);

            // Then
            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("성공 - depth 순으로 정렬된 도메인 목록 반환")
        void findByIds_orderedByDepth() {
            // When
            List<Category> result = categoryQueryAdapter.findByIds(savedIds);

            // Then
            assertThat(result.get(0).getDepthValue()).isEqualTo(0);
            assertThat(result.get(1).getDepthValue()).isEqualTo(1);
            assertThat(result.get(2).getDepthValue()).isEqualTo(2);
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID 포함 시 존재하는 것만 반환")
        void findByIds_partiallyExisting_returnsExistingOnly() {
            // Given
            List<Long> mixedIds = List.of(savedIds.get(0), 9999L);

            // When
            List<Category> result = categoryQueryAdapter.findByIds(mixedIds);

            // Then
            assertThat(result).hasSize(1);
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
        @DisplayName("성공 - 존재하는 CategoryId인 경우 true 반환")
        void existsById_existingCategoryId_returnsTrue() {
            // When
            boolean result = categoryQueryAdapter.existsById(CategoryId.of(savedCategory.getId()));

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 CategoryId인 경우 false 반환")
        void existsById_nonExistingCategoryId_returnsFalse() {
            // When
            boolean result = categoryQueryAdapter.existsById(CategoryId.of(9999L));

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
            boolean result = categoryQueryAdapter.existsActiveById(activeCategory.getId());

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("성공 - 비활성 Category ID인 경우 false 반환")
        void existsActiveById_inactiveCategoryId_returnsFalse() {
            // When
            boolean result = categoryQueryAdapter.existsActiveById(inactiveCategory.getId());

            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID인 경우 false 반환")
        void existsActiveById_nonExistingId_returnsFalse() {
            // When
            boolean result = categoryQueryAdapter.existsActiveById(9999L);

            // Then
            assertThat(result).isFalse();
        }
    }
}
