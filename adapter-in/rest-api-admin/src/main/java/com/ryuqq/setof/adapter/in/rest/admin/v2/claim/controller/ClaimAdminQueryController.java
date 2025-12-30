package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.response.ClaimAdminV2ApiResponse;
import com.ryuqq.setof.application.claim.dto.query.GetAdminClaimsQuery;
import com.ryuqq.setof.application.claim.dto.response.ClaimResponse;
import com.ryuqq.setof.application.claim.port.in.query.GetAdminClaimsUseCase;
import com.ryuqq.setof.application.claim.port.in.query.GetClaimUseCase;
import com.ryuqq.setof.application.common.response.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Claim Admin Query Controller
 *
 * <p>클레임 조회 API 엔드포인트 (CQRS Query 분리)
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
@Tag(name = "Admin - Claim", description = "클레임 관리 API (관리자)")
@RestController
@RequestMapping(ApiV2Paths.Claims.BASE)
@Validated
public class ClaimAdminQueryController {

    private final GetClaimUseCase getClaimUseCase;
    private final GetAdminClaimsUseCase getAdminClaimsUseCase;

    public ClaimAdminQueryController(
            GetClaimUseCase getClaimUseCase, GetAdminClaimsUseCase getAdminClaimsUseCase) {
        this.getClaimUseCase = getClaimUseCase;
        this.getAdminClaimsUseCase = getAdminClaimsUseCase;
    }

    /**
     * 클레임 목록 조회
     *
     * @param sellerId 셀러 ID (선택)
     * @param memberId 회원 ID (선택)
     * @param claimStatuses 클레임 상태 목록 (선택)
     * @param claimTypes 클레임 유형 목록 (선택)
     * @param searchKeyword 검색어 (선택)
     * @param startDate 시작 일시 (선택)
     * @param endDate 종료 일시 (선택)
     * @param lastClaimId 마지막 클레임 ID (커서)
     * @param pageSize 페이지 크기
     * @return 클레임 목록 (Slice)
     */
    @Operation(summary = "클레임 목록 조회", description = "클레임 목록을 조회합니다. 커서 기반 페이지네이션을 지원합니다.")
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
    public ResponseEntity<ApiResponse<SliceResponse<ClaimAdminV2ApiResponse>>> getClaims(
            @Parameter(description = "셀러 ID") @RequestParam(required = false) Long sellerId,
            @Parameter(description = "회원 ID") @RequestParam(required = false) Long memberId,
            @Parameter(description = "클레임 상태 목록") @RequestParam(required = false)
                    List<String> claimStatuses,
            @Parameter(description = "클레임 유형 목록") @RequestParam(required = false)
                    List<String> claimTypes,
            @Parameter(description = "검색어 (클레임번호, 주문ID)") @RequestParam(required = false)
                    String searchKeyword,
            @Parameter(description = "시작 일시") @RequestParam(required = false) Instant startDate,
            @Parameter(description = "종료 일시") @RequestParam(required = false) Instant endDate,
            @Parameter(description = "마지막 클레임 ID (커서)") @RequestParam(required = false)
                    String lastClaimId,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "20") int pageSize) {

        GetAdminClaimsQuery query =
                GetAdminClaimsQuery.of(
                        sellerId,
                        memberId,
                        claimStatuses,
                        claimTypes,
                        searchKeyword,
                        startDate,
                        endDate,
                        lastClaimId,
                        pageSize);

        SliceResponse<ClaimResponse> result = getAdminClaimsUseCase.getClaims(query);

        List<ClaimAdminV2ApiResponse> apiResponses =
                result.content().stream().map(ClaimAdminV2ApiResponse::from).toList();

        SliceResponse<ClaimAdminV2ApiResponse> apiResult =
                SliceResponse.of(
                        apiResponses, result.size(), result.hasNext(), result.nextCursor());

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResult));
    }

    /**
     * 클레임 조회
     *
     * @param claimId 클레임 ID (UUID)
     * @return 클레임 정보
     */
    @Operation(summary = "클레임 조회", description = "클레임 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "클레임 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Claims.ID_PATH)
    public ResponseEntity<ApiResponse<ClaimAdminV2ApiResponse>> getClaim(
            @Parameter(
                            description = "클레임 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440001")
                    @PathVariable
                    String claimId) {

        ClaimResponse response = getClaimUseCase.getByClaimId(claimId);
        ClaimAdminV2ApiResponse apiResponse = ClaimAdminV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
