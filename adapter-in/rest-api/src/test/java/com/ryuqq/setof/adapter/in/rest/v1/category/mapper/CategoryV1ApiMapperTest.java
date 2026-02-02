package com.ryuqq.setof.adapter.in.rest.v1.category.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.category.CategoryV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.CategoryDisplayV1ApiResponse;
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
 * <p>V1 Public 카테고리 API Mapper의 변환 로직을 검증합니다.
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
    @DisplayName("toListResponse 테스트")
    class ToListResponseTest {

        @Test
        @DisplayName("단일 카테고리 결과 변환")
        void shouldConvertSingleCategoryResult() {
            // given
            List<CategoryDisplayResult> results =
                    List.of(CategoryV1ApiFixtures.categoryDisplayResult());

            // when
            List<CategoryDisplayV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).hasSize(1);
            assertThat(response.get(0).categoryId()).isEqualTo(1L);
            assertThat(response.get(0).categoryName()).isEqualTo("의류");
            assertThat(response.get(0).categoryDepth()).isEqualTo(1);
            assertThat(response.get(0).parentCategoryId()).isZero();
            assertThat(response.get(0).children()).isEmpty();
        }

        @Test
        @DisplayName("자식 포함 카테고리 변환")
        void shouldConvertCategoryWithChildren() {
            // given
            List<CategoryDisplayResult> results =
                    List.of(CategoryV1ApiFixtures.categoryDisplayResultWithChildren());

            // when
            List<CategoryDisplayV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).hasSize(1);
            CategoryDisplayV1ApiResponse parent = response.get(0);
            assertThat(parent.children()).hasSize(2);
            assertThat(parent.children().get(0).categoryName()).isEqualTo("상의");
            assertThat(parent.children().get(0).categoryDepth()).isEqualTo(2);
            assertThat(parent.children().get(0).parentCategoryId()).isEqualTo(1L);
            assertThat(parent.children().get(1).categoryName()).isEqualTo("하의");
        }

        @Test
        @DisplayName("여러 카테고리 결과 변환")
        void shouldConvertMultipleCategoryResults() {
            // given
            List<CategoryDisplayResult> results =
                    CategoryV1ApiFixtures.multipleCategoryDisplayResults();

            // when
            List<CategoryDisplayV1ApiResponse> response = mapper.toListResponse(results);

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
            List<CategoryDisplayResult> results = List.of();

            // when
            List<CategoryDisplayV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).isEmpty();
        }

        @Test
        @DisplayName("트리 구조 응답 필드 매핑 검증")
        void shouldMapTreeResponseFieldsCorrectly() {
            // given
            CategoryDisplayResult result = CategoryDisplayResult.leaf(100L, "테스트카테고리", 10L, 2);
            List<CategoryDisplayResult> results = List.of(result);

            // when
            List<CategoryDisplayV1ApiResponse> response = mapper.toListResponse(results);

            // then
            CategoryDisplayV1ApiResponse categoryResponse = response.get(0);
            assertThat(categoryResponse.categoryId()).isEqualTo(100L);
            assertThat(categoryResponse.categoryName()).isEqualTo("테스트카테고리");
            assertThat(categoryResponse.categoryDepth()).isEqualTo(2);
            assertThat(categoryResponse.parentCategoryId()).isEqualTo(10L);
            assertThat(categoryResponse.children()).isEmpty();
        }

        @Test
        @DisplayName("중첩 트리 구조 재귀 변환")
        void shouldConvertNestedTreeStructure() {
            // given
            List<CategoryDisplayResult> results =
                    CategoryV1ApiFixtures.treeCategoryDisplayResults();

            // when
            List<CategoryDisplayV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).hasSize(3);

            // 첫 번째 카테고리는 자식이 있음
            CategoryDisplayV1ApiResponse clothing = response.get(0);
            assertThat(clothing.categoryName()).isEqualTo("의류");
            assertThat(clothing.children()).hasSize(2);

            // 자식 카테고리 검증
            assertThat(clothing.children().get(0).categoryName()).isEqualTo("상의");
            assertThat(clothing.children().get(0).parentCategoryId()).isEqualTo(1L);
            assertThat(clothing.children().get(1).categoryName()).isEqualTo("하의");

            // 나머지 카테고리는 자식이 없음
            assertThat(response.get(1).categoryName()).isEqualTo("신발");
            assertThat(response.get(1).children()).isEmpty();
            assertThat(response.get(2).categoryName()).isEqualTo("가방");
            assertThat(response.get(2).children()).isEmpty();
        }
    }
}
