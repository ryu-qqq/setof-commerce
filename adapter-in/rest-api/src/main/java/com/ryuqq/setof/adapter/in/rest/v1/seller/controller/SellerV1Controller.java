package com.ryuqq.setof.adapter.in.rest.v1.seller.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

@Deprecated
@RestController
@RequestMapping(ApiPaths.Seller.BASE)
public class SellerV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] 셀러 정보 조회", description = "셀러 정보를 조회합니다.")
    @GetMapping(ApiPaths.Seller.DETAIL)
    public ResponseEntity<ApiResponse<SellerV1ApiResponse>> fetchSeller(
            @PathVariable("sellerId") long sellerId) {
        throw new UnsupportedOperationException("셀러 정보 조회 기능은 아직 지원되지 않습니다.");
    }

}
