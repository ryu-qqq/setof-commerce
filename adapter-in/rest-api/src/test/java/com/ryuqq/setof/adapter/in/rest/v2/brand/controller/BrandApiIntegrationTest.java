package com.ryuqq.setof.adapter.in.rest.v2.brand.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.brand.dto.response.BrandSummaryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.brand.dto.response.BrandV2ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * Brand API 통합 테스트
 *
 * <p>Brand V2 REST API 엔드포인트의 통합 동작을 검증합니다.
 *
 * <p><strong>테스트 범위:</strong>
 *
 * <ul>
 *   <li>GET /api/v2/brands - 브랜드 목록 조회
 *   <li>GET /api/v2/brands/{brandId} - 브랜드 단건 조회
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
 * @see BrandV2Controller
 */
@DisplayName("Brand API 통합 테스트")
@Sql(scripts = "/sql/schema/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class BrandApiIntegrationTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = ApiV2Paths.Brands.BASE;

    @Nested
    @DisplayName("GET /api/v2/brands - 브랜드 목록 조회")
    class GetBrands {

        @Test
        @Sql("/sql/brand/brands-test-data.sql")
        @DisplayName("성공 - 기본 목록 조회")
        void getBrands_success() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<BrandSummaryV2ApiResponse>>> response =
                    get(BASE_URL + "?page=0&size=20", new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            PageApiResponse<BrandSummaryV2ApiResponse> pageResponse = response.getBody().data();
            assertThat(pageResponse).isNotNull();
            assertThat(pageResponse.content()).isNotEmpty();
            assertThat(pageResponse.page()).isEqualTo(0);
            assertThat(pageResponse.size()).isEqualTo(20);
        }

        @Test
        @Sql("/sql/brand/brands-test-data.sql")
        @DisplayName("성공 - 키워드 검색 (한글)")
        void getBrands_keywordSearchKorean() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<BrandSummaryV2ApiResponse>>> response =
                    get(
                            BASE_URL + "?keyword=나이키&page=0&size=20",
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            PageApiResponse<BrandSummaryV2ApiResponse> pageResponse = response.getBody().data();
            assertThat(pageResponse.content()).hasSize(1);
            assertThat(pageResponse.content().get(0).nameKo()).isEqualTo("나이키");
        }

        @Test
        @Sql("/sql/brand/brands-test-data.sql")
        @DisplayName("성공 - 키워드 검색 (영문)")
        void getBrands_keywordSearchEnglish() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<BrandSummaryV2ApiResponse>>> response =
                    get(
                            BASE_URL + "?keyword=Nike&page=0&size=20",
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageApiResponse<BrandSummaryV2ApiResponse> pageResponse = response.getBody().data();
            assertThat(pageResponse.content()).hasSize(1);
            assertThat(pageResponse.content().get(0).code()).isEqualTo("NIKE");
        }

        @Test
        @Sql("/sql/brand/brands-test-data.sql")
        @DisplayName("성공 - 페이징 적용")
        void getBrands_withPaging() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<BrandSummaryV2ApiResponse>>> response =
                    get(BASE_URL + "?page=0&size=5", new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageApiResponse<BrandSummaryV2ApiResponse> pageResponse = response.getBody().data();
            assertThat(pageResponse.content()).hasSize(5);
            assertThat(pageResponse.page()).isEqualTo(0);
            assertThat(pageResponse.size()).isEqualTo(5);
            assertThat(pageResponse.first()).isTrue();
            assertThat(pageResponse.last()).isFalse();
        }

        @Test
        @Sql("/sql/brand/brands-test-data.sql")
        @DisplayName("성공 - 두 번째 페이지 조회")
        void getBrands_secondPage() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<BrandSummaryV2ApiResponse>>> response =
                    get(BASE_URL + "?page=1&size=5", new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageApiResponse<BrandSummaryV2ApiResponse> pageResponse = response.getBody().data();
            assertThat(pageResponse.page()).isEqualTo(1);
            assertThat(pageResponse.first()).isFalse();
        }

        @Test
        @Sql("/sql/brand/brands-test-data.sql")
        @DisplayName("성공 - 검색 결과 없음")
        void getBrands_noResults() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<BrandSummaryV2ApiResponse>>> response =
                    get(
                            BASE_URL + "?keyword=존재하지않는브랜드&page=0&size=20",
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageApiResponse<BrandSummaryV2ApiResponse> pageResponse = response.getBody().data();
            assertThat(pageResponse.content()).isEmpty();
            assertThat(pageResponse.totalElements()).isEqualTo(0);
        }

        @Test
        @Sql("/sql/brand/brands-test-data.sql")
        @DisplayName("성공 - 응답 필드 검증")
        void getBrands_responseFieldsVerification() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<BrandSummaryV2ApiResponse>>> response =
                    get(
                            BASE_URL + "?keyword=NIKE&page=0&size=20",
                            new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            BrandSummaryV2ApiResponse brand = response.getBody().data().content().get(0);
            assertThat(brand.brandId()).isEqualTo(1L);
            assertThat(brand.code()).isEqualTo("NIKE");
            assertThat(brand.nameKo()).isEqualTo("나이키");
            assertThat(brand.logoUrl()).isEqualTo("https://cdn.example.com/brand/nike.png");
        }
    }

    @Nested
    @DisplayName("GET /api/v2/brands/{brandId} - 브랜드 단건 조회")
    class GetBrand {

        @Test
        @Sql("/sql/brand/brands-test-data.sql")
        @DisplayName("성공 - 브랜드 단건 조회")
        void getBrand_success() {
            // Given
            Long brandId = 1L;

            // When
            ResponseEntity<ApiResponse<BrandV2ApiResponse>> response =
                    get(BASE_URL + "/{brandId}", new ParameterizedTypeReference<>() {}, brandId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            BrandV2ApiResponse brand = response.getBody().data();
            assertThat(brand).isNotNull();
            assertThat(brand.brandId()).isEqualTo(1L);
            assertThat(brand.code()).isEqualTo("NIKE");
            assertThat(brand.nameKo()).isEqualTo("나이키");
            assertThat(brand.nameEn()).isEqualTo("Nike");
            assertThat(brand.logoUrl()).isEqualTo("https://cdn.example.com/brand/nike.png");
            assertThat(brand.status()).isEqualTo("ACTIVE");
        }

        @Test
        @Sql("/sql/brand/brands-test-data.sql")
        @DisplayName("성공 - 다른 브랜드 조회")
        void getBrand_anotherBrand() {
            // Given
            Long brandId = 2L;

            // When
            ResponseEntity<ApiResponse<BrandV2ApiResponse>> response =
                    get(BASE_URL + "/{brandId}", new ParameterizedTypeReference<>() {}, brandId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            BrandV2ApiResponse brand = response.getBody().data();
            assertThat(brand.brandId()).isEqualTo(2L);
            assertThat(brand.code()).isEqualTo("ADIDAS");
            assertThat(brand.nameKo()).isEqualTo("아디다스");
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 브랜드 조회 시 404 에러")
        void getBrand_notFound() {
            // Given
            Long nonExistentId = 999999L;

            // When
            ResponseEntity<ProblemDetail> response =
                    get(BASE_URL + "/{brandId}", ProblemDetail.class, nonExistentId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @Sql("/sql/brand/brands-test-data.sql")
        @DisplayName("성공 - 비활성 브랜드도 조회 가능")
        void getBrand_inactiveBrand() {
            // Given - ID 99는 INACTIVE 상태
            Long inactiveBrandId = 99L;

            // When
            ResponseEntity<ApiResponse<BrandV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{brandId}",
                            new ParameterizedTypeReference<>() {},
                            inactiveBrandId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            BrandV2ApiResponse brand = response.getBody().data();
            assertThat(brand.brandId()).isEqualTo(99L);
            assertThat(brand.status()).isEqualTo("INACTIVE");
        }
    }
}
