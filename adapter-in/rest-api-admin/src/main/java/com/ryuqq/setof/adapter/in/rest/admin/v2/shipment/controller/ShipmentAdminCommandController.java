package com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.dto.command.ChangeInvoiceV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.dto.command.ChangeStatusV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.dto.command.RegisterShipmentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.mapper.ShipmentAdminV2ApiMapper;
import com.ryuqq.setof.application.shipment.dto.command.ChangeInvoiceCommand;
import com.ryuqq.setof.application.shipment.dto.command.ChangeShipmentStatusCommand;
import com.ryuqq.setof.application.shipment.dto.command.RegisterShipmentCommand;
import com.ryuqq.setof.application.shipment.port.in.command.ChangeInvoiceUseCase;
import com.ryuqq.setof.application.shipment.port.in.command.ChangeShipmentStatusUseCase;
import com.ryuqq.setof.application.shipment.port.in.command.RegisterShipmentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Shipment Admin Command Controller
 *
 * <p>운송장 등록/수정/상태변경 API 엔드포인트 (CQRS Command 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>POST, PUT, PATCH 메서드만 포함
 *   <li>Command DTO는 @RequestBody로 바인딩
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>DELETE 메서드 금지 - Soft Delete는 PATCH로 처리
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin Shipment", description = "운송장 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Shipments.BASE)
@Validated
public class ShipmentAdminCommandController {

    private final RegisterShipmentUseCase registerShipmentUseCase;
    private final ChangeInvoiceUseCase changeInvoiceUseCase;
    private final ChangeShipmentStatusUseCase changeShipmentStatusUseCase;
    private final ShipmentAdminV2ApiMapper mapper;

    public ShipmentAdminCommandController(
            RegisterShipmentUseCase registerShipmentUseCase,
            ChangeInvoiceUseCase changeInvoiceUseCase,
            ChangeShipmentStatusUseCase changeShipmentStatusUseCase,
            ShipmentAdminV2ApiMapper mapper) {
        this.registerShipmentUseCase = registerShipmentUseCase;
        this.changeInvoiceUseCase = changeInvoiceUseCase;
        this.changeShipmentStatusUseCase = changeShipmentStatusUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "운송장 등록", description = "새로운 운송장을 등록합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "등록 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "택배사/셀러/결제건 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> registerShipment(
            @Valid @RequestBody RegisterShipmentV2ApiRequest request) {

        RegisterShipmentCommand command = mapper.toRegisterCommand(request);
        Long shipmentId = registerShipmentUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ofSuccess(shipmentId));
    }

    @Operation(summary = "운송장 번호 변경", description = "발송 전(PENDING 상태)에만 운송장 번호를 변경할 수 있습니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "변경 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "발송 후 변경 불가",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "운송장/택배사 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.Shipments.INVOICE_PATH)
    public ResponseEntity<ApiResponse<Void>> changeInvoice(
            @Parameter(description = "운송장 ID") @PathVariable Long shipmentId,
            @Valid @RequestBody ChangeInvoiceV2ApiRequest request) {

        ChangeInvoiceCommand command = mapper.toChangeInvoiceCommand(request);
        changeInvoiceUseCase.execute(shipmentId, command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "운송장 상태 변경", description = "운송장의 배송 상태를 변경합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "변경 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "유효하지 않은 상태 전이",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "운송장 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.Shipments.STATUS_PATH)
    public ResponseEntity<ApiResponse<Void>> changeStatus(
            @Parameter(description = "운송장 ID") @PathVariable Long shipmentId,
            @Valid @RequestBody ChangeStatusV2ApiRequest request) {

        ChangeShipmentStatusCommand command = mapper.toChangeStatusCommand(request);
        changeShipmentStatusUseCase.execute(shipmentId, command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
