package com.ryuqq.setof.domain.category.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CategorySearchCriteria 테스트")
class CategorySearchCriteriaTest {

    private static final CategoryId PARENT_ID = CategoryId.of(1L);

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 검색 조건을 생성한다")
        void createWithOf() {
            // given
            QueryContext<CategorySortKey> queryContext =
                    QueryContext.of(
                            CategorySortKey.CREATED_AT, SortDirection.DESC, PageRequest.of(0, 20));

            // when
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(
                            PARENT_ID,
                            true,
                            TargetGroup.MALE,
                            CategoryType.CLOTHING,
                            "테스트카테고리",
                            queryContext);

            // then
            assertThat(criteria.parentCategoryId()).isEqualTo(PARENT_ID);
            assertThat(criteria.displayed()).isTrue();
            assertThat(criteria.targetGroup()).isEqualTo(TargetGroup.MALE);
            assertThat(criteria.categoryType()).isEqualTo(CategoryType.CLOTHING);
            assertThat(criteria.categoryName()).isEqualTo("테스트카테고리");
            assertThat(criteria.queryContext()).isEqualTo(queryContext);
        }

        @Test
        @DisplayName("defaultOf()로 기본 검색 조건을 생성한다")
        void createDefaultOf() {
            // when
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();

            // then
            assertThat(criteria.parentCategoryId()).isNull();
            assertThat(criteria.displayed()).isNull();
            assertThat(criteria.targetGroup()).isNull();
            assertThat(criteria.categoryType()).isNull();
            assertThat(criteria.categoryName()).isNull();
            assertThat(criteria.queryContext().sortKey()).isEqualTo(CategorySortKey.CREATED_AT);
        }

        @Test
        @DisplayName("byParent()로 부모별 하위 카테고리 조회 조건을 생성한다")
        void createByParent() {
            // when
            CategorySearchCriteria criteria = CategorySearchCriteria.byParent(PARENT_ID);

            // then
            assertThat(criteria.parentCategoryId()).isEqualTo(PARENT_ID);
            assertThat(criteria.displayed()).isNull();
        }

        @Test
        @DisplayName("rootOnly()로 루트 카테고리만 조회하는 조건을 생성한다")
        void createRootOnly() {
            // when
            CategorySearchCriteria criteria = CategorySearchCriteria.rootOnly();

            // then
            assertThat(criteria.parentCategoryId()).isEqualTo(CategoryId.of(0L));
            assertThat(criteria.displayed()).isNull();
        }
    }

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("null queryContext는 기본값으로 대체된다")
        void nullQueryContextDefaultsToDefault() {
            // when
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(null, null, null, null, null, null);

            // then
            assertThat(criteria.queryContext()).isNotNull();
            assertThat(criteria.queryContext().sortKey()).isEqualTo(CategorySortKey.CREATED_AT);
        }

        @Test
        @DisplayName("categoryName은 trim된다")
        void categoryNameIsTrimmed() {
            // when
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(
                            null,
                            null,
                            null,
                            null,
                            "  테스트카테고리  ",
                            QueryContext.defaultOf(CategorySortKey.CREATED_AT));

            // then
            assertThat(criteria.categoryName()).isEqualTo("테스트카테고리");
        }

        @Test
        @DisplayName("빈 문자열 categoryName은 null로 변환된다")
        void blankCategoryNameBecomesNull() {
            // when
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(
                            null,
                            null,
                            null,
                            null,
                            "   ",
                            QueryContext.defaultOf(CategorySortKey.CREATED_AT));

            // then
            assertThat(criteria.categoryName()).isNull();
        }
    }

    @Nested
    @DisplayName("편의 메서드 테스트")
    class ConvenienceMethodTest {

        @Test
        @DisplayName("hasParentFilter()는 부모 필터가 있으면 true를 반환한다")
        void hasParentFilterReturnsTrueWhenParentExists() {
            // given
            CategorySearchCriteria criteria = CategorySearchCriteria.byParent(PARENT_ID);

            // then
            assertThat(criteria.hasParentFilter()).isTrue();
        }

        @Test
        @DisplayName("hasParentFilter()는 부모 필터가 없으면 false를 반환한다")
        void hasParentFilterReturnsFalseWhenNoParent() {
            // given
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();

            // then
            assertThat(criteria.hasParentFilter()).isFalse();
        }

        @Test
        @DisplayName("hasDisplayedFilter()는 표시 필터가 있으면 true를 반환한다")
        void hasDisplayedFilterReturnsTrueWhenDisplayedExists() {
            // given
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(
                            null,
                            true,
                            null,
                            null,
                            null,
                            QueryContext.defaultOf(CategorySortKey.CREATED_AT));

            // then
            assertThat(criteria.hasDisplayedFilter()).isTrue();
        }

        @Test
        @DisplayName("hasCategoryNameFilter()는 카테고리명 필터가 있으면 true를 반환한다")
        void hasCategoryNameFilterReturnsTrueWhenCategoryNameExists() {
            // given
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(
                            null,
                            null,
                            null,
                            null,
                            "테스트",
                            QueryContext.defaultOf(CategorySortKey.CREATED_AT));

            // then
            assertThat(criteria.hasCategoryNameFilter()).isTrue();
        }

        @Test
        @DisplayName("size()는 페이지 크기를 반환한다")
        void returnsSize() {
            // given
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();

            // then
            assertThat(criteria.size()).isEqualTo(PageRequest.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("offset()은 오프셋을 반환한다")
        void returnsOffset() {
            // given
            QueryContext<CategorySortKey> queryContext =
                    QueryContext.of(
                            CategorySortKey.CREATED_AT, SortDirection.DESC, PageRequest.of(2, 20));
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(null, null, null, null, null, queryContext);

            // then
            assertThat(criteria.offset()).isEqualTo(40);
        }

        @Test
        @DisplayName("page()는 현재 페이지를 반환한다")
        void returnsPage() {
            // given
            QueryContext<CategorySortKey> queryContext =
                    QueryContext.of(
                            CategorySortKey.CREATED_AT, SortDirection.DESC, PageRequest.of(3, 20));
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(null, null, null, null, null, queryContext);

            // then
            assertThat(criteria.page()).isEqualTo(3);
        }
    }
}
