package com.ryuqq.setof.adapter.in.rest.v2.payment.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.payment.dto.command.ApprovePaymentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.payment.dto.response.PaymentV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.payment.mapper.PaymentV2ApiMapper;
import com.ryuqq.setof.application.payment.dto.command.ApprovePaymentCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import com.ryuqq.setof.application.payment.port.in.command.ApprovePaymentUseCase;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Payment V2 Controller
 *
 * <p>결제 관련 API 엔드포인트 (조회/승인)
 *
 * <p>결제 흐름:
 *
 * <ol>
 *   <li>체크아웃 생성 시 결제 정보(paymentId) 생성
 *   <li>프론트엔드에서 PG사 결제 진행
 *   <li>PG 결제 완료 후 결제 승인 API 호출
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
@Tag(name = "Payment", description = "결제 API")
@RestController
@RequestMapping(ApiV2Paths.Payments.BASE)
@Validated
public class PaymentV2Controller {

    private final ApprovePaymentUseCase approvePaymentUseCase;
    private final GetPaymentUseCase getPaymentUseCase;
    private final PaymentV2ApiMapper paymentV2ApiMapper;

    public PaymentV2Controller(
            ApprovePaymentUseCase approvePaymentUseCase,
            GetPaymentUseCase getPaymentUseCase,
            PaymentV2ApiMapper paymentV2ApiMapper) {
        this.approvePaymentUseCase = approvePaymentUseCase;
        this.getPaymentUseCase = getPaymentUseCase;
        this.paymentV2ApiMapper = paymentV2ApiMapper;
    }

    /**
     * 결제 조회
     *
     * <p>결제 ID로 결제 상세 정보를 조회합니다.
     *
     * @param paymentId 결제 ID (UUID)
     * @return 결제 정보
     */
    @Operation(summary = "결제 조회", description = "결제 ID로 결제 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "결제 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Payments.ID_PATH)
    public ResponseEntity<ApiResponse<PaymentV2ApiResponse>> getPayment(
            @Parameter(
                            description = "결제 ID (UUID)",
                            example = "550e8400-e29b-41d4-a716-446655440000")
                    @PathVariable
                    String paymentId) {

        PaymentResponse response = getPaymentUseCase.getPayment(paymentId);
        PaymentV2ApiResponse apiResponse = PaymentV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 체크아웃 ID로 결제 조회
     *
     * <p>체크아웃 ID로 연결된 결제 정보를 조회합니다.
     *
     * @param checkoutId 체크아웃 ID (UUID)
     * @return 결제 정보
     */
    @Operation(summary = "체크아웃 ID로 결제 조회", description = "체크아웃 ID로 연결된 결제 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "결제 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Payments.BY_CHECKOUT_PATH)
    public ResponseEntity<ApiResponse<PaymentV2ApiResponse>> getPaymentByCheckoutId(
            @Parameter(
                            description = "체크아웃 ID (UUID)",
                            example = "660e8400-e29b-41d4-a716-446655440001")
                    @PathVariable
                    String checkoutId) {

        PaymentResponse response = getPaymentUseCase.getPaymentByCheckoutId(checkoutId);
        PaymentV2ApiResponse apiResponse = PaymentV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 결제 승인
     *
     * <p>PG사 결제 완료 후 결제를 승인 처리합니다. 승인 금액과 요청 금액을 검증합니다.
     *
     * @param request 결제 승인 요청
     * @return 승인된 결제 정보
     */
    @Operation(summary = "결제 승인", description = "PG사 결제 완료 후 결제를 승인 처리합니다. 승인 금액과 요청 금액을 검증합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "승인 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청 (금액 불일치 등)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "결제 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "이미 승인됨 또는 취소됨",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Payments.APPROVE_PATH)
    public ResponseEntity<ApiResponse<PaymentV2ApiResponse>> approvePayment(
            @Valid @RequestBody ApprovePaymentV2ApiRequest request) {

        ApprovePaymentCommand command = paymentV2ApiMapper.toApproveCommand(request);

        PaymentResponse response = approvePaymentUseCase.approvePayment(command);
        PaymentV2ApiResponse apiResponse = PaymentV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
