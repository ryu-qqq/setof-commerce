package com.ryuqq.setof.application.category.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.category.CategoryQueryFixtures;
import com.ryuqq.setof.application.category.assembler.CategoryAssembler;
import com.ryuqq.setof.application.category.dto.query.CategorySearchParams;
import com.ryuqq.setof.application.category.dto.response.CategoryPageResult;
import com.ryuqq.setof.application.category.dto.response.CategoryResult;
import com.ryuqq.setof.application.category.factory.CategoryQueryFactory;
import com.ryuqq.setof.application.category.manager.CategoryReadManager;
import com.ryuqq.setof.domain.category.CategoryFixtures;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SearchCategoryByOffsetService 단위 테스트")
class SearchCategoryByOffsetServiceTest {

    @InjectMocks private SearchCategoryByOffsetService sut;

    @Mock private CategoryReadManager readManager;
    @Mock private CategoryQueryFactory queryFactory;
    @Mock private CategoryAssembler assembler;

    @Nested
    @DisplayName("execute() - 카테고리 검색")
    class ExecuteTest {

        @Test
        @DisplayName("검색 조건으로 카테고리 목록을 페이징하여 반환한다")
        void execute_ReturnsPagedResult() {
            // given
            CategorySearchParams params =
                    CategoryQueryFixtures.searchParams(null, null, null, null, 0, 20);
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();

            List<Category> categories =
                    List.of(CategoryFixtures.activeCategory(), CategoryFixtures.inactiveCategory());
            long totalElements = 2L;

            List<CategoryResult> results =
                    categories.stream()
                            .map(
                                    c ->
                                            CategoryResult.of(
                                                    c.idValue(),
                                                    c.categoryNameValue(),
                                                    c.parentCategoryIdValue(),
                                                    c.categoryDepthValue(),
                                                    c.isDisplayed(),
                                                    c.pathValue(),
                                                    c.pathValue(),
                                                    c.targetGroup() != null
                                                            ? c.targetGroup().name()
                                                            : null))
                            .toList();
            CategoryPageResult expected =
                    CategoryPageResult.of(results, params.page(), params.size(), totalElements);

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(categories);
            given(readManager.countByCriteria(criteria)).willReturn(totalElements);
            given(assembler.toPageResult(categories, params.page(), params.size(), totalElements))
                    .willReturn(expected);

            // when
            CategoryPageResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.content()).hasSize(2);
            then(queryFactory).should().createCriteria(params);
            then(readManager).should().findByCriteria(criteria);
            then(readManager).should().countByCriteria(criteria);
            then(assembler)
                    .should()
                    .toPageResult(categories, params.page(), params.size(), totalElements);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void execute_NoResults_ReturnsEmptyPage() {
            // given
            CategorySearchParams params = CategoryQueryFixtures.searchParams();
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();
            List<Category> emptyCategories = Collections.emptyList();
            long totalElements = 0L;

            CategoryPageResult expected = CategoryPageResult.empty();

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(emptyCategories);
            given(readManager.countByCriteria(criteria)).willReturn(totalElements);
            given(
                            assembler.toPageResult(
                                    emptyCategories, params.page(), params.size(), totalElements))
                    .willReturn(expected);

            // when
            CategoryPageResult result = sut.execute(params);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.pageMeta().totalElements()).isZero();
        }
    }
}
