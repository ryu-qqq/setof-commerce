package com.setof.connectly.module.product.controller;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.product.dto.ProductGroupResponse;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.filter.ProductFilter;
import com.setof.connectly.module.product.service.group.fetch.ProductGroupFindService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ProductController {
    private final ProductGroupFindService productGroupFindService;

    @GetMapping("/product/group/{productGroupId}")
    public ResponseEntity<ApiResponse<ProductGroupResponse>> fetchProductGroup(
            @PathVariable("productGroupId") long productGroupId) {
        return ResponseEntity.ok(
                ApiResponse.success(productGroupFindService.fetchProductGroup(productGroupId)));
    }

    @GetMapping("/products/group/recent")
    public ResponseEntity<ApiResponse<List<ProductGroupThumbnail>>> fetchProductGroupLikes(
            @RequestParam List<Long> productGroupIds) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        productGroupFindService.fetchProductGroupRecent(productGroupIds)));
    }

    @GetMapping("/products/group")
    public ResponseEntity<ApiResponse<CustomSlice<ProductGroupThumbnail>>> fetchProductGroups(
            @ModelAttribute ProductFilter filter, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(productGroupFindService.fetchProductGroups(filter, pageable)));
    }

    @GetMapping("/product/group/brand/{brandId}")
    public ResponseEntity<ApiResponse<List<ProductGroupThumbnail>>> fetchProductGroupWithBrand(
            @PathVariable("brandId") long brandId, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        productGroupFindService.fetchProductGroupWithBrand(brandId, pageable)));
    }

    @GetMapping("/product/group/seller/{sellerId}")
    public ResponseEntity<ApiResponse<List<ProductGroupThumbnail>>> fetchProductGroupWithSeller(
            @PathVariable("sellerId") long sellerId, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        productGroupFindService.fetchProductGroupWithSeller(sellerId, pageable)));
    }
}
