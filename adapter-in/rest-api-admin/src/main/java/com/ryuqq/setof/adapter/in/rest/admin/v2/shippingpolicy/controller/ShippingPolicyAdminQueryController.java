package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.response.ShippingPolicyV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.mapper.ShippingPolicyAdminV2ApiMapper;
import com.ryuqq.setof.application.shippingpolicy.dto.query.ShippingPolicySearchQuery;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetShippingPoliciesUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetShippingPolicyUseCase;
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
 * ShippingPolicy Admin Query Controller
 *
 * <p>배송 정책 조회 API 엔드포인트 (CQRS Query 분리)
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
@Tag(name = "Admin ShippingPolicy", description = "배송 정책 관리 API")
@RestController
@RequestMapping(ApiV2Paths.ShippingPolicies.BASE)
@Validated
@PreAuthorize("@sellerAccess.canAccess(#sellerId)")
public class ShippingPolicyAdminQueryController {

    private final GetShippingPolicyUseCase getShippingPolicyUseCase;
    private final GetShippingPoliciesUseCase getShippingPoliciesUseCase;
    private final ShippingPolicyAdminV2ApiMapper mapper;

    public ShippingPolicyAdminQueryController(
            GetShippingPolicyUseCase getShippingPolicyUseCase,
            GetShippingPoliciesUseCase getShippingPoliciesUseCase,
            ShippingPolicyAdminV2ApiMapper mapper) {
        this.getShippingPolicyUseCase = getShippingPolicyUseCase;
        this.getShippingPoliciesUseCase = getShippingPoliciesUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "배송 정책 목록 조회", description = "셀러의 배송 정책 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ShippingPolicyV2ApiResponse>>> getShippingPolicies(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "삭제된 정책 포함 여부") @RequestParam(defaultValue = "false")
                    boolean includeDeleted) {

        ShippingPolicySearchQuery query = mapper.toSearchQuery(sellerId, includeDeleted);
        List<ShippingPolicyResponse> responses = getShippingPoliciesUseCase.execute(query);

        List<ShippingPolicyV2ApiResponse> apiResponses =
                responses.stream().map(ShippingPolicyV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    @Operation(summary = "배송 정책 상세 조회", description = "배송 정책 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "배송 정책 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.ShippingPolicies.ID_PATH)
    public ResponseEntity<ApiResponse<ShippingPolicyV2ApiResponse>> getShippingPolicy(
            @Parameter(description = "셀러 ID") @PathVariable Long sellerId,
            @Parameter(description = "배송 정책 ID") @PathVariable Long shippingPolicyId) {

        // UseCase에서 소유권 검증 수행
        ShippingPolicyResponse response =
                getShippingPolicyUseCase.execute(shippingPolicyId, sellerId);

        ShippingPolicyV2ApiResponse apiResponse = ShippingPolicyV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
