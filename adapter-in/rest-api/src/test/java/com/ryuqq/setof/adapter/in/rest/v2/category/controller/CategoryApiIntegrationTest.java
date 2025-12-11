package com.ryuqq.setof.adapter.in.rest.v2.category.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.common.TestSecurityConfig;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.category.dto.response.CategoryPathV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.category.dto.response.CategoryTreeV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.category.dto.response.CategoryV2ApiResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * Category API 통합 테스트
 *
 * <p>Category V2 REST API 엔드포인트의 통합 동작을 검증합니다.
 *
 * <p><strong>테스트 범위:</strong>
 *
 * <ul>
 *   <li>GET /api/v2/categories/{categoryId} - 카테고리 단건 조회
 *   <li>GET /api/v2/categories - 하위 카테고리 목록 조회
 *   <li>GET /api/v2/categories/tree - 카테고리 트리 조회
 *   <li>GET /api/v2/categories/{categoryId}/path - 카테고리 경로 조회 (Breadcrumb)
 * </ul>
 *
 * <p><strong>사용 도구:</strong>
 *
 * <ul>
 *   <li>TestRestTemplate - 실제 HTTP 요청/응답 테스트
 *   <li>TestContainers MySQL - 실제 DB 테스트
 *   <li>@Sql - 테스트 데이터 준비
 * </ul>
 *
 * @author Development Team
 * @since 2.0.0
 * @see CategoryV2Controller
 */
@DisplayName("Category API 통합 테스트")
@Import(TestSecurityConfig.class)
class CategoryApiIntegrationTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = ApiV2Paths.Categories.BASE;

    @Nested
    @DisplayName("GET /api/v2/categories/{categoryId} - 카테고리 단건 조회")
    class GetCategory {

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - 카테고리 단건 조회")
        void getCategory_success() {
            // Given
            Long categoryId = 1L;

            // When
            ResponseEntity<ApiResponse<CategoryV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{categoryId}",
                            new ParameterizedTypeReference<>() {},
                            categoryId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            CategoryV2ApiResponse category = response.getBody().data();
            assertThat(category).isNotNull();
            assertThat(category.categoryId()).isEqualTo(1L);
            assertThat(category.code()).isEqualTo("FASHION");
            assertThat(category.nameKo()).isEqualTo("패션");
            assertThat(category.depth()).isEqualTo(0);
            assertThat(category.isLeaf()).isFalse();
            assertThat(category.status()).isEqualTo("ACTIVE");
        }

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - 중분류 카테고리 조회")
        void getCategory_middleCategory() {
            // Given
            Long categoryId = 2L;

            // When
            ResponseEntity<ApiResponse<CategoryV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{categoryId}",
                            new ParameterizedTypeReference<>() {},
                            categoryId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            CategoryV2ApiResponse category = response.getBody().data();
            assertThat(category.categoryId()).isEqualTo(2L);
            assertThat(category.code()).isEqualTo("CLOTHING");
            assertThat(category.nameKo()).isEqualTo("의류");
            assertThat(category.parentId()).isEqualTo(1L);
            assertThat(category.depth()).isEqualTo(1);
            assertThat(category.path()).isEqualTo("/1/2/");
        }

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - 리프 노드 카테고리 조회")
        void getCategory_leafCategory() {
            // Given
            Long categoryId = 3L;

            // When
            ResponseEntity<ApiResponse<CategoryV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{categoryId}",
                            new ParameterizedTypeReference<>() {},
                            categoryId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            CategoryV2ApiResponse category = response.getBody().data();
            assertThat(category.categoryId()).isEqualTo(3L);
            assertThat(category.code()).isEqualTo("TOPS");
            assertThat(category.depth()).isEqualTo(2);
            assertThat(category.isLeaf()).isTrue();
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 카테고리 조회 시 404 에러")
        void getCategory_notFound() {
            // Given
            Long nonExistentId = 999999L;

            // When
            ResponseEntity<ProblemDetail> response =
                    get(BASE_URL + "/{categoryId}", ProblemDetail.class, nonExistentId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("GET /api/v2/categories - 하위 카테고리 목록 조회")
    class GetCategories {

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - 최상위 카테고리 목록 조회 (parentId 없음)")
        void getCategories_rootCategories() {
            // When
            ResponseEntity<ApiResponse<List<CategoryV2ApiResponse>>> response =
                    get(BASE_URL, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            List<CategoryV2ApiResponse> categories = response.getBody().data();
            assertThat(categories).isNotEmpty();
            assertThat(categories).allMatch(c -> c.depth() == 0);
            assertThat(categories).allMatch(c -> "ACTIVE".equals(c.status()));
        }

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - 하위 카테고리 목록 조회 (parentId 지정)")
        void getCategories_childCategories() {
            // Given
            Long parentId = 1L;

            // When
            ResponseEntity<ApiResponse<List<CategoryV2ApiResponse>>> response =
                    get(
                            BASE_URL + "?parentId={parentId}",
                            new ParameterizedTypeReference<>() {},
                            parentId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            List<CategoryV2ApiResponse> categories = response.getBody().data();
            assertThat(categories).isNotEmpty();
            assertThat(categories)
                    .allMatch(c -> c.parentId() != null && c.parentId().equals(parentId));
        }

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - 하위 카테고리가 없는 경우 빈 목록 반환")
        void getCategories_noChildren() {
            // Given - 리프 노드의 하위 카테고리 조회
            Long leafCategoryId = 3L;

            // When
            ResponseEntity<ApiResponse<List<CategoryV2ApiResponse>>> response =
                    get(
                            BASE_URL + "?parentId={parentId}",
                            new ParameterizedTypeReference<>() {},
                            leafCategoryId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            List<CategoryV2ApiResponse> categories = response.getBody().data();
            assertThat(categories).isEmpty();
        }

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - sortOrder 순으로 정렬된 목록 반환")
        void getCategories_orderedBySortOrder() {
            // Given
            Long parentId = 2L;

            // When
            ResponseEntity<ApiResponse<List<CategoryV2ApiResponse>>> response =
                    get(
                            BASE_URL + "?parentId={parentId}",
                            new ParameterizedTypeReference<>() {},
                            parentId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            List<CategoryV2ApiResponse> categories = response.getBody().data();
            assertThat(categories).hasSize(2);
            assertThat(categories.get(0).code()).isEqualTo("TOPS");
            assertThat(categories.get(1).code()).isEqualTo("BOTTOMS");
        }
    }

    @Nested
    @DisplayName("GET /api/v2/categories/tree - 카테고리 트리 조회")
    class GetCategoryTree {

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - 전체 카테고리 트리 조회")
        void getCategoryTree_success() {
            // When
            ResponseEntity<ApiResponse<List<CategoryTreeV2ApiResponse>>> response =
                    get(BASE_URL + "/tree", new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            List<CategoryTreeV2ApiResponse> treeResponse = response.getBody().data();
            assertThat(treeResponse).isNotEmpty();
            assertThat(treeResponse).allMatch(c -> c.depth() == 0);
        }

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - 트리 구조 검증 (패션 > 의류 > 상의/하의)")
        void getCategoryTree_hierarchyVerification() {
            // When
            ResponseEntity<ApiResponse<List<CategoryTreeV2ApiResponse>>> response =
                    get(BASE_URL + "/tree", new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            List<CategoryTreeV2ApiResponse> rootCategories = response.getBody().data();

            CategoryTreeV2ApiResponse fashionTree =
                    rootCategories.stream()
                            .filter(c -> "FASHION".equals(c.code()))
                            .findFirst()
                            .orElse(null);

            assertThat(fashionTree).isNotNull();
            assertThat(fashionTree.children()).isNotEmpty();

            CategoryTreeV2ApiResponse clothingTree =
                    fashionTree.children().stream()
                            .filter(c -> "CLOTHING".equals(c.code()))
                            .findFirst()
                            .orElse(null);

            assertThat(clothingTree).isNotNull();
            assertThat(clothingTree.children()).hasSize(2);
        }

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - 리프 노드는 children이 비어있음")
        void getCategoryTree_leafNodeHasNoChildren() {
            // When
            ResponseEntity<ApiResponse<List<CategoryTreeV2ApiResponse>>> response =
                    get(BASE_URL + "/tree", new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            List<CategoryTreeV2ApiResponse> rootCategories = response.getBody().data();

            CategoryTreeV2ApiResponse fashionTree =
                    rootCategories.stream()
                            .filter(c -> "FASHION".equals(c.code()))
                            .findFirst()
                            .orElse(null);

            assertThat(fashionTree).isNotNull();

            CategoryTreeV2ApiResponse clothingTree =
                    fashionTree.children().stream()
                            .filter(c -> "CLOTHING".equals(c.code()))
                            .findFirst()
                            .orElse(null);

            assertThat(clothingTree).isNotNull();

            CategoryTreeV2ApiResponse topsTree =
                    clothingTree.children().stream()
                            .filter(c -> "TOPS".equals(c.code()))
                            .findFirst()
                            .orElse(null);

            assertThat(topsTree).isNotNull();
            assertThat(topsTree.isLeaf()).isTrue();
            assertThat(topsTree.children()).isEmpty();
        }
    }

    @Nested
    @DisplayName("GET /api/v2/categories/{categoryId}/path - 카테고리 경로 조회")
    class GetCategoryPath {

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - 리프 노드의 경로 조회 (패션 > 의류 > 상의)")
        void getCategoryPath_leafNode() {
            // Given
            Long categoryId = 3L;

            // When
            ResponseEntity<ApiResponse<CategoryPathV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{categoryId}/path",
                            new ParameterizedTypeReference<>() {},
                            categoryId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            CategoryPathV2ApiResponse pathResponse = response.getBody().data();
            assertThat(pathResponse.categoryId()).isEqualTo(3L);
            assertThat(pathResponse.breadcrumbs()).hasSize(3);

            assertThat(pathResponse.breadcrumbs().get(0).code()).isEqualTo("FASHION");
            assertThat(pathResponse.breadcrumbs().get(0).depth()).isEqualTo(0);

            assertThat(pathResponse.breadcrumbs().get(1).code()).isEqualTo("CLOTHING");
            assertThat(pathResponse.breadcrumbs().get(1).depth()).isEqualTo(1);

            assertThat(pathResponse.breadcrumbs().get(2).code()).isEqualTo("TOPS");
            assertThat(pathResponse.breadcrumbs().get(2).depth()).isEqualTo(2);
        }

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - 최상위 카테고리의 경로 조회 (1개 항목)")
        void getCategoryPath_rootCategory() {
            // Given
            Long categoryId = 1L;

            // When
            ResponseEntity<ApiResponse<CategoryPathV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{categoryId}/path",
                            new ParameterizedTypeReference<>() {},
                            categoryId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            CategoryPathV2ApiResponse pathResponse = response.getBody().data();
            assertThat(pathResponse.categoryId()).isEqualTo(1L);
            assertThat(pathResponse.breadcrumbs()).hasSize(1);
            assertThat(pathResponse.breadcrumbs().get(0).code()).isEqualTo("FASHION");
        }

        @Test
        @Sql("/sql/category/categories-test-data.sql")
        @DisplayName("성공 - 중분류 카테고리의 경로 조회")
        void getCategoryPath_middleCategory() {
            // Given
            Long categoryId = 6L;

            // When
            ResponseEntity<ApiResponse<CategoryPathV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{categoryId}/path",
                            new ParameterizedTypeReference<>() {},
                            categoryId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            CategoryPathV2ApiResponse pathResponse = response.getBody().data();
            assertThat(pathResponse.categoryId()).isEqualTo(6L);
            assertThat(pathResponse.breadcrumbs()).hasSize(2);
            assertThat(pathResponse.breadcrumbs().get(0).code()).isEqualTo("ELECTRONICS");
            assertThat(pathResponse.breadcrumbs().get(1).code()).isEqualTo("COMPUTERS");
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 카테고리 경로 조회 시 404 에러")
        void getCategoryPath_notFound() {
            // Given
            Long nonExistentId = 999999L;

            // When
            ResponseEntity<ProblemDetail> response =
                    get(BASE_URL + "/{categoryId}/path", ProblemDetail.class, nonExistentId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
