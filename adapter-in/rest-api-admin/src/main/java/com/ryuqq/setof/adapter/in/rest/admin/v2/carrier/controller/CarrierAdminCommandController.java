package com.ryuqq.setof.adapter.in.rest.admin.v2.carrier.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.carrier.dto.command.RegisterCarrierV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.carrier.dto.command.UpdateCarrierV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.carrier.mapper.CarrierAdminV2ApiMapper;
import com.ryuqq.setof.application.carrier.dto.command.RegisterCarrierCommand;
import com.ryuqq.setof.application.carrier.dto.command.UpdateCarrierCommand;
import com.ryuqq.setof.application.carrier.port.in.command.ActivateCarrierUseCase;
import com.ryuqq.setof.application.carrier.port.in.command.DeactivateCarrierUseCase;
import com.ryuqq.setof.application.carrier.port.in.command.RegisterCarrierUseCase;
import com.ryuqq.setof.application.carrier.port.in.command.UpdateCarrierUseCase;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Carrier Admin Command Controller
 *
 * <p>택배사 등록/수정/활성화/비활성화 API 엔드포인트 (CQRS Command 분리)
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
@Tag(name = "Admin Carrier", description = "택배사 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Carriers.BASE)
@Validated
public class CarrierAdminCommandController {

    private final RegisterCarrierUseCase registerCarrierUseCase;
    private final UpdateCarrierUseCase updateCarrierUseCase;
    private final ActivateCarrierUseCase activateCarrierUseCase;
    private final DeactivateCarrierUseCase deactivateCarrierUseCase;
    private final CarrierAdminV2ApiMapper mapper;

    public CarrierAdminCommandController(
            RegisterCarrierUseCase registerCarrierUseCase,
            UpdateCarrierUseCase updateCarrierUseCase,
            ActivateCarrierUseCase activateCarrierUseCase,
            DeactivateCarrierUseCase deactivateCarrierUseCase,
            CarrierAdminV2ApiMapper mapper) {
        this.registerCarrierUseCase = registerCarrierUseCase;
        this.updateCarrierUseCase = updateCarrierUseCase;
        this.activateCarrierUseCase = activateCarrierUseCase;
        this.deactivateCarrierUseCase = deactivateCarrierUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "택배사 등록", description = "새로운 택배사를 등록합니다.")
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
                        responseCode = "409",
                        description = "중복된 택배사 코드",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> registerCarrier(
            @Valid @RequestBody RegisterCarrierV2ApiRequest request) {

        RegisterCarrierCommand command = mapper.toRegisterCommand(request);
        Long carrierId = registerCarrierUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ofSuccess(carrierId));
    }

    @Operation(summary = "택배사 수정", description = "택배사 정보를 수정합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "택배사 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(ApiV2Paths.Carriers.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> updateCarrier(
            @Parameter(description = "택배사 ID") @PathVariable Long carrierId,
            @Valid @RequestBody UpdateCarrierV2ApiRequest request) {

        UpdateCarrierCommand command = mapper.toUpdateCommand(carrierId, request);
        updateCarrierUseCase.execute(carrierId, command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "택배사 활성화", description = "비활성 상태의 택배사를 활성화합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "활성화 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "택배사 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.Carriers.ACTIVATE_PATH)
    public ResponseEntity<ApiResponse<Void>> activateCarrier(
            @Parameter(description = "택배사 ID") @PathVariable Long carrierId) {

        activateCarrierUseCase.execute(carrierId);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "택배사 비활성화", description = "활성 상태의 택배사를 비활성화합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "비활성화 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "택배사 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.Carriers.DEACTIVATE_PATH)
    public ResponseEntity<ApiResponse<Void>> deactivateCarrier(
            @Parameter(description = "택배사 ID") @PathVariable Long carrierId) {

        deactivateCarrierUseCase.execute(carrierId);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
