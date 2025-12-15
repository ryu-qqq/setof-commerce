package com.setof.connectly.module.brand.controller;

import com.setof.connectly.module.brand.dto.BrandDisplayDto;
import com.setof.connectly.module.brand.dto.BrandFilter;
import com.setof.connectly.module.brand.service.BrandFindService;
import com.setof.connectly.module.payload.ApiResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class BrandController {

    private final BrandFindService brandFindService;

    public BrandController(BrandFindService brandFindService) {
        this.brandFindService = brandFindService;
    }

    @GetMapping("/brand")
    public ResponseEntity<ApiResponse<List<BrandDisplayDto>>> fetchBrands(
            @ModelAttribute BrandFilter filter) {
        return ResponseEntity.ok(ApiResponse.success(brandFindService.fetchBrands(filter)));
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<ApiResponse<BrandDisplayDto>> fetchBrand(
            @PathVariable("brandId") long brandId) {
        return ResponseEntity.ok(ApiResponse.success(brandFindService.fetchBrand(brandId)));
    }
}
