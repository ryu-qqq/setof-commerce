package com.ryuqq.setof.application.category.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.category.port.out.query.CategoryQueryPort;
import com.ryuqq.setof.domain.category.CategoryFixtures;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.exception.CategoryNotFoundException;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
@DisplayName("CategoryReadManager 단위 테스트")
class CategoryReadManagerTest {

    @InjectMocks private CategoryReadManager sut;

    @Mock private CategoryQueryPort queryPort;

    @Nested
    @DisplayName("getById() - ID로 카테고리 조회")
    class GetByIdTest {

        @Test
        @DisplayName("ID로 카테고리를 조회한다")
        void getById_ExistingCategory_ReturnsCategory() {
            // given
            CategoryId id = CategoryId.of(1L);
            Category expected = CategoryFixtures.activeCategory();

            given(queryPort.findById(id)).willReturn(Optional.of(expected));

            // when
            Category result = sut.getById(id);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findById(id);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다")
        void getById_NonExistingCategory_ThrowsException() {
            // given
            CategoryId id = CategoryId.of(999L);

            given(queryPort.findById(id)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getById(id)).isInstanceOf(CategoryNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findByCriteria() - 검색 조건으로 카테고리 조회")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 카테고리 목록을 조회한다")
        void findByCriteria_ReturnsMatchingCategories() {
            // given
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();
            List<Category> expected =
                    List.of(CategoryFixtures.activeCategory(), CategoryFixtures.inactiveCategory());

            given(queryPort.findByCriteria(criteria)).willReturn(expected);

            // when
            List<Category> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void findByCriteria_NoResults_ReturnsEmptyList() {
            // given
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();

            given(queryPort.findByCriteria(criteria)).willReturn(Collections.emptyList());

            // when
            List<Category> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findChildrenByParentId() - 부모 ID로 자식 카테고리 조회")
    class FindChildrenByParentIdTest {

        @Test
        @DisplayName("부모 ID로 자식 카테고리 목록을 조회한다")
        void findChildrenByParentId_ReturnsChildren() {
            // given
            CategoryId parentId = CategoryId.of(1L);
            List<Category> expected = List.of(CategoryFixtures.activeChildCategory(2L, 1L));

            given(queryPort.findChildrenByParentId(parentId)).willReturn(expected);

            // when
            List<Category> result = sut.findChildrenByParentId(parentId);

            // then
            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("findParentsByChildId() - 자식 ID로 부모 카테고리 조회")
    class FindParentsByChildIdTest {

        @Test
        @DisplayName("자식 ID로 부모 카테고리 목록을 조회한다")
        void findParentsByChildId_ReturnsParents() {
            // given
            CategoryId childId = CategoryId.of(2L);
            List<Category> expected = List.of(CategoryFixtures.activeCategory());

            given(queryPort.findParentsByChildId(childId)).willReturn(expected);

            // when
            List<Category> result = sut.findParentsByChildId(childId);

            // then
            assertThat(result).hasSize(1);
        }
    }
}
