package com.ryuqq.setof.adapter.in.rest.v2.product.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.product.dto.response.FullProductV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.product.dto.response.ProductGroupSummaryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.product.dto.response.ProductGroupV2ApiResponse;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * Product API 통합 테스트
 *
 * <p>Product V2 REST API 엔드포인트의 통합 동작을 검증합니다.
 *
 * <p><strong>테스트 범위:</strong>
 *
 * <ul>
 *   <li>GET /api/v2/products - 상품 목록 조회
 *   <li>GET /api/v2/products/{productGroupId} - 상품 단건 조회
 *   <li>GET /api/v2/products/{productGroupId}/full - 전체 상품 조회
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
 * @see ProductV2Controller
 */
@DisplayName("Product API 통합 테스트")
@Sql(scripts = "/sql/schema/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class ProductApiIntegrationTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = ApiV2Paths.Products.BASE;

    @Nested
    @DisplayName("GET /api/v2/products - 상품 목록 조회")
    class GetProducts {

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 기본 목록 조회")
        void getProducts_success() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<ProductGroupSummaryV2ApiResponse>>>
                    response =
                            get(
                                    BASE_URL + "?page=0&size=20",
                                    new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            PageApiResponse<ProductGroupSummaryV2ApiResponse> pageResponse =
                    response.getBody().data();
            assertThat(pageResponse).isNotNull();
            assertThat(pageResponse.content()).isNotEmpty();
            assertThat(pageResponse.page()).isEqualTo(0);
            assertThat(pageResponse.size()).isEqualTo(20);
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 셀러 ID로 필터링")
        void getProducts_filterBySellerId() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<ProductGroupSummaryV2ApiResponse>>>
                    response =
                            get(
                                    BASE_URL + "?sellerId=1&page=0&size=20",
                                    new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            PageApiResponse<ProductGroupSummaryV2ApiResponse> pageResponse =
                    response.getBody().data();
            assertThat(pageResponse.content()).isNotEmpty();
            assertThat(pageResponse.content()).allMatch(product -> product.sellerId().equals(1L));
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 카테고리 ID로 필터링")
        void getProducts_filterByCategoryId() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<ProductGroupSummaryV2ApiResponse>>>
                    response =
                            get(
                                    BASE_URL + "?categoryId=100&page=0&size=20",
                                    new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageApiResponse<ProductGroupSummaryV2ApiResponse> pageResponse =
                    response.getBody().data();
            assertThat(pageResponse.content()).isNotEmpty();
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 상품명 검색")
        void getProducts_searchByName() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<ProductGroupSummaryV2ApiResponse>>>
                    response =
                            get(
                                    BASE_URL + "?name=티셔츠&page=0&size=20",
                                    new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageApiResponse<ProductGroupSummaryV2ApiResponse> pageResponse =
                    response.getBody().data();
            assertThat(pageResponse.content()).isNotEmpty();
            assertThat(pageResponse.content().get(0).name()).contains("티셔츠");
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 상태로 필터링")
        void getProducts_filterByStatus() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<ProductGroupSummaryV2ApiResponse>>>
                    response =
                            get(
                                    BASE_URL + "?status=ACTIVE&page=0&size=20",
                                    new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageApiResponse<ProductGroupSummaryV2ApiResponse> pageResponse =
                    response.getBody().data();
            assertThat(pageResponse.content()).isNotEmpty();
            assertThat(pageResponse.content())
                    .allMatch(product -> product.status().equals("ACTIVE"));
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 페이징 적용")
        void getProducts_withPaging() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<ProductGroupSummaryV2ApiResponse>>>
                    response =
                            get(BASE_URL + "?page=0&size=5", new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageApiResponse<ProductGroupSummaryV2ApiResponse> pageResponse =
                    response.getBody().data();
            assertThat(pageResponse.content()).hasSize(5);
            assertThat(pageResponse.page()).isEqualTo(0);
            assertThat(pageResponse.size()).isEqualTo(5);
            assertThat(pageResponse.first()).isTrue();
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 두 번째 페이지 조회")
        void getProducts_secondPage() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<ProductGroupSummaryV2ApiResponse>>>
                    response =
                            get(BASE_URL + "?page=1&size=5", new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageApiResponse<ProductGroupSummaryV2ApiResponse> pageResponse =
                    response.getBody().data();
            assertThat(pageResponse.page()).isEqualTo(1);
            assertThat(pageResponse.first()).isFalse();
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 검색 결과 없음")
        void getProducts_noResults() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<ProductGroupSummaryV2ApiResponse>>>
                    response =
                            get(
                                    BASE_URL + "?name=존재하지않는상품명&page=0&size=20",
                                    new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageApiResponse<ProductGroupSummaryV2ApiResponse> pageResponse =
                    response.getBody().data();
            assertThat(pageResponse.content()).isEmpty();
            assertThat(pageResponse.totalElements()).isEqualTo(0);
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 응답 필드 검증")
        void getProducts_responseFieldsVerification() {
            // When
            ResponseEntity<ApiResponse<PageApiResponse<ProductGroupSummaryV2ApiResponse>>>
                    response =
                            get(
                                    BASE_URL + "?name=기본 반팔&page=0&size=20",
                                    new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            ProductGroupSummaryV2ApiResponse product = response.getBody().data().content().get(0);
            assertThat(product.productGroupId()).isEqualTo(1L);
            assertThat(product.sellerId()).isEqualTo(1L);
            assertThat(product.name()).isEqualTo("기본 반팔 티셔츠");
            assertThat(product.optionType()).isEqualTo("SINGLE");
            assertThat(product.currentPrice()).isEqualByComparingTo(new BigDecimal("19900.00"));
            assertThat(product.status()).isEqualTo("ACTIVE");
            // Note: productCount는 현재 구현에서 0으로 반환됨 (별도 조회 필요)
            // TODO: productCount 조회 기능 구현 후 테스트 업데이트
            assertThat(product.productCount()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("GET /api/v2/products/{productGroupId} - 상품 단건 조회")
    class GetProduct {

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 상품 단건 조회")
        void getProduct_success() {
            // Given
            Long productGroupId = 1L;

            // When
            ResponseEntity<ApiResponse<ProductGroupV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{productGroupId}",
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            ProductGroupV2ApiResponse product = response.getBody().data();
            assertThat(product).isNotNull();
            assertThat(product.productGroupId()).isEqualTo(1L);
            assertThat(product.sellerId()).isEqualTo(1L);
            assertThat(product.categoryId()).isEqualTo(100L);
            assertThat(product.brandId()).isEqualTo(1L);
            assertThat(product.name()).isEqualTo("기본 반팔 티셔츠");
            assertThat(product.optionType()).isEqualTo("SINGLE");
            assertThat(product.regularPrice()).isEqualByComparingTo(new BigDecimal("29000.00"));
            assertThat(product.currentPrice()).isEqualByComparingTo(new BigDecimal("19900.00"));
            assertThat(product.status()).isEqualTo("ACTIVE");
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 상품 상세 정보에 SKU 목록 포함")
        void getProduct_withProducts() {
            // Given
            Long productGroupId = 2L;

            // When
            ResponseEntity<ApiResponse<ProductGroupV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{productGroupId}",
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            ProductGroupV2ApiResponse product = response.getBody().data();
            assertThat(product.productGroupId()).isEqualTo(2L);
            assertThat(product.optionType()).isEqualTo("TWO_LEVEL");
            assertThat(product.products()).isNotEmpty();
            assertThat(product.products()).hasSize(3);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 상품 조회 시 404 에러")
        void getProduct_notFound() {
            // Given
            Long nonExistentId = 999999L;

            // When
            ResponseEntity<ProblemDetail> response =
                    get(BASE_URL + "/{productGroupId}", ProblemDetail.class, nonExistentId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 비활성 상품도 조회 가능")
        void getProduct_inactiveProduct() {
            // Given - ID 6은 INACTIVE 상태
            Long inactiveProductGroupId = 6L;

            // When
            ResponseEntity<ApiResponse<ProductGroupV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{productGroupId}",
                            new ParameterizedTypeReference<>() {},
                            inactiveProductGroupId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            ProductGroupV2ApiResponse product = response.getBody().data();
            assertThat(product.productGroupId()).isEqualTo(6L);
            assertThat(product.status()).isEqualTo("INACTIVE");
        }
    }

    @Nested
    @DisplayName("GET /api/v2/products/{productGroupId}/full - 전체 상품 조회")
    class GetFullProduct {

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 전체 상품 조회")
        void getFullProduct_success() {
            // Given
            Long productGroupId = 1L;

            // When
            ResponseEntity<ApiResponse<FullProductV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{productGroupId}/full",
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            FullProductV2ApiResponse fullProduct = response.getBody().data();
            assertThat(fullProduct).isNotNull();
            assertThat(fullProduct.productGroup()).isNotNull();
            assertThat(fullProduct.productGroup().productGroupId()).isEqualTo(1L);
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 전체 상품 조회에 이미지 포함")
        void getFullProduct_withImages() {
            // Given
            Long productGroupId = 1L;

            // When
            ResponseEntity<ApiResponse<FullProductV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{productGroupId}/full",
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            FullProductV2ApiResponse fullProduct = response.getBody().data();
            assertThat(fullProduct.images()).isNotEmpty();
            assertThat(fullProduct.images()).hasSize(3);
            assertThat(fullProduct.images().get(0).imageType()).isEqualTo("MAIN");
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 전체 상품 조회에 상세설명 포함")
        void getFullProduct_withDescription() {
            // Given
            Long productGroupId = 1L;

            // When
            ResponseEntity<ApiResponse<FullProductV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{productGroupId}/full",
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            FullProductV2ApiResponse fullProduct = response.getBody().data();
            assertThat(fullProduct.description()).isNotNull();
            assertThat(fullProduct.description().htmlContent()).contains("기본 반팔 티셔츠");
            assertThat(fullProduct.description().images()).isNotEmpty();
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 전체 상품 조회에 고시정보 포함")
        void getFullProduct_withNotice() {
            // Given
            Long productGroupId = 1L;

            // When
            ResponseEntity<ApiResponse<FullProductV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{productGroupId}/full",
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            FullProductV2ApiResponse fullProduct = response.getBody().data();
            assertThat(fullProduct.notice()).isNotNull();
            assertThat(fullProduct.notice().items()).isNotEmpty();
            assertThat(fullProduct.notice().items()).hasSize(3);
        }

        @Test
        @Sql("/sql/product/products-test-data.sql")
        @DisplayName("성공 - 전체 상품 조회에 재고 포함")
        void getFullProduct_withStocks() {
            // Given
            Long productGroupId = 1L;

            // When
            ResponseEntity<ApiResponse<FullProductV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{productGroupId}/full",
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            FullProductV2ApiResponse fullProduct = response.getBody().data();
            assertThat(fullProduct.stocks()).isNotEmpty();
            assertThat(fullProduct.stocks().get(0).quantity()).isEqualTo(100);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 상품 전체 조회 시 404 에러")
        void getFullProduct_notFound() {
            // Given
            Long nonExistentId = 999999L;

            // When
            ResponseEntity<ProblemDetail> response =
                    get(BASE_URL + "/{productGroupId}/full", ProblemDetail.class, nonExistentId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
