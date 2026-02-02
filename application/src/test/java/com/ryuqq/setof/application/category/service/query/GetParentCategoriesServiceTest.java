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
@DisplayName("GetParentCategoriesService 단위 테스트")
class GetParentCategoriesServiceTest {

    @InjectMocks private GetParentCategoriesService sut;

    @Mock private CategoryReadManager readManager;
    @Mock private CategoryAssembler assembler;

    @Nested
    @DisplayName("execute(Long) - 단일 자식 ID로 부모 카테고리 조회")
    class ExecuteSingleTest {

        @Test
        @DisplayName("자식 카테고리 ID로 부모 카테고리 목록을 반환한다")
        void execute_ReturnsParentCategories() {
            // given
            Long childCategoryId = 2L;
            CategoryId childId = CategoryId.of(childCategoryId);
            List<Category> parents = List.of(CategoryFixtures.activeCategory());
            List<TreeCategoryResult> expected =
                    parents.stream()
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

            given(readManager.findParentsByChildId(childId)).willReturn(parents);
            given(assembler.toLeafResults(parents)).willReturn(expected);

            // when
            List<TreeCategoryResult> result = sut.execute(childCategoryId);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result).hasSize(1);
            then(readManager).should().findParentsByChildId(childId);
            then(assembler).should().toLeafResults(parents);
        }

        @Test
        @DisplayName("부모 카테고리가 없으면 빈 목록을 반환한다")
        void execute_NoParents_ReturnsEmptyList() {
            // given
            Long childCategoryId = 1L;
            CategoryId childId = CategoryId.of(childCategoryId);
            List<Category> emptyParents = Collections.emptyList();
            List<TreeCategoryResult> expected = Collections.emptyList();

            given(readManager.findParentsByChildId(childId)).willReturn(emptyParents);
            given(assembler.toLeafResults(emptyParents)).willReturn(expected);

            // when
            List<TreeCategoryResult> result = sut.execute(childCategoryId);

            // then
            assertThat(result).isEmpty();
        }
    }
}
