package com.ryuqq.setof.integration.test.brand;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.integration.test.brand.fixture.BrandIntegrationTestFixture;
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
 * Brand Integration Test
 *
 * <p>브랜드 API의 통합 테스트를 수행합니다.
 *
 * <h3>테스트 시나리오</h3>
 *
 * <ul>
 *   <li>브랜드 목록 조회 (페이징, 검색)
 *   <li>브랜드 단건 조회
 *   <li>존재하지 않는 브랜드 조회 시 404
 * </ul>
 *
 * @since 1.0.0
 */
@DisplayName("Brand Integration Test")
class BrandIntegrationTest extends IntegrationTestBase {

    @Nested
    @DisplayName("브랜드 목록 조회")
    @Sql(
            scripts = "classpath:sql/brand/brand-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class GetBrandsTest {

        @Test
        @DisplayName("전체 브랜드 목록을 조회한다")
        void shouldReturnAllBrands() {
            // given
            String url = apiV2Url("/brands?page=0&size=10");

            // when
            ResponseEntity<ApiResponse<Map<String, Object>>> response =
                    restTemplate.exchange(
                            url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            Map<String, Object> data = response.getBody().data();
            assertThat(data).containsKey("content");

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> content = (List<Map<String, Object>>) data.get("content");
            assertThat(content).isNotEmpty();
        }

        @Test
        @DisplayName("키워드로 브랜드를 검색한다")
        void shouldSearchBrandsByKeyword() {
            // given
            String url =
                    apiV2Url(
                            "/brands?keyword="
                                    + BrandIntegrationTestFixture.SEARCH_KEYWORD_NIKE
                                    + "&page=0&size=10");

            // when
            ResponseEntity<ApiResponse<Map<String, Object>>> response =
                    restTemplate.exchange(
                            url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            Map<String, Object> data = response.getBody().data();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> content = (List<Map<String, Object>>) data.get("content");

            // 나이키 검색 결과 확인
            assertThat(content).hasSize(1);
            assertThat(content.get(0).get("nameKo"))
                    .isEqualTo(BrandIntegrationTestFixture.NIKE_NAME_KO);
        }

        @Test
        @DisplayName("페이징이 정상적으로 동작한다")
        void shouldSupportPaging() {
            // given
            String url = apiV2Url("/brands?page=0&size=2");

            // when
            ResponseEntity<ApiResponse<Map<String, Object>>> response =
                    restTemplate.exchange(
                            url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            Map<String, Object> data = response.getBody().data();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> content = (List<Map<String, Object>>) data.get("content");

            // size=2이므로 최대 2개
            assertThat(content.size()).isLessThanOrEqualTo(2);
        }
    }

    @Nested
    @DisplayName("브랜드 단건 조회")
    @Sql(
            scripts = "classpath:sql/brand/brand-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class GetBrandTest {

        @Test
        @DisplayName("브랜드 ID로 상세 정보를 조회한다")
        void shouldReturnBrandById() {
            // given
            String url = apiV2Url("/brands/" + BrandIntegrationTestFixture.NIKE_ID);

            // when
            ResponseEntity<ApiResponse<Map<String, Object>>> response =
                    restTemplate.exchange(
                            url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            Map<String, Object> brand = response.getBody().data();
            assertThat(brand.get("brandId"))
                    .isEqualTo(BrandIntegrationTestFixture.NIKE_ID.intValue());
            assertThat(brand.get("code")).isEqualTo(BrandIntegrationTestFixture.NIKE_CODE);
            assertThat(brand.get("nameKo")).isEqualTo(BrandIntegrationTestFixture.NIKE_NAME_KO);
            assertThat(brand.get("nameEn")).isEqualTo(BrandIntegrationTestFixture.NIKE_NAME_EN);
            assertThat(brand.get("logoUrl")).isEqualTo(BrandIntegrationTestFixture.NIKE_LOGO_URL);
            assertThat(brand.get("status")).isEqualTo(BrandIntegrationTestFixture.ACTIVE_STATUS);
        }

        @Test
        @DisplayName("존재하지 않는 브랜드 조회 시 404를 반환한다")
        void shouldReturn404WhenBrandNotFound() {
            // given
            String url = apiV2Url("/brands/" + BrandIntegrationTestFixture.NON_EXISTENT_BRAND_ID);

            // when
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
