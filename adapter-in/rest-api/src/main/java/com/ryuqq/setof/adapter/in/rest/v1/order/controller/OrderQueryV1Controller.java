package com.ryuqq.setof.adapter.in.rest.v1.order.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.OrderV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.request.SearchOrderCountsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.request.SearchOrdersCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderCountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderHistoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.mapper.OrderV1ApiMapper;
import com.ryuqq.setof.application.order.dto.query.OrderSearchParams;
import com.ryuqq.setof.application.order.dto.response.OrderHistoryResult;
import com.ryuqq.setof.application.order.dto.response.OrderSliceResult;
import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResult;
import com.ryuqq.setof.application.order.port.in.query.GetOrderHistoryUseCase;
import com.ryuqq.setof.application.order.port.in.query.GetOrderStatusCountsUseCase;
import com.ryuqq.setof.application.order.port.in.query.GetOrdersUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderQueryV1Controller - 주문 조회 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "주문 조회 V1", description = "주문 조회 V1 Public API (인증 필요)")
@RestController
public class OrderQueryV1Controller {

    private final GetOrderStatusCountsUseCase getOrderStatusCountsUseCase;
    private final GetOrderHistoryUseCase getOrderHistoryUseCase;
    private final GetOrdersUseCase getOrdersUseCase;
    private final OrderV1ApiMapper mapper;

    public OrderQueryV1Controller(
            GetOrderStatusCountsUseCase getOrderStatusCountsUseCase,
            GetOrderHistoryUseCase getOrderHistoryUseCase,
            GetOrdersUseCase getOrdersUseCase,
            OrderV1ApiMapper mapper) {
        this.getOrderStatusCountsUseCase = getOrderStatusCountsUseCase;
        this.getOrderHistoryUseCase = getOrderHistoryUseCase;
        this.getOrdersUseCase = getOrdersUseCase;
        this.mapper = mapper;
    }

    /**
     * 주문 상태별 건수 조회 API.
     *
     * <p>GET /api/v1/order/status-counts
     *
     * @param userId 인증된 사용자 ID
     * @param request 조회할 주문 상태 목록
     * @return 상태별 건수 목록
     */
    @Operation(summary = "주문 상태별 건수 조회", description = "인증된 사용자의 주문 상태별 건수를 조회합니다 (최근 3개월).")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(OrderV1Endpoints.ORDER_STATUS_COUNTS)
    public ResponseEntity<V1ApiResponse<List<OrderCountV1ApiResponse>>> getOrderStatusCounts(
            @AuthenticatedUserId Long userId,
            @ParameterObject @Valid SearchOrderCountsV1ApiRequest request) {
        List<OrderStatusCountResult> results =
                getOrderStatusCountsUseCase.execute(userId, request.orderStatuses());
        List<OrderCountV1ApiResponse> response = mapper.toOrderCountListResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 주문 이력 조회 API.
     *
     * <p>GET /api/v1/order/history/{orderId}
     *
     * @param orderId 주문 ID
     * @return 주문 상태 변경 이력 목록
     */
    @Operation(summary = "주문 이력 조회", description = "특정 주문의 상태 변경 이력을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(OrderV1Endpoints.ORDER_HISTORY)
    public ResponseEntity<V1ApiResponse<List<OrderHistoryV1ApiResponse>>> getOrderHistory(
            @PathVariable long orderId) {
        List<OrderHistoryResult> results = getOrderHistoryUseCase.execute(orderId);
        List<OrderHistoryV1ApiResponse> response = mapper.toOrderHistoryListResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 주문 목록 조회 API.
     *
     * <p>GET /api/v1/orders
     *
     * @param userId 인증된 사용자 ID
     * @param request 검색 조건 (커서 페이징)
     * @return 주문 슬라이스 (커서 + 상태별 건수 포함)
     */
    @Operation(
            summary = "주문 목록 조회",
            description = "인증된 사용자의 주문 목록을 커서 기반 페이징으로 조회합니다. 상태별 건수도 함께 반환됩니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(OrderV1Endpoints.ORDERS)
    public ResponseEntity<V1ApiResponse<OrderSliceV1ApiResponse>> getOrders(
            @AuthenticatedUserId Long userId,
            @ParameterObject @Valid SearchOrdersCursorV1ApiRequest request) {
        OrderSearchParams params = mapper.toSearchParams(userId, request);
        OrderSliceResult result = getOrdersUseCase.execute(params);
        int size = (request.size() != null) ? request.size() : 20;
        OrderSliceV1ApiResponse response =
                mapper.toOrderSliceResponse(result, request.lastOrderId(), size);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
