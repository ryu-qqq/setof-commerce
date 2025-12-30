package com.connectly.partnerAdmin.module.external.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductInfoDto;
import com.connectly.partnerAdmin.module.external.dto.product.ExternalProductUpdateDto;
import com.connectly.partnerAdmin.module.external.filter.ExternalProductFilter;
import com.connectly.partnerAdmin.module.external.service.product.ExternalProductFetchService;
import com.connectly.partnerAdmin.module.external.service.product.ExternalProductQueryService;
import com.connectly.partnerAdmin.module.payload.ApiResponse;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;

import lombok.RequiredArgsConstructor;

//@PreAuthorize(HAS_AUTHORITY_MASTER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class ExternalMallProductController {

    private final ExternalProductFetchService externalProductFetchService;
    private final ExternalProductQueryService externalProductQueryService;

    @GetMapping("/interlocking/products")
    public ResponseEntity<ApiResponse<CustomPageable<ExternalProductInfoDto>>> fetchExternalProducts(@ModelAttribute ExternalProductFilter filter, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(externalProductFetchService.fetchExternalProducts(filter, pageable)));
    }


    @PatchMapping("/interlocking/products/{siteId}")
    public ResponseEntity<ApiResponse<Long>> updateExternalProducts(@RequestBody List<ExternalProductUpdateDto> externalProductUpdates, @PathVariable("siteId") long siteId){
        return ResponseEntity.ok(ApiResponse.success(externalProductQueryService.updateMappingStatus(externalProductUpdates, siteId)));
    }


}

