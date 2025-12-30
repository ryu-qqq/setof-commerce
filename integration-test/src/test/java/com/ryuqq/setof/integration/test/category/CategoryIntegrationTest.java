package com.ryuqq.setof.integration.test.category;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.integration.test.category.fixture.CategoryIntegrationTestFixture;
import com.ryuqq.setof.integration.test.common.IntegrationTestBase;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * Category Integration Test
 *
 * <p>카테고리 API의 통합 테스트를 수행합니다.
 *
 * <h3>테스트 시나리오</h3>
 *
 * <ul>
 *   <li>카테고리 단건 조회
 *   <li>하위 카테고리 목록 조회 (최상위, 특정 부모)
 *   <li>카테고리 트리 조회
 *   <li>카테고리 경로 (Breadcrumb) 조회
 *   <li>존재하지 않는 카테고리 조회 시 404
 * </ul>
 *
 * @since 1.0.0
 */
@DisplayName("Category Integration Test")
class CategoryIntegrationTest extends IntegrationTestBase {

    @Nested
    @DisplayName("카테고리 단건 조회")
    @Sql(
            scripts = "classpath:sql/category/category-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class GetCategoryTest {

        @Test
        @DisplayName("카테고리 ID로 상세 정보를 조회한다")
        void shouldReturnCategoryById() {
            // given
            String url = apiV2Url("/categories/" + CategoryIntegrationTestFixture.FASHION_ID);

            // when
            ResponseEntity<ApiResponse<Map<String, Object>>> response =
                    restTemplate.exchange(
                            url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            Map<String, Object> category = response.getBody().data();
            assertThat(category.get("categoryId"))
                    .isEqualTo(CategoryIntegrationTestFixture.FASHION_ID.intValue());
            assertThat(category.get("code")).isEqualTo(CategoryIntegrationTestFixture.FASHION_CODE);
            assertThat(category.get("nameKo"))
                    .isEqualTo(CategoryIntegrationTestFixture.FASHION_NAME_KO);
            assertThat(category.get("depth"))
                    .isEqualTo(CategoryIntegrationTestFixture.FASHION_DEPTH);
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 조회 시 404를 반환한다")
        void shouldReturn404WhenCategoryNotFound() {
            // given
            String url =
                    apiV2Url(
                            "/categories/"
                                    + CategoryIntegrationTestFixture.NON_EXISTENT_CATEGORY_ID);

            // when
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("하위 카테고리 목록 조회")
    @Sql(
            scripts = "classpath:sql/category/category-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class GetCategoriesTest {

        @Test
        @DisplayName("최상위 카테고리 목록을 조회한다")
        void shouldReturnRootCategories() {
            // given
            String url = apiV2Url("/categories");

            // when
            ResponseEntity<ApiResponse<List<Map<String, Object>>>> response =
                    restTemplate.exchange(
                            url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            List<Map<String, Object>> categories = response.getBody().data();
            assertThat(categories).hasSize(CategoryIntegrationTestFixture.ROOT_CATEGORY_COUNT);

            // 패션, 전자기기 확인
            assertThat(categories)
                    .extracting(c -> c.get("nameKo"))
                    .containsExactlyInAnyOrder(
                            CategoryIntegrationTestFixture.FASHION_NAME_KO,
                            CategoryIntegrationTestFixture.ELECTRONICS_NAME_KO);
        }

        @Test
        @DisplayName("특정 부모의 하위 카테고리 목록을 조회한다")
        void shouldReturnChildCategories() {
            // given
            String url =
                    apiV2Url("/categories?parentId=" + CategoryIntegrationTestFixture.FASHION_ID);

            // when
            ResponseEntity<ApiResponse<List<Map<String, Object>>>> response =
                    restTemplate.exchange(
                            url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            List<Map<String, Object>> categories = response.getBody().data();
            assertThat(categories).hasSize(CategoryIntegrationTestFixture.FASHION_CHILDREN_COUNT);

            // 남성의류, 여성의류 확인
            assertThat(categories)
                    .extracting(c -> c.get("nameKo"))
                    .containsExactlyInAnyOrder("남성의류", "여성의류");
        }
    }

    @Nested
    @DisplayName("카테고리 트리 조회")
    @Sql(
            scripts = "classpath:sql/category/category-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class GetCategoryTreeTest {

        @Test
        @DisplayName("전체 카테고리를 트리 구조로 조회한다")
        void shouldReturnCategoryTree() {
            // given
            String url = apiV2Url("/categories/tree");

            // when
            ResponseEntity<ApiResponse<List<Map<String, Object>>>> response =
                    restTemplate.exchange(
                            url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            List<Map<String, Object>> tree = response.getBody().data();
            // 최상위 카테고리 2개 (패션, 전자기기)
            assertThat(tree).hasSize(CategoryIntegrationTestFixture.ROOT_CATEGORY_COUNT);

            // 첫 번째 카테고리(패션)의 children 확인
            Map<String, Object> fashionCategory =
                    tree.stream()
                            .filter(
                                    c ->
                                            CategoryIntegrationTestFixture.FASHION_NAME_KO.equals(
                                                    c.get("nameKo")))
                            .findFirst()
                            .orElseThrow();

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> children =
                    (List<Map<String, Object>>) fashionCategory.get("children");
            assertThat(children).isNotNull();
            assertThat(children).hasSize(CategoryIntegrationTestFixture.FASHION_CHILDREN_COUNT);
        }
    }

    @Nested
    @DisplayName("카테고리 경로 조회")
    @Sql(
            scripts = "classpath:sql/category/category-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class GetCategoryPathTest {

        @Test
        @DisplayName("카테고리 경로(Breadcrumb)를 조회한다")
        void shouldReturnCategoryPath() {
            // given - 남성의류 > 상의 (depth=2)
            String url =
                    apiV2Url(
                            "/categories/"
                                    + CategoryIntegrationTestFixture.FASHION_MEN_TOP_ID
                                    + "/path");

            // when
            ResponseEntity<ApiResponse<Map<String, Object>>> response =
                    restTemplate.exchange(
                            url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            Map<String, Object> pathResponse = response.getBody().data();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> breadcrumbs =
                    (List<Map<String, Object>>) pathResponse.get("breadcrumbs");

            // 패션 > 남성의류 > 상의 (3단계)
            assertThat(breadcrumbs)
                    .hasSize(CategoryIntegrationTestFixture.FASHION_MEN_TOP_PATH_LENGTH);

            // 순서 확인: 패션 -> 남성의류 -> 상의
            assertThat(breadcrumbs.get(0).get("nameKo"))
                    .isEqualTo(CategoryIntegrationTestFixture.FASHION_NAME_KO);
            assertThat(breadcrumbs.get(1).get("nameKo"))
                    .isEqualTo(CategoryIntegrationTestFixture.FASHION_MEN_NAME_KO);
            assertThat(breadcrumbs.get(2).get("nameKo"))
                    .isEqualTo(CategoryIntegrationTestFixture.FASHION_MEN_TOP_NAME_KO);
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 경로 조회 시 404를 반환한다")
        void shouldReturn404WhenCategoryPathNotFound() {
            // given
            String url =
                    apiV2Url(
                            "/categories/"
                                    + CategoryIntegrationTestFixture.NON_EXISTENT_CATEGORY_ID
                                    + "/path");

            // when
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
