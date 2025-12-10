package com.ryuqq.setof.adapter.in.rest.v1.order.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.SliceApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.command.UpdateOrderV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.query.OrderFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderCountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderHistoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.UpdateOrderV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Order Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Order 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Order (Legacy V1)", description = "레거시 Order API - V2로 마이그레이션 권장")
@RestController
@RequestMapping
@Validated
@Deprecated
public class OrderV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] 주문 히스토리 조회", description = "특정 주문의 히스토리를 조회합니다.")
    @GetMapping(ApiPaths.Order.HISTORY)
    public ResponseEntity<ApiResponse<List<OrderHistoryV1ApiResponse>>> fetchOrderHistory(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("orderId") long orderId) {

        throw new UnsupportedOperationException("주문 히스토리 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 주문 수정", description = "주문을 수정합니다 (취소/환불 요청 등).")
    @PutMapping(ApiPaths.Order.UPDATE)
    public ResponseEntity<ApiResponse<List<UpdateOrderV1ApiResponse>>> updateOrders(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody UpdateOrderV1ApiRequest updateOrder) {

        throw new UnsupportedOperationException("주문 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 주문 목록 조회", description = "주문 목록을 조회합니다.")
    @GetMapping(ApiPaths.Order.LIST)
    public ResponseEntity<ApiResponse<SliceApiResponse<OrderV1ApiResponse>>> fetchOrders(
            @AuthenticationPrincipal MemberPrincipal principal,
            @ModelAttribute @Validated OrderFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("주문 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 주문 상태별 개수 조회", description = "주문 상태별 개수를 조회합니다.")
    @GetMapping(ApiPaths.Order.STATUS_COUNTS)
    public ResponseEntity<ApiResponse<List<OrderCountV1ApiResponse>>> fetchOrderCounts(
            @AuthenticationPrincipal MemberPrincipal principal,
            @RequestParam List<String> orderStatuses) {

        throw new UnsupportedOperationException("주문 상태별 개수 조회 기능은 아직 지원되지 않습니다.");
    }
}
