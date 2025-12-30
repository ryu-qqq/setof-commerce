package com.ryuqq.setof.adapter.in.rest.admin.v1.payment.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.payment.dto.response.PaymentAdminV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.payment.mapper.PaymentAdminV1ApiMapper;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Payment Controller (Admin Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Payment 엔드포인트 (Admin)
 *
 * <p>Strangler Fig 패턴에 따라 V2 UseCase를 재사용하면서 V1 호환 응답을 제공합니다.
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Payment Admin (Legacy V1)", description = "레거시 Payment Admin API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
@PreAuthorize("@access.orgAdminOrHigher()")
public class PaymentAdminV1Controller {

    private final GetPaymentUseCase getPaymentUseCase;
    private final PaymentAdminV1ApiMapper paymentAdminV1ApiMapper;

    public PaymentAdminV1Controller(
            GetPaymentUseCase getPaymentUseCase, PaymentAdminV1ApiMapper paymentAdminV1ApiMapper) {
        this.getPaymentUseCase = getPaymentUseCase;
        this.paymentAdminV1ApiMapper = paymentAdminV1ApiMapper;
    }

    /**
     * 결제 상세 조회 (Legacy ID 사용)
     *
     * <p>Legacy Long ID를 사용하여 결제 상세 정보를 조회합니다. V2 UseCase를 내부적으로 호출하고 결과를 V1 형식으로 변환합니다.
     *
     * @param paymentId 결제 ID (Legacy Long)
     * @return V1 결제 상세 응답
     */
    @Deprecated
    @Operation(summary = "[Legacy] 결제 상세 조회", description = "특정 결제의 상세 정보를 조회합니다.")
    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<V1ApiResponse<PaymentAdminV1ApiResponse>> getPayment(
            @Parameter(description = "결제 ID (Legacy)", example = "12345") @PathVariable("paymentId")
                    long paymentId) {

        PaymentResponse paymentResponse = getPaymentUseCase.getPaymentByLegacyId(paymentId);

        // Note: PaymentResponse에 orderId가 없으므로 Order 정보 없이 응답
        PaymentAdminV1ApiResponse response =
                paymentAdminV1ApiMapper.toPaymentAdminV1Response(paymentResponse);

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
