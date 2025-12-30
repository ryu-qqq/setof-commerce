package com.ryuqq.setof.adapter.in.rest.admin.v2.order.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.order.dto.response.OrderAdminV2ApiResponse;
import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.application.order.dto.query.GetAdminOrdersQuery;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.port.in.query.GetAdminOrdersUseCase;
import com.ryuqq.setof.application.order.port.in.query.GetOrderUseCase;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order Admin Query Controller
 *
 * <p>주문 조회 API 엔드포인트 (CQRS Query 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>GET 메서드만 포함
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
public class OrderAdminQueryController {

    private final GetOrderUseCase getOrderUseCase;
    private final GetAdminOrdersUseCase getAdminOrdersUseCase;

    public OrderAdminQueryController(
            GetOrderUseCase getOrderUseCase, GetAdminOrdersUseCase getAdminOrdersUseCase) {
        this.getOrderUseCase = getOrderUseCase;
        this.getAdminOrdersUseCase = getAdminOrdersUseCase;
    }

    /**
     * 주문 목록 조회
     *
     * <p>관리자용 주문 목록을 커서 기반 페이지네이션으로 조회합니다.
     *
     * @param sellerId 셀러 ID (선택)
     * @param orderStatuses 주문 상태 목록 (선택)
     * @param searchKeyword 검색어 (선택)
     * @param startDate 시작 일시 (선택)
     * @param endDate 종료 일시 (선택)
     * @param lastOrderId 마지막 주문 ID (커서)
     * @param pageSize 페이지 크기
     * @return 주문 목록 (Slice)
     */
    @Operation(summary = "주문 목록 조회", description = "관리자용 주문 목록을 조회합니다. 커서 기반 페이지네이션을 지원합니다.")
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
                        description = "권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @GetMapping
    public ResponseEntity<ApiResponse<SliceResponse<OrderAdminV2ApiResponse>>> getOrders(
            @Parameter(description = "셀러 ID") @RequestParam(required = false) Long sellerId,
            @Parameter(description = "주문 상태 목록") @RequestParam(required = false)
                    List<String> orderStatuses,
            @Parameter(description = "검색어 (주문번호, 수령인명)") @RequestParam(required = false)
                    String searchKeyword,
            @Parameter(description = "시작 일시") @RequestParam(required = false) Instant startDate,
            @Parameter(description = "종료 일시") @RequestParam(required = false) Instant endDate,
            @Parameter(description = "마지막 주문 ID (커서)") @RequestParam(required = false)
                    String lastOrderId,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "20") int pageSize) {

        GetAdminOrdersQuery query =
                GetAdminOrdersQuery.of(
                        sellerId,
                        orderStatuses,
                        searchKeyword,
                        startDate,
                        endDate,
                        lastOrderId,
                        pageSize);

        SliceResponse<OrderResponse> result = getAdminOrdersUseCase.getOrders(query);

        List<OrderAdminV2ApiResponse> apiResponses =
                result.content().stream().map(OrderAdminV2ApiResponse::from).toList();

        SliceResponse<OrderAdminV2ApiResponse> apiResult =
                SliceResponse.of(
                        apiResponses, result.size(), result.hasNext(), result.nextCursor());

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResult));
    }

    /**
     * 주문 조회
     *
     * <p>주문 ID로 주문 상세 정보를 조회합니다.
     *
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
                        description = "권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "주문 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @GetMapping(ApiV2Paths.Orders.ID_PATH)
    public ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> getOrder(
            @Parameter(
                            description = "주문 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440000")
                    @PathVariable
                    String orderId) {

        OrderResponse response = getOrderUseCase.getOrder(orderId);
        OrderAdminV2ApiResponse apiResponse = OrderAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 주문 번호로 조회
     *
     * <p>주문 번호로 주문 상세 정보를 조회합니다.
     *
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
                        description = "권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "주문 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PreAuthorize("@access.orgAdminOrHigher()")
    @GetMapping(ApiV2Paths.Orders.BY_NUMBER_PATH)
    public ResponseEntity<ApiResponse<OrderAdminV2ApiResponse>> getOrderByNumber(
            @Parameter(description = "주문 번호", example = "ORD-20241215-000001") @PathVariable
                    String orderNumber) {

        OrderResponse response = getOrderUseCase.getOrderByOrderNumber(orderNumber);
        OrderAdminV2ApiResponse apiResponse = OrderAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
