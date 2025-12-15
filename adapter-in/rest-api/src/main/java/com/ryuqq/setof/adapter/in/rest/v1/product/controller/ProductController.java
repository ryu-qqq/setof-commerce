package com.ryuqq.setof.adapter.in.rest.v1.product.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.SliceApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.query.ProductGroupPageV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.query.ProductGroupV1KeywordSearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.query.ProductGroupV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupThumbnailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.mapper.ProductV1ApiMapper;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.port.in.query.GetFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Product V1 Controller
 *
 * <p>레거시 Product API - V2로 마이그레이션 권장
 *
 * @author development-team
 * @since 1.0.0
 */
@Tag(name = "Product (Legacy V1)", description = "레거시 Product API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class ProductController {

    private final GetFullProductUseCase getFullProductUseCase;
    private final GetProductGroupsUseCase getProductGroupsUseCase;
    private final ProductV1ApiMapper mapper;

    public ProductController(
            GetFullProductUseCase getFullProductUseCase,
            GetProductGroupsUseCase getProductGroupsUseCase,
            ProductV1ApiMapper mapper) {
        this.getFullProductUseCase = getFullProductUseCase;
        this.getProductGroupsUseCase = getProductGroupsUseCase;
        this.mapper = mapper;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 상세 조회", description = "상품 상세를 조회합니다.")
    @GetMapping(ApiPaths.Product.GROUP_DETAIL)
    public ResponseEntity<ApiResponse<ProductGroupDetailV1ApiResponse>>
            getProductGroupDetailByProductGroupId(
                    @PathVariable("productGroupId") long productGroupId) {
        FullProductResponse response = getFullProductUseCase.getFullProduct(productGroupId);
        ProductGroupDetailV1ApiResponse v1Response = mapper.toDetailResponse(response);
        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Response));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 목록 조회", description = "최근 조회한 상품 목록을 조회합니다.")
    @GetMapping(ApiPaths.Product.GROUP_RECENT)
    public ResponseEntity<ApiResponse<List<ProductGroupThumbnailV1ApiResponse>>>
            getProductGroupsDesc(@RequestParam List<Long> productGroupIds) {
        throw new UnsupportedOperationException("최근 상품 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 목록 조회", description = "상품 목록을 조회합니다.")
    @GetMapping(ApiPaths.Product.GROUP_LIST)
    public ResponseEntity<ApiResponse<SliceApiResponse<ProductGroupThumbnailV1ApiResponse>>>
            getProductGroups(@ModelAttribute ProductGroupV1SearchApiRequest request) {
        ProductGroupSearchQuery query = mapper.toQuery(request, 0, 20);
        List<ProductGroupSummaryResponse> responses = getProductGroupsUseCase.execute(query);
        List<ProductGroupThumbnailV1ApiResponse> v1Responses =
                mapper.toThumbnailResponses(responses);
        boolean hasNext = v1Responses.size() >= 20;
        SliceApiResponse<ProductGroupThumbnailV1ApiResponse> sliceResponse =
                new SliceApiResponse<>(v1Responses, v1Responses.size(), hasNext, null);
        return ResponseEntity.ok(ApiResponse.ofSuccess(sliceResponse));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 목록 조회", description = "브랜드별 상품 목록을 조회합니다.")
    @GetMapping(ApiPaths.Product.GROUP_BY_BRAND)
    public ResponseEntity<ApiResponse<List<ProductGroupThumbnailV1ApiResponse>>>
            getProductGroupByBrandId(
                    @PathVariable("brandId") long brandId,
                    @ModelAttribute ProductGroupPageV1SearchApiRequest request) {
        ProductGroupSearchQuery query =
                new ProductGroupSearchQuery(null, null, brandId, null, null, 0, 20);
        List<ProductGroupSummaryResponse> responses = getProductGroupsUseCase.execute(query);
        List<ProductGroupThumbnailV1ApiResponse> v1Responses =
                mapper.toThumbnailResponses(responses);
        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Responses));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 목록 조회", description = "셀러별 상품 목록을 조회합니다.")
    @GetMapping(ApiPaths.Product.GROUP_BY_SELLER)
    public ResponseEntity<ApiResponse<List<ProductGroupThumbnailV1ApiResponse>>>
            getProductGroupBySellerId(
                    @PathVariable("sellerId") long sellerId,
                    @ModelAttribute ProductGroupPageV1SearchApiRequest request) {
        ProductGroupSearchQuery query =
                new ProductGroupSearchQuery(sellerId, null, null, null, null, 0, 20);
        List<ProductGroupSummaryResponse> responses = getProductGroupsUseCase.execute(query);
        List<ProductGroupThumbnailV1ApiResponse> v1Responses =
                mapper.toThumbnailResponses(responses);
        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Responses));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 검색 목록 조회", description = "상품을 검색하여 목록을 조회합니다.")
    @GetMapping(ApiPaths.Search.SEARCH)
    public ResponseEntity<ApiResponse<SliceApiResponse<ProductGroupThumbnailV1ApiResponse>>>
            getProductGroupBySearchKeyword(
                    @ModelAttribute ProductGroupV1KeywordSearchApiRequest request) {
        throw new UnsupportedOperationException("상품 검색 목록 조회 기능은 아직 지원되지 않습니다.");
    }
}
