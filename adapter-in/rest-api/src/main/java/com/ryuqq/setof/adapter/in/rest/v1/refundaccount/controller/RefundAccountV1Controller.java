package com.ryuqq.setof.adapter.in.rest.v1.refundaccount.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.command.RefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.response.RefundAccountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.mapper.RefundAccountV1ApiMapper;
import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountByBankNameCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountByBankNameCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import com.ryuqq.setof.application.refundaccount.port.in.command.DeleteRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.command.RegisterRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.command.UpdateRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.query.GetRefundAccountUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 RefundAccount Controller
 *
 * <p>레거시 API 호환을 위한 V1 환불계좌 엔드포인트 V2 UseCase를 재사용하며, V1 DTO로 변환하여 응답
 *
 * <p>경로: /api/v1/user/refund-account (레거시 스펙 유지)
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "RefundAccount (Legacy V1)", description = "레거시 환불계좌 API - V2로 마이그레이션 권장")
@RestController
@RequestMapping(ApiPaths.User.RefundAccount.BASE)
@Validated
@Deprecated
public class RefundAccountV1Controller {

    private final RegisterRefundAccountUseCase registerRefundAccountUseCase;
    private final GetRefundAccountUseCase getRefundAccountUseCase;
    private final UpdateRefundAccountUseCase updateRefundAccountUseCase;
    private final DeleteRefundAccountUseCase deleteRefundAccountUseCase;
    private final RefundAccountV1ApiMapper refundAccountV1ApiMapper;

    public RefundAccountV1Controller(
            RegisterRefundAccountUseCase registerRefundAccountUseCase,
            GetRefundAccountUseCase getRefundAccountUseCase,
            UpdateRefundAccountUseCase updateRefundAccountUseCase,
            DeleteRefundAccountUseCase deleteRefundAccountUseCase,
            RefundAccountV1ApiMapper refundAccountV1ApiMapper) {
        this.registerRefundAccountUseCase = registerRefundAccountUseCase;
        this.getRefundAccountUseCase = getRefundAccountUseCase;
        this.updateRefundAccountUseCase = updateRefundAccountUseCase;
        this.deleteRefundAccountUseCase = deleteRefundAccountUseCase;
        this.refundAccountV1ApiMapper = refundAccountV1ApiMapper;
    }

    /**
     * 환불계좌 조회
     *
     * @param principal 인증된 사용자 정보
     * @return 환불계좌 정보 (없으면 null)
     */
    @Deprecated
    @Operation(summary = "[Legacy] 환불계좌 조회", description = "현재 로그인한 회원의 환불계좌를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<RefundAccountV1ApiResponse>> fetchRefundAccount(
            @AuthenticationPrincipal MemberPrincipal principal) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        Optional<RefundAccountResponse> response = getRefundAccountUseCase.execute(memberId);

        RefundAccountV1ApiResponse v1Response =
                response.map(refundAccountV1ApiMapper::toV1Response).orElse(null);

        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Response));
    }

    /**
     * 환불계좌 등록
     *
     * @param principal 인증된 사용자 정보
     * @param request 환불계좌 등록 요청
     * @return 등록된 환불계좌 정보
     */
    @Deprecated
    @Operation(summary = "[Legacy] 환불계좌 등록", description = "환불계좌를 등록합니다. 회원당 1개만 등록 가능합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<RefundAccountV1ApiResponse>> createRefundAccount(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody RefundAccountV1ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        RegisterRefundAccountByBankNameCommand command =
                refundAccountV1ApiMapper.toRegisterCommand(memberId, request);

        RefundAccountResponse response = registerRefundAccountUseCase.execute(command);
        RefundAccountV1ApiResponse v1Response = refundAccountV1ApiMapper.toV1Response(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Response));
    }

    /**
     * 환불계좌 수정
     *
     * @param principal 인증된 사용자 정보
     * @param refundAccountId 환불계좌 ID
     * @param request 환불계좌 수정 요청
     * @return 수정된 환불계좌 정보
     */
    @Deprecated
    @Operation(summary = "[Legacy] 환불계좌 수정", description = "환불계좌 정보를 수정합니다.")
    @PutMapping("/{refundAccountId}")
    public ResponseEntity<ApiResponse<RefundAccountV1ApiResponse>> updateRefundAccount(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("refundAccountId") long refundAccountId,
            @Valid @RequestBody RefundAccountV1ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        UpdateRefundAccountByBankNameCommand command =
                refundAccountV1ApiMapper.toUpdateCommand(memberId, refundAccountId, request);

        RefundAccountResponse response = updateRefundAccountUseCase.execute(command);
        RefundAccountV1ApiResponse v1Response = refundAccountV1ApiMapper.toV1Response(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Response));
    }

    /**
     * 환불계좌 삭제
     *
     * <p>레거시 스펙 유지를 위해 DELETE 메서드 사용 (V2에서는 PATCH 사용)
     *
     * @param principal 인증된 사용자 정보
     * @param refundAccountId 환불계좌 ID
     * @return 성공 응답
     */
    @Deprecated
    @Operation(summary = "[Legacy] 환불계좌 삭제", description = "환불계좌를 삭제합니다.")
    @DeleteMapping("/{refundAccountId}")
    public ResponseEntity<ApiResponse<RefundAccountV1ApiResponse>> deleteRefundAccount(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("refundAccountId") long refundAccountId) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        DeleteRefundAccountCommand command =
                refundAccountV1ApiMapper.toDeleteCommand(memberId, refundAccountId);

        deleteRefundAccountUseCase.execute(command);

        // 레거시 스펙: 삭제된 환불계좌 정보 반환하지 않고 null 반환
        return ResponseEntity.ok(ApiResponse.ofSuccess(null));
    }
}
