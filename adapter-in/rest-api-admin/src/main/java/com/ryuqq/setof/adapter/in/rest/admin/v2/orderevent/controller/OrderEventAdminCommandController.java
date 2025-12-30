package com.ryuqq.setof.adapter.in.rest.admin.v2.orderevent.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.orderevent.dto.request.RecordOrderEventV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.orderevent.mapper.OrderEventAdminV2ApiMapper;
import com.ryuqq.setof.application.orderevent.dto.command.RecordOrderEventCommand;
import com.ryuqq.setof.application.orderevent.dto.response.OrderEventResponse;
import com.ryuqq.setof.application.orderevent.port.in.command.RecordOrderEventUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderEvent Admin Command Controller
 *
 * <p>관리자용 주문 이벤트(타임라인) 관리 API (CQRS Command 분리)
 *
 * <p>수동으로 주문 타임라인에 이벤트를 기록합니다. (시스템에서 자동 기록하지 못하는 예외적인 상황에서 사용)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>POST, PUT, PATCH 메서드만 포함
 *   <li>Command DTO는 @RequestBody로 바인딩
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin - Order Event", description = "주문 이벤트 관리 API (관리자)")
@RestController
@RequestMapping(ApiV2Paths.OrderEvents.BASE)
@Validated
public class OrderEventAdminCommandController {

    private final RecordOrderEventUseCase recordOrderEventUseCase;
    private final OrderEventAdminV2ApiMapper orderEventAdminV2ApiMapper;

    public OrderEventAdminCommandController(
            RecordOrderEventUseCase recordOrderEventUseCase,
            OrderEventAdminV2ApiMapper orderEventAdminV2ApiMapper) {
        this.recordOrderEventUseCase = recordOrderEventUseCase;
        this.orderEventAdminV2ApiMapper = orderEventAdminV2ApiMapper;
    }

    /**
     * 주문 이벤트 수동 기록
     *
     * <p>주문 타임라인에 이벤트를 수동으로 기록합니다.
     *
     * @param request 이벤트 기록 요청
     * @return 생성된 이벤트 ID
     */
    @Operation(
            summary = "주문 이벤트 수동 기록",
            description = "주문 타임라인에 이벤트를 수동으로 기록합니다. 시스템에서 자동 기록하지 못하는 예외적인 상황에서 사용합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "이벤트 기록 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "403",
                        description = "권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> recordEvent(
            @Valid @RequestBody RecordOrderEventV2ApiRequest request) {

        RecordOrderEventCommand command = orderEventAdminV2ApiMapper.toCommand(request);
        OrderEventResponse response = recordOrderEventUseCase.recordEvent(command);

        return ResponseEntity.created(URI.create(ApiV2Paths.OrderEvents.BASE + "/" + response.id()))
                .body(ApiResponse.ofSuccess(response.id()));
    }
}
