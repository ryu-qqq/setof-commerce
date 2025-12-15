package com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.RegisterRefundPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.UpdateRefundPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.response.RefundPolicyV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.mapper.RefundPolicyAdminV2ApiMapper;
import com.ryuqq.setof.application.refundpolicy.dto.command.DeleteRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.SetDefaultRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.UpdateRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.query.RefundPolicySearchQuery;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import com.ryuqq.setof.application.refundpolicy.port.in.command.DeleteRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.RegisterRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.SetDefaultRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.UpdateRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetRefundPoliciesUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetRefundPolicyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RefundPolicy Admin V2 Controller
 *
 * <p>환불 정책 관리 API 엔드포인트 (등록/조회/수정/기본설정/삭제)
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
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Admin RefundPolicy", description = "환불 정책 관리 API")
@RestController
@RequestMapping(ApiV2Paths.RefundPolicies.BASE)
@Validated
public class RefundPolicyAdminV2Controller {

    private final RegisterRefundPolicyUseCase registerRefundPolicyUseCase;
    private final GetRefundPolicyUseCase getRefundPolicyUseCase;
    private final GetRefundPoliciesUseCase getRefundPoliciesUseCase;
    private final UpdateRefundPolicyUseCase updateRefundPolicyUseCase;
    private final SetDefaultRefundPolicyUseCase setDefaultRefundPolicyUseCase;
    private final DeleteRefundPolicyUseCase deleteRefundPolicyUseCase;
    private final RefundPolicyAdminV2ApiMapper mapper;

    public RefundPolicyAdminV2Controller(
            RegisterRefundPolicyUseCase registerRefundPolicyUseCase,
            GetRefundPolicyUseCase getRefundPolicyUseCase,
            GetRefundPoliciesUseCase getRefundPoliciesUseCase,
            UpdateRefundPolicyUseCase updateRefundPolicyUseCase,
            SetDefaultRefundPolicyUseCase setDefaultRefundPolicyUseCase,
            DeleteRefundPolicyUseCase deleteRefundPolicyUseCase,
            RefundPolicyAdminV2ApiMapper mapper) {
        this.registerRefundPolicyUseCase = registerRefundPolicyUseCase;
        this.getRefundPolicyUseCase = getRefundPolicyUseCase;
        this.getRefundPoliciesUseCase = getRefundPoliciesUseCase;
        this.updateRefundPolicyUseCase = updateRefundPolicyUseCase;
        this.setDefaultRefundPolicyUseCase = setDefaultRefundPolicyUseCase;
        this.deleteRefundPolicyUseCase = deleteRefundPolicyUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "환불 정책 목록 조회", description = "셀러의 환불 정책 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<RefundPolicyV2ApiResponse>>> getRefundPolicies(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "삭제된 정책 포함 여부") @RequestParam(defaultValue = "false")
                    boolean includeDeleted) {

        RefundPolicySearchQuery query = mapper.toSearchQuery(sellerId, includeDeleted);
        List<RefundPolicyResponse> responses = getRefundPoliciesUseCase.execute(query);

        List<RefundPolicyV2ApiResponse> apiResponses =
                responses.stream().map(RefundPolicyV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    @Operation(summary = "환불 정책 상세 조회", description = "환불 정책 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "환불 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.RefundPolicies.ID_PATH)
    public ResponseEntity<ApiResponse<RefundPolicyV2ApiResponse>> getRefundPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "환불 정책 ID") @PathVariable Long refundPolicyId) {

        // UseCase에서 소유권 검증 수행
        RefundPolicyResponse response = getRefundPolicyUseCase.execute(refundPolicyId, sellerId);

        RefundPolicyV2ApiResponse apiResponse = RefundPolicyV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
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
