package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.query.BrandFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.ExtendedBrandContextV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.mapper.BrandAdminV1ApiMapper;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchQuery;
import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandsUseCase;
import com.ryuqq.setof.application.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Brand Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Brand 엔드포인트. 내부적으로 V2 UseCase를 사용합니다.
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Brand (Legacy V1)", description = "레거시 Brand API - V2로 마이그레이션 권장")
@RestController
@RequestMapping(ApiPaths.Brand.BASE)
@Validated
@Deprecated
public class BrandV1Controller {

    private final GetBrandsUseCase getBrandsUseCase;
    private final BrandAdminV1ApiMapper brandAdminV1ApiMapper;

    public BrandV1Controller(
            GetBrandsUseCase getBrandsUseCase, BrandAdminV1ApiMapper brandAdminV1ApiMapper) {
        this.getBrandsUseCase = getBrandsUseCase;
        this.brandAdminV1ApiMapper = brandAdminV1ApiMapper;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 브랜드 목록 조회", description = "브랜드 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<PageApiResponse<ExtendedBrandContextV1ApiResponse>>>
            fetchBrands(
                    @ModelAttribute BrandFilterV1ApiRequest filter,
                    @RequestParam(value = "page", defaultValue = "0") int page,
                    @RequestParam(value = "size", defaultValue = "20") int size) {

        BrandSearchQuery query = BrandSearchQuery.ofKeyword(filter.brandName(), page, size);

        PageResponse<BrandSummaryResponse> pageResponse = getBrandsUseCase.execute(query);

        PageApiResponse<ExtendedBrandContextV1ApiResponse> apiResponse =
                PageApiResponse.from(pageResponse, brandAdminV1ApiMapper::toV1Response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
