package com.setof.connectly.module.payment.controller;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.payment.dto.filter.PaymentFilter;
import com.setof.connectly.module.payment.dto.payment.*;
import com.setof.connectly.module.payment.dto.paymethod.PayMethodResponse;
import com.setof.connectly.module.payment.dto.refund.BankResponse;
import com.setof.connectly.module.payment.service.bill.fetch.PaymentBillFindService;
import com.setof.connectly.module.payment.service.method.PaymentMethodFindService;
import com.setof.connectly.module.payment.service.pay.PayService;
import com.setof.connectly.module.payment.service.pay.fetch.PaymentFindService;
import com.setof.connectly.module.payment.service.vbank.fetch.VBankFindService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    private final PaymentMethodFindService paymentMethodFindService;
    private final PaymentFindService paymentFindService;
    private final PaymentBillFindService paymentBillFindService;
    private final VBankFindService vBankFindService;
    private final PayService payService;

    @GetMapping("/payment/payment-method")
    public ResponseEntity<ApiResponse<List<PayMethodResponse>>> fetchPaymentMethods() {
        return ResponseEntity.ok(ApiResponse.success(paymentMethodFindService.fetchPayMethods()));
    }

    @GetMapping("/payment/vbank")
    public ResponseEntity<ApiResponse<List<BankResponse>>> fetchVBankRefundAccounts() {
        return ResponseEntity.ok(ApiResponse.success(vBankFindService.fetchVBanks()));
    }

    @GetMapping("/payment/refund-bank")
    public ResponseEntity<ApiResponse<List<BankResponse>>> fetchRefundBankAccounts() {
        return ResponseEntity.ok(ApiResponse.success(vBankFindService.fetchRefundBanks()));
    }

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<CustomSlice<PaymentResponse>>> fetchPayments(
            @ModelAttribute PaymentFilter filter, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(paymentFindService.fetchPayments(filter, pageable)));
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> fetchPayment(
            @PathVariable("paymentId") long paymentId) {
        return ResponseEntity.ok(ApiResponse.success(paymentFindService.fetchPayment(paymentId)));
    }

    @GetMapping("/payment/{paymentId}/status")
    public ResponseEntity<ApiResponse<PaymentResult>> fetchPayResult(
            @PathVariable("paymentId") long paymentId) {
        return ResponseEntity.ok(
                ApiResponse.success(paymentBillFindService.fetchPaymentResult(paymentId)));
    }

    @PostMapping("/payment")
    public ResponseEntity<ApiResponse<PaymentGatewayRequestDto>> doPay(
            @RequestBody @Validated CreatePayment createPayment) {
        return ResponseEntity.ok(ApiResponse.success(payService.pay(createPayment)));
    }

    @PostMapping("/payment/cart")
    public ResponseEntity<ApiResponse<PaymentGatewayRequestDto>> doPayInCart(
            @RequestBody @Validated CreatePaymentInCart createPaymentInCart) {
        return ResponseEntity.ok(ApiResponse.success(payService.pay(createPaymentInCart)));
    }

    @PostMapping("/payment/mileage")
    public ResponseEntity<ApiResponse<PaymentGatewayRequestDto>> doPayOnlyUseMileage(
            @RequestBody @Validated CreatePaymentInCart createPaymentInCart) {
        return ResponseEntity.ok(ApiResponse.success(payService.pay(createPaymentInCart)));
    }

    @PostMapping("/payment/failure")
    public ResponseEntity<ApiResponse<FailPaymentResponse>> payFailure(
            @RequestBody @Validated FailPayment failPayment) {
        return ResponseEntity.ok(ApiResponse.success(payService.payFailed(failPayment)));
    }
}
