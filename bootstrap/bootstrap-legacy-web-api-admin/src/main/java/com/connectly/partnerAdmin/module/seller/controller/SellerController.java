package com.connectly.partnerAdmin.module.seller.controller;


import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import com.connectly.partnerAdmin.module.seller.controller.request.CreateSellerSettlementAccount;
import com.connectly.partnerAdmin.module.seller.controller.request.SellerApprovalStatusRequestDto;
import com.connectly.partnerAdmin.module.seller.controller.request.SellerInfoContextRequestDto;
import com.connectly.partnerAdmin.module.seller.controller.request.SellerUpdateDetailRequestDto;
import com.connectly.partnerAdmin.module.seller.core.SellerContext;
import com.connectly.partnerAdmin.module.seller.dto.BusinessValidation;
import com.connectly.partnerAdmin.module.seller.dto.SellerDetailResponse;
import com.connectly.partnerAdmin.module.seller.dto.SellerResponse;
import com.connectly.partnerAdmin.module.seller.filter.SellerFilter;
import com.connectly.partnerAdmin.module.seller.service.SellerCommandService;
import com.connectly.partnerAdmin.module.seller.service.SellerFetchService;
import com.connectly.partnerAdmin.module.seller.service.SellerSettlementAccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_ANY_AUTHORITY_MASTER_SELLER;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_AUTHORITY_MASTER;

@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class SellerController {

    private final SellerFetchService sellerFetchService;
    private final SellerCommandService sellerCommandService;
    private final SellerSettlementAccountValidator sellerSettlementAccountValidator;

    @PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)
    @GetMapping("/seller")
    public ResponseEntity<ApiResponse<SellerContext>> fetchSeller(){
        return ResponseEntity.ok(ApiResponse.success(sellerFetchService.fetchAuthorizedSeller()));
    }

    @PreAuthorize(HAS_AUTHORITY_MASTER)
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<SellerDetailResponse>> fetchSeller(@PathVariable("sellerId") long sellerId){
        return ResponseEntity.ok(ApiResponse.success(sellerFetchService.fetchSellerDetail(sellerId)));
    }

    @PreAuthorize(HAS_AUTHORITY_MASTER)
    @GetMapping("/sellers")
    public ResponseEntity<ApiResponse<CustomPageable<SellerResponse>>> fetchSellers(@ModelAttribute @Validated SellerFilter filter, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(sellerFetchService.fetchSellers(filter, pageable)));
    }

    @GetMapping("/sellers/business-validation")
    public ResponseEntity<ApiResponse<Boolean>> fetchBusinessValidation(@ModelAttribute BusinessValidation businessValidation){
        return ResponseEntity.ok(ApiResponse.success(sellerFetchService.fetchBusinessValidation(businessValidation)));
    }

    @PostMapping("/seller")
    public ResponseEntity<ApiResponse<Long>> insertSeller(@RequestBody SellerInfoContextRequestDto sellerInfoContextRequestDto){
        return ResponseEntity.ok(ApiResponse.success(sellerCommandService.insert(sellerInfoContextRequestDto)));
    }

    @PostMapping("/seller-account")
    public ResponseEntity<ApiResponse<Long>> checkSellerSettlementAccount(@RequestBody CreateSellerSettlementAccount createSellerSettlementAccount){
        sellerSettlementAccountValidator.checkAccount(createSellerSettlementAccount);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PreAuthorize(HAS_AUTHORITY_MASTER)
    @PutMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<Long>> updateSeller(@PathVariable long sellerId, @RequestBody SellerUpdateDetailRequestDto sellerUpdateDetailRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(sellerCommandService.update(sellerId, sellerUpdateDetailRequestDto)));
    }

    @PreAuthorize(HAS_AUTHORITY_MASTER)
    @PutMapping("/seller/approval-status")
    public ResponseEntity<ApiResponse<List<Long>>> updateApprovalStatus(@RequestBody SellerApprovalStatusRequestDto sellerApprovalStatusRequestDto){
        return ResponseEntity.ok(ApiResponse.success(sellerCommandService.updateApprovalStatus(sellerApprovalStatusRequestDto)));
    }

}
