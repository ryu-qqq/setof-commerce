package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.RegisterShippingPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.UpdateShippingPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.mapper.ShippingPolicyAdminV2ApiMapper;
import com.ryuqq.setof.application.shippingpolicy.dto.command.DeleteShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.SetDefaultShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.UpdateShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.DeleteShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.RegisterShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.SetDefaultShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.UpdateShippingPolicyUseCase;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ShippingPolicy Admin Command Controller
 *
 * <p>배송 정책 등록/수정/삭제 API 엔드포인트 (CQRS Command 분리)
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
@Tag(name = "Admin ShippingPolicy", description = "배송 정책 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ShippingPolicies.BASE)
@Validated
@PreAuthorize("@sellerAccess.canAccess(#sellerId)")
public class ShippingPolicyAdminCommandController {

    private final RegisterShippingPolicyUseCase registerShippingPolicyUseCase;
    private final UpdateShippingPolicyUseCase updateShippingPolicyUseCase;
    private final SetDefaultShippingPolicyUseCase setDefaultShippingPolicyUseCase;
    private final DeleteShippingPolicyUseCase deleteShippingPolicyUseCase;
    private final ShippingPolicyAdminV2ApiMapper mapper;

    public ShippingPolicyAdminCommandController(
            RegisterShippingPolicyUseCase registerShippingPolicyUseCase,
            UpdateShippingPolicyUseCase updateShippingPolicyUseCase,
            SetDefaultShippingPolicyUseCase setDefaultShippingPolicyUseCase,
            DeleteShippingPolicyUseCase deleteShippingPolicyUseCase,
            ShippingPolicyAdminV2ApiMapper mapper) {
        this.registerShippingPolicyUseCase = registerShippingPolicyUseCase;
        this.updateShippingPolicyUseCase = updateShippingPolicyUseCase;
        this.setDefaultShippingPolicyUseCase = setDefaultShippingPolicyUseCase;
        this.deleteShippingPolicyUseCase = deleteShippingPolicyUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "배송 정책 등록", description = "새로운 배송 정책을 등록합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "등록 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> registerShippingPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Valid @RequestBody RegisterShippingPolicyV2ApiRequest request) {

        RegisterShippingPolicyCommand command = mapper.toRegisterCommand(sellerId, request);
        Long shippingPolicyId = registerShippingPolicyUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ofSuccess(shippingPolicyId));
    }

    @Operation(summary = "배송 정책 수정", description = "배송 정책 정보를 수정합니다.")
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
                        description = "배송 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(ApiV2Paths.ShippingPolicies.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> updateShippingPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "배송 정책 ID") @PathVariable Long shippingPolicyId,
            @Valid @RequestBody UpdateShippingPolicyV2ApiRequest request) {

        UpdateShippingPolicyCommand command =
                mapper.toUpdateCommand(shippingPolicyId, sellerId, request);
        updateShippingPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "기본 배송 정책 설정", description = "해당 배송 정책을 기본 정책으로 설정합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "설정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "배송 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.ShippingPolicies.DEFAULT_PATH)
    public ResponseEntity<ApiResponse<Void>> setDefaultShippingPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "배송 정책 ID") @PathVariable Long shippingPolicyId) {

        SetDefaultShippingPolicyCommand command =
                mapper.toSetDefaultCommand(shippingPolicyId, sellerId);
        setDefaultShippingPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "배송 정책 삭제", description = "배송 정책을 삭제합니다 (소프트 삭제).")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "삭제 불가 (기본 정책)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "배송 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.ShippingPolicies.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> deleteShippingPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "배송 정책 ID") @PathVariable Long shippingPolicyId) {

        DeleteShippingPolicyCommand command = mapper.toDeleteCommand(shippingPolicyId, sellerId);
        deleteShippingPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
