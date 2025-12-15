package com.ryuqq.setof.adapter.in.rest.v2.product.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.product.dto.query.ProductSearchV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.product.dto.response.FullProductV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.product.dto.response.ProductGroupSummaryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.product.dto.response.ProductGroupV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.product.mapper.ProductV2ApiMapper;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.port.in.query.GetFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Product V2 Controller
 *
 * <p>상품 정보 조회 API 엔드포인트 (조회 전용)
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Product", description = "상품 정보 조회 API")
@RestController
@RequestMapping(ApiV2Paths.Products.BASE)
public class ProductV2Controller {

    private final GetProductGroupUseCase getProductGroupUseCase;
    private final GetProductGroupsUseCase getProductGroupsUseCase;
    private final GetFullProductUseCase getFullProductUseCase;
    private final ProductV2ApiMapper mapper;

    public ProductV2Controller(
            GetProductGroupUseCase getProductGroupUseCase,
            GetProductGroupsUseCase getProductGroupsUseCase,
            GetFullProductUseCase getFullProductUseCase,
            ProductV2ApiMapper mapper) {
        this.getProductGroupUseCase = getProductGroupUseCase;
        this.getProductGroupsUseCase = getProductGroupsUseCase;
        this.getFullProductUseCase = getFullProductUseCase;
        this.mapper = mapper;
    }

    /**
     * 상품 목록 조회
     *
     * <p>검색 조건과 페이징을 지원하는 상품 목록을 조회합니다.
     *
     * @param request 검색 조건
     * @return 페이징된 상품 목록
     */
    @Operation(summary = "상품 목록 조회", description = "검색 조건으로 상품을 검색하고 페이징된 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<PageApiResponse<ProductGroupSummaryV2ApiResponse>>>
            getProducts(@Valid @ModelAttribute ProductSearchV2ApiRequest request) {

        ProductGroupSearchQuery query = mapper.toSearchQuery(request);

        List<ProductGroupSummaryResponse> content = getProductGroupsUseCase.execute(query);
        long totalElements = getProductGroupsUseCase.count(query);

        int totalPages = (int) Math.ceil((double) totalElements / query.size());
        boolean first = query.page() == 0;
        boolean last = query.page() >= totalPages - 1 || totalPages == 0;

        PageResponse<ProductGroupSummaryResponse> pageResponse =
                new PageResponse<>(
                        content,
                        query.page(),
                        query.size(),
                        totalElements,
                        totalPages,
                        first,
                        last);

        PageApiResponse<ProductGroupSummaryV2ApiResponse> apiResponse =
                PageApiResponse.from(pageResponse, ProductGroupSummaryV2ApiResponse::from);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 상품 단건 조회
     *
     * <p>상품그룹 ID로 상세 정보를 조회합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품그룹 상세 정보
     */
    @Operation(summary = "상품 단건 조회", description = "상품그룹 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "상품을 찾을 수 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Products.ID_PATH)
    public ResponseEntity<ApiResponse<ProductGroupV2ApiResponse>> getProduct(
            @Parameter(description = "상품그룹 ID", example = "1") @PathVariable Long productGroupId) {

        ProductGroupResponse response = getProductGroupUseCase.execute(productGroupId);

        ProductGroupV2ApiResponse apiResponse = ProductGroupV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 전체 상품 조회
     *
     * <p>상품그룹 ID로 모든 관련 Aggregate (이미지, 설명, 고시, 재고)를 포함한 전체 정보를 조회합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 전체 상품 정보
     */
    @Operation(
            summary = "전체 상품 조회",
            description = "상품그룹 ID로 모든 관련 정보 (이미지, 설명, 고시, 재고)를 포함한 전체 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "상품을 찾을 수 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Products.FULL_PATH)
    public ResponseEntity<ApiResponse<FullProductV2ApiResponse>> getFullProduct(
            @Parameter(description = "상품그룹 ID", example = "1") @PathVariable Long productGroupId) {

        FullProductResponse response = getFullProductUseCase.getFullProduct(productGroupId);

        FullProductV2ApiResponse apiResponse = FullProductV2ApiResponse.from(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
