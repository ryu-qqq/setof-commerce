package com.ryuqq.setof.adapter.in.rest.admin.integration.v1.order;

import static com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.OrderAdminTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.port.in.query.GetAdminOrdersUseCase;
import com.ryuqq.setof.application.order.port.in.query.GetOrderUseCase;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentUseCase;
import com.ryuqq.setof.domain.order.exception.OrderNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Admin Order V1 API 통합 테스트 (Legacy)
 *
 * <p>OrderV1Controller의 모든 엔드포인트를 통합 테스트합니다.
 *
 * <p><strong>테스트 대상 엔드포인트:</strong>
 *
 * <ul>
 *   <li>GET /api/v1/order/{orderId} - 주문 조회
 *   <li>GET /api/v1/orders - 주문 목록 조회
 *   <li>GET /api/v1/settlements - 정산 목록 조회 (미구현)
 *   <li>GET /api/v1/order/history/{orderId} - 주문 히스토리 조회 (미구현)
 *   <li>PUT /api/v1/order - 주문 상태 수정 (미구현)
 *   <li>PUT /api/v1/orders - 주문 상태 목록 수정 (미구현)
 *   <li>PATCH /api/v1/shipment/order/{orderId} - 배송 시작 (미구현)
 *   <li>GET /api/v1/order/today-dashboard - 오늘 주문 대시보드 (미구현)
 *   <li>GET /api/v1/order/date-dashboard - 날짜별 주문 대시보드 (미구현)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("Admin Order V1 API 통합 테스트 (Legacy)")
class OrderAdminV1IntegrationTest extends ApiIntegrationTestSupport {

    private static final String V1_ORDER_BASE_URL = "/api/v1/order";
    private static final String V1_ORDERS_BASE_URL = "/api/v1/orders";
    private static final String V1_SETTLEMENTS_URL = "/api/v1/settlements";
    private static final String V1_SHIPMENT_URL = "/api/v1/shipment/order";

    // Query UseCases
    @Autowired private GetOrderUseCase getOrderUseCase;
    @Autowired private GetAdminOrdersUseCase getAdminOrdersUseCase;
    @Autowired private GetPaymentUseCase getPaymentUseCase;

    // ============================================================
    // GET /api/v1/order/{orderId} - 주문 조회
    // ============================================================

    @Nested
    @DisplayName("GET /api/v1/order/{orderId} - 주문 조회")
    class FetchOrder {

        @Test
        @DisplayName("[AORD-V1-001] 주문 조회 - 성공")
        void fetchOrder_success() {
            // Given
            OrderResponse orderResponse = createOrderResponse();
            given(getOrderUseCase.getOrder(DEFAULT_ORDER_ID)).willReturn(orderResponse);
            given(getPaymentUseCase.getPayment(DEFAULT_PAYMENT_ID))
                    .willReturn(createPaymentResponse());

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(
                            V1_ORDER_BASE_URL + "/" + DEFAULT_ORDER_ID, V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data()).isNotNull();
        }

        @Test
        @DisplayName("[AORD-V1-002] 주문 조회 - 결제 정보 없음 성공")
        void fetchOrder_withoutPayment_success() {
            // Given
            OrderResponse orderResponse = createOrderResponseWithoutPayment();
            given(getOrderUseCase.getOrder(DEFAULT_ORDER_ID)).willReturn(orderResponse);

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(
                            V1_ORDER_BASE_URL + "/" + DEFAULT_ORDER_ID, V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
        }

        @Test
        @DisplayName("[AORD-V1-003] 주문 조회 - 존재하지 않는 주문 ID")
        void fetchOrder_notFound() {
            // Given
            String nonExistentOrderId = "non-existent-order-id";
            given(getOrderUseCase.getOrder(nonExistentOrderId))
                    .willThrow(new OrderNotFoundException(nonExistentOrderId));

            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    getExpectingError(V1_ORDER_BASE_URL + "/" + nonExistentOrderId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("[AORD-V1-004] 주문 조회 - 결제 정보 조회 실패해도 주문 정보 반환")
        void fetchOrder_paymentQueryFailed_stillReturnsOrder() {
            // Given
            OrderResponse orderResponse = createOrderResponse();
            given(getOrderUseCase.getOrder(DEFAULT_ORDER_ID)).willReturn(orderResponse);
            given(getPaymentUseCase.getPayment(DEFAULT_PAYMENT_ID))
                    .willThrow(new RuntimeException("Payment service unavailable"));

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(
                            V1_ORDER_BASE_URL + "/" + DEFAULT_ORDER_ID, V1ApiResponse.class);

            // Then - 결제 조회 실패해도 주문 조회는 성공
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
        }
    }

    // ============================================================
    // GET /api/v1/orders - 주문 목록 조회
    // ============================================================

    @Nested
    @DisplayName("GET /api/v1/orders - 주문 목록 조회")
    class GetOrders {

        @Test
        @DisplayName("[AORD-V1-005] 주문 목록 조회 - 성공 (데이터 있음)")
        void getOrders_success() {
            // Given
            SliceResponse<OrderResponse> sliceResponse = createOrderSliceResponse(5, false);
            given(getAdminOrdersUseCase.getOrders(any())).willReturn(sliceResponse);

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(
                            V1_ORDERS_BASE_URL + "?periodType=MONTH", V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
        }

        @Test
        @DisplayName("[AORD-V1-006] 주문 목록 조회 - 성공 (빈 목록)")
        void getOrders_emptyList() {
            // Given
            SliceResponse<OrderResponse> emptySlice =
                    SliceResponse.of(java.util.List.of(), 20, false, null);
            given(getAdminOrdersUseCase.getOrders(any())).willReturn(emptySlice);

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(
                            V1_ORDERS_BASE_URL + "?periodType=MONTH", V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
        }

        @Test
        @DisplayName("[AORD-V1-007] 주문 목록 조회 - 성공 (다음 페이지 있음)")
        void getOrders_hasNext() {
            // Given
            SliceResponse<OrderResponse> sliceResponse = createOrderSliceResponse(20, true);
            given(getAdminOrdersUseCase.getOrders(any())).willReturn(sliceResponse);

            // When
            ResponseEntity<V1ApiResponse> response =
                    restTemplate.getForEntity(
                            V1_ORDERS_BASE_URL + "?periodType=MONTH", V1ApiResponse.class);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
        }
    }

    // ============================================================
    // 미구현 엔드포인트 테스트 - UnsupportedOperationException
    // ============================================================

    @Nested
    @DisplayName("미구현 엔드포인트 테스트")
    class UnsupportedEndpoints {

        @Test
        @DisplayName("[AORD-V1-008] 정산 목록 조회 - 미구현 (500 에러)")
        void getSettlements_unsupported() {
            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    getExpectingError(V1_SETTLEMENTS_URL + "?periodType=MONTH");

            // Then - UnsupportedOperationException → 500 Internal Server Error
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("[AORD-V1-009] 주문 히스토리 조회 - 미구현 (500 에러)")
        void getOrderHistory_unsupported() {
            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    getExpectingError(V1_ORDER_BASE_URL + "/history/1");

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("[AORD-V1-010] 주문 상태 수정 - 미구현 (500 에러)")
        void modifyOrderStatus_unsupported() {
            // Given - UpdateOrderV1ApiRequest는 sealed interface로 type 필드 필수
            java.util.Map<String, Object> requestBody =
                    java.util.Map.of(
                            "type", "normalOrder",
                            "orderId", 1,
                            "orderStatus", "COMPLETED");

            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    putExpectingError(V1_ORDER_BASE_URL, requestBody);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("[AORD-V1-011] 주문 상태 목록 수정 - 미구현 (500 에러)")
        void modifyOrderStatusList_unsupported() {
            // Given - UpdateOrderV1ApiRequest는 sealed interface로 type 필드 필수
            java.util.List<java.util.Map<String, Object>> requestBody =
                    java.util.List.of(
                            java.util.Map.of(
                                    "type", "normalOrder",
                                    "orderId", 1,
                                    "orderStatus", "COMPLETED"));

            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    putExpectingError(V1_ORDERS_BASE_URL, requestBody);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("[AORD-V1-012] 배송 시작 - 미구현 (500 에러)")
        void shipOrder_unsupported() {
            // Given - ShipmentInfoV1ApiRequest 필드명: shipmentType, companyCode, invoiceNo
            java.util.Map<String, Object> requestBody =
                    java.util.Map.of(
                            "shipmentType", "DOMESTIC",
                            "companyCode", "CJ",
                            "invoiceNo", "1234567890");

            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    patchExpectingError(V1_SHIPMENT_URL + "/1", requestBody);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("[AORD-V1-013] 오늘 주문 대시보드 조회 - 미구현 (500 에러)")
        void getTodayDashboard_unsupported() {
            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    getExpectingError(V1_ORDER_BASE_URL + "/today-dashboard?periodType=MONTH");

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("[AORD-V1-014] 날짜별 주문 대시보드 조회 - 미구현 (500 에러)")
        void getDateDashboard_unsupported() {
            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    getExpectingError(V1_ORDER_BASE_URL + "/date-dashboard?periodType=MONTH");

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
