package com.ryuqq.setof.adapter.in.rest.v1.pg.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.pg.dto.PortOneWebHookV1ApiRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "PortOne Webhook (Legacy V1)", description = "레거시 PortOne Webhook API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class PortOneWebhookController {

    @Deprecated
    @Operation(summary = "[Legacy] PG사 결제 연동 웹훅", description = "PG사 결제 연동 웹훅")
    @PostMapping(ApiPaths.PortOne.WEBHOOK)
    public ResponseEntity<ApiResponse<Void>> paymentWebHook(@RequestBody PortOneWebHookV1ApiRequest request){
        throw new UnsupportedOperationException("PG사 결제 연동 웹훅 기능은 아직 지원되지 않습니다.");
    }



}
