package com.ryuqq.setof.adapter.in.rest.v1.order.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.OrderV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.request.UpdateOrderV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.UpdateOrderV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.mapper.OrderV1ApiMapper;
import com.ryuqq.setof.application.order.dto.command.UpdateLegacyOrderStatusCommand;
import com.ryuqq.setof.application.order.dto.response.UpdateOrderResult;
import com.ryuqq.setof.application.order.port.in.command.UpdateLegacyOrderStatusUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderCommandV1Controller - 주문 명령 V1 Public API.
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
@Tag(name = "주문 명령 V1", description = "주문 상태 변경 V1 Public API (인증 필요)")
@RestController
public class OrderCommandV1Controller {

    private final UpdateLegacyOrderStatusUseCase updateLegacyOrderStatusUseCase;
    private final OrderV1ApiMapper mapper;

    public OrderCommandV1Controller(
            UpdateLegacyOrderStatusUseCase updateLegacyOrderStatusUseCase,
            OrderV1ApiMapper mapper) {
        this.updateLegacyOrderStatusUseCase = updateLegacyOrderStatusUseCase;
        this.mapper = mapper;
    }

    /**
     * 주문 상태 변경 API.
     *
     * <p>PUT /api/v1/order
     *
     * @param userId 인증된 사용자 ID
     * @param request 주문 상태 변경 요청
     * @return 상태 변경 결과 목록
     */
    @Operation(summary = "주문 상태 변경", description = "주문의 상태를 변경합니다 (취소 요청, 반품 요청, 철회 등).")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "상태 변경 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PutMapping(OrderV1Endpoints.ORDER)
    public ResponseEntity<V1ApiResponse<List<UpdateOrderV1ApiResponse>>> updateOrder(
            @AuthenticatedUserId Long userId, @Valid @RequestBody UpdateOrderV1ApiRequest request) {
        UpdateLegacyOrderStatusCommand command = mapper.toUpdateCommand(userId, request);
        UpdateOrderResult result = updateLegacyOrderStatusUseCase.execute(command);
        UpdateOrderV1ApiResponse response = mapper.toUpdateOrderResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(List.of(response)));
    }
}
