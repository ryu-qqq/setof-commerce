package com.connectly.partnerAdmin.module.brand.controller;

import com.connectly.partnerAdmin.module.brand.core.BrandMappingInfo;
import com.connectly.partnerAdmin.module.brand.core.ExtendedBrandContext;
import com.connectly.partnerAdmin.module.brand.filter.BrandFilter;
import com.connectly.partnerAdmin.module.brand.service.BrandFetchService;
import com.connectly.partnerAdmin.module.brand.service.BrandMappingManager;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_ANY_AUTHORITY_MASTER_SELLER;

@PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)
@RequestMapping(BASE_END_POINT_V1)
@RestController
@RequiredArgsConstructor
public class BrandController {

    private final BrandFetchService brandFetchService;
    private final BrandMappingManager brandMappingManager;

    @GetMapping("/brands")
    public ResponseEntity<ApiResponse<CustomPageable<ExtendedBrandContext>>> fetchBrands(@ModelAttribute BrandFilter filter, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(brandFetchService.fetchBrands(filter, pageable)));
    }

    @PostMapping("/brand/external/{siteId}/mapping")
    public ResponseEntity<ApiResponse<List<BrandMappingInfo>>> convertExternalBrandToInternalBrand(@PathVariable("siteId") long siteId, @RequestBody List<BrandMappingInfo> brandMappingInfos) {
        brandMappingManager.convertExternalBrandToInternalBrand(siteId, brandMappingInfos);
        return ResponseEntity.ok(ApiResponse.success(brandMappingInfos));
    }

}
