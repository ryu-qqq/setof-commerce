package com.setof.connectly.module.seller.controller;

import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.seller.dto.SellerInfo;
import com.setof.connectly.module.seller.service.SellerFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/seller")
@RestController
@RequiredArgsConstructor
public class SellerController {

    private final SellerFindService sellerFindService;

    @GetMapping("/{sellerId}")
    public ResponseEntity<ApiResponse<SellerInfo>> fetchSeller(
            @PathVariable("sellerId") long sellerId) {
        return ResponseEntity.ok(ApiResponse.success(sellerFindService.fetchSeller(sellerId)));
    }
}
