package com.ryuqq.setof.storage.legacy.category.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.query.CategorySearchField;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * LegacyCategoryConditionBuilder 단위 테스트.
 *
 * <p>레거시 카테고리 QueryDSL 조건 빌더의 조건 생성 로직을 검증합니다.
 */
@DisplayName("레거시 카테고리 ConditionBuilder 테스트")
@Tag("unit")
class LegacyCategoryConditionBuilderTest {

    private LegacyCategoryConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new LegacyCategoryConditionBuilder();
    }

    @Nested
    @DisplayName("idEq 메서드 테스트")
    class IdEqTest {

        @Test
        @DisplayName("ID가 주어지면 ID 일치 조건을 반환합니다")
        void shouldReturnIdEqConditionWhenIdIsGiven() {
            // given
            Long id = 100L;

            // when
            BooleanExpression condition = conditionBuilder.idEq(id);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).contains("legacyCategoryEntity.id = 100");
        }

        @Test
        @DisplayName("ID가 null이면 null을 반환합니다")
        void shouldReturnNullWhenIdIsNull() {
            // when
            BooleanExpression condition = conditionBuilder.idEq(null);

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("idIn 메서드 테스트")
    class IdInTest {

        @Test
        @DisplayName("ID 목록이 주어지면 ID 포함 조건을 반환합니다")
        void shouldReturnIdInConditionWhenIdsAreGiven() {
            // given
            List<Long> ids = List.of(100L, 200L, 300L);

            // when
            BooleanExpression condition = conditionBuilder.idIn(ids);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).contains("legacyCategoryEntity.id in");
        }

        @Test
        @DisplayName("ID 목록이 null이면 null을 반환합니다")
        void shouldReturnNullWhenIdsAreNull() {
            // when
            BooleanExpression condition = conditionBuilder.idIn(null);

            // then
            assertThat(condition).isNull();
        }

        @Test
        @DisplayName("ID 목록이 빈 리스트면 null을 반환합니다")
        void shouldReturnNullWhenIdsAreEmpty() {
            // when
            BooleanExpression condition = conditionBuilder.idIn(List.of());

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("parentCategoryIdEq 메서드 테스트")
    class ParentCategoryIdEqTest {

        @Test
        @DisplayName("부모 카테고리 ID가 주어지면 일치 조건을 반환합니다")
        void shouldReturnParentCategoryIdEqConditionWhenGiven() {
            // given
            CategoryId parentCategoryId = CategoryId.of(100L);

            // when
            BooleanExpression condition = conditionBuilder.parentCategoryIdEq(parentCategoryId);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString())
                    .contains("legacyCategoryEntity.parentCategoryId = 100");
        }

        @Test
        @DisplayName("부모 카테고리 ID가 null이면 null을 반환합니다")
        void shouldReturnNullWhenParentCategoryIdIsNull() {
            // when
            BooleanExpression condition = conditionBuilder.parentCategoryIdEq(null);

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("displayedEq 메서드 테스트")
    class DisplayedEqTest {

        @Test
        @DisplayName("displayed가 true이면 displayYn = Y 조건을 반환합니다")
        void shouldReturnDisplayYnYWhenDisplayedIsTrue() {
            // when
            BooleanExpression condition = conditionBuilder.displayedEq(true);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).contains("legacyCategoryEntity.displayYn = Y");
        }

        @Test
        @DisplayName("displayed가 false이면 displayYn = N 조건을 반환합니다")
        void shouldReturnDisplayYnNWhenDisplayedIsFalse() {
            // when
            BooleanExpression condition = conditionBuilder.displayedEq(false);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).contains("legacyCategoryEntity.displayYn = N");
        }

        @Test
        @DisplayName("displayed가 null이면 null을 반환합니다")
        void shouldReturnNullWhenDisplayedIsNull() {
            // when
            BooleanExpression condition = conditionBuilder.displayedEq(null);

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("targetGroupEq 메서드 테스트")
    class TargetGroupEqTest {

        @Test
        @DisplayName("타겟 그룹이 주어지면 일치 조건을 반환합니다")
        void shouldReturnTargetGroupEqConditionWhenGiven() {
            // given
            TargetGroup targetGroup = TargetGroup.MALE;

            // when
            BooleanExpression condition = conditionBuilder.targetGroupEq(targetGroup);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).contains("legacyCategoryEntity.targetGroup = MALE");
        }

        @Test
        @DisplayName("타겟 그룹이 null이면 null을 반환합니다")
        void shouldReturnNullWhenTargetGroupIsNull() {
            // when
            BooleanExpression condition = conditionBuilder.targetGroupEq(null);

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("categoryTypeEq 메서드 테스트")
    class CategoryTypeEqTest {

        @Test
        @DisplayName("카테고리 타입이 주어지면 일치 조건을 반환합니다")
        void shouldReturnCategoryTypeEqConditionWhenGiven() {
            // given
            CategoryType categoryType = CategoryType.CLOTHING;

            // when
            BooleanExpression condition = conditionBuilder.categoryTypeEq(categoryType);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString())
                    .contains("legacyCategoryEntity.categoryType = CLOTHING");
        }

        @Test
        @DisplayName("카테고리 타입이 null이면 null을 반환합니다")
        void shouldReturnNullWhenCategoryTypeIsNull() {
            // when
            BooleanExpression condition = conditionBuilder.categoryTypeEq(null);

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("categoryNameContains 메서드 테스트")
    class CategoryNameContainsTest {

        @Test
        @DisplayName("카테고리명이 주어지면 포함 조건을 반환합니다")
        void shouldReturnCategoryNameContainsConditionWhenGiven() {
            // given
            String categoryName = "상의";

            // when
            BooleanExpression condition = conditionBuilder.categoryNameContains(categoryName);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).containsIgnoringCase("categoryName");
        }

        @Test
        @DisplayName("카테고리명이 null이면 null을 반환합니다")
        void shouldReturnNullWhenCategoryNameIsNull() {
            // when
            BooleanExpression condition = conditionBuilder.categoryNameContains(null);

            // then
            assertThat(condition).isNull();
        }

        @Test
        @DisplayName("카테고리명이 빈 문자열이면 null을 반환합니다")
        void shouldReturnNullWhenCategoryNameIsBlank() {
            // when
            BooleanExpression condition = conditionBuilder.categoryNameContains("  ");

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("searchFieldContains 메서드 테스트")
    class SearchFieldContainsTest {

        @Test
        @DisplayName("CATEGORY_NAME 필드로 검색 조건을 생성합니다")
        void shouldSearchByCategoryName() {
            // given
            CategorySearchField searchField = CategorySearchField.CATEGORY_NAME;
            String searchWord = "상의";

            // when
            BooleanExpression condition =
                    conditionBuilder.searchFieldContains(searchField, searchWord);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).containsIgnoringCase("categoryName");
        }

        @Test
        @DisplayName("DISPLAY_NAME 필드로 검색 조건을 생성합니다")
        void shouldSearchByDisplayName() {
            // given
            CategorySearchField searchField = CategorySearchField.DISPLAY_NAME;
            String searchWord = "상의";

            // when
            BooleanExpression condition =
                    conditionBuilder.searchFieldContains(searchField, searchWord);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).containsIgnoringCase("displayName");
        }

        @Test
        @DisplayName("검색 필드가 null이면 카테고리명과 표시명 OR 조건을 생성합니다")
        void shouldSearchBothFieldsWhenSearchFieldIsNull() {
            // given
            String searchWord = "상의";

            // when
            BooleanExpression condition = conditionBuilder.searchFieldContains(null, searchWord);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).containsIgnoringCase("or");
        }

        @Test
        @DisplayName("검색어가 null이면 null을 반환합니다")
        void shouldReturnNullWhenSearchWordIsNull() {
            // when
            BooleanExpression condition =
                    conditionBuilder.searchFieldContains(CategorySearchField.CATEGORY_NAME, null);

            // then
            assertThat(condition).isNull();
        }

        @Test
        @DisplayName("검색어가 빈 문자열이면 null을 반환합니다")
        void shouldReturnNullWhenSearchWordIsBlank() {
            // when
            BooleanExpression condition =
                    conditionBuilder.searchFieldContains(CategorySearchField.CATEGORY_NAME, "  ");

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("searchCondition 메서드 테스트")
    class SearchConditionTest {

        @Test
        @DisplayName("검색 조건이 있으면 검색 조건을 반환합니다")
        void shouldReturnSearchConditionWhenCriteriaHasSearchCondition() {
            // given
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(
                            null, null, null, null, CategorySearchField.CATEGORY_NAME, "상의", null);

            // when
            BooleanExpression condition = conditionBuilder.searchCondition(criteria);

            // then
            assertThat(condition).isNotNull();
        }

        @Test
        @DisplayName("검색 조건이 없으면 null을 반환합니다")
        void shouldReturnNullWhenCriteriaHasNoSearchCondition() {
            // given
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();

            // when
            BooleanExpression condition = conditionBuilder.searchCondition(criteria);

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("pathStartsWith 메서드 테스트")
    class PathStartsWithTest {

        @Test
        @DisplayName("경로가 주어지면 시작 조건을 반환합니다")
        void shouldReturnPathStartsWithConditionWhenGiven() {
            // given
            String path = "/100";

            // when
            BooleanExpression condition = conditionBuilder.pathStartsWith(path);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).containsIgnoringCase("startsWith");
        }

        @Test
        @DisplayName("경로가 null이면 null을 반환합니다")
        void shouldReturnNullWhenPathIsNull() {
            // when
            BooleanExpression condition = conditionBuilder.pathStartsWith(null);

            // then
            assertThat(condition).isNull();
        }

        @Test
        @DisplayName("경로가 빈 문자열이면 null을 반환합니다")
        void shouldReturnNullWhenPathIsBlank() {
            // when
            BooleanExpression condition = conditionBuilder.pathStartsWith("  ");

            // then
            assertThat(condition).isNull();
        }
    }
}
