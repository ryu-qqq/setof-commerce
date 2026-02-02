package com.ryuqq.setof.application.category.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.category.assembler.CategoryAssembler;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.application.category.manager.CategoryReadManager;
import com.ryuqq.setof.domain.category.CategoryFixtures;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.id.CategoryId;
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
@DisplayName("GetChildCategoriesService 단위 테스트")
class GetChildCategoriesServiceTest {

    @InjectMocks private GetChildCategoriesService sut;

    @Mock private CategoryReadManager readManager;
    @Mock private CategoryAssembler assembler;

    @Nested
    @DisplayName("execute() - 자식 카테고리 조회")
    class ExecuteTest {

        @Test
        @DisplayName("부모 카테고리 ID로 자식 카테고리 목록을 반환한다")
        void execute_ReturnsChildCategories() {
            // given
            Long parentCategoryId = 1L;
            CategoryId parentId = CategoryId.of(parentCategoryId);
            List<Category> children = List.of(CategoryFixtures.activeChildCategory(2L, 1L));
            List<TreeCategoryResult> expected =
                    children.stream()
                            .map(
                                    c ->
                                            TreeCategoryResult.leaf(
                                                    c.idValue(),
                                                    c.categoryNameValue(),
                                                    c.parentCategoryIdValue(),
                                                    c.categoryDepthValue(),
                                                    c.isDisplayed(),
                                                    c.pathValue()))
                            .toList();

            given(readManager.findChildrenByParentId(parentId)).willReturn(children);
            given(assembler.toTreeResults(children)).willReturn(expected);

            // when
            List<TreeCategoryResult> result = sut.execute(parentCategoryId);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result).hasSize(1);
            then(readManager).should().findChildrenByParentId(parentId);
            then(assembler).should().toTreeResults(children);
        }

        @Test
        @DisplayName("자식 카테고리가 없으면 빈 목록을 반환한다")
        void execute_NoChildren_ReturnsEmptyList() {
            // given
            Long parentCategoryId = 1L;
            CategoryId parentId = CategoryId.of(parentCategoryId);
            List<Category> emptyChildren = Collections.emptyList();
            List<TreeCategoryResult> expected = Collections.emptyList();

            given(readManager.findChildrenByParentId(parentId)).willReturn(emptyChildren);
            given(assembler.toTreeResults(emptyChildren)).willReturn(expected);

            // when
            List<TreeCategoryResult> result = sut.execute(parentCategoryId);

            // then
            assertThat(result).isEmpty();
        }
    }
}
