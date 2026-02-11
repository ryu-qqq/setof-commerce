package com.ryuqq.setof.adapter.in.rest.admin.v1.category.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.v1.category.CategoryAdminApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.request.SearchCategoriesV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.ProductCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.category.dto.query.CategorySearchParams;
import com.ryuqq.setof.application.category.dto.response.CategoryPageResult;
import com.ryuqq.setof.application.category.dto.response.CategoryResult;
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
    @DisplayName("toSearchParams 메서드 테스트")
    class ToSearchParamsTest {

        @Test
        @DisplayName("검색 요청을 SearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            SearchCategoriesV1ApiRequest request =
                    CategoryAdminApiFixtures.searchCategoriesRequest();

            // when
            CategorySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchField()).isEqualTo("displayName");
            assertThat(params.searchWord()).isEqualTo("의류");
            assertThat(params.depth()).isEqualTo(2);
            assertThat(params.displayed()).isNull();
            assertThat(params.searchParams().page()).isEqualTo(0);
            assertThat(params.searchParams().size()).isEqualTo(20);
            assertThat(params.searchParams().sortKey()).isEqualTo("createdAt");
            assertThat(params.searchParams().sortDirection()).isEqualTo("ASC");
        }

        @Test
        @DisplayName("null 값에 대해 기본값을 적용한다")
        void toSearchParams_WithNullValues_AppliesDefaults() {
            // given
            SearchCategoriesV1ApiRequest request =
                    CategoryAdminApiFixtures.searchCategoriesRequestWithDefaults();

            // when
            CategorySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchWord()).isNull();
            assertThat(params.searchParams().page()).isEqualTo(0);
            assertThat(params.searchParams().size()).isEqualTo(20);
        }

        @Test
        @DisplayName("빈 문자열 categoryName은 null로 변환된다")
        void toSearchParams_WithBlankCategoryName_ConvertsToNull() {
            // given
            SearchCategoriesV1ApiRequest request =
                    CategoryAdminApiFixtures.searchCategoriesRequestWithBlankName();

            // when
            CategorySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchWord()).isNull();
        }
    }

    @Nested
    @DisplayName("toTreeListResponse 메서드 테스트")
    class ToTreeListResponseTest {

        @Test
        @DisplayName("TreeCategoryResult 목록을 TreeCategoryV1ApiResponse 목록으로 변환한다")
        void toTreeListResponse_Success() {
            // given
            List<TreeCategoryResult> results =
                    List.of(
                            CategoryAdminApiFixtures.treeCategoryResult(100L),
                            CategoryAdminApiFixtures.treeCategoryResult(200L));

            // when
            List<TreeCategoryV1ApiResponse> responses = mapper.toTreeListResponse(results);

            // then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).categoryId()).isEqualTo(100L);
            assertThat(responses.get(0).children()).hasSize(1);
            assertThat(responses.get(1).categoryId()).isEqualTo(200L);
        }

        @Test
        @DisplayName("빈 목록을 처리한다")
        void toTreeListResponse_WithEmptyList_ReturnsEmptyList() {
            // given
            List<TreeCategoryResult> results = List.of();

            // when
            List<TreeCategoryV1ApiResponse> responses = mapper.toTreeListResponse(results);

            // then
            assertThat(responses).isEmpty();
        }
    }

    @Nested
    @DisplayName("toTreeResponse 메서드 테스트")
    class ToTreeResponseTest {

        @Test
        @DisplayName("TreeCategoryResult를 TreeCategoryV1ApiResponse로 변환한다 (재귀)")
        void toTreeResponse_Success() {
            // given
            TreeCategoryResult result = CategoryAdminApiFixtures.treeCategoryResult(100L);

            // when
            TreeCategoryV1ApiResponse response = mapper.toTreeResponse(result);

            // then
            assertThat(response.categoryId()).isEqualTo(100L);
            assertThat(response.categoryName()).isEqualTo("의류");
            assertThat(response.displayName()).isEqualTo("의류");
            assertThat(response.categoryDepth()).isEqualTo(1);
            assertThat(response.parentCategoryId()).isEqualTo(0L);
            assertThat(response.children()).hasSize(1);
            assertThat(response.children().get(0).categoryId()).isEqualTo(101L);
        }

        @Test
        @DisplayName("null 값을 기본값으로 변환한다")
        void toTreeResponse_WithNullValues_AppliesDefaults() {
            // given
            TreeCategoryResult result = CategoryAdminApiFixtures.treeCategoryResultWithNulls();

            // when
            TreeCategoryV1ApiResponse response = mapper.toTreeResponse(result);

            // then
            assertThat(response.categoryId()).isEqualTo(0L);
            assertThat(response.categoryName()).isEmpty();
            assertThat(response.displayName()).isEmpty();
            assertThat(response.parentCategoryId()).isEqualTo(0L);
            assertThat(response.children()).isEmpty();
        }

        @Test
        @DisplayName("자식이 없는 카테고리를 처리한다")
        void toTreeResponse_WithNoChildren_ReturnsEmptyChildrenList() {
            // given
            TreeCategoryResult result = CategoryAdminApiFixtures.leafTreeCategoryResult(300L);

            // when
            TreeCategoryV1ApiResponse response = mapper.toTreeResponse(result);

            // then
            assertThat(response.children()).isEmpty();
        }
    }

    @Nested
    @DisplayName("toTreeLeafResponse 메서드 테스트")
    class ToTreeLeafResponseTest {

        @Test
        @DisplayName("TreeCategoryResult를 Leaf Response로 변환한다")
        void toTreeLeafResponse_Success() {
            // given
            TreeCategoryResult result = CategoryAdminApiFixtures.leafTreeCategoryResult(300L);

            // when
            TreeCategoryV1ApiResponse response = mapper.toTreeLeafResponse(result);

            // then
            assertThat(response.categoryId()).isEqualTo(300L);
            assertThat(response.categoryName()).isEqualTo("티셔츠");
            assertThat(response.displayName()).isEqualTo("티셔츠");
            assertThat(response.categoryDepth()).isEqualTo(3);
            assertThat(response.parentCategoryId()).isEqualTo(200L);
            assertThat(response.children()).isEmpty();
        }

        @Test
        @DisplayName("null 값을 기본값으로 변환하고 children은 빈 목록이다")
        void toTreeLeafResponse_WithNullValues_AppliesDefaults() {
            // given
            TreeCategoryResult result = CategoryAdminApiFixtures.treeCategoryResultWithNulls();

            // when
            TreeCategoryV1ApiResponse response = mapper.toTreeLeafResponse(result);

            // then
            assertThat(response.categoryId()).isEqualTo(0L);
            assertThat(response.categoryName()).isEmpty();
            assertThat(response.displayName()).isEmpty();
            assertThat(response.parentCategoryId()).isEqualTo(0L);
            assertThat(response.children()).isEmpty();
        }
    }

    @Nested
    @DisplayName("toPageResponse 메서드 테스트")
    class ToPageResponseTest {

        @Test
        @DisplayName("CategoryPageResult를 CustomPageableV1ApiResponse로 변환한다")
        void toPageResponse_Success() {
            // given
            CategoryPageResult pageResult = CategoryAdminApiFixtures.categoryPageResult();

            // when
            CustomPageableV1ApiResponse<ProductCategoryV1ApiResponse> response =
                    mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(2);
            assertThat(response.totalElements()).isEqualTo(50L);
            assertThat(response.number()).isEqualTo(0);
            assertThat(response.size()).isEqualTo(20);
            assertThat(response.totalPages()).isEqualTo(3);
        }

        @Test
        @DisplayName("빈 페이지를 처리한다")
        void toPageResponse_WithEmptyPage_ReturnsEmptyResponse() {
            // given
            CategoryPageResult pageResult = CategoryAdminApiFixtures.categoryPageResultEmpty();

            // when
            CustomPageableV1ApiResponse<ProductCategoryV1ApiResponse> response =
                    mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).isEmpty();
            assertThat(response.totalElements()).isEqualTo(0L);
            assertThat(response.empty()).isTrue();
        }
    }

    @Nested
    @DisplayName("toProductResponse 메서드 테스트")
    class ToProductResponseTest {

        @Test
        @DisplayName("CategoryResult를 ProductCategoryV1ApiResponse로 변환한다")
        void toProductResponse_Success() {
            // given
            CategoryResult result = CategoryAdminApiFixtures.categoryResult(100L);

            // when
            ProductCategoryV1ApiResponse response = mapper.toProductResponse(result);

            // then
            assertThat(response.categoryId()).isEqualTo(100L);
            assertThat(response.categoryName()).isEqualTo("티셔츠");
            assertThat(response.displayName()).isEqualTo("티셔츠");
            assertThat(response.categoryDepth()).isEqualTo(3);
            assertThat(response.categoryFullPath()).isEqualTo("의류 > 상의 > 티셔츠");
            assertThat(response.targetGroup()).isEqualTo("ALL");
        }

        @Test
        @DisplayName("null 값을 기본값으로 변환한다")
        void toProductResponse_WithNullValues_AppliesDefaults() {
            // given
            CategoryResult result = CategoryAdminApiFixtures.categoryResultWithNulls();

            // when
            ProductCategoryV1ApiResponse response = mapper.toProductResponse(result);

            // then
            assertThat(response.categoryId()).isEqualTo(0L);
            assertThat(response.categoryName()).isEmpty();
            assertThat(response.displayName()).isEmpty();
            assertThat(response.categoryDepth()).isEqualTo(0);
            assertThat(response.categoryFullPath()).isEmpty();
            assertThat(response.targetGroup()).isEqualTo("ALL");
        }
    }
}
