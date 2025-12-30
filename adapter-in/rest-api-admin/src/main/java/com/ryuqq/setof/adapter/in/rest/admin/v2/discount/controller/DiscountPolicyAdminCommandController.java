package com.ryuqq.setof.adapter.in.rest.admin.v2.discount.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.RegisterDiscountPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.UpdateDiscountPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.UpdateDiscountTargetsV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.mapper.DiscountPolicyAdminV2ApiMapper;
import com.ryuqq.setof.application.discount.dto.command.ActivateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.DeactivateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.DeleteDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.RegisterDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountTargetsCommand;
import com.ryuqq.setof.application.discount.port.in.command.ActivateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.DeactivateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.DeleteDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.RegisterDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountTargetsUseCase;
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
 * DiscountPolicy Admin Command Controller
 *
 * <p>할인 정책 등록/수정/삭제 API 엔드포인트 (CQRS Command 분리)
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
@Tag(name = "Admin DiscountPolicy", description = "할인 정책 관리 API")
@RestController
@RequestMapping(ApiV2Paths.DiscountPolicies.BASE)
@Validated
@PreAuthorize("@sellerAccess.canAccess(#sellerId)")
public class DiscountPolicyAdminCommandController {

    private final RegisterDiscountPolicyUseCase registerDiscountPolicyUseCase;
    private final UpdateDiscountPolicyUseCase updateDiscountPolicyUseCase;
    private final ActivateDiscountPolicyUseCase activateDiscountPolicyUseCase;
    private final DeactivateDiscountPolicyUseCase deactivateDiscountPolicyUseCase;
    private final DeleteDiscountPolicyUseCase deleteDiscountPolicyUseCase;
    private final UpdateDiscountTargetsUseCase updateDiscountTargetsUseCase;
    private final DiscountPolicyAdminV2ApiMapper mapper;

    public DiscountPolicyAdminCommandController(
            RegisterDiscountPolicyUseCase registerDiscountPolicyUseCase,
            UpdateDiscountPolicyUseCase updateDiscountPolicyUseCase,
            ActivateDiscountPolicyUseCase activateDiscountPolicyUseCase,
            DeactivateDiscountPolicyUseCase deactivateDiscountPolicyUseCase,
            DeleteDiscountPolicyUseCase deleteDiscountPolicyUseCase,
            UpdateDiscountTargetsUseCase updateDiscountTargetsUseCase,
            DiscountPolicyAdminV2ApiMapper mapper) {
        this.registerDiscountPolicyUseCase = registerDiscountPolicyUseCase;
        this.updateDiscountPolicyUseCase = updateDiscountPolicyUseCase;
        this.activateDiscountPolicyUseCase = activateDiscountPolicyUseCase;
        this.deactivateDiscountPolicyUseCase = deactivateDiscountPolicyUseCase;
        this.deleteDiscountPolicyUseCase = deleteDiscountPolicyUseCase;
        this.updateDiscountTargetsUseCase = updateDiscountTargetsUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "할인 정책 등록", description = "새로운 할인 정책을 등록합니다.")
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
    public ResponseEntity<ApiResponse<Long>> registerDiscountPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Valid @RequestBody RegisterDiscountPolicyV2ApiRequest request) {

        RegisterDiscountPolicyCommand command = mapper.toRegisterCommand(sellerId, request);
        Long discountPolicyId = registerDiscountPolicyUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ofSuccess(discountPolicyId));
    }

    @Operation(summary = "할인 정책 수정", description = "할인 정책 정보를 수정합니다.")
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
                        description = "할인 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(ApiV2Paths.DiscountPolicies.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> updateDiscountPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "할인 정책 ID") @PathVariable Long discountPolicyId,
            @Valid @RequestBody UpdateDiscountPolicyV2ApiRequest request) {

        UpdateDiscountPolicyCommand command =
                mapper.toUpdateCommand(discountPolicyId, sellerId, request);
        updateDiscountPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "할인 정책 활성화", description = "할인 정책을 활성화합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "활성화 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "할인 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.DiscountPolicies.ACTIVATE_PATH)
    public ResponseEntity<ApiResponse<Void>> activateDiscountPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "할인 정책 ID") @PathVariable Long discountPolicyId) {

        ActivateDiscountPolicyCommand command =
                mapper.toActivateCommand(discountPolicyId, sellerId);
        activateDiscountPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "할인 정책 비활성화", description = "할인 정책을 비활성화합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "비활성화 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "할인 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.DiscountPolicies.DEACTIVATE_PATH)
    public ResponseEntity<ApiResponse<Void>> deactivateDiscountPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "할인 정책 ID") @PathVariable Long discountPolicyId) {

        DeactivateDiscountPolicyCommand command =
                mapper.toDeactivateCommand(discountPolicyId, sellerId);
        deactivateDiscountPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "할인 정책 삭제", description = "할인 정책을 삭제합니다 (소프트 삭제).")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "할인 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.DiscountPolicies.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> deleteDiscountPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "할인 정책 ID") @PathVariable Long discountPolicyId) {

        DeleteDiscountPolicyCommand command = mapper.toDeleteCommand(discountPolicyId, sellerId);
        deleteDiscountPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "할인 정책 적용 대상 수정", description = "할인 정책의 적용 대상 ID 목록을 수정합니다.")
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
                        description = "할인 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(ApiV2Paths.DiscountPolicies.TARGETS_PATH)
    public ResponseEntity<ApiResponse<Void>> updateDiscountTargets(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "할인 정책 ID") @PathVariable Long discountPolicyId,
            @Valid @RequestBody UpdateDiscountTargetsV2ApiRequest request) {

        UpdateDiscountTargetsCommand command =
                mapper.toUpdateTargetsCommand(discountPolicyId, sellerId, request);
        updateDiscountTargetsUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
