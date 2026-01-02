package com.connectly.partnerAdmin.module.discount.controller;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.dto.DiscountTargetResponseDto;
import com.connectly.partnerAdmin.module.discount.dto.DiscountUseHistoryDto;
import com.connectly.partnerAdmin.module.discount.dto.query.*;
import com.connectly.partnerAdmin.module.discount.filter.DiscountFilter;
import com.connectly.partnerAdmin.module.discount.entity.DiscountTarget;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.connectly.partnerAdmin.module.discount.service.DiscountQueryService;
import com.connectly.partnerAdmin.module.discount.service.DiscountTargetQueryService;
import com.connectly.partnerAdmin.module.discount.service.fetch.DiscountFetchService;
import com.connectly.partnerAdmin.module.discount.service.fetch.DiscountTargetFetchService;
import com.connectly.partnerAdmin.module.discount.service.fetch.DiscountHistoryFetchService;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_AUTHORITY_MASTER;

@PreAuthorize(HAS_AUTHORITY_MASTER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountQueryService discountQueryService;
    private final DiscountTargetQueryService discountTargetQueryService;
    private final DiscountFetchService discountFetchService;
    private final DiscountTargetFetchService discountTargetFetchService;
    private final DiscountHistoryFetchService discountHistoryFetchService;


    @GetMapping("/discount/{discountPolicyId}")
    public ResponseEntity<ApiResponse<DiscountPolicyResponseDto>> fetchDiscountPolicy(@PathVariable("discountPolicyId") long discountPolicyId){
        return ResponseEntity.ok(ApiResponse.success(discountFetchService.fetchDiscountPolicy(discountPolicyId)));
    }

    @GetMapping("/discounts")
    public ResponseEntity<ApiResponse<CustomPageable<DiscountPolicyResponseDto>>> getDiscountPolies(@ModelAttribute @Validated DiscountFilter filter, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(discountFetchService.fetchDiscountPolicies(filter, pageable)));
    }

    @PostMapping("/discount")
    public ResponseEntity<ApiResponse<DiscountPolicyResponseDto>> createDiscountPolicy(@Valid @RequestBody CreateDiscount createDiscount){
        return ResponseEntity.ok(ApiResponse.success(discountQueryService.createDiscount(createDiscount)));
    }

    @PutMapping("/discount/{discountPolicyId}")
    public ResponseEntity<ApiResponse<DiscountPolicyResponseDto>> updateDiscountPolicy(@PathVariable("discountPolicyId") long discountPolicyId, @Valid @RequestBody UpdateDiscount updateDiscount) {
        return ResponseEntity.ok(ApiResponse.success(discountQueryService.updateDiscount(discountPolicyId, updateDiscount)));
    }

    @PatchMapping("/discounts")
    public ResponseEntity<ApiResponse<List<DiscountPolicyResponseDto>>> updateDiscountPolicyUsageStatus(@RequestBody UpdateUseDiscount updateUseDiscount){
        return ResponseEntity.ok(ApiResponse.success(discountQueryService.updateDiscountUseYn(updateUseDiscount)));
    }


    @PostMapping("/discounts/excel")
    public ResponseEntity<ApiResponse<List<DiscountPolicyResponseDto>>> createDiscountsFromExcelUpload(@Valid @RequestBody List<CreateDiscountFromExcel> createDiscountFromExcels){
        return ResponseEntity.ok(ApiResponse.success(discountQueryService.createDiscountFromExcel(createDiscountFromExcels)));
    }

    @GetMapping("/discount/{discountPolicyId}/targets")
    public ResponseEntity<ApiResponse<Page<? extends DiscountTargetResponseDto>>> getDiscountTargets(@PathVariable("discountPolicyId") long discountPolicyId,
                                                                                           @RequestParam IssueType issueType, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(discountTargetFetchService.fetchDiscountTargets(discountPolicyId, issueType, pageable)));
    }

    @PostMapping("/discount/{discountPolicyId}/targets")
    public ResponseEntity<ApiResponse<List<DiscountTarget>>> createDiscountTargets(@PathVariable("discountPolicyId") long discountPolicyId, @Valid @RequestBody CreateDiscountTarget createDiscountTarget){
        return ResponseEntity.ok(ApiResponse.success(discountTargetQueryService.createDiscountTargets(discountPolicyId, createDiscountTarget)));
    }

    @PutMapping("/discount/{discountPolicyId}/targets")
    public ResponseEntity<ApiResponse<List<DiscountTarget>>> updateDiscountTargets(@PathVariable("discountPolicyId") long discountPolicyId, @Valid @RequestBody CreateDiscountTarget createDiscountTarget){
        return ResponseEntity.ok(ApiResponse.success(discountTargetQueryService.updateDiscountTargets(discountPolicyId, createDiscountTarget)));
    }

    @GetMapping("/discounts/history")
    public ResponseEntity<ApiResponse<Page<DiscountPolicyResponseDto>>> getDiscountHistories(@ModelAttribute @Validated DiscountFilter discountFilter, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(discountHistoryFetchService.fetchDiscountPolicyHistories(discountFilter, pageable)));
    }

    @GetMapping("/discount/history/{discountPolicyId}/use")
    public ResponseEntity<ApiResponse<Page<DiscountUseHistoryDto>>> getDiscountUseHistories(@PathVariable("discountPolicyId") long discountPolicyId, @ModelAttribute DiscountFilter discountFilterDto, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(discountHistoryFetchService.fetchDiscountUseHistories(discountPolicyId, discountFilterDto, pageable)));
    }

}
