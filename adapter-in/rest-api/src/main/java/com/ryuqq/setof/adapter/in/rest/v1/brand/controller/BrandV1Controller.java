package com.ryuqq.setof.adapter.in.rest.v1.brand.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.query.BrandV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.brand.mapper.BrandV1ApiMapper;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchQuery;
import com.ryuqq.setof.application.brand.dto.response.BrandResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandUseCase;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandsUseCase;
import com.ryuqq.setof.application.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Legacy Brand V1 Controller
 *
 * <p>V2 API로 마이그레이션을 권장합니다. 내부적으로 V2 UseCase를 사용합니다.
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API 사용을 권장합니다 ({@link
 *     com.ryuqq.setof.adapter.in.rest.v2.brand.controller.BrandV2Controller})
 */
@Tag(name = "Brand (Legacy V1)", description = "레거시 Brand API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class BrandV1Controller {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;

    private final GetBrandUseCase getBrandUseCase;
    private final GetBrandsUseCase getBrandsUseCase;
    private final BrandV1ApiMapper brandV1ApiMapper;

    public BrandV1Controller(
            GetBrandUseCase getBrandUseCase,
            GetBrandsUseCase getBrandsUseCase,
            BrandV1ApiMapper brandV1ApiMapper) {
        this.getBrandUseCase = getBrandUseCase;
        this.getBrandsUseCase = getBrandsUseCase;
        this.brandV1ApiMapper = brandV1ApiMapper;
    }

    @Deprecated
    @Operation(summary = "[Legacy] Brand 조회", description = "Brand을 조회합니다.")
    @GetMapping(ApiPaths.Brand.DETAIL)
    public ResponseEntity<ApiResponse<BrandV1ApiResponse>> getBrand(
            @PathVariable("brandId") long brandId) {
        BrandResponse response = getBrandUseCase.execute(brandId);
        BrandV1ApiResponse v1Response = brandV1ApiMapper.toV1Response(response);
        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Response));
    }

    @Deprecated
    @Operation(summary = "[Legacy] Brand 목록 조회", description = "Brand 목록을 조회합니다.")
    @GetMapping(ApiPaths.Brand.LIST)
    public ResponseEntity<ApiResponse<PageApiResponse<BrandV1ApiResponse>>> getBrands(
            @ModelAttribute BrandV1SearchApiRequest request,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        BrandSearchQuery query = BrandSearchQuery.ofKeyword(request.searchWord(), page, size);

        PageResponse<BrandSummaryResponse> pageResponse = getBrandsUseCase.execute(query);

        PageApiResponse<BrandV1ApiResponse> apiResponse =
                PageApiResponse.from(pageResponse, brandV1ApiMapper::toV1Response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
