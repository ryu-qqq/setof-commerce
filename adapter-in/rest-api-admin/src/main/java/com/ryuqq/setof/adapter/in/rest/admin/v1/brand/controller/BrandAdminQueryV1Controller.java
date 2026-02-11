package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.controller;

import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.BrandAdminV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.request.BrandSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.mapper.BrandAdminV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;
import com.ryuqq.setof.application.brand.port.in.query.SearchBrandByOffsetUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BrandAdminQueryV1Controller - 브랜드 조회 Admin V1 API.
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
 * <p>레거시 BrandController.fetchBrands 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "브랜드 조회 Admin V1", description = "브랜드 조회 Admin V1 API")
@RestController
@RequestMapping(BrandAdminV1Endpoints.BRANDS)
public class BrandAdminQueryV1Controller {

    private final SearchBrandByOffsetUseCase searchBrandByOffsetUseCase;
    private final BrandAdminV1ApiMapper mapper;

    public BrandAdminQueryV1Controller(
            SearchBrandByOffsetUseCase searchBrandByOffsetUseCase, BrandAdminV1ApiMapper mapper) {
        this.searchBrandByOffsetUseCase = searchBrandByOffsetUseCase;
        this.mapper = mapper;
    }

    /**
     * 브랜드 목록 검색 API.
     *
     * <p>GET /api/v1/brands - mainDisplayType에 따라 한글/영문 검색, Offset 페이징.
     *
     * @param request 검색 요청 (brandName, mainDisplayType, page, size, sortDirection)
     * @return 브랜드 목록 (페이징)
     */
    @Operation(summary = "브랜드 목록 검색", description = "mainDisplayType에 따라 한글/영문 브랜드명으로 검색합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<V1ApiResponse<CustomPageableV1ApiResponse<BrandV1ApiResponse>>>
            fetchBrands(@ParameterObject @Valid BrandSearchV1ApiRequest request) {
        BrandSearchParams params = mapper.toSearchParams(request);
        BrandPageResult pageResult = searchBrandByOffsetUseCase.execute(params);
        CustomPageableV1ApiResponse<BrandV1ApiResponse> response =
                mapper.toPageResponse(pageResult);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
