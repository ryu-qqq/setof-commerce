package com.ryuqq.setof.adapter.in.rest.admin.integration.v1.category;

import static com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.CategoryAdminTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryChildrenUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryTreeUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Admin Category V1 API 통합 테스트 (Legacy)
 *
 * <p>CategoryV1Controller의 모든 엔드포인트를 통합 테스트합니다.
 *
 * <p><strong>테스트 대상 엔드포인트:</strong>
 *
 * <ul>
 *   <li>GET /api/v1/category - 전체 카테고리 트리 조회
 *   <li>GET /api/v1/category/{categoryId} - 하위 카테고리 목록 조회
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("Admin Category V1 API 통합 테스트 (Legacy)")
class CategoryAdminV1IntegrationTest extends ApiIntegrationTestSupport {

    private static final String V1_CATEGORY_BASE_URL = "/api/v1/category";

    @Autowired private GetCategoryTreeUseCase getCategoryTreeUseCase;
    @Autowired private GetCategoryChildrenUseCase getCategoryChildrenUseCase;

    // ============================================================
    // GET /api/v1/category - 전체 카테고리 트리 조회
    // ============================================================

    @Nested
    @DisplayName("GET /api/v1/category - 전체 카테고리 트리 조회")
    class FetchCategoryTree {

        @Test
        @DisplayName("[ACAT-V1-001] 전체 카테고리 트리 조회 - 성공 (데이터 있음)")
        void fetchCategoryTree_success() {
            // Given
            List<CategoryTreeResponse> treeResponses = createCategoryTreeWithHierarchy();
            given(getCategoryTreeUseCase.getCategoryTree()).willReturn(treeResponses);

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(V1_CATEGORY_BASE_URL, V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data()).isNotNull();
        }

        @Test
        @DisplayName("[ACAT-V1-002] 전체 카테고리 트리 조회 - 성공 (빈 목록)")
        void fetchCategoryTree_emptyList() {
            // Given
            given(getCategoryTreeUseCase.getCategoryTree()).willReturn(List.of());

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(V1_CATEGORY_BASE_URL, V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
        }

        @Test
        @DisplayName("[ACAT-V1-003] 전체 카테고리 트리 조회 - 성공 (단일 루트)")
        void fetchCategoryTree_singleRoot() {
            // Given
            List<CategoryTreeResponse> singleRoot =
                    List.of(createCategoryTreeResponse(1L, "CAT001", true));
            given(getCategoryTreeUseCase.getCategoryTree()).willReturn(singleRoot);

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(V1_CATEGORY_BASE_URL, V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
        }
    }

    // ============================================================
    // GET /api/v1/category/{categoryId} - 하위 카테고리 목록 조회
    // ============================================================

    @Nested
    @DisplayName("GET /api/v1/category/{categoryId} - 하위 카테고리 목록 조회")
    class FetchCategoryChildren {

        @Test
        @DisplayName("[ACAT-V1-004] 하위 카테고리 조회 - 성공 (데이터 있음)")
        void fetchCategoryChildren_success() {
            // Given
            long parentCategoryId = 1L;
            List<CategoryTreeResponse> children = createCategoryTreeResponses(3);
            given(getCategoryChildrenUseCase.getCategoryChildren(parentCategoryId))
                    .willReturn(children);

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(
                            V1_CATEGORY_BASE_URL + "/" + parentCategoryId, V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data()).isNotNull();
        }

        @Test
        @DisplayName("[ACAT-V1-005] 하위 카테고리 조회 - 성공 (빈 목록 - 리프 노드)")
        void fetchCategoryChildren_leafNode() {
            // Given
            long leafCategoryId = 100L;
            given(getCategoryChildrenUseCase.getCategoryChildren(leafCategoryId))
                    .willReturn(List.of());

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(
                            V1_CATEGORY_BASE_URL + "/" + leafCategoryId, V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
        }

        @Test
        @DisplayName("[ACAT-V1-006] 하위 카테고리 조회 - 성공 (중첩 계층)")
        void fetchCategoryChildren_nestedHierarchy() {
            // Given
            long parentCategoryId = 1L;
            List<CategoryTreeResponse> nestedChildren = createCategoryTreeWithHierarchy();
            given(getCategoryChildrenUseCase.getCategoryChildren(parentCategoryId))
                    .willReturn(nestedChildren);

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(
                            V1_CATEGORY_BASE_URL + "/" + parentCategoryId, V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
        }
    }
}
