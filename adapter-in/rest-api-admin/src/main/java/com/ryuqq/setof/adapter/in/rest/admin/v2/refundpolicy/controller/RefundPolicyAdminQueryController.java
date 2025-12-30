package com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.response.RefundPolicyV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.mapper.RefundPolicyAdminV2ApiMapper;
import com.ryuqq.setof.application.refundpolicy.dto.query.RefundPolicySearchQuery;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetRefundPoliciesUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetRefundPolicyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RefundPolicy Admin Query Controller
 *
 * <p>환불 정책 조회 API 엔드포인트 (CQRS Query 분리)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>GET 메서드만 포함
 *   <li>비즈니스 로직은 UseCase에 위임
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
public class RefundPolicyAdminQueryController {

    private final GetRefundPolicyUseCase getRefundPolicyUseCase;
    private final GetRefundPoliciesUseCase getRefundPoliciesUseCase;
    private final RefundPolicyAdminV2ApiMapper mapper;

    public RefundPolicyAdminQueryController(
            GetRefundPolicyUseCase getRefundPolicyUseCase,
            GetRefundPoliciesUseCase getRefundPoliciesUseCase,
            RefundPolicyAdminV2ApiMapper mapper) {
        this.getRefundPolicyUseCase = getRefundPolicyUseCase;
        this.getRefundPoliciesUseCase = getRefundPoliciesUseCase;
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
}
