package com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.command.BatchSetStockV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.command.SetStockV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.response.ProductStockV2ApiResponse;
import com.ryuqq.setof.application.productstock.dto.command.SetStockCommand;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import com.ryuqq.setof.application.productstock.port.in.command.SetStockUseCase;
import com.ryuqq.setof.application.productstock.port.in.query.GetProductStockUseCase;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * ProductStock Admin V2 Controller 통합 테스트
 *
 * <p>재고 관리 API 통합 테스트
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("ProductStockAdminV2Controller 통합 테스트")
class ProductStockAdminV2ControllerTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = "/api/v2/admin/products";

    @Autowired private GetProductStockUseCase getProductStockUseCase;

    @Autowired private SetStockUseCase setStockUseCase;

    @Nested
    @DisplayName("GET /api/v2/admin/products/{productId}/stock - 단일 재고 조회")
    class GetStockTest {

        @Test
        @DisplayName("존재하는 상품 ID로 재고를 조회하면 200 OK와 재고 정보를 반환한다")
        void getStock_success() {
            // Given
            Long productId = 1001L;
            ProductStockResponse stockResponse =
                    ProductStockResponse.of(1L, productId, 100, Instant.now());

            given(getProductStockUseCase.execute(productId)).willReturn(stockResponse);

            // When
            ResponseEntity<ApiResponse<ProductStockV2ApiResponse>> response =
                    get(
                            BASE_URL + "/{productId}/stock",
                            new ParameterizedTypeReference<>() {},
                            productId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().productId()).isEqualTo(productId);
            assertThat(response.getBody().data().quantity()).isEqualTo(100);

            verify(getProductStockUseCase).execute(productId);
        }
    }

    @Nested
    @DisplayName("PUT /api/v2/admin/products/{productId}/stock - 단일 재고 설정")
    class SetStockTest {

        @Test
        @DisplayName("유효한 요청으로 재고를 설정하면 200 OK를 반환한다")
        void setStock_success() {
            // Given
            Long productId = 1001L;
            SetStockV2ApiRequest request = new SetStockV2ApiRequest(150);

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    put(
                            BASE_URL + "/{productId}/stock",
                            request,
                            new ParameterizedTypeReference<>() {},
                            productId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(setStockUseCase).execute(any(SetStockCommand.class));
        }

        @Test
        @DisplayName("재고 수량이 0 미만이면 400 Bad Request를 반환한다")
        void setStock_invalidQuantity_returnsBadRequest() {
            // Given
            Long productId = 1001L;
            SetStockV2ApiRequest request = new SetStockV2ApiRequest(-1);

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    put(
                            BASE_URL + "/{productId}/stock",
                            request,
                            new ParameterizedTypeReference<>() {},
                            productId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("PUT /api/v2/admin/products/groups/{productGroupId}/stocks - 그룹 내 일괄 재고 설정")
    class SetGroupStocksTest {

        @Test
        @DisplayName("유효한 요청으로 일괄 재고를 설정하면 200 OK를 반환한다")
        void setGroupStocks_success() {
            // Given
            Long productGroupId = 100L;
            BatchSetStockV2ApiRequest request =
                    new BatchSetStockV2ApiRequest(
                            List.of(
                                    new BatchSetStockV2ApiRequest.StockItem(1001L, 100),
                                    new BatchSetStockV2ApiRequest.StockItem(1002L, 200),
                                    new BatchSetStockV2ApiRequest.StockItem(1003L, 300)));

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    put(
                            BASE_URL + "/groups/{productGroupId}/stocks",
                            request,
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(setStockUseCase, times(3)).execute(any(SetStockCommand.class));
        }

        @Test
        @DisplayName("빈 목록으로 요청하면 400 Bad Request를 반환한다")
        void setGroupStocks_emptyList_returnsBadRequest() {
            // Given
            Long productGroupId = 100L;
            BatchSetStockV2ApiRequest request = new BatchSetStockV2ApiRequest(List.of());

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    put(
                            BASE_URL + "/groups/{productGroupId}/stocks",
                            request,
                            new ParameterizedTypeReference<>() {},
                            productGroupId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }
}
