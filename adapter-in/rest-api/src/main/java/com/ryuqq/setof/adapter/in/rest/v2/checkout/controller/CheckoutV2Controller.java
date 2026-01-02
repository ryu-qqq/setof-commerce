package com.ryuqq.setof.adapter.in.rest.v2.checkout.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command.CompleteCheckoutV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command.CreateCheckoutV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.response.CheckoutV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.mapper.CheckoutV2ApiMapper;
import com.ryuqq.setof.application.checkout.dto.command.CompleteCheckoutCommand;
import com.ryuqq.setof.application.checkout.dto.command.CreateCheckoutCommand;
import com.ryuqq.setof.application.checkout.dto.response.CheckoutResponse;
import com.ryuqq.setof.application.checkout.port.in.command.CompleteCheckoutUseCase;
import com.ryuqq.setof.application.checkout.port.in.command.CreateCheckoutUseCase;
import com.ryuqq.setof.application.checkout.port.in.query.GetCheckoutUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
 * Checkout V2 Controller
 *
 * <p>체크아웃 관련 API 엔드포인트 (생성/조회/완료)
 *
 * <p>체크아웃 흐름:
 *
 * <ol>
 *   <li>프론트엔드에서 체크아웃 생성 요청 (장바구니 → 결제 페이지)
 *   <li>PG사 결제 처리 (프론트엔드)
 *   <li>PG 결제 완료 후 체크아웃 완료 요청
 * </ol>
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>Controller는 HTTP 처리만 담당
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>Command/Query 분리 (CQRS)
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Checkout", description = "체크아웃 API")
@RestController
@RequestMapping(ApiV2Paths.Checkouts.BASE)
@Validated
public class CheckoutV2Controller {

    private final CreateCheckoutUseCase createCheckoutUseCase;
    private final GetCheckoutUseCase getCheckoutUseCase;
    private final CompleteCheckoutUseCase completeCheckoutUseCase;
    private final CheckoutV2ApiMapper checkoutV2ApiMapper;

    public CheckoutV2Controller(
            CreateCheckoutUseCase createCheckoutUseCase,
            GetCheckoutUseCase getCheckoutUseCase,
            CompleteCheckoutUseCase completeCheckoutUseCase,
            CheckoutV2ApiMapper checkoutV2ApiMapper) {
        this.createCheckoutUseCase = createCheckoutUseCase;
        this.getCheckoutUseCase = getCheckoutUseCase;
        this.completeCheckoutUseCase = completeCheckoutUseCase;
        this.checkoutV2ApiMapper = checkoutV2ApiMapper;
    }

    /**
     * 체크아웃 생성
     *
     * <p>장바구니 아이템을 체크아웃 세션으로 변환합니다. 멱등성 키를 통해 중복 요청을 방지합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param request 체크아웃 생성 요청
     * @return 생성된 체크아웃 정보 (결제 ID 포함)
     */
    @Operation(
            summary = "체크아웃 생성",
            description = "장바구니 아이템을 체크아웃 세션으로 변환합니다. 반환된 paymentId로 PG 결제를 진행합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "체크아웃 생성 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청 (재고 부족, 유효하지 않은 상품 등)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "중복 요청 (동일 멱등성 키)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping
    public ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> createCheckout(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody CreateCheckoutV2ApiRequest request) {

        String memberId = principal.getMemberId();
        CreateCheckoutCommand command = checkoutV2ApiMapper.toCreateCommand(memberId, request);

        CheckoutResponse response = createCheckoutUseCase.createCheckout(command);
        CheckoutV2ApiResponse apiResponse = CheckoutV2ApiResponse.from(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 체크아웃 조회
     *
     * <p>체크아웃 ID로 체크아웃 상세 정보를 조회합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param checkoutId 체크아웃 ID (UUID)
     * @return 체크아웃 정보
     */
    @Operation(summary = "체크아웃 조회", description = "체크아웃 ID로 체크아웃 상세 정보를 조회합니다.")
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
                        description = "접근 권한 없음 (다른 회원의 체크아웃)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "체크아웃 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Checkouts.ID_PATH)
    public ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> getCheckout(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(
                            description = "체크아웃 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440000")
                    @PathVariable
                    String checkoutId) {

        CheckoutResponse response = getCheckoutUseCase.getCheckout(checkoutId);
        CheckoutV2ApiResponse apiResponse = CheckoutV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 체크아웃 완료
     *
     * <p>PG 결제 완료 후 체크아웃을 완료 처리합니다. 결제 승인 금액과 체크아웃 금액을 검증합니다.
     *
     * @param request 체크아웃 완료 요청 (PG 결제 정보)
     * @return 완료된 체크아웃 정보
     */
    @Operation(
            summary = "체크아웃 완료",
            description = "PG 결제 완료 후 체크아웃을 완료 처리합니다. 재고 차감 및 주문 생성이 수행됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "체크아웃 완료 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청 (금액 불일치, 만료된 체크아웃 등)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "결제 정보 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "재고 부족 (결제 취소 필요)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Checkouts.COMPLETE_PATH)
    public ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> completeCheckout(
            @Valid @RequestBody CompleteCheckoutV2ApiRequest request) {

        CompleteCheckoutCommand command = checkoutV2ApiMapper.toCompleteCommand(request);

        completeCheckoutUseCase.completeCheckout(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess(null));
    }
}
