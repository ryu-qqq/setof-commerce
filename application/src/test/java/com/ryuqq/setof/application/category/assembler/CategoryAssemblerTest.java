package com.ryuqq.setof.application.category.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.category.dto.response.CategoryDisplayResult;
import com.ryuqq.setof.application.category.dto.response.CategoryPageResult;
import com.ryuqq.setof.application.category.dto.response.CategoryResult;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.domain.category.CategoryFixtures;
import com.ryuqq.setof.domain.category.aggregate.Category;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CategoryAssembler 단위 테스트")
class CategoryAssemblerTest {

    private final CategoryAssembler sut = new CategoryAssembler();

    @Nested
    @DisplayName("toResult() - Domain → Result 변환")
    class ToResultTest {

        @Test
        @DisplayName("Category를 CategoryResult로 변환한다")
        void toResult_ConvertsToResult() {
            // given
            Category domain = CategoryFixtures.activeCategory();

            // when
            CategoryResult result = sut.toResult(domain);

            // then
            assertThat(result).isNotNull();
            assertThat(result.categoryId()).isEqualTo(domain.idValue());
            assertThat(result.categoryName()).isEqualTo(domain.categoryNameValue());
        }
    }

    @Nested
    @DisplayName("toResults() - Domain List → Result List 변환")
    class ToResultsTest {

        @Test
        @DisplayName("Category 목록을 CategoryResult 목록으로 변환한다")
        void toResults_ConvertsAllToResults() {
            // given
            List<Category> domains =
                    List.of(CategoryFixtures.activeCategory(), CategoryFixtures.inactiveCategory());

            // when
            List<CategoryResult> results = sut.toResults(domains);

            // then
            assertThat(results).hasSize(2);
        }

        @Test
        @DisplayName("빈 목록이면 빈 결과를 반환한다")
        void toResults_EmptyList_ReturnsEmptyList() {
            // given
            List<Category> domains = Collections.emptyList();

            // when
            List<CategoryResult> results = sut.toResults(domains);

            // then
            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("toPageResult() - Domain List → PageResult 변환")
    class ToPageResultTest {

        @Test
        @DisplayName("Domain 목록과 페이징 정보로 PageResult를 생성한다")
        void toPageResult_CreatesPageResult() {
            // given
            List<Category> domains =
                    List.of(CategoryFixtures.activeCategory(), CategoryFixtures.inactiveCategory());
            int page = 0;
            int size = 20;
            long totalCount = 100L;

            // when
            CategoryPageResult result = sut.toPageResult(domains, page, size, totalCount);

            // then
            assertThat(result).isNotNull();
            assertThat(result.content()).hasSize(2);
            assertThat(result.pageMeta().totalElements()).isEqualTo(totalCount);
            assertThat(result.pageMeta().page()).isEqualTo(page);
            assertThat(result.pageMeta().size()).isEqualTo(size);
        }

        @Test
        @DisplayName("빈 목록으로 PageResult를 생성한다")
        void toPageResult_EmptyList_CreatesEmptyPageResult() {
            // given
            List<Category> domains = Collections.emptyList();
            int page = 0;
            int size = 20;
            long totalCount = 0L;

            // when
            CategoryPageResult result = sut.toPageResult(domains, page, size, totalCount);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.pageMeta().totalElements()).isZero();
        }
    }

    @Nested
    @DisplayName("toTreeResults() - Domain List → TreeCategoryResult 변환")
    class ToTreeResultsTest {

        @Test
        @DisplayName("Category 목록을 트리 구조로 변환한다")
        void toTreeResults_ConvertsToTree() {
            // given
            Category root = CategoryFixtures.activeCategory();
            Category child = CategoryFixtures.activeChildCategory(2L, 1L);
            List<Category> domains = List.of(root, child);

            // when
            List<TreeCategoryResult> results = sut.toTreeResults(domains);

            // then
            assertThat(results).hasSize(1);
            assertThat(results.get(0).children()).hasSize(1);
        }

        @Test
        @DisplayName("빈 목록이면 빈 트리를 반환한다")
        void toTreeResults_EmptyList_ReturnsEmptyList() {
            // given
            List<Category> domains = Collections.emptyList();

            // when
            List<TreeCategoryResult> results = sut.toTreeResults(domains);

            // then
            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("toLeafResult() - Domain → TreeCategoryResult leaf 변환")
    class ToLeafResultTest {

        @Test
        @DisplayName("Category를 TreeCategoryResult leaf로 변환한다")
        void toLeafResult_ConvertsToLeaf() {
            // given
            Category domain = CategoryFixtures.activeCategory();

            // when
            TreeCategoryResult result = sut.toLeafResult(domain);

            // then
            assertThat(result).isNotNull();
            assertThat(result.categoryId()).isEqualTo(domain.idValue());
            assertThat(result.children()).isEmpty();
        }
    }

    @Nested
    @DisplayName("toLeafResults() - Domain List → TreeCategoryResult leaf List 변환")
    class ToLeafResultsTest {

        @Test
        @DisplayName("Category 목록을 TreeCategoryResult leaf 목록으로 변환한다")
        void toLeafResults_ConvertsAllToLeaves() {
            // given
            List<Category> domains =
                    List.of(CategoryFixtures.activeCategory(), CategoryFixtures.inactiveCategory());

            // when
            List<TreeCategoryResult> results = sut.toLeafResults(domains);

            // then
            assertThat(results).hasSize(2);
            assertThat(results).allMatch(r -> r.children().isEmpty());
        }
    }

    @Nested
    @DisplayName("toDisplayResults() - Domain List → CategoryDisplayResult 트리 변환")
    class ToDisplayResultsTest {

        @Test
        @DisplayName("Category 목록을 CategoryDisplayResult 트리로 변환한다")
        void toDisplayResults_ConvertsToDisplayTree() {
            // given
            Category root = CategoryFixtures.activeCategory();
            Category child = CategoryFixtures.activeChildCategory(2L, 1L);
            List<Category> domains = List.of(root, child);

            // when
            List<CategoryDisplayResult> results = sut.toDisplayResults(domains);

            // then
            assertThat(results).hasSize(1);
            assertThat(results.get(0).children()).hasSize(1);
        }
    }
}
