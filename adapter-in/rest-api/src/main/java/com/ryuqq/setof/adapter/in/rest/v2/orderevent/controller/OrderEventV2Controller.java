package com.ryuqq.setof.adapter.in.rest.v2.orderevent.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.orderevent.dto.response.OrderTimelineV2ApiResponse;
import com.ryuqq.setof.application.orderevent.dto.response.OrderTimelineResponse;
import com.ryuqq.setof.application.orderevent.port.in.query.GetOrderTimelineUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderEvent V2 Controller (Customer)
 *
 * <p>고객용 주문 타임라인 조회 API
 *
 * <p>타임라인에는 주문의 전체 히스토리가 포함됩니다:
 *
 * <ul>
 *   <li>주문 생성, 확인, 배송 시작, 배송 완료
 *   <li>결제 승인, 취소, 환불
 *   <li>클레임 요청, 승인, 완료
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Order Timeline", description = "주문 타임라인 API (고객)")
@RestController
@RequestMapping(ApiV2Paths.Orders.BASE)
@Validated
public class OrderEventV2Controller {

    private final GetOrderTimelineUseCase getOrderTimelineUseCase;

    public OrderEventV2Controller(GetOrderTimelineUseCase getOrderTimelineUseCase) {
        this.getOrderTimelineUseCase = getOrderTimelineUseCase;
    }

    /**
     * 주문 타임라인 조회
     *
     * <p>주문의 전체 히스토리를 시간순으로 조회합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param orderId 주문 ID (UUID)
     * @return 타임라인 이벤트 목록
     */
    @Operation(summary = "주문 타임라인 조회", description = "주문의 전체 히스토리를 시간순으로 조회합니다.")
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
                        description = "접근 권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "주문 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Orders.ID_PATH + ApiV2Paths.Orders.TIMELINE_PATH)
    public ResponseEntity<ApiResponse<OrderTimelineV2ApiResponse>> getOrderTimeline(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(
                            description = "주문 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440000")
                    @PathVariable
                    String orderId) {

        // TODO: memberId 검증 로직 추가 필요 (UseCase에서 처리)
        OrderTimelineResponse response = getOrderTimelineUseCase.getTimeline(orderId);
        OrderTimelineV2ApiResponse apiResponse = OrderTimelineV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
