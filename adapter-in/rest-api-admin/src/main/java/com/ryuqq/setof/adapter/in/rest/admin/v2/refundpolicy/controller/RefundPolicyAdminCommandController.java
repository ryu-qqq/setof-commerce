package com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.RegisterRefundPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.UpdateRefundPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.mapper.RefundPolicyAdminV2ApiMapper;
import com.ryuqq.setof.application.refundpolicy.dto.command.DeleteRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.SetDefaultRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.UpdateRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.port.in.command.DeleteRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.RegisterRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.SetDefaultRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.UpdateRefundPolicyUseCase;
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
 * RefundPolicy Admin Command Controller
 *
 * <p>환불 정책 등록/수정/삭제 API 엔드포인트 (CQRS Command 분리)
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
@Tag(name = "Admin RefundPolicy", description = "환불 정책 관리 API")
@RestController
@RequestMapping(ApiV2Paths.RefundPolicies.BASE)
@Validated
@PreAuthorize("@sellerAccess.canAccess(#sellerId)")
public class RefundPolicyAdminCommandController {

    private final RegisterRefundPolicyUseCase registerRefundPolicyUseCase;
    private final UpdateRefundPolicyUseCase updateRefundPolicyUseCase;
    private final SetDefaultRefundPolicyUseCase setDefaultRefundPolicyUseCase;
    private final DeleteRefundPolicyUseCase deleteRefundPolicyUseCase;
    private final RefundPolicyAdminV2ApiMapper mapper;

    public RefundPolicyAdminCommandController(
            RegisterRefundPolicyUseCase registerRefundPolicyUseCase,
            UpdateRefundPolicyUseCase updateRefundPolicyUseCase,
            SetDefaultRefundPolicyUseCase setDefaultRefundPolicyUseCase,
            DeleteRefundPolicyUseCase deleteRefundPolicyUseCase,
            RefundPolicyAdminV2ApiMapper mapper) {
        this.registerRefundPolicyUseCase = registerRefundPolicyUseCase;
        this.updateRefundPolicyUseCase = updateRefundPolicyUseCase;
        this.setDefaultRefundPolicyUseCase = setDefaultRefundPolicyUseCase;
        this.deleteRefundPolicyUseCase = deleteRefundPolicyUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "환불 정책 등록", description = "새로운 환불 정책을 등록합니다.")
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
    public ResponseEntity<ApiResponse<Long>> registerRefundPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Valid @RequestBody RegisterRefundPolicyV2ApiRequest request) {

        RegisterRefundPolicyCommand command = mapper.toRegisterCommand(sellerId, request);
        Long refundPolicyId = registerRefundPolicyUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ofSuccess(refundPolicyId));
    }

    @Operation(summary = "환불 정책 수정", description = "환불 정책 정보를 수정합니다.")
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
                        description = "환불 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(ApiV2Paths.RefundPolicies.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> updateRefundPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "환불 정책 ID") @PathVariable Long refundPolicyId,
            @Valid @RequestBody UpdateRefundPolicyV2ApiRequest request) {

        UpdateRefundPolicyCommand command =
                mapper.toUpdateCommand(refundPolicyId, sellerId, request);
        updateRefundPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "기본 환불 정책 설정", description = "해당 환불 정책을 기본 정책으로 설정합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "설정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "환불 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.RefundPolicies.DEFAULT_PATH)
    public ResponseEntity<ApiResponse<Void>> setDefaultRefundPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "환불 정책 ID") @PathVariable Long refundPolicyId) {

        SetDefaultRefundPolicyCommand command =
                mapper.toSetDefaultCommand(refundPolicyId, sellerId);
        setDefaultRefundPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    @Operation(summary = "환불 정책 삭제", description = "환불 정책을 삭제합니다 (소프트 삭제).")
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
                        description = "환불 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.RefundPolicies.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> deleteRefundPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "환불 정책 ID") @PathVariable Long refundPolicyId) {

        DeleteRefundPolicyCommand command = mapper.toDeleteCommand(refundPolicyId, sellerId);
        deleteRefundPolicyUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
