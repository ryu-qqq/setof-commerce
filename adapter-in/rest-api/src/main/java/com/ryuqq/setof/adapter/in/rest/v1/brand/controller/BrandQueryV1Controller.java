package com.ryuqq.setof.adapter.in.rest.v1.brand.controller;

import com.ryuqq.setof.adapter.in.rest.v1.brand.BrandV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.query.BrandSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandDisplayV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.brand.mapper.BrandV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.brand.dto.query.BrandDisplaySearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandByIdUseCase;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandsForDisplayUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * BrandQueryV1Controller - 브랜드 조회 V1 API.
 *
 * <p>레거시 호환을 위한 V1 브랜드 조회 엔드포인트를 제공합니다.
 *
 * <p>API-CTR-001: @RestController 어노테이션 필수.
 *
 * <p>API-CTR-004: ResponseEntity&lt;V1ApiResponse&lt;T&gt;&gt; 래핑 필수.
 *
 * <p>API-CTR-005: Controller에서 @Transactional 금지.
 *
 * <p>API-CTR-007: Controller에 비즈니스 로직 포함 금지.
 *
 * <p>API-CTR-010: CQRS Controller 분리.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "브랜드 조회 V1", description = "브랜드 조회 V1 API (레거시 호환)")
@RestController
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
     * @param request 필터 조건
     * @return 브랜드 목록
     */
    @Operation(summary = "브랜드 목록 조회", description = "브랜드 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping(BrandV1Endpoints.BRAND)
    public ResponseEntity<V1ApiResponse<List<BrandDisplayV1ApiResponse>>> getBrands(
            @ModelAttribute @Valid BrandSearchV1ApiRequest request) {
        BrandDisplaySearchParams params = mapper.toSearchParams(request);
        List<BrandDisplayResult> results = getBrandsForDisplayUseCase.execute(params);
        List<BrandDisplayV1ApiResponse> response = mapper.toListResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 브랜드 단건 조회 API.
     *
     * @param brandId 브랜드 ID
     * @return 브랜드 정보
     */
    @Operation(summary = "브랜드 단건 조회", description = "특정 브랜드 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "브랜드를 찾을 수 없음")
    })
    @GetMapping(BrandV1Endpoints.BRAND_BY_ID)
    public ResponseEntity<V1ApiResponse<BrandDisplayV1ApiResponse>> getBrand(
            @Parameter(description = "브랜드 ID", required = true)
                    @PathVariable(BrandV1Endpoints.PATH_BRAND_ID)
                    Long brandId) {
        BrandResult result = getBrandByIdUseCase.execute(brandId);
        BrandDisplayV1ApiResponse response = mapper.toResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
