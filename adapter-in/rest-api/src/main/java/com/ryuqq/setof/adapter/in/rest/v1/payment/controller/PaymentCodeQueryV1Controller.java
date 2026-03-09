package com.ryuqq.setof.adapter.in.rest.v1.payment.controller;

import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.PaymentCodeV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.BankV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PayMethodV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.mapper.PaymentCodeV1ApiMapper;
import com.ryuqq.setof.application.commoncode.dto.response.CommonCodeResult;
import com.ryuqq.setof.application.payment.dto.response.PaymentMethodResult;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentMethodsUseCase;
import com.ryuqq.setof.application.payment.port.in.query.GetRefundBankCodesUseCase;
import com.ryuqq.setof.application.payment.port.in.query.GetVBankCodesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PaymentCodeQueryV1Controller - 결제 코드 조회 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * <p>결제 수단, 가상계좌 은행, 환불 은행 코드 목록을 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "결제 코드 조회 V1", description = "결제 수단/은행 코드 조회 V1 Public API")
@RestController
public class PaymentCodeQueryV1Controller {

    private final GetPaymentMethodsUseCase getPaymentMethodsUseCase;
    private final GetVBankCodesUseCase getVBankCodesUseCase;
    private final GetRefundBankCodesUseCase getRefundBankCodesUseCase;
    private final PaymentCodeV1ApiMapper mapper;

    public PaymentCodeQueryV1Controller(
            GetPaymentMethodsUseCase getPaymentMethodsUseCase,
            GetVBankCodesUseCase getVBankCodesUseCase,
            GetRefundBankCodesUseCase getRefundBankCodesUseCase,
            PaymentCodeV1ApiMapper mapper) {
        this.getPaymentMethodsUseCase = getPaymentMethodsUseCase;
        this.getVBankCodesUseCase = getVBankCodesUseCase;
        this.getRefundBankCodesUseCase = getRefundBankCodesUseCase;
        this.mapper = mapper;
    }

    /**
     * 결제 수단 목록 조회 API.
     *
     * <p>GET /api/v1/payment/codes/methods
     *
     * @return 활성 결제 수단 목록
     */
    @Operation(summary = "결제 수단 목록 조회", description = "활성화된 결제 수단 목록을 조회합니다.")
    @GetMapping(PaymentCodeV1Endpoints.METHODS)
    public ResponseEntity<V1ApiResponse<List<PayMethodV1ApiResponse>>> getPaymentMethods() {
        List<PaymentMethodResult> results = getPaymentMethodsUseCase.execute();
        List<PayMethodV1ApiResponse> response = mapper.toPayMethodListResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 가상계좌 환불용 은행 목록 조회 API.
     *
     * <p>GET /api/v1/payment/codes/vbank-refund-accounts
     *
     * @return 가상계좌 은행 코드 목록
     */
    @Operation(summary = "가상계좌 환불 은행 목록 조회", description = "가상계좌 환불용 은행 코드 목록을 조회합니다.")
    @GetMapping(PaymentCodeV1Endpoints.VBANK_REFUND_ACCOUNTS)
    public ResponseEntity<V1ApiResponse<List<BankV1ApiResponse>>> getVBankRefundAccounts() {
        List<CommonCodeResult> results = getVBankCodesUseCase.execute();
        List<BankV1ApiResponse> response = mapper.toBankListResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 환불 은행 목록 조회 API.
     *
     * <p>GET /api/v1/payment/codes/refund-bank-accounts
     *
     * @return 환불 은행 코드 목록
     */
    @Operation(summary = "환불 은행 목록 조회", description = "환불 계좌용 은행 코드 목록을 조회합니다.")
    @GetMapping(PaymentCodeV1Endpoints.REFUND_BANK_ACCOUNTS)
    public ResponseEntity<V1ApiResponse<List<BankV1ApiResponse>>> getRefundBankAccounts() {
        List<CommonCodeResult> results = getRefundBankCodesUseCase.execute();
        List<BankV1ApiResponse> response = mapper.toBankListResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
