package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.controller;

import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.BrandAdminV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.query.BrandSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.ExtendedBrandV1ApiResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    private final SearchBrandByOffsetUseCase searchBrandByOffsetUseCase;
    private final BrandAdminV1ApiMapper mapper;

    public BrandQueryV1Controller(
            SearchBrandByOffsetUseCase searchBrandByOffsetUseCase, BrandAdminV1ApiMapper mapper) {
        this.searchBrandByOffsetUseCase = searchBrandByOffsetUseCase;
        this.mapper = mapper;
    }

    /**
     * 브랜드 목록 조회 API (Offset 페이징).
     *
     * <p>필터 조건과 페이징으로 브랜드 목록을 조회합니다.
     *
     * @param request 필터 조건
     * @return 브랜드 목록 (페이징)
     */
    @Operation(summary = "브랜드 목록 조회", description = "브랜드 목록을 페이지 기반으로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @GetMapping(BrandAdminV1Endpoints.BRANDS)
    public ResponseEntity<V1ApiResponse<CustomPageableV1ApiResponse<ExtendedBrandV1ApiResponse>>>
            searchBrandsByOffset(@ModelAttribute @Valid BrandSearchV1ApiRequest request) {
        BrandSearchParams params = mapper.toSearchParams(request);
        BrandPageResult pageResult = searchBrandByOffsetUseCase.execute(params);
        CustomPageableV1ApiResponse<ExtendedBrandV1ApiResponse> pageResponse =
                mapper.toPageResponse(pageResult);
        return ResponseEntity.ok(V1ApiResponse.success(pageResponse));
    }
}
