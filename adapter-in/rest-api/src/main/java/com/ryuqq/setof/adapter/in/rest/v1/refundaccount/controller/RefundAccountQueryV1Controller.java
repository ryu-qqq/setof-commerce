package com.ryuqq.setof.adapter.in.rest.v1.refundaccount.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.RefundAccountV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.response.RefundAccountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.mapper.RefundAccountV1ApiMapper;
import com.ryuqq.setof.application.refundaccount.port.in.query.GetRefundAccountUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RefundAccountQueryV1Controller - 환불 계좌 조회 V1 Public API.
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
@Tag(name = "환불 계좌 조회 V1", description = "환불 계좌 조회 V1 Public API (인증 필요)")
@RestController
@RequestMapping(RefundAccountV1Endpoints.REFUND_ACCOUNT)
public class RefundAccountQueryV1Controller {

    private final GetRefundAccountUseCase getRefundAccountUseCase;
    private final RefundAccountV1ApiMapper mapper;

    public RefundAccountQueryV1Controller(
            GetRefundAccountUseCase getRefundAccountUseCase, RefundAccountV1ApiMapper mapper) {
        this.getRefundAccountUseCase = getRefundAccountUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "환불 계좌 조회", description = "인증된 사용자의 환불 계좌를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "환불 계좌를 찾을 수 없음")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping
    public ResponseEntity<V1ApiResponse<RefundAccountV1ApiResponse>> getRefundAccount(
            @AuthenticatedUserId Long userId) {
        RefundAccountV1ApiResponse response =
                mapper.toResponse(getRefundAccountUseCase.execute(userId));
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
