package com.ryuqq.setof.adapter.in.rest.v1.payment.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.PaymentV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request.SearchPaymentsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentResultV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.mapper.PaymentV1ApiMapper;
import com.ryuqq.setof.application.payment.dto.query.PaymentSearchParams;
import com.ryuqq.setof.application.payment.dto.response.PaymentDetailResult;
import com.ryuqq.setof.application.payment.dto.response.PaymentSliceResult;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentDetailUseCase;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentResultUseCase;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * PaymentQueryV1Controller - 결제 조회 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: @Transactional 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "결제 조회 V1", description = "결제 조회 V1 Public API (인증 필요)")
@RestController
public class PaymentQueryV1Controller {

    private final GetPaymentResultUseCase getPaymentResultUseCase;
    private final GetPaymentsUseCase getPaymentsUseCase;
    private final GetPaymentDetailUseCase getPaymentDetailUseCase;
    private final PaymentV1ApiMapper paymentMapper;

    public PaymentQueryV1Controller(
            GetPaymentResultUseCase getPaymentResultUseCase,
            GetPaymentsUseCase getPaymentsUseCase,
            GetPaymentDetailUseCase getPaymentDetailUseCase,
            PaymentV1ApiMapper paymentMapper) {
        this.getPaymentResultUseCase = getPaymentResultUseCase;
        this.getPaymentsUseCase = getPaymentsUseCase;
        this.getPaymentDetailUseCase = getPaymentDetailUseCase;
        this.paymentMapper = paymentMapper;
    }

    /**
     * 결제 성공 여부 조회 API.
     *
     * <p>GET /api/v1/payment/{paymentId}/status
     *
     * <p>PG사(PortOne) API를 통해 실제 결제 상태를 확인합니다.
     *
     * @param paymentId 결제 ID
     * @return 결제 성공 여부
     */
    @Operation(summary = "결제 성공 여부 조회", description = "PG사 API를 통해 결제 성공 여부를 확인합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(PaymentV1Endpoints.PAYMENT_STATUS)
    public ResponseEntity<V1ApiResponse<PaymentResultV1ApiResponse>> getPaymentResult(
            @PathVariable long paymentId) {
        boolean success = getPaymentResultUseCase.execute(paymentId);
        return ResponseEntity.ok(V1ApiResponse.success(new PaymentResultV1ApiResponse(success)));
    }

    /**
     * 결제 목록 조회 API (커서 페이징).
     *
     * <p>GET /api/v1/payments
     *
     * <p>날짜 범위, 주문 상태 필터를 적용한 커서 기반 페이징 조회입니다.
     *
     * @param userId 인증된 사용자 ID
     * @param request 결제 목록 검색 요청
     * @return 결제 슬라이스 응답
     */
    @Operation(summary = "결제 목록 조회", description = "커서 기반 페이징으로 결제 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(PaymentV1Endpoints.PAYMENTS)
    public ResponseEntity<V1ApiResponse<PaymentSliceV1ApiResponse>> getPayments(
            @AuthenticatedUserId Long userId,
            @ParameterObject @Valid SearchPaymentsCursorV1ApiRequest request) {
        PaymentSearchParams params = paymentMapper.toSearchParams(userId, request);
        PaymentSliceResult result = getPaymentsUseCase.execute(params);
        PaymentSliceV1ApiResponse response = paymentMapper.toPaymentSliceResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 결제 단건 상세 조회 API.
     *
     * <p>GET /api/v1/payment/{paymentId}
     *
     * <p>본인 확인 후 결제 전체 상세 정보를 반환합니다.
     *
     * @param userId 인증된 사용자 ID
     * @param paymentId 결제 ID
     * @return 결제 상세 응답
     */
    @Operation(summary = "결제 단건 상세 조회", description = "결제 ID로 결제 전체 상세 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "403",
                description = "접근 권한 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "결제 정보를 찾을 수 없음")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(PaymentV1Endpoints.PAYMENT_DETAIL)
    public ResponseEntity<V1ApiResponse<PaymentDetailV1ApiResponse>> getPaymentDetail(
            @AuthenticatedUserId Long userId,
            @Parameter(description = "결제 ID", example = "12345") @PathVariable long paymentId) {
        PaymentDetailResult result = getPaymentDetailUseCase.execute(paymentId, userId);
        PaymentDetailV1ApiResponse response = paymentMapper.toPaymentDetailResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
