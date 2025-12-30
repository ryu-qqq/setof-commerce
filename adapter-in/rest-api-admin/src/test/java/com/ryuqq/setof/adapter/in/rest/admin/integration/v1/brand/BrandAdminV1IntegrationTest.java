package com.ryuqq.setof.adapter.in.rest.admin.integration.v1.brand;

import static com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.BrandAdminTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandsUseCase;
import com.ryuqq.setof.application.common.response.PageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Admin Brand V1 API 통합 테스트 (Legacy)
 *
 * <p>BrandV1Controller의 모든 엔드포인트를 통합 테스트합니다.
 *
 * <p><strong>테스트 대상 엔드포인트:</strong>
 *
 * <ul>
 *   <li>GET /api/v1/brands - 브랜드 목록 조회
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("Admin Brand V1 API 통합 테스트 (Legacy)")
class BrandAdminV1IntegrationTest extends ApiIntegrationTestSupport {

    private static final String V1_BRANDS_BASE_URL = "/api/v1/brands";

    @Autowired private GetBrandsUseCase getBrandsUseCase;

    // ============================================================
    // GET /api/v1/brands - 브랜드 목록 조회
    // ============================================================

    @Nested
    @DisplayName("GET /api/v1/brands - 브랜드 목록 조회")
    class FetchBrands {

        @Test
        @DisplayName("[ABRN-V1-001] 브랜드 목록 조회 - 성공 (데이터 있음)")
        void fetchBrands_success() {
            // Given
            PageResponse<?> pageResponse = createBrandPageResponse(5, 0, 20);
            given(getBrandsUseCase.execute(any())).willReturn((PageResponse) pageResponse);

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(V1_BRANDS_BASE_URL, V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data()).isNotNull();
        }

        @Test
        @DisplayName("[ABRN-V1-002] 브랜드 목록 조회 - 성공 (빈 목록)")
        void fetchBrands_emptyList() {
            // Given
            PageResponse<?> emptyResponse = createBrandPageResponse(0, 0, 20);
            given(getBrandsUseCase.execute(any())).willReturn((PageResponse) emptyResponse);

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(V1_BRANDS_BASE_URL, V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
        }

        @Test
        @DisplayName("[ABRN-V1-003] 브랜드 목록 조회 - 성공 (필터 적용)")
        void fetchBrands_withFilter() {
            // Given
            PageResponse<?> pageResponse = createBrandPageResponse(3, 0, 20);
            given(getBrandsUseCase.execute(any())).willReturn((PageResponse) pageResponse);

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(
                            V1_BRANDS_BASE_URL + "?brandName=테스트&page=0&size=20",
                            V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
        }

        @Test
        @DisplayName("[ABRN-V1-004] 브랜드 목록 조회 - 성공 (페이징 적용)")
        void fetchBrands_withPaging() {
            // Given
            PageResponse<?> pageResponse = createBrandPageResponse(20, 1, 20);
            given(getBrandsUseCase.execute(any())).willReturn((PageResponse) pageResponse);

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(
                            V1_BRANDS_BASE_URL + "?page=1&size=20", V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
        }
    }
}
