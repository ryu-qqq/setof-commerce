package com.ryuqq.setof.adapter.in.rest.v2.order.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.order.dto.response.OrderV2ApiResponse;
import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.application.order.dto.query.GetOrdersQuery;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.port.in.command.CancelOrderUseCase;
import com.ryuqq.setof.application.order.port.in.query.GetOrderUseCase;
import com.ryuqq.setof.application.order.port.in.query.GetOrdersUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order V2 Controller (Customer)
 *
 * <p>고객용 주문 관련 API 엔드포인트 (조회/취소)
 *
 * <p>주문 상태 흐름:
 *
 * <pre>
 * ORDERED → CONFIRMED → SHIPPED → DELIVERED → COMPLETED
 *    ↓                               ↓
 * CANCELLED                      (환불가능)
 * </pre>
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>Controller는 HTTP 처리만 담당
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>Command/Query 분리 (CQRS)
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Order", description = "주문 API (고객)")
@RestController
@RequestMapping(ApiV2Paths.Orders.BASE)
@Validated
public class OrderV2Controller {

    private final GetOrderUseCase getOrderUseCase;
    private final GetOrdersUseCase getOrdersUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    public OrderV2Controller(
            GetOrderUseCase getOrderUseCase,
            GetOrdersUseCase getOrdersUseCase,
            CancelOrderUseCase cancelOrderUseCase) {
        this.getOrderUseCase = getOrderUseCase;
        this.getOrdersUseCase = getOrdersUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
    }

    /**
     * 주문 목록 조회
     *
     * <p>회원의 주문 목록을 커서 기반 페이지네이션으로 조회합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param orderStatuses 주문 상태 목록 (선택)
     * @param startDate 시작 일시 (선택)
     * @param endDate 종료 일시 (선택)
     * @param lastOrderId 마지막 주문 ID (커서)
     * @param pageSize 페이지 크기
     * @return 주문 목록 (Slice)
     */
    @Operation(summary = "주문 목록 조회", description = "회원의 주문 목록을 조회합니다. 커서 기반 페이지네이션을 지원합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<SliceResponse<OrderV2ApiResponse>>> getOrders(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(description = "주문 상태 목록") @RequestParam(required = false)
                    List<String> orderStatuses,
            @Parameter(description = "시작 일시") @RequestParam(required = false) Instant startDate,
            @Parameter(description = "종료 일시") @RequestParam(required = false) Instant endDate,
            @Parameter(description = "마지막 주문 ID (커서)") @RequestParam(required = false)
                    String lastOrderId,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "20") int pageSize) {

        GetOrdersQuery query =
                GetOrdersQuery.of(
                        principal.getMemberId(),
                        orderStatuses,
                        startDate,
                        endDate,
                        lastOrderId,
                        pageSize);

        SliceResponse<OrderResponse> result = getOrdersUseCase.getOrders(query);

        List<OrderV2ApiResponse> apiResponses =
                result.content().stream().map(OrderV2ApiResponse::from).toList();

        SliceResponse<OrderV2ApiResponse> apiResult =
                SliceResponse.of(
                        apiResponses, result.size(), result.hasNext(), result.nextCursor());

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResult));
    }

    /**
     * 주문 조회
     *
     * <p>주문 ID로 주문 상세 정보를 조회합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param orderId 주문 ID (UUID)
     * @return 주문 정보
     */
    @Operation(summary = "주문 조회", description = "주문 ID로 주문 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "403",
                        description = "접근 권한 없음 (다른 회원의 주문)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "주문 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Orders.ID_PATH)
    public ResponseEntity<ApiResponse<OrderV2ApiResponse>> getOrder(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(
                            description = "주문 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440000")
                    @PathVariable
                    String orderId) {

        // TODO: memberId 검증 로직 추가 필요 (UseCase에서 처리)
        OrderResponse response = getOrderUseCase.getOrder(orderId);
        OrderV2ApiResponse apiResponse = OrderV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 주문 번호로 조회
     *
     * <p>주문 번호로 주문 상세 정보를 조회합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param orderNumber 주문 번호
     * @return 주문 정보
     */
    @Operation(summary = "주문 번호로 조회", description = "주문 번호로 주문 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "403",
                        description = "접근 권한 없음 (다른 회원의 주문)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "주문 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Orders.BY_NUMBER_PATH)
    public ResponseEntity<ApiResponse<OrderV2ApiResponse>> getOrderByNumber(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(description = "주문 번호", example = "ORD-20241215-000001") @PathVariable
                    String orderNumber) {

        // TODO: memberId 검증 로직 추가 필요 (UseCase에서 처리)
        OrderResponse response = getOrderUseCase.getOrderByOrderNumber(orderNumber);
        OrderV2ApiResponse apiResponse = OrderV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 주문 취소
     *
     * <p>주문을 취소합니다. 배송 시작 전까지만 취소 가능합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param orderId 주문 ID (UUID)
     * @return 취소된 주문 정보
     */
    @Operation(
            summary = "주문 취소",
            description = "주문을 취소합니다. 배송 시작 전(ORDERED, CONFIRMED 상태)까지만 취소 가능합니다.")
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
                        description = "접근 권한 없음 (다른 회원의 주문)",
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
    @PostMapping(ApiV2Paths.Orders.ID_PATH + ApiV2Paths.Orders.CANCEL_PATH)
    public ResponseEntity<ApiResponse<OrderV2ApiResponse>> cancelOrder(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(
                            description = "주문 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440000")
                    @PathVariable
                    String orderId) {

        // TODO: memberId 검증 로직 추가 필요 (UseCase에서 처리)
        OrderResponse response = cancelOrderUseCase.cancelOrder(orderId);
        OrderV2ApiResponse apiResponse = OrderV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
