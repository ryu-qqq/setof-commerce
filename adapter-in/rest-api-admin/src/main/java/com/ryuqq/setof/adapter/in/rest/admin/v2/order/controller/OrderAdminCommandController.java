package com.ryuqq.setof.adapter.in.rest.admin.v2.order.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.order.dto.response.OrderAdminV2ApiResponse;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.port.in.command.CancelOrderUseCase;
import com.ryuqq.setof.application.order.port.in.command.CompleteOrderUseCase;
import com.ryuqq.setof.application.order.port.in.command.ConfirmOrderUseCase;
import com.ryuqq.setof.application.order.port.in.command.DeliverOrderUseCase;
import com.ryuqq.setof.application.order.port.in.command.ShipOrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order Admin Command Controller
 *
 * <p>주문 상태 변경 API 엔드포인트 (CQRS Command 분리)
 *
 * <p>주문 상태 흐름:
 *
 * <pre>
 * ORDERED → CONFIRMED → SHIPPED → DELIVERED → COMPLETED
 *    ↓         ↓
 * CANCELLED  CANCELLED
 * </pre>
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>POST 메서드만 포함 (상태 변경 액션)
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin Order", description = "주문 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Orders.BASE)
@Validated
public class OrderAdminCommandController {

    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final ShipOrderUseCase shipOrderUseCase;
    private final DeliverOrderUseCase deliverOrderUseCase;
    private final CompleteOrderUseCase completeOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    public OrderAdminCommandController(
            ConfirmOrderUseCase confirmOrderUseCase,
            ShipOrderUseCase shipOrderUseCase,
            DeliverOrderUseCase deliverOrderUseCase,
            CompleteOrderUseCase completeOrderUseCase,
            CancelOrderUseCase cancelOrderUseCase) {
        this.confirmOrderUseCase = confirmOrderUseCase;
        this.shipOrderUseCase = shipOrderUseCase;
        this.deliverOrderUseCase = deliverOrderUseCase;
        this.completeOrderUseCase = completeOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
    }

    /**
     * 주문 확정
     *
     * <p>주문을 확정 처리합니다. ORDERED → CONFIRMED 상태 전이.
     *
     * @param orderId 주문 ID (UUID)
     * @return 확정된 주문 정보
     */
    @Operation(summary = "주문 확정", description = "주문을 확정 처리합니다. 판매자가 주문을 접수하고 상품 준비를 시작합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "확정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "403",
                        description = "권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "주문 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "상태 전이 불가 (현재 상태에서 확정 불가)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @PostMapping(ApiV2Paths.Orders.ID_PATH + ApiV2Paths.Orders.CONFIRM_PATH)
    public ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> confirmOrder(
            @Parameter(
                            description = "주문 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440000")
                    @PathVariable
                    String orderId) {

        OrderResponse response = confirmOrderUseCase.confirmOrder(orderId);
        OrderAdminV2ApiResponse apiResponse = OrderAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 배송 시작
     *
     * <p>주문의 배송을 시작합니다. CONFIRMED → SHIPPED 상태 전이.
     *
     * @param orderId 주문 ID (UUID)
     * @return 배송 시작된 주문 정보
     */
    @Operation(summary = "배송 시작", description = "주문의 배송을 시작합니다. 상품이 출고되어 배송 중 상태가 됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "배송 시작 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "403",
                        description = "권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "주문 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "상태 전이 불가 (CONFIRMED 상태가 아님)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @PostMapping(ApiV2Paths.Orders.ID_PATH + ApiV2Paths.Orders.SHIP_PATH)
    public ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> shipOrder(
            @Parameter(
                            description = "주문 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440000")
                    @PathVariable
                    String orderId) {

        OrderResponse response = shipOrderUseCase.shipOrder(orderId);
        OrderAdminV2ApiResponse apiResponse = OrderAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 배송 완료
     *
     * <p>주문의 배송을 완료 처리합니다. SHIPPED → DELIVERED 상태 전이.
     *
     * @param orderId 주문 ID (UUID)
     * @return 배송 완료된 주문 정보
     */
    @Operation(summary = "배송 완료", description = "주문의 배송을 완료 처리합니다. 고객이 상품을 수령했음을 표시합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "배송 완료 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "403",
                        description = "권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "주문 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "상태 전이 불가 (SHIPPED 상태가 아님)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @PostMapping(ApiV2Paths.Orders.ID_PATH + ApiV2Paths.Orders.DELIVER_PATH)
    public ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> deliverOrder(
            @Parameter(
                            description = "주문 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440000")
                    @PathVariable
                    String orderId) {

        OrderResponse response = deliverOrderUseCase.deliverOrder(orderId);
        OrderAdminV2ApiResponse apiResponse = OrderAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 구매 확정
     *
     * <p>주문의 구매를 확정합니다. DELIVERED → COMPLETED 상태 전이.
     *
     * @param orderId 주문 ID (UUID)
     * @return 구매 확정된 주문 정보
     */
    @Operation(summary = "구매 확정", description = "주문의 구매를 확정합니다. 환불이 불가능한 상태가 됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "구매 확정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "403",
                        description = "권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "주문 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "상태 전이 불가 (DELIVERED 상태가 아님)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @PostMapping(ApiV2Paths.Orders.ID_PATH + ApiV2Paths.Orders.COMPLETE_PATH)
    public ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> completeOrder(
            @Parameter(
                            description = "주문 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440000")
                    @PathVariable
                    String orderId) {

        OrderResponse response = completeOrderUseCase.completeOrder(orderId);
        OrderAdminV2ApiResponse apiResponse = OrderAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 주문 취소
     *
     * <p>주문을 취소합니다. ORDERED/CONFIRMED 상태에서만 취소 가능합니다.
     *
     * @param orderId 주문 ID (UUID)
     * @return 취소된 주문 정보
     */
    @Operation(
            summary = "주문 취소",
            description = "주문을 취소합니다. 배송 시작 전(ORDERED, CONFIRMED 상태)에만 취소 가능합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "취소 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "403",
                        description = "권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "주문 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "취소 불가 (이미 배송중 또는 완료)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @PostMapping(ApiV2Paths.Orders.ID_PATH + ApiV2Paths.Orders.CANCEL_PATH)
    public ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> cancelOrder(
            @Parameter(
                            description = "주문 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440000")
                    @PathVariable
                    String orderId) {

        OrderResponse response = cancelOrderUseCase.cancelOrder(orderId);
        OrderAdminV2ApiResponse apiResponse = OrderAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
