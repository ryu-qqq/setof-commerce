package com.ryuqq.setof.adapter.in.rest.v2.brand.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.brand.dto.query.BrandV2SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.brand.dto.response.BrandSummaryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.brand.dto.response.BrandV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.brand.mapper.BrandV2ApiMapper;
import com.ryuqq.setof.application.brand.dto.response.BrandResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandUseCase;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandsUseCase;
import com.ryuqq.setof.application.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Brand V2 Controller
 *
 * <p>브랜드 정보 조회 API 엔드포인트
 *
 * <p>브랜드 정보는 MarketPlace에서 배치로 동기화되므로 조회만 지원합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Brand", description = "브랜드 정보 조회 API")
@RestController
@RequestMapping(ApiV2Paths.Brands.BASE)
public class BrandV2Controller {

    private final GetBrandUseCase getBrandUseCase;
    private final GetBrandsUseCase getBrandsUseCase;
    private final BrandV2ApiMapper brandV2ApiMapper;

    public BrandV2Controller(
            GetBrandUseCase getBrandUseCase,
            GetBrandsUseCase getBrandsUseCase,
            BrandV2ApiMapper brandV2ApiMapper) {
        this.getBrandUseCase = getBrandUseCase;
        this.getBrandsUseCase = getBrandsUseCase;
        this.brandV2ApiMapper = brandV2ApiMapper;
    }

    /**
     * 브랜드 목록 조회
     *
     * <p>키워드 검색과 페이징을 지원하는 브랜드 목록을 조회합니다.
     *
     * @param request 검색 조건 (키워드, 페이지, 사이즈)
     * @return 페이징된 브랜드 목록
     */
    @Operation(summary = "브랜드 목록 조회", description = "키워드로 브랜드를 검색하고 페이징된 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "서버 오류",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<PageApiResponse<BrandSummaryV2ApiResponse>>> getBrands(
            @Valid @ModelAttribute BrandV2SearchApiRequest request) {

        PageResponse<BrandSummaryResponse> pageResponse =
                getBrandsUseCase.execute(brandV2ApiMapper.toSearchQuery(request));

        PageApiResponse<BrandSummaryV2ApiResponse> response =
                PageApiResponse.from(pageResponse, BrandSummaryV2ApiResponse::from);

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }

    /**
     * 브랜드 단건 조회
     *
     * <p>브랜드 ID로 상세 정보를 조회합니다.
     *
     * @param brandId 브랜드 ID
     * @return 브랜드 상세 정보
     */
    @Operation(summary = "브랜드 단건 조회", description = "브랜드 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "브랜드를 찾을 수 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "서버 오류",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Brands.ID_PATH)
    public ResponseEntity<ApiResponse<BrandV2ApiResponse>> getBrand(
            @Parameter(description = "브랜드 ID", example = "1") @PathVariable Long brandId) {

        BrandResponse brandResponse = getBrandUseCase.execute(brandId);

        BrandV2ApiResponse response = BrandV2ApiResponse.from(brandResponse);

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }
}
