package com.ryuqq.setof.adapter.in.rest.v1.brand.controller;

import com.ryuqq.setof.adapter.in.rest.v1.brand.BrandV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.request.SearchBrandsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.brand.mapper.BrandV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.brand.dto.query.BrandDisplaySearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import com.ryuqq.setof.application.brand.port.in.GetBrandByIdUseCase;
import com.ryuqq.setof.application.brand.port.in.GetBrandsForDisplayUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BrandQueryV1Controller - 브랜드 조회 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * <p>API-CTR-007: Controller 비즈니스 로직 금지.
 *
 * <p>레거시 BrandController.fetchBrand / fetchBrands 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "브랜드 조회 V1", description = "브랜드 조회 V1 Public API")
@RestController
@RequestMapping(BrandV1Endpoints.BRANDS)
public class BrandQueryV1Controller {

    private final GetBrandsForDisplayUseCase getBrandsForDisplayUseCase;
    private final GetBrandByIdUseCase getBrandByIdUseCase;
    private final BrandV1ApiMapper mapper;

    public BrandQueryV1Controller(
            GetBrandsForDisplayUseCase getBrandsForDisplayUseCase,
            GetBrandByIdUseCase getBrandByIdUseCase,
            BrandV1ApiMapper mapper) {
        this.getBrandsForDisplayUseCase = getBrandsForDisplayUseCase;
        this.getBrandByIdUseCase = getBrandByIdUseCase;
        this.mapper = mapper;
    }

    /**
     * 브랜드 목록 조회 API.
     *
     * <p>GET /api/v1/brand - searchWord로 필터링 또는 전체 목록 조회.
     *
     * @param request 검색 요청 (searchWord 옵션)
     * @return 브랜드 목록
     */
    @Operation(summary = "브랜드 목록 조회", description = "브랜드명 검색어로 필터링하거나 전체 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<V1ApiResponse<List<BrandV1ApiResponse>>> getBrands(
            @ParameterObject @Valid SearchBrandsV1ApiRequest request) {
        BrandDisplaySearchParams params = mapper.toSearchParams(request);
        List<BrandDisplayResult> results = getBrandsForDisplayUseCase.execute(params);
        List<BrandV1ApiResponse> response = mapper.toListResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 브랜드 단건 조회 API.
     *
     * <p>GET /api/v1/brand/{brandId}
     *
     * @param brandId 브랜드 ID
     * @return 브랜드 상세
     */
    @Operation(summary = "브랜드 단건 조회", description = "브랜드 ID로 단건 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "브랜드를 찾을 수 없음 (레거시 호환: HTTP 200, body status 404)")
    })
    @GetMapping(BrandV1Endpoints.BRAND_ID)
    public ResponseEntity<V1ApiResponse<BrandV1ApiResponse>> getBrand(
            @Parameter(description = "브랜드 ID", required = true)
                    @PathVariable(BrandV1Endpoints.PATH_BRAND_ID)
                    Long brandId) {
        BrandResult result = getBrandByIdUseCase.execute(brandId);
        BrandV1ApiResponse response = mapper.toResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
