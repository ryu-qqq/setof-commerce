package com.ryuqq.setof.adapter.in.rest.v1.refundaccount.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.RefundAccountV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.request.RegisterRefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.request.UpdateRefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.mapper.RefundAccountV1ApiMapper;
import com.ryuqq.setof.application.refundaccount.port.in.command.DeleteRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.command.RegisterRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.command.UpdateRefundAccountUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RefundAccountCommandV1Controller - 환불 계좌 명령 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * <p>API-CTR-007: Controller 비즈니스 로직 금지.
 *
 * <p>인증 필수 엔드포인트: @AuthenticatedUserId로 userId 추출.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "환불 계좌 명령 V1", description = "환불 계좌 등록/수정/삭제 V1 Public API (인증 필요)")
@RestController
@RequestMapping(RefundAccountV1Endpoints.REFUND_ACCOUNT)
public class RefundAccountCommandV1Controller {

    private final RegisterRefundAccountUseCase registerRefundAccountUseCase;
    private final UpdateRefundAccountUseCase updateRefundAccountUseCase;
    private final DeleteRefundAccountUseCase deleteRefundAccountUseCase;
    private final RefundAccountV1ApiMapper mapper;

    public RefundAccountCommandV1Controller(
            RegisterRefundAccountUseCase registerRefundAccountUseCase,
            UpdateRefundAccountUseCase updateRefundAccountUseCase,
            DeleteRefundAccountUseCase deleteRefundAccountUseCase,
            RefundAccountV1ApiMapper mapper) {
        this.registerRefundAccountUseCase = registerRefundAccountUseCase;
        this.updateRefundAccountUseCase = updateRefundAccountUseCase;
        this.deleteRefundAccountUseCase = deleteRefundAccountUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "환불 계좌 등록", description = "인증된 사용자의 환불 계좌를 등록합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping
    public ResponseEntity<V1ApiResponse<Long>> registerRefundAccount(
            @AuthenticatedUserId Long userId,
            @Valid @RequestBody RegisterRefundAccountV1ApiRequest request) {
        Long id = registerRefundAccountUseCase.execute(mapper.toRegisterCommand(userId, request));
        return ResponseEntity.ok(V1ApiResponse.success(id));
    }

    @Operation(summary = "환불 계좌 수정", description = "인증된 사용자의 특정 환불 계좌를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "환불 계좌를 찾을 수 없음")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PutMapping(RefundAccountV1Endpoints.REFUND_ACCOUNT_ID)
    public ResponseEntity<V1ApiResponse<Void>> updateRefundAccount(
            @AuthenticatedUserId Long userId,
            @Parameter(description = "환불 계좌 ID", required = true)
                    @PathVariable(RefundAccountV1Endpoints.PATH_REFUND_ACCOUNT_ID)
                    Long refundAccountId,
            @Valid @RequestBody UpdateRefundAccountV1ApiRequest request) {
        updateRefundAccountUseCase.execute(
                mapper.toUpdateCommand(userId, refundAccountId, request));
        return ResponseEntity.ok(V1ApiResponse.success(null));
    }

    @Operation(summary = "환불 계좌 삭제", description = "인증된 사용자의 특정 환불 계좌를 삭제합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "환불 계좌를 찾을 수 없음")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @DeleteMapping(RefundAccountV1Endpoints.REFUND_ACCOUNT_ID)
    public ResponseEntity<V1ApiResponse<Void>> deleteRefundAccount(
            @AuthenticatedUserId Long userId,
            @Parameter(description = "환불 계좌 ID", required = true)
                    @PathVariable(RefundAccountV1Endpoints.PATH_REFUND_ACCOUNT_ID)
                    Long refundAccountId) {
        deleteRefundAccountUseCase.execute(mapper.toDeleteCommand(userId, refundAccountId));
        return ResponseEntity.ok(V1ApiResponse.success(null));
    }
}
