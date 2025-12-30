package com.ryuqq.setof.adapter.in.rest.v2.claim.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.claim.dto.command.RequestClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.claim.dto.response.ClaimV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.claim.mapper.ClaimV2ApiMapper;
import com.ryuqq.setof.application.claim.dto.command.RequestClaimCommand;
import com.ryuqq.setof.application.claim.dto.response.ClaimResponse;
import com.ryuqq.setof.application.claim.port.in.command.RequestClaimUseCase;
import com.ryuqq.setof.application.claim.port.in.query.GetClaimUseCase;
import com.ryuqq.setof.application.claim.port.in.query.GetClaimsByOrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Claim V2 Controller (Customer)
 *
 * <p>고객용 클레임(취소/반품/교환/환불) API
 *
 * <p>클레임 상태 흐름:
 *
 * <pre>
 * REQUESTED → APPROVED → IN_PROGRESS → COMPLETED
 *    ↓           ↓
 * CANCELLED  REJECTED
 * </pre>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Claim", description = "클레임 API (고객)")
@RestController
@RequestMapping
@Validated
public class ClaimV2Controller {

    private final RequestClaimUseCase requestClaimUseCase;
    private final GetClaimUseCase getClaimUseCase;
    private final GetClaimsByOrderUseCase getClaimsByOrderUseCase;
    private final ClaimV2ApiMapper claimV2ApiMapper;

    public ClaimV2Controller(
            RequestClaimUseCase requestClaimUseCase,
            GetClaimUseCase getClaimUseCase,
            GetClaimsByOrderUseCase getClaimsByOrderUseCase,
            ClaimV2ApiMapper claimV2ApiMapper) {
        this.requestClaimUseCase = requestClaimUseCase;
        this.getClaimUseCase = getClaimUseCase;
        this.getClaimsByOrderUseCase = getClaimsByOrderUseCase;
        this.claimV2ApiMapper = claimV2ApiMapper;
    }

    /**
     * 클레임 요청
     *
     * <p>취소/반품/교환/부분환불을 요청합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param request 클레임 요청 정보
     * @return 생성된 클레임 ID
     */
    @Operation(summary = "클레임 요청", description = "취소/반품/교환/부분환불을 요청합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "클레임 생성 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "클레임 불가 상태 (이미 클레임 진행 중 등)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Claims.BASE)
    public ResponseEntity<ApiResponse<ClaimV2ApiResponse>> requestClaim(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody RequestClaimV2ApiRequest request) {

        RequestClaimCommand command = claimV2ApiMapper.toCommand(request);
        String claimId = requestClaimUseCase.request(command);

        ClaimResponse response = getClaimUseCase.getByClaimId(claimId);
        ClaimV2ApiResponse apiResponse = ClaimV2ApiResponse.from(response);

        return ResponseEntity.created(URI.create(ApiV2Paths.Claims.BASE + "/" + claimId))
                .body(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 클레임 조회
     *
     * @param principal 인증된 사용자 정보
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
                        responseCode = "403",
                        description = "접근 권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "클레임 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Claims.BASE + ApiV2Paths.Claims.ID_PATH)
    public ResponseEntity<ApiResponse<ClaimV2ApiResponse>> getClaim(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(
                            description = "클레임 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440001")
                    @PathVariable
                    String claimId) {

        // TODO: memberId 검증 로직 추가 필요 (UseCase에서 처리)
        ClaimResponse response = getClaimUseCase.getByClaimId(claimId);
        ClaimV2ApiResponse apiResponse = ClaimV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 주문별 클레임 목록 조회
     *
     * @param principal 인증된 사용자 정보
     * @param orderId 주문 ID (UUID)
     * @return 클레임 목록
     */
    @Operation(summary = "주문별 클레임 목록 조회", description = "특정 주문에 대한 모든 클레임을 조회합니다.")
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
                        responseCode = "403",
                        description = "접근 권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Orders.BASE + ApiV2Paths.Orders.ID_PATH + ApiV2Paths.Orders.CLAIMS_PATH)
    public ResponseEntity<ApiResponse<List<ClaimV2ApiResponse>>> getClaimsByOrder(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(
                            description = "주문 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440000")
                    @PathVariable
                    String orderId) {

        // TODO: memberId 검증 로직 추가 필요 (UseCase에서 처리)
        List<ClaimResponse> responses = getClaimsByOrderUseCase.getByOrderId(orderId);
        List<ClaimV2ApiResponse> apiResponses =
                responses.stream().map(ClaimV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }
}
