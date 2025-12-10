package com.ryuqq.setof.adapter.in.rest.v1.payment.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.SliceApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.command.CreatePaymentInCartV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.command.CreatePaymentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.command.FailPaymentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.query.PaymentFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.BankV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.FailPaymentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PayMethodV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentGatewayRequestV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentResultV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * V1 Payment Controller (Legacy)
 *
 * <p>
 * 레거시 API 호환을 위한 V1 Payment 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Payment (Legacy V1)", description = "레거시 Payment API - V2로 마이그레이션 권장")
@RestController
@RequestMapping
@Validated
@Deprecated
public class PaymentV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] 결제수단 목록 조회", description = "사용 가능한 결제수단 목록을 조회합니다.")
    @GetMapping(ApiPaths.Payment.METHODS)
    public ResponseEntity<ApiResponse<List<PayMethodV1ApiResponse>>> getPaymentMethods(
            @AuthenticationPrincipal MemberPrincipal principal) {

        throw new UnsupportedOperationException("결제수단 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 가상계좌 은행 목록 조회", description = "가상계좌 은행 목록을 조회합니다.")
    @GetMapping(ApiPaths.Payment.VBANK)
    public ResponseEntity<ApiResponse<List<BankV1ApiResponse>>> getVBankRefundAccounts(
            @AuthenticationPrincipal MemberPrincipal principal) {

        throw new UnsupportedOperationException("가상계좌 은행 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 환불계좌 은행 목록 조회", description = "환불계좌 은행 목록을 조회합니다.")
    @GetMapping(ApiPaths.Payment.REFUND_BANK)
    public ResponseEntity<ApiResponse<List<BankV1ApiResponse>>> getRefundBankAccounts(
            @AuthenticationPrincipal MemberPrincipal principal) {

        throw new UnsupportedOperationException("환불계좌 은행 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 결제 목록 조회", description = "결제 목록을 조회합니다.")
    @GetMapping(ApiPaths.Payment.LIST)
    public ResponseEntity<ApiResponse<SliceApiResponse<PaymentV1ApiResponse>>> getPayments(
            @AuthenticationPrincipal MemberPrincipal principal,
            @ModelAttribute PaymentFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("결제 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 결제 상세 조회", description = "특정 결제의 상세 정보를 조회합니다.")
    @GetMapping(ApiPaths.Payment.DETAIL)
    public ResponseEntity<ApiResponse<PaymentV1ApiResponse>> getPayment(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("paymentId") long paymentId) {

        throw new UnsupportedOperationException("결제 상세 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 결제 상태 조회", description = "특정 결제의 상태를 조회합니다.")
    @GetMapping(ApiPaths.Payment.STATUS)
    public ResponseEntity<ApiResponse<PaymentResultV1ApiResponse>> getPayResult(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("paymentId") long paymentId) {

        throw new UnsupportedOperationException("결제 상태 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 일반 결제 요청", description = "일반 결제를 요청합니다.")
    @PostMapping(ApiPaths.Payment.PAY)
    public ResponseEntity<ApiResponse<PaymentGatewayRequestV1ApiResponse>> doPay(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody CreatePaymentV1ApiRequest createPayment) {

        throw new UnsupportedOperationException("일반 결제 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 장바구니 결제 요청", description = "장바구니에서 결제를 요청합니다.")
    @PostMapping(ApiPaths.Payment.PAY_CART)
    public ResponseEntity<ApiResponse<PaymentGatewayRequestV1ApiResponse>> doPayInCart(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody CreatePaymentInCartV1ApiRequest createPaymentInCart) {

        throw new UnsupportedOperationException("장바구니 결제 기능은 아직 지원되지 않습니다.");
    }


    @Deprecated
    @Operation(summary = "[Legacy] 결제 실패 처리", description = "결제 실패를 처리합니다.")
    @PostMapping(ApiPaths.Payment.FAILURE)
    public ResponseEntity<ApiResponse<FailPaymentV1ApiResponse>> payFailure(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody FailPaymentV1ApiRequest failPayment) {

        throw new UnsupportedOperationException("결제 실패 처리 기능은 아직 지원되지 않습니다.");
    }
}
