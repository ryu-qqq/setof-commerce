package com.ryuqq.setof.adapter.in.rest.admin.v1.category.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.category.CategoryV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * CategoryAdminV1ApiMapper 단위 테스트.
 *
 * <p>V1 Admin 카테고리 API Mapper의 변환 로직을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CategoryAdminV1ApiMapper 단위 테스트")
class CategoryAdminV1ApiMapperTest {

    private CategoryAdminV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CategoryAdminV1ApiMapper();
    }

    @Nested
    @DisplayName("toTreeResponse 테스트")
    class ToTreeResponseTest {

        @Test
        @DisplayName("단일 트리 카테고리 결과 변환")
        void shouldConvertSingleTreeCategoryResult() {
            // given
            List<TreeCategoryResult> results = List.of(CategoryV1ApiFixtures.treeCategoryResult());

            // when
            List<TreeCategoryV1ApiResponse> response = mapper.toTreeResponse(results);

            // then
            assertThat(response).hasSize(1);
            assertThat(response.get(0).categoryId()).isEqualTo(1L);
            assertThat(response.get(0).categoryName()).isEqualTo("의류");
            assertThat(response.get(0).categoryDepth()).isEqualTo(1);
            assertThat(response.get(0).parentCategoryId()).isZero();
        }

        @Test
        @DisplayName("자식 포함 트리 카테고리 변환")
        void shouldConvertTreeCategoryWithChildren() {
            // given
            List<TreeCategoryResult> results =
                    List.of(CategoryV1ApiFixtures.treeCategoryResultWithChildren());

            // when
            List<TreeCategoryV1ApiResponse> response = mapper.toTreeResponse(results);

            // then
            assertThat(response).hasSize(1);
            TreeCategoryV1ApiResponse parent = response.get(0);
            assertThat(parent.children()).hasSize(2);
            assertThat(parent.children().get(0).categoryName()).isEqualTo("상의");
            assertThat(parent.children().get(1).categoryName()).isEqualTo("하의");
        }

        @Test
        @DisplayName("여러 트리 카테고리 결과 변환")
        void shouldConvertMultipleTreeCategoryResults() {
            // given
            List<TreeCategoryResult> results = CategoryV1ApiFixtures.multipleTreeCategoryResults();

            // when
            List<TreeCategoryV1ApiResponse> response = mapper.toTreeResponse(results);

            // then
            assertThat(response).hasSize(3);
            assertThat(response.get(0).categoryName()).isEqualTo("의류");
            assertThat(response.get(1).categoryName()).isEqualTo("신발");
            assertThat(response.get(2).categoryName()).isEqualTo("가방");
        }

        @Test
        @DisplayName("빈 결과 리스트 변환")
        void shouldConvertEmptyResultList() {
            // given
            List<TreeCategoryResult> results = List.of();

            // when
            List<TreeCategoryV1ApiResponse> response = mapper.toTreeResponse(results);

            // then
            assertThat(response).isEmpty();
        }

        @Test
        @DisplayName("트리 응답 필드 매핑 검증")
        void shouldMapTreeResponseFieldsCorrectly() {
            // given
            TreeCategoryResult result =
                    TreeCategoryResult.leaf(100L, "테스트카테고리", 10L, 2, true, "/10/100");
            List<TreeCategoryResult> results = List.of(result);

            // when
            List<TreeCategoryV1ApiResponse> response = mapper.toTreeResponse(results);

            // then
            TreeCategoryV1ApiResponse categoryResponse = response.get(0);
            assertThat(categoryResponse.categoryId()).isEqualTo(100L);
            assertThat(categoryResponse.categoryName()).isEqualTo("테스트카테고리");
            assertThat(categoryResponse.categoryDepth()).isEqualTo(2);
            assertThat(categoryResponse.parentCategoryId()).isEqualTo(10L);
            assertThat(categoryResponse.children()).isEmpty();
        }
    }
}
