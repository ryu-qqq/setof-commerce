package com.ryuqq.setof.adapter.in.rest.v1.brand.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.query.BrandV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Brand (Legacy V1)", description = "레거시 Brand API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class BrandV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] Brand 조회", description = "Brand을 조회합니다.")
    @GetMapping(ApiPaths.Brand.DETAIL)
    public ResponseEntity<ApiResponse<PageApiResponse<BrandV1ApiResponse>>> getBrand(
            @PathVariable("brandId") long brandId) {
        throw new UnsupportedOperationException("Brand 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] Brand 목록 조회", description = "Brand 목록을 조회합니다.")
    @GetMapping(ApiPaths.Brand.LIST)
    public ResponseEntity<ApiResponse<PageApiResponse<List<BrandV1ApiResponse>>>> getBrands(
            @ModelAttribute BrandV1SearchApiRequest request) {
        throw new UnsupportedOperationException("Brand 목록 조회 기능은 아직 지원되지 않습니다.");
    }

}
