package com.setof.connectly.module.portone.controller;

import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.payment.service.pay.PayService;
import com.setof.connectly.module.portone.dto.PortOneWebHookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PortOneController {
    private final PayService payService;

    @PostMapping("/portone/webhook")
    public ResponseEntity<ApiResponse<PortOneWebHookDto>> paymentWebHook(
            @RequestBody PortOneWebHookDto portOneWebHookDto) {
        return ResponseEntity.ok(ApiResponse.success(payService.paymentWebHook(portOneWebHookDto)));
    }

}
