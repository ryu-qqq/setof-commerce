package com.ryuqq.setof.adapter.in.rest.admin.v2.discount.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.response.DiscountPolicyV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.mapper.DiscountPolicyAdminV2ApiMapper;
import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchQuery;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResponse;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPoliciesUseCase;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPolicyUseCase;
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
 * DiscountPolicy Admin Query Controller
 *
 * <p>할인 정책 조회 API 엔드포인트 (CQRS Query 분리)
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
@Tag(name = "Admin DiscountPolicy", description = "할인 정책 관리 API")
@RestController
@RequestMapping(ApiV2Paths.DiscountPolicies.BASE)
@Validated
@PreAuthorize("@sellerAccess.canAccess(#sellerId)")
public class DiscountPolicyAdminQueryController {

    private final GetDiscountPolicyUseCase getDiscountPolicyUseCase;
    private final GetDiscountPoliciesUseCase getDiscountPoliciesUseCase;
    private final DiscountPolicyAdminV2ApiMapper mapper;

    public DiscountPolicyAdminQueryController(
            GetDiscountPolicyUseCase getDiscountPolicyUseCase,
            GetDiscountPoliciesUseCase getDiscountPoliciesUseCase,
            DiscountPolicyAdminV2ApiMapper mapper) {
        this.getDiscountPolicyUseCase = getDiscountPolicyUseCase;
        this.getDiscountPoliciesUseCase = getDiscountPoliciesUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "할인 정책 목록 조회", description = "셀러의 할인 정책 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<DiscountPolicyV2ApiResponse>>> getDiscountPolicies(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "활성 정책만 조회") @RequestParam(defaultValue = "false")
                    boolean activeOnly,
            @Parameter(description = "삭제된 정책 포함 여부") @RequestParam(defaultValue = "false")
                    boolean includeDeleted,
            @Parameter(description = "현재 유효한 정책만 조회") @RequestParam(defaultValue = "false")
                    boolean validOnly) {

        DiscountPolicySearchQuery query =
                mapper.toSearchQuery(sellerId, activeOnly, includeDeleted, validOnly);
        List<DiscountPolicyResponse> responses = getDiscountPoliciesUseCase.execute(query);

        List<DiscountPolicyV2ApiResponse> apiResponses =
                responses.stream().map(DiscountPolicyV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    @Operation(summary = "현재 유효한 할인 정책 조회", description = "현재 유효한 할인 정책만 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.DiscountPolicies.VALID_PATH)
    public ResponseEntity<ApiResponse<List<DiscountPolicyV2ApiResponse>>> getValidDiscountPolicies(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId) {

        DiscountPolicySearchQuery query = mapper.toActiveSearchQuery(sellerId);
        List<DiscountPolicyResponse> responses = getDiscountPoliciesUseCase.execute(query);

        List<DiscountPolicyV2ApiResponse> apiResponses =
                responses.stream().map(DiscountPolicyV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    @Operation(summary = "할인 정책 상세 조회", description = "할인 정책 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "할인 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.DiscountPolicies.ID_PATH)
    public ResponseEntity<ApiResponse<DiscountPolicyV2ApiResponse>> getDiscountPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "할인 정책 ID") @PathVariable Long discountPolicyId) {

        DiscountPolicyResponse response = getDiscountPolicyUseCase.execute(discountPolicyId);

        DiscountPolicyV2ApiResponse apiResponse = DiscountPolicyV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
