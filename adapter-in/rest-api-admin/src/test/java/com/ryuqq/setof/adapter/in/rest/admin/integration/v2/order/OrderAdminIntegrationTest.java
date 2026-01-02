package com.ryuqq.setof.adapter.in.rest.admin.integration.v2.order;

import static com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.ClaimOrderAdminTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.order.dto.response.OrderAdminV2ApiResponse;
import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.application.order.dto.query.GetAdminOrdersQuery;
import com.ryuqq.setof.application.order.dto.response.OrderItemResponse;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.port.in.command.CancelOrderUseCase;
import com.ryuqq.setof.application.order.port.in.command.CompleteOrderUseCase;
import com.ryuqq.setof.application.order.port.in.command.ConfirmOrderUseCase;
import com.ryuqq.setof.application.order.port.in.command.DeliverOrderUseCase;
import com.ryuqq.setof.application.order.port.in.command.ShipOrderUseCase;
import com.ryuqq.setof.application.order.port.in.query.GetAdminOrdersUseCase;
import com.ryuqq.setof.application.order.port.in.query.GetOrderUseCase;
import com.ryuqq.setof.domain.order.exception.OrderNotFoundException;
import com.ryuqq.setof.domain.order.exception.OrderStatusException;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderStatus;
import java.math.BigDecimal;
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
 * Order Admin API 통합 테스트
 *
 * <p>주문 관리 API의 통합 테스트를 수행합니다.
 *
 * <p>테스트 범위:
 *
 * <ul>
 *   <li>Query API: 주문 목록 조회, 주문 상세 조회 (ID/주문번호)
 *   <li>Command API: 주문 확정, 배송 시작, 배송 완료, 구매 확정, 주문 취소
 * </ul>
 *
 * <p>주문 상태 흐름:
 *
 * <pre>
 * ORDERED → CONFIRMED → SHIPPED → DELIVERED → COMPLETED
 *    ↓         ↓
 * CANCELLED  CANCELLED
 * </pre>
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("Admin Order API 통합 테스트")
class OrderAdminIntegrationTest extends ApiIntegrationTestSupport {

    // ============================================================
    // Query UseCases
    // ============================================================

    @Autowired private GetOrderUseCase getOrderUseCase;

    @Autowired private GetAdminOrdersUseCase getAdminOrdersUseCase;

    // ============================================================
    // Command UseCases
    // ============================================================

    @Autowired private ConfirmOrderUseCase confirmOrderUseCase;

    @Autowired private ShipOrderUseCase shipOrderUseCase;

    @Autowired private DeliverOrderUseCase deliverOrderUseCase;

    @Autowired private CompleteOrderUseCase completeOrderUseCase;

    @Autowired private CancelOrderUseCase cancelOrderUseCase;

    // ============================================================
    // Query Tests
    // ============================================================

    @Nested
    @DisplayName("주문 목록 조회")
    class GetOrdersTest {

        private static final String ORDERS_URL = ApiV2Paths.Orders.BASE;

        @Test
        @DisplayName("AORD-001: 필터 없이 전체 목록을 조회하면 성공한다")
        void getOrders_withoutFilters_success() {
            // given
            List<OrderResponse> orders =
                    List.of(
                            createOrderResponse(ORDERED_ORDER_ID, ORDER_STATUS_ORDERED),
                            createOrderResponse(CONFIRMED_ORDER_ID, ORDER_STATUS_CONFIRMED),
                            createOrderResponse(SHIPPED_ORDER_ID, ORDER_STATUS_SHIPPED));
            SliceResponse<OrderResponse> sliceResponse = SliceResponse.of(orders, 3, false, null);

            given(getAdminOrdersUseCase.getOrders(any(GetAdminOrdersQuery.class)))
                    .willReturn(sliceResponse);

            // when
            ResponseEntity<ApiResponse<SliceResponse<OrderAdminV2ApiResponse>>> response =
                    get(
                            ORDERS_URL,
                            new ParameterizedTypeReference<
                                    ApiResponse<SliceResponse<OrderAdminV2ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data().content()).hasSize(3);
            assertThat(response.getBody().data().hasNext()).isFalse();
        }

        @Test
        @DisplayName("AORD-002: sellerId와 status로 필터링하면 해당 조건의 주문만 반환된다")
        void getOrders_withFilters_success() {
            // given
            List<OrderResponse> filteredOrders =
                    List.of(createOrderResponse(ORDERED_ORDER_ID, ORDER_STATUS_ORDERED));
            SliceResponse<OrderResponse> sliceResponse =
                    SliceResponse.of(filteredOrders, 1, false, null);

            given(getAdminOrdersUseCase.getOrders(any(GetAdminOrdersQuery.class)))
                    .willReturn(sliceResponse);

            String url =
                    ORDERS_URL
                            + "?sellerId="
                            + DEFAULT_SELLER_ID
                            + "&orderStatuses="
                            + ORDER_STATUS_ORDERED;

            // when
            ResponseEntity<ApiResponse<SliceResponse<OrderAdminV2ApiResponse>>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<SliceResponse<OrderAdminV2ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().content()).hasSize(1);
            assertThat(response.getBody().data().content().get(0).status())
                    .isEqualTo(ORDER_STATUS_ORDERED);
        }

        @Test
        @DisplayName("AORD-003: 다음 페이지가 있으면 hasNext가 true이고 nextCursor가 반환된다")
        void getOrders_withNextPage_returnsNextCursor() {
            // given
            List<OrderResponse> orders =
                    List.of(createOrderResponse(ORDERED_ORDER_ID, ORDER_STATUS_ORDERED));
            SliceResponse<OrderResponse> sliceResponse =
                    SliceResponse.of(orders, 1, true, ORDERED_ORDER_ID);

            given(getAdminOrdersUseCase.getOrders(any(GetAdminOrdersQuery.class)))
                    .willReturn(sliceResponse);

            String url = ORDERS_URL + "?pageSize=1";

            // when
            ResponseEntity<ApiResponse<SliceResponse<OrderAdminV2ApiResponse>>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<SliceResponse<OrderAdminV2ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().hasNext()).isTrue();
            assertThat(response.getBody().data().nextCursor()).isEqualTo(ORDERED_ORDER_ID);
        }

        @Test
        @DisplayName("AORD-004: 검색어로 주문번호/수령인명을 검색할 수 있다")
        void getOrders_withSearchKeyword_success() {
            // given
            List<OrderResponse> orders =
                    List.of(createOrderResponse(ORDERED_ORDER_ID, ORDER_STATUS_ORDERED));
            SliceResponse<OrderResponse> sliceResponse = SliceResponse.of(orders, 1, false, null);

            given(getAdminOrdersUseCase.getOrders(any(GetAdminOrdersQuery.class)))
                    .willReturn(sliceResponse);

            String url = ORDERS_URL + "?searchKeyword=" + ORDERED_ORDER_NUMBER;

            // when
            ResponseEntity<ApiResponse<SliceResponse<OrderAdminV2ApiResponse>>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<SliceResponse<OrderAdminV2ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().content()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("주문 단건 조회 (ID)")
    class GetOrderByIdTest {

        @Test
        @DisplayName("AORD-005: 유효한 주문 ID로 조회하면 성공한다")
        void getOrder_withValidId_success() {
            // given
            OrderResponse orderResponse =
                    createOrderResponse(ORDERED_ORDER_ID, ORDER_STATUS_ORDERED);

            given(getOrderUseCase.getOrder(ORDERED_ORDER_ID)).willReturn(orderResponse);

            String url = ApiV2Paths.Orders.BASE + "/" + ORDERED_ORDER_ID;

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data().orderId()).isEqualTo(ORDERED_ORDER_ID);
            assertThat(response.getBody().data().status()).isEqualTo(ORDER_STATUS_ORDERED);
        }

        @Test
        @DisplayName("AORD-006: 존재하지 않는 주문 ID로 조회하면 404를 반환한다")
        void getOrder_withNonExistentId_returns404() {
            // given
            given(getOrderUseCase.getOrder(NON_EXISTENT_ORDER_ID))
                    .willThrow(new OrderNotFoundException(NON_EXISTENT_ORDER_ID));

            String url = ApiV2Paths.Orders.BASE + "/" + NON_EXISTENT_ORDER_ID;

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("주문 단건 조회 (주문번호)")
    class GetOrderByNumberTest {

        @Test
        @DisplayName("AORD-007: 유효한 주문번호로 조회하면 성공한다")
        void getOrderByNumber_withValidNumber_success() {
            // given
            OrderResponse orderResponse =
                    createOrderResponse(ORDERED_ORDER_ID, ORDER_STATUS_ORDERED);

            given(getOrderUseCase.getOrderByOrderNumber(ORDERED_ORDER_NUMBER))
                    .willReturn(orderResponse);

            String url = ApiV2Paths.Orders.BASE + "/by-number/" + ORDERED_ORDER_NUMBER;

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().orderNumber()).isEqualTo(ORDERED_ORDER_NUMBER);
        }

        @Test
        @DisplayName("AORD-008: 존재하지 않는 주문번호로 조회하면 404를 반환한다")
        void getOrderByNumber_withNonExistentNumber_returns404() {
            // given
            given(getOrderUseCase.getOrderByOrderNumber(NON_EXISTENT_ORDER_NUMBER))
                    .willThrow(new OrderNotFoundException(NON_EXISTENT_ORDER_NUMBER));

            String url = ApiV2Paths.Orders.BASE + "/by-number/" + NON_EXISTENT_ORDER_NUMBER;

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // Command Tests
    // ============================================================

    @Nested
    @DisplayName("주문 확정 (ORDERED → CONFIRMED)")
    class ConfirmOrderTest {

        @Test
        @DisplayName("AORD-009: ORDERED 상태의 주문을 확정하면 CONFIRMED 상태가 된다")
        void confirmOrder_orderedStatus_success() {
            // given
            OrderResponse confirmedOrder =
                    createOrderResponseWithTimestamps(
                            ORDERED_ORDER_ID,
                            ORDER_STATUS_CONFIRMED,
                            Instant.now(),
                            Instant.now(),
                            null,
                            null,
                            null,
                            null);

            given(confirmOrderUseCase.confirmOrder(ORDERED_ORDER_ID)).willReturn(confirmedOrder);

            String url = ApiV2Paths.Orders.BASE + "/" + ORDERED_ORDER_ID + "/confirm";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().status()).isEqualTo(ORDER_STATUS_CONFIRMED);
            assertThat(response.getBody().data().confirmedAt()).isNotNull();
        }

        @Test
        @DisplayName("AORD-010: 존재하지 않는 주문을 확정하면 404를 반환한다")
        void confirmOrder_nonExistentOrder_returns404() {
            // given
            given(confirmOrderUseCase.confirmOrder(NON_EXISTENT_ORDER_ID))
                    .willThrow(new OrderNotFoundException(NON_EXISTENT_ORDER_ID));

            String url = ApiV2Paths.Orders.BASE + "/" + NON_EXISTENT_ORDER_ID + "/confirm";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("배송 시작 (CONFIRMED → SHIPPED)")
    class ShipOrderTest {

        @Test
        @DisplayName("AORD-011: CONFIRMED 상태의 주문을 배송 시작하면 SHIPPED 상태가 된다")
        void shipOrder_confirmedStatus_success() {
            // given
            OrderResponse shippedOrder =
                    createOrderResponseWithTimestamps(
                            CONFIRMED_ORDER_ID,
                            ORDER_STATUS_SHIPPED,
                            Instant.now(),
                            Instant.now(),
                            Instant.now(),
                            null,
                            null,
                            null);

            given(shipOrderUseCase.shipOrder(CONFIRMED_ORDER_ID)).willReturn(shippedOrder);

            String url = ApiV2Paths.Orders.BASE + "/" + CONFIRMED_ORDER_ID + "/ship";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().status()).isEqualTo(ORDER_STATUS_SHIPPED);
            assertThat(response.getBody().data().shippedAt()).isNotNull();
        }

        @Test
        @DisplayName("AORD-012: 존재하지 않는 주문을 배송 시작하면 404를 반환한다")
        void shipOrder_nonExistentOrder_returns404() {
            // given
            given(shipOrderUseCase.shipOrder(NON_EXISTENT_ORDER_ID))
                    .willThrow(new OrderNotFoundException(NON_EXISTENT_ORDER_ID));

            String url = ApiV2Paths.Orders.BASE + "/" + NON_EXISTENT_ORDER_ID + "/ship";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("배송 완료 (SHIPPED → DELIVERED)")
    class DeliverOrderTest {

        @Test
        @DisplayName("AORD-013: SHIPPED 상태의 주문을 배송 완료하면 DELIVERED 상태가 된다")
        void deliverOrder_shippedStatus_success() {
            // given
            OrderResponse deliveredOrder =
                    createOrderResponseWithTimestamps(
                            SHIPPED_ORDER_ID,
                            ORDER_STATUS_DELIVERED,
                            Instant.now(),
                            Instant.now(),
                            Instant.now(),
                            Instant.now(),
                            null,
                            null);

            given(deliverOrderUseCase.deliverOrder(SHIPPED_ORDER_ID)).willReturn(deliveredOrder);

            String url = ApiV2Paths.Orders.BASE + "/" + SHIPPED_ORDER_ID + "/deliver";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().status()).isEqualTo(ORDER_STATUS_DELIVERED);
            assertThat(response.getBody().data().deliveredAt()).isNotNull();
        }

        @Test
        @DisplayName("AORD-014: 존재하지 않는 주문을 배송 완료하면 404를 반환한다")
        void deliverOrder_nonExistentOrder_returns404() {
            // given
            given(deliverOrderUseCase.deliverOrder(NON_EXISTENT_ORDER_ID))
                    .willThrow(new OrderNotFoundException(NON_EXISTENT_ORDER_ID));

            String url = ApiV2Paths.Orders.BASE + "/" + NON_EXISTENT_ORDER_ID + "/deliver";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("구매 확정 (DELIVERED → COMPLETED)")
    class CompleteOrderTest {

        @Test
        @DisplayName("AORD-015: DELIVERED 상태의 주문을 구매 확정하면 COMPLETED 상태가 된다")
        void completeOrder_deliveredStatus_success() {
            // given
            OrderResponse completedOrder =
                    createOrderResponseWithTimestamps(
                            DELIVERED_ORDER_ID,
                            ORDER_STATUS_COMPLETED,
                            Instant.now(),
                            Instant.now(),
                            Instant.now(),
                            Instant.now(),
                            Instant.now(),
                            null);

            given(completeOrderUseCase.completeOrder(DELIVERED_ORDER_ID))
                    .willReturn(completedOrder);

            String url = ApiV2Paths.Orders.BASE + "/" + DELIVERED_ORDER_ID + "/complete";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().status()).isEqualTo(ORDER_STATUS_COMPLETED);
            assertThat(response.getBody().data().completedAt()).isNotNull();
        }

        @Test
        @DisplayName("AORD-016: 존재하지 않는 주문을 구매 확정하면 404를 반환한다")
        void completeOrder_nonExistentOrder_returns404() {
            // given
            given(completeOrderUseCase.completeOrder(NON_EXISTENT_ORDER_ID))
                    .willThrow(new OrderNotFoundException(NON_EXISTENT_ORDER_ID));

            String url = ApiV2Paths.Orders.BASE + "/" + NON_EXISTENT_ORDER_ID + "/complete";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("주문 취소 (ORDERED/CONFIRMED → CANCELLED)")
    class CancelOrderTest {

        @Test
        @DisplayName("AORD-017: ORDERED 상태의 주문을 취소하면 CANCELLED 상태가 된다")
        void cancelOrder_orderedStatus_success() {
            // given
            OrderResponse cancelledOrder =
                    createOrderResponseWithTimestamps(
                            ORDERED_ORDER_ID,
                            ORDER_STATUS_CANCELLED,
                            Instant.now(),
                            null,
                            null,
                            null,
                            null,
                            Instant.now());

            given(cancelOrderUseCase.cancelOrder(ORDERED_ORDER_ID)).willReturn(cancelledOrder);

            String url = ApiV2Paths.Orders.BASE + "/" + ORDERED_ORDER_ID + "/cancel";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().status()).isEqualTo(ORDER_STATUS_CANCELLED);
            assertThat(response.getBody().data().cancelledAt()).isNotNull();
        }

        @Test
        @DisplayName("AORD-018: CONFIRMED 상태의 주문을 취소하면 CANCELLED 상태가 된다")
        void cancelOrder_confirmedStatus_success() {
            // given
            OrderResponse cancelledOrder =
                    createOrderResponseWithTimestamps(
                            CONFIRMED_ORDER_ID,
                            ORDER_STATUS_CANCELLED,
                            Instant.now(),
                            Instant.now(),
                            null,
                            null,
                            null,
                            Instant.now());

            given(cancelOrderUseCase.cancelOrder(CONFIRMED_ORDER_ID)).willReturn(cancelledOrder);

            String url = ApiV2Paths.Orders.BASE + "/" + CONFIRMED_ORDER_ID + "/cancel";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().status()).isEqualTo(ORDER_STATUS_CANCELLED);
        }

        @Test
        @DisplayName("AORD-019: 존재하지 않는 주문을 취소하면 404를 반환한다")
        void cancelOrder_nonExistentOrder_returns404() {
            // given
            given(cancelOrderUseCase.cancelOrder(NON_EXISTENT_ORDER_ID))
                    .willThrow(new OrderNotFoundException(NON_EXISTENT_ORDER_ID));

            String url = ApiV2Paths.Orders.BASE + "/" + NON_EXISTENT_ORDER_ID + "/cancel";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // State Transition Failure Tests (409 Conflict)
    // ============================================================

    @Nested
    @DisplayName("상태 전이 실패 테스트 (409 Conflict)")
    class StatusTransitionFailureTest {

        @Test
        @DisplayName("AORD-020: SHIPPED 상태에서 취소를 시도하면 409를 반환한다")
        void cancelOrder_shippedStatus_returns409() {
            // given
            given(cancelOrderUseCase.cancelOrder(SHIPPED_ORDER_ID))
                    .willThrow(
                            OrderStatusException.notCancellable(
                                    OrderId.fromString(SHIPPED_ORDER_ID), OrderStatus.SHIPPED));

            String url = ApiV2Paths.Orders.BASE + "/" + SHIPPED_ORDER_ID + "/cancel";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("AORD-021: DELIVERED 상태에서 취소를 시도하면 409를 반환한다")
        void cancelOrder_deliveredStatus_returns409() {
            // given
            given(cancelOrderUseCase.cancelOrder(DELIVERED_ORDER_ID))
                    .willThrow(
                            OrderStatusException.notCancellable(
                                    OrderId.fromString(DELIVERED_ORDER_ID), OrderStatus.DELIVERED));

            String url = ApiV2Paths.Orders.BASE + "/" + DELIVERED_ORDER_ID + "/cancel";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("AORD-022: COMPLETED 상태에서 상태 변경을 시도하면 409를 반환한다")
        void cancelOrder_completedStatus_returns409() {
            // given
            given(cancelOrderUseCase.cancelOrder(COMPLETED_ORDER_ID))
                    .willThrow(
                            OrderStatusException.alreadyCompleted(
                                    OrderId.fromString(COMPLETED_ORDER_ID)));

            String url = ApiV2Paths.Orders.BASE + "/" + COMPLETED_ORDER_ID + "/cancel";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("AORD-023: CANCELLED 상태에서 확정을 시도하면 409를 반환한다")
        void confirmOrder_cancelledStatus_returns409() {
            // given
            given(confirmOrderUseCase.confirmOrder(CANCELLED_ORDER_ID))
                    .willThrow(
                            OrderStatusException.alreadyCancelled(
                                    OrderId.fromString(CANCELLED_ORDER_ID)));

            String url = ApiV2Paths.Orders.BASE + "/" + CANCELLED_ORDER_ID + "/confirm";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("AORD-024: ORDERED 상태에서 바로 배송 시작하면 409를 반환한다 (단계 건너뛰기)")
        void shipOrder_orderedStatus_returns409() {
            // given
            given(shipOrderUseCase.shipOrder(ORDERED_ORDER_ID))
                    .willThrow(
                            OrderStatusException.notShippable(
                                    OrderId.fromString(ORDERED_ORDER_ID), OrderStatus.PENDING));

            String url = ApiV2Paths.Orders.BASE + "/" + ORDERED_ORDER_ID + "/ship";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @DisplayName("AORD-025: CONFIRMED 상태에서 바로 배송 완료하면 409를 반환한다 (단계 건너뛰기)")
        void deliverOrder_confirmedStatus_returns409() {
            // given
            given(deliverOrderUseCase.deliverOrder(CONFIRMED_ORDER_ID))
                    .willThrow(
                            OrderStatusException.notShippable(
                                    OrderId.fromString(CONFIRMED_ORDER_ID), OrderStatus.CONFIRMED));

            String url = ApiV2Paths.Orders.BASE + "/" + CONFIRMED_ORDER_ID + "/deliver";

            // when
            ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> response =
                    post(
                            url,
                            null,
                            new ParameterizedTypeReference<
                                    ApiResponse<OrderAdminV2ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }

    // ============================================================
    // Helper Methods
    // ============================================================

    /**
     * 기본 주문 응답 생성
     *
     * @param orderId 주문 ID
     * @param status 주문 상태
     * @return OrderResponse
     */
    private OrderResponse createOrderResponse(String orderId, String status) {
        return createOrderResponseWithTimestamps(
                orderId,
                status,
                Instant.now(),
                ORDER_STATUS_CONFIRMED.equals(status)
                                || ORDER_STATUS_SHIPPED.equals(status)
                                || ORDER_STATUS_DELIVERED.equals(status)
                                || ORDER_STATUS_COMPLETED.equals(status)
                        ? Instant.now()
                        : null,
                ORDER_STATUS_SHIPPED.equals(status)
                                || ORDER_STATUS_DELIVERED.equals(status)
                                || ORDER_STATUS_COMPLETED.equals(status)
                        ? Instant.now()
                        : null,
                ORDER_STATUS_DELIVERED.equals(status) || ORDER_STATUS_COMPLETED.equals(status)
                        ? Instant.now()
                        : null,
                ORDER_STATUS_COMPLETED.equals(status) ? Instant.now() : null,
                ORDER_STATUS_CANCELLED.equals(status) ? Instant.now() : null);
    }

    /**
     * 타임스탬프를 포함한 주문 응답 생성
     *
     * @param orderId 주문 ID
     * @param status 주문 상태
     * @param orderedAt 주문 일시
     * @param confirmedAt 확정 일시
     * @param shippedAt 배송 시작 일시
     * @param deliveredAt 배송 완료 일시
     * @param completedAt 구매 확정 일시
     * @param cancelledAt 취소 일시
     * @return OrderResponse
     */
    private OrderResponse createOrderResponseWithTimestamps(
            String orderId,
            String status,
            Instant orderedAt,
            Instant confirmedAt,
            Instant shippedAt,
            Instant deliveredAt,
            Instant completedAt,
            Instant cancelledAt) {

        OrderItemResponse item =
                new OrderItemResponse(
                        ORDERED_ORDER_ITEM_ID,
                        100L,
                        1001L,
                        2,
                        0,
                        0,
                        2,
                        new BigDecimal("29900"),
                        new BigDecimal("59800"),
                        status,
                        "테스트 상품",
                        "https://example.com/image.jpg",
                        "블랙 / XL",
                        "Nike",
                        "공식스토어");

        String orderNumber = getOrderNumberForId(orderId);

        return new OrderResponse(
                orderId,
                orderNumber,
                "660e8400-e29b-41d4-a716-446655440001",
                "770e8400-e29b-41d4-a716-446655440002",
                DEFAULT_SELLER_ID,
                "01912345-6789-7abc-def0-123456789abc",
                status,
                List.of(item),
                "홍길동",
                "010-1234-5678",
                "서울시 강남구 테헤란로 123",
                "456호",
                "06234",
                "부재 시 경비실에 맡겨주세요",
                new BigDecimal("59800"),
                BigDecimal.ZERO,
                new BigDecimal("59800"),
                orderedAt,
                confirmedAt,
                shippedAt,
                deliveredAt,
                completedAt,
                cancelledAt);
    }

    /**
     * 주문 ID에 해당하는 주문번호 반환
     *
     * @param orderId 주문 ID
     * @return 주문번호
     */
    private String getOrderNumberForId(String orderId) {
        if (ORDERED_ORDER_ID.equals(orderId)) {
            return ORDERED_ORDER_NUMBER;
        } else if (CONFIRMED_ORDER_ID.equals(orderId)) {
            return CONFIRMED_ORDER_NUMBER;
        } else if (SHIPPED_ORDER_ID.equals(orderId)) {
            return SHIPPED_ORDER_NUMBER;
        } else if (DELIVERED_ORDER_ID.equals(orderId)) {
            return DELIVERED_ORDER_NUMBER;
        } else if (COMPLETED_ORDER_ID.equals(orderId)) {
            return COMPLETED_ORDER_NUMBER;
        } else if (CANCELLED_ORDER_ID.equals(orderId)) {
            return CANCELLED_ORDER_NUMBER;
        }
        return "ORD-UNKNOWN";
    }
}
