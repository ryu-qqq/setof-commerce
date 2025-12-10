package com.ryuqq.setof.adapter.in.rest.v1.product.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.SliceApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.query.ProductGroupPageV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.query.ProductGroupV1KeywordSearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.query.ProductGroupV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupThumbnailV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product (Legacy V1)", description = "레거시 Product API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class ProductController {

    @Deprecated
    @Operation(summary = "[Legacy] 상품 상세 조회", description = "상품 상세를 조회합니다.")
    @GetMapping(ApiPaths.Product.GROUP_DETAIL)
    public ResponseEntity<ApiResponse<ProductGroupDetailV1ApiResponse>> getProductGroupDetailByProductGroupId(
            @PathVariable("productGroupId") long productGroupId) {
        throw new UnsupportedOperationException("상품 상제 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 목록 조회", description = "최근 조회한 상품 목록을 조회합니다.")
    @GetMapping(ApiPaths.Product.GROUP_RECENT)
    public ResponseEntity<ApiResponse<List<ProductGroupThumbnailV1ApiResponse>>> getProductGroupsDesc(
            @RequestParam List<Long> productGroupIds) {
        throw new UnsupportedOperationException("최근 상품 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 목록 조회", description = "상품 목록을 조회합니다.")
    @GetMapping(ApiPaths.Product.GROUP_LIST)
    public ResponseEntity<ApiResponse<SliceApiResponse<ProductGroupThumbnailV1ApiResponse>>> getProductGroups(
            @ModelAttribute ProductGroupV1SearchApiRequest request) {
        throw new UnsupportedOperationException("상품 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 목록 조회", description = "브랜드별 상품 목록을 조회합니다.")
    @GetMapping(ApiPaths.Product.GROUP_BY_BRAND)
    public ResponseEntity<ApiResponse<List<ProductGroupThumbnailV1ApiResponse>>> getProductGroupByBrandId(
            @PathVariable("brandId") long brandId,
            @ModelAttribute ProductGroupPageV1SearchApiRequest request) {
        throw new UnsupportedOperationException("브랜드 상품 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 목록 조회", description = "셀러별 상품 목록을 조회합니다.")
    @GetMapping(ApiPaths.Product.GROUP_BY_SELLER)
    public ResponseEntity<ApiResponse<List<ProductGroupThumbnailV1ApiResponse>>> getProductGroupBySellerId(
            @PathVariable("sellerId") long sellerId,
            @ModelAttribute ProductGroupPageV1SearchApiRequest request) {
        throw new UnsupportedOperationException("셀러 상품 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 검색 목록 조회", description = "상품을 검색하여 목록을 조회합니다.")
    @GetMapping(ApiPaths.Search.SEARCH)
    public ResponseEntity<ApiResponse<SliceApiResponse<ProductGroupThumbnailV1ApiResponse>>> getProductGroupBySearchKeyword(
            @ModelAttribute ProductGroupV1KeywordSearchApiRequest request) {
        throw new UnsupportedOperationException("상품 검색 목록 조회 기능은 아직 지원되지 않습니다.");
    }


}
