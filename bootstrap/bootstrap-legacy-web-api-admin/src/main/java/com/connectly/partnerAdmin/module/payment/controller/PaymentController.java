package com.connectly.partnerAdmin.module.payment.controller;

import com.connectly.partnerAdmin.module.payload.ApiResponse;
import com.connectly.partnerAdmin.module.payment.dto.PaymentResponse;
import com.connectly.partnerAdmin.module.payment.service.PaymentFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_ANY_AUTHORITY_MASTER_SELLER;

@PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentFetchService paymentFetchService;

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> fetchPayment(@PathVariable("paymentId") long paymentId){
        return ResponseEntity.ok(ApiResponse.success(paymentFetchService.fetchPayment(paymentId)));
    }

}
