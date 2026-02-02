package com.ryuqq.setof.application.category.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.category.assembler.CategoryAssembler;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.application.category.manager.CategoryReadManager;
import com.ryuqq.setof.domain.category.CategoryFixtures;
import com.ryuqq.setof.domain.category.aggregate.Category;
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
@DisplayName("GetAllCategoriesAsTreeService 단위 테스트")
class GetAllCategoriesAsTreeServiceTest {

    @InjectMocks private GetAllCategoriesAsTreeService sut;

    @Mock private CategoryReadManager readManager;
    @Mock private CategoryAssembler assembler;

    @Nested
    @DisplayName("execute() - 전체 카테고리 트리 조회")
    class ExecuteTest {

        @Test
        @DisplayName("노출 카테고리 목록을 트리 구조로 반환한다")
        void execute_ReturnsCategoryTree() {
            // given
            Category root = CategoryFixtures.activeCategory();
            Category child = CategoryFixtures.activeChildCategory(2L, 1L);
            List<Category> categories = List.of(root, child);

            List<TreeCategoryResult> expected =
                    List.of(
                            TreeCategoryResult.of(
                                    1L,
                                    "테스트카테고리",
                                    0L,
                                    1,
                                    true,
                                    "/1",
                                    List.of(
                                            TreeCategoryResult.leaf(
                                                    2L, "테스트카테고리", 1L, 2, true, "/1/2"))));

            given(readManager.findAllDisplayed()).willReturn(categories);
            given(assembler.toTreeResults(categories)).willReturn(expected);

            // when
            List<TreeCategoryResult> result = sut.execute();

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result).hasSize(1);
            assertThat(result.get(0).children()).hasSize(1);
            then(readManager).should().findAllDisplayed();
            then(assembler).should().toTreeResults(categories);
        }

        @Test
        @DisplayName("노출 카테고리가 없으면 빈 목록을 반환한다")
        void execute_NoCategories_ReturnsEmptyList() {
            // given
            List<Category> emptyCategories = Collections.emptyList();
            List<TreeCategoryResult> expected = Collections.emptyList();

            given(readManager.findAllDisplayed()).willReturn(emptyCategories);
            given(assembler.toTreeResults(emptyCategories)).willReturn(expected);

            // when
            List<TreeCategoryResult> result = sut.execute();

            // then
            assertThat(result).isEmpty();
        }
    }
}
