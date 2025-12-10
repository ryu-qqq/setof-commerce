package com.ryuqq.setof.adapter.in.rest.admin.v1.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.command.ShipmentInfoV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.command.UpdateOrderV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.query.OrderFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderDashboardV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderDateDashboardV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderHistoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderListV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.SettlementV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.ShipmentInfoV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.UpdateOrderV1ApiResponse;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Order Controller (Legacy)
 *
 * <p>
 * 레거시 API 호환을 위한 V1 Order 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Order (Legacy V1)", description = "레거시 Order API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
public class OrderV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] 주문 조회", description = "특정 주문을 조회합니다.")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<OrderV1ApiResponse>> fetchOrder(
            @PathVariable("orderId") long orderId) {

        throw new UnsupportedOperationException("주문 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 주문 목록 조회", description = "주문 목록을 조회합니다.")
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<PageApiResponse<OrderListV1ApiResponse>>> getOrders(
            @ModelAttribute @Validated OrderFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("주문 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 정산 목록 조회", description = "정산 목록을 조회합니다.")
    @GetMapping("/settlements")
    public ResponseEntity<ApiResponse<PageApiResponse<SettlementV1ApiResponse>>> getSettlements(
            @ModelAttribute @Validated OrderFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("정산 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 주문 히스토리 조회", description = "특정 주문의 히스토리를 조회합니다.")
    @GetMapping("/order/history/{orderId}")
    public ResponseEntity<ApiResponse<List<OrderHistoryV1ApiResponse>>> getOrderHistory(
            @PathVariable("orderId") long orderId) {

        throw new UnsupportedOperationException("주문 히스토리 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 주문 상태 수정", description = "주문 상태를 수정합니다.")
    @PutMapping("/order")
    public ResponseEntity<ApiResponse<UpdateOrderV1ApiResponse>> modifyOrderStatus(
            @RequestBody @Validated UpdateOrderV1ApiRequest dto) {

        throw new UnsupportedOperationException("주문 상태 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 주문 상태 목록 수정", description = "여러 주문의 상태를 수정합니다.")
    @PutMapping("/orders")
    public ResponseEntity<ApiResponse<List<UpdateOrderV1ApiResponse>>> modifyOrderStatusList(
            @RequestBody @Validated List<UpdateOrderV1ApiRequest> dto) {

        throw new UnsupportedOperationException("주문 상태 목록 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 배송 시작", description = "주문의 배송을 시작합니다.")
    @PatchMapping("/shipment/order/{orderId}")
    public ResponseEntity<ApiResponse<ShipmentInfoV1ApiResponse>> modifyOrderStatusList(
            @PathVariable("orderId") long orderId,
            @Valid @RequestBody ShipmentInfoV1ApiRequest shipmentInfo) {

        throw new UnsupportedOperationException("배송 시작 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 오늘 주문 대시보드 조회", description = "오늘의 주문 대시보드를 조회합니다.")
    @GetMapping("/order/today-dashboard")
    public ResponseEntity<ApiResponse<OrderDashboardV1ApiResponse>> getOrderTodayDashboard(
            @ModelAttribute @Validated OrderFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("오늘 주문 대시보드 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 날짜별 주문 대시보드 조회", description = "날짜별 주문 대시보드를 조회합니다.")
    @GetMapping("/order/date-dashboard")
    public ResponseEntity<ApiResponse<OrderDateDashboardV1ApiResponse>> getOrderDateDashboard(
            @ModelAttribute @Validated OrderFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("날짜별 주문 대시보드 조회 기능은 아직 지원되지 않습니다.");
    }
}
