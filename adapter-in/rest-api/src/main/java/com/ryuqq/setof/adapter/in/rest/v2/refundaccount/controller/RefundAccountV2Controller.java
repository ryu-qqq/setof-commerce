package com.ryuqq.setof.adapter.in.rest.v2.refundaccount.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.refundaccount.dto.command.RegisterRefundAccountV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.refundaccount.dto.command.UpdateRefundAccountV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.refundaccount.dto.response.RefundAccountV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.refundaccount.mapper.RefundAccountV2ApiMapper;
import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import com.ryuqq.setof.application.refundaccount.port.in.command.DeleteRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.command.RegisterRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.command.UpdateRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.query.GetRefundAccountUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RefundAccount V2 Controller
 *
 * <p>환불계좌 관련 API 엔드포인트 (등록/조회/수정/삭제)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>Controller는 HTTP 처리만 담당
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>Command/Query 분리 (CQRS)
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 *   <li>DELETE 메서드 금지 - Soft Delete는 PATCH로 처리
 * </ul>
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>회원당 최대 1개까지만 등록 가능
 *   <li>등록/수정 시 외부 계좌 검증 API 통해 검증 필수
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "RefundAccount", description = "환불계좌 관리 API")
@RestController
@RequestMapping(ApiV2Paths.RefundAccount.BASE)
@Validated
public class RefundAccountV2Controller {

    private final RegisterRefundAccountUseCase registerRefundAccountUseCase;
    private final GetRefundAccountUseCase getRefundAccountUseCase;
    private final UpdateRefundAccountUseCase updateRefundAccountUseCase;
    private final DeleteRefundAccountUseCase deleteRefundAccountUseCase;
    private final RefundAccountV2ApiMapper refundAccountV2ApiMapper;

    public RefundAccountV2Controller(
            RegisterRefundAccountUseCase registerRefundAccountUseCase,
            GetRefundAccountUseCase getRefundAccountUseCase,
            UpdateRefundAccountUseCase updateRefundAccountUseCase,
            DeleteRefundAccountUseCase deleteRefundAccountUseCase,
            RefundAccountV2ApiMapper refundAccountV2ApiMapper) {
        this.registerRefundAccountUseCase = registerRefundAccountUseCase;
        this.getRefundAccountUseCase = getRefundAccountUseCase;
        this.updateRefundAccountUseCase = updateRefundAccountUseCase;
        this.deleteRefundAccountUseCase = deleteRefundAccountUseCase;
        this.refundAccountV2ApiMapper = refundAccountV2ApiMapper;
    }

    /**
     * 환불계좌 조회
     *
     * <p>회원당 최대 1개이므로 단건 조회
     *
     * @param principal 인증된 사용자 정보
     * @return 환불계좌 정보 (없으면 null)
     */
    @Operation(
            summary = "환불계좌 조회",
            description = "현재 로그인한 회원의 환불계좌를 조회합니다. 등록된 계좌가 없으면 null을 반환합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<RefundAccountV2ApiResponse>> getRefundAccount(
            @AuthenticationPrincipal MemberPrincipal principal) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        Optional<RefundAccountResponse> response = getRefundAccountUseCase.execute(memberId);

        RefundAccountV2ApiResponse apiResponse =
                response.map(RefundAccountV2ApiResponse::from).orElse(null);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 환불계좌 등록
     *
     * <p>회원당 최대 1개까지만 등록 가능합니다. 등록 시 외부 계좌 검증 API를 통해 계좌 소유 확인을 진행합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param request 환불계좌 등록 요청
     * @return 등록된 환불계좌 정보
     */
    @Operation(
            summary = "환불계좌 등록",
            description =
                    "환불계좌를 등록합니다. 회원당 1개만 등록 가능합니다. 등록 시 계좌 소유 확인을 진행합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "등록 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청 또는 계좌 검증 실패",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "환불계좌 이미 등록됨",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping
    public ResponseEntity<ApiResponse<RefundAccountV2ApiResponse>> registerRefundAccount(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody RegisterRefundAccountV2ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        RegisterRefundAccountCommand command =
                refundAccountV2ApiMapper.toRegisterCommand(memberId, request);

        RefundAccountResponse response = registerRefundAccountUseCase.execute(command);
        RefundAccountV2ApiResponse apiResponse = RefundAccountV2ApiResponse.from(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 환불계좌 수정
     *
     * <p>수정 시 외부 계좌 검증 API를 통해 계좌 소유 확인을 다시 진행합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param request 환불계좌 수정 요청
     * @return 수정된 환불계좌 정보
     */
    @Operation(
            summary = "환불계좌 수정",
            description = "환불계좌 정보를 수정합니다. 수정 시 계좌 소유 확인을 다시 진행합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청 또는 계좌 검증 실패",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "환불계좌 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping
    public ResponseEntity<ApiResponse<RefundAccountV2ApiResponse>> updateRefundAccount(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody UpdateRefundAccountV2ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());

        // 기존 환불계좌 조회하여 ID 획득
        Optional<RefundAccountResponse> existingAccount = getRefundAccountUseCase.execute(memberId);
        if (existingAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Long refundAccountId = existingAccount.get().id();
        UpdateRefundAccountCommand command =
                refundAccountV2ApiMapper.toUpdateCommand(memberId, refundAccountId, request);

        RefundAccountResponse response = updateRefundAccountUseCase.execute(command);
        RefundAccountV2ApiResponse apiResponse = RefundAccountV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 환불계좌 삭제 (Soft Delete)
     *
     * <p>컨벤션: DELETE 메서드 금지, 소프트 삭제는 PATCH로 처리
     *
     * @param principal 인증된 사용자 정보
     * @return 성공 응답
     */
    @Operation(summary = "환불계좌 삭제", description = "환불계좌를 삭제합니다 (소프트 삭제).")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "환불계좌 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.RefundAccount.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> deleteRefundAccount(
            @AuthenticationPrincipal MemberPrincipal principal) {

        UUID memberId = UUID.fromString(principal.getMemberId());

        // 기존 환불계좌 조회하여 ID 획득
        Optional<RefundAccountResponse> existingAccount = getRefundAccountUseCase.execute(memberId);
        if (existingAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Long refundAccountId = existingAccount.get().id();
        DeleteRefundAccountCommand command =
                refundAccountV2ApiMapper.toDeleteCommand(memberId, refundAccountId);

        deleteRefundAccountUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
