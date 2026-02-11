package com.ryuqq.setof.adapter.in.rest.v1.category.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.category.CategoryApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryDisplayResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * CategoryV1ApiMapper 단위 테스트.
 *
 * <p>Category API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CategoryV1ApiMapper 단위 테스트")
class CategoryV1ApiMapperTest {

    private CategoryV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CategoryV1ApiMapper();
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("자식 없는 카테고리 결과를 응답으로 변환한다")
        void toResponse_LeafNode_Success() {
            // given
            CategoryDisplayResult result = CategoryApiFixtures.displayResult(1L);

            // when
            TreeCategoryV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.categoryId()).isEqualTo(1L);
            assertThat(response.categoryName()).isEqualTo("여성");
            assertThat(response.categoryDepth()).isEqualTo(1);
            assertThat(response.parentCategoryId()).isEqualTo(0L);
            assertThat(response.children()).isEmpty();
        }

        @Test
        @DisplayName("자식 포함 카테고리 결과를 응답으로 변환한다")
        void toResponse_WithChildren_Success() {
            // given
            CategoryDisplayResult child1 = CategoryApiFixtures.displayResult(11L, "상의", 1L, 2);
            CategoryDisplayResult child2 = CategoryApiFixtures.displayResult(12L, "하의", 1L, 2);
            CategoryDisplayResult parent =
                    CategoryApiFixtures.displayResultWithChildren(
                            1L, "여성", 0L, 1, List.of(child1, child2));

            // when
            TreeCategoryV1ApiResponse response = mapper.toResponse(parent);

            // then
            assertThat(response.categoryId()).isEqualTo(1L);
            assertThat(response.categoryName()).isEqualTo("여성");
            assertThat(response.categoryDepth()).isEqualTo(1);
            assertThat(response.parentCategoryId()).isEqualTo(0L);
            assertThat(response.children()).hasSize(2);

            TreeCategoryV1ApiResponse childResponse1 = response.children().get(0);
            assertThat(childResponse1.categoryId()).isEqualTo(11L);
            assertThat(childResponse1.categoryName()).isEqualTo("상의");
            assertThat(childResponse1.categoryDepth()).isEqualTo(2);
            assertThat(childResponse1.parentCategoryId()).isEqualTo(1L);
            assertThat(childResponse1.children()).isEmpty();

            TreeCategoryV1ApiResponse childResponse2 = response.children().get(1);
            assertThat(childResponse2.categoryId()).isEqualTo(12L);
            assertThat(childResponse2.categoryName()).isEqualTo("하의");
        }

        @Test
        @DisplayName("null 자식 목록을 빈 목록으로 변환한다")
        void toResponse_NullChildren_Success() {
            // given
            CategoryDisplayResult result = CategoryDisplayResult.of(1L, "여성", 0L, 1, null);

            // when
            TreeCategoryV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.children()).isEmpty();
        }

        @Test
        @DisplayName("null 필드를 기본값으로 변환한다")
        void toResponse_NullFields_Success() {
            // given
            CategoryDisplayResult result = CategoryDisplayResult.of(null, null, null, 1, List.of());

            // when
            TreeCategoryV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.categoryId()).isEqualTo(0L);
            assertThat(response.categoryName()).isEmpty();
            assertThat(response.parentCategoryId()).isEqualTo(0L);
            assertThat(response.categoryDepth()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("toListResponse")
    class ToListResponseTest {

        @Test
        @DisplayName("카테고리 결과 목록을 응답 목록으로 변환한다")
        void toListResponse_Success() {
            // given
            List<CategoryDisplayResult> results = CategoryApiFixtures.displayResultTreeList();

            // when
            List<TreeCategoryV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).hasSize(1);
            TreeCategoryV1ApiResponse parent = response.get(0);
            assertThat(parent.categoryId()).isEqualTo(1L);
            assertThat(parent.categoryName()).isEqualTo("여성");
            assertThat(parent.children()).hasSize(2);
        }

        @Test
        @DisplayName("빈 목록을 빈 응답으로 변환한다")
        void toListResponse_Empty() {
            // given
            List<CategoryDisplayResult> results = List.of();

            // when
            List<TreeCategoryV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).isEmpty();
        }

        @Test
        @DisplayName("다단계 트리 구조를 올바르게 변환한다")
        void toListResponse_MultiLevel_Success() {
            // given
            CategoryDisplayResult grandChild =
                    CategoryApiFixtures.displayResult(111L, "티셔츠", 11L, 3);
            CategoryDisplayResult child =
                    CategoryApiFixtures.displayResultWithChildren(
                            11L, "상의", 1L, 2, List.of(grandChild));
            CategoryDisplayResult parent =
                    CategoryApiFixtures.displayResultWithChildren(1L, "여성", 0L, 1, List.of(child));

            List<CategoryDisplayResult> results = List.of(parent);

            // when
            List<TreeCategoryV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).hasSize(1);
            TreeCategoryV1ApiResponse parentResponse = response.get(0);
            assertThat(parentResponse.children()).hasSize(1);

            TreeCategoryV1ApiResponse childResponse = parentResponse.children().get(0);
            assertThat(childResponse.categoryId()).isEqualTo(11L);
            assertThat(childResponse.children()).hasSize(1);

            TreeCategoryV1ApiResponse grandChildResponse = childResponse.children().get(0);
            assertThat(grandChildResponse.categoryId()).isEqualTo(111L);
            assertThat(grandChildResponse.categoryName()).isEqualTo("티셔츠");
            assertThat(grandChildResponse.categoryDepth()).isEqualTo(3);
            assertThat(grandChildResponse.parentCategoryId()).isEqualTo(11L);
            assertThat(grandChildResponse.children()).isEmpty();
        }
    }
}
