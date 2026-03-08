package com.ryuqq.setof.adapter.in.rest.v1.productgroup.controller;

import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.productgroup.ProductGroupV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.request.SearchProductGroupsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.response.ProductGroupDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.response.ProductGroupSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.response.ProductGroupThumbnailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.productgroup.mapper.ProductGroupV1ApiMapper;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupDetailResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.port.in.query.FetchProductGroupDetailUseCase;
import com.ryuqq.setof.application.productgroup.port.in.query.FetchProductGroupsByBrandUseCase;
import com.ryuqq.setof.application.productgroup.port.in.query.FetchProductGroupsByIdsUseCase;
import com.ryuqq.setof.application.productgroup.port.in.query.FetchProductGroupsBySellerUseCase;
import com.ryuqq.setof.application.productgroup.port.in.query.FetchProductGroupsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductGroupQueryV1Controller - 상품그룹 조회 V1 Public API.
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
 * <p>레거시 ProductController 5개 엔드포인트 변환. URL 단수/복수 혼재 패턴을 정확히 유지합니다:
 *
 * <ul>
 *   <li>GET /api/v1/product/group/{productGroupId} (단수)
 *   <li>GET /api/v1/products/group (복수)
 *   <li>GET /api/v1/products/group/recent (복수)
 *   <li>GET /api/v1/product/group/brand/{brandId} (단수)
 *   <li>GET /api/v1/product/group/seller/{sellerId} (단수)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "상품그룹 조회 V1", description = "상품그룹 조회 V1 Public API")
@RestController
@RequestMapping(ProductGroupV1Endpoints.BASE_V1)
public class ProductGroupQueryV1Controller {

    private final FetchProductGroupDetailUseCase fetchDetailUseCase;
    private final FetchProductGroupsUseCase fetchProductGroupsUseCase;
    private final FetchProductGroupsByIdsUseCase fetchByIdsUseCase;
    private final FetchProductGroupsByBrandUseCase fetchByBrandUseCase;
    private final FetchProductGroupsBySellerUseCase fetchBySellerUseCase;
    private final ProductGroupV1ApiMapper mapper;

    public ProductGroupQueryV1Controller(
            FetchProductGroupDetailUseCase fetchDetailUseCase,
            FetchProductGroupsUseCase fetchProductGroupsUseCase,
            FetchProductGroupsByIdsUseCase fetchByIdsUseCase,
            FetchProductGroupsByBrandUseCase fetchByBrandUseCase,
            FetchProductGroupsBySellerUseCase fetchBySellerUseCase,
            ProductGroupV1ApiMapper mapper) {
        this.fetchDetailUseCase = fetchDetailUseCase;
        this.fetchProductGroupsUseCase = fetchProductGroupsUseCase;
        this.fetchByIdsUseCase = fetchByIdsUseCase;
        this.fetchByBrandUseCase = fetchByBrandUseCase;
        this.fetchBySellerUseCase = fetchBySellerUseCase;
        this.mapper = mapper;
    }

    /**
     * 상품그룹 단건 상세 조회 API.
     *
     * <p>GET /api/v1/product/group/{productGroupId}
     *
     * <p>레거시 ProductController.fetchProductGroup 변환. 데이터가 없으면 HTTP 200 + body status 404 반환 (레거시
     * 호환).
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품그룹 상세
     */
    @Operation(
            summary = "상품그룹 단건 상세 조회",
            description = "상품그룹 ID로 단건 상세 조회합니다. 레거시 /api/v1/product/group/{productGroupId} 호환.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "데이터 없음 (레거시 호환: HTTP 200, body status 404)")
    })
    @GetMapping("/product/group/{productGroupId}")
    public ResponseEntity<V1ApiResponse<ProductGroupDetailV1ApiResponse>> fetchProductGroup(
            @Parameter(description = "상품그룹 ID", required = true)
                    @PathVariable(ProductGroupV1Endpoints.PATH_PRODUCT_GROUP_ID)
                    Long productGroupId) {

        ProductGroupDetailResult result = fetchDetailUseCase.execute(productGroupId);
        ProductGroupDetailV1ApiResponse response = mapper.toDetailResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 상품그룹 목록 조회 API (커서 페이징).
     *
     * <p>GET /api/v1/products/group
     *
     * <p>레거시 ProductController.fetchProductGroups 변환. 다중 필터 + 커서 기반 슬라이스 페이징을 지원합니다.
     *
     * @param request 검색 필터 요청 DTO
     * @return 상품그룹 슬라이스 응답
     */
    @Operation(
            summary = "상품그룹 목록 조회 (커서 페이징)",
            description =
                    "다중 필터와 커서 기반 슬라이스 페이징으로 상품그룹 목록을 조회합니다." + " 레거시 /api/v1/products/group 호환.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping("/products/group")
    public ResponseEntity<V1ApiResponse<ProductGroupSliceV1ApiResponse>> fetchProductGroups(
            @ParameterObject @ModelAttribute SearchProductGroupsCursorV1ApiRequest request) {

        ProductGroupSearchParams params = mapper.toSearchParams(request);
        ProductGroupSliceResult result = fetchProductGroupsUseCase.execute(params);
        ProductGroupSliceV1ApiResponse response = mapper.toSliceResponse(result);

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 찜 목록 기반 상품그룹 조회 API.
     *
     * <p>GET /api/v1/products/group/recent
     *
     * <p>레거시 ProductController.fetchProductGroupLikes 변환. 요청 ID 순서로 재정렬하여 반환합니다 (UseCase 내부에서 처리).
     *
     * @param productGroupIds 조회할 상품그룹 ID 목록
     * @return 상품그룹 썸네일 목록 (요청 ID 순서 보장)
     */
    @Operation(
            summary = "찜 목록 기반 상품그룹 조회",
            description =
                    "상품그룹 ID 목록으로 썸네일 목록을 조회합니다. 요청 ID 순서로 재정렬됩니다."
                            + " 레거시 /api/v1/products/group/recent 호환.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping("/products/group/recent")
    public ResponseEntity<V1ApiResponse<List<ProductGroupThumbnailV1ApiResponse>>>
            fetchProductGroupLikes(
                    @Parameter(description = "조회할 상품그룹 ID 목록", required = true) @RequestParam
                            List<Long> productGroupIds) {

        ProductGroupSliceResult result = fetchByIdsUseCase.execute(productGroupIds);
        List<ProductGroupThumbnailV1ApiResponse> response =
                mapper.toThumbnailListResponse(result.content());

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 브랜드별 상품그룹 조회 API.
     *
     * <p>GET /api/v1/product/group/brand/{brandId}
     *
     * <p>레거시 ProductController.fetchProductGroupWithBrand 변환. score DESC 정렬. size 미지정 시 20 기본 적용.
     *
     * @param brandId 브랜드 ID
     * @param size 조회 크기 (기본값 20)
     * @return 상품그룹 썸네일 목록
     */
    @Operation(
            summary = "브랜드별 상품그룹 조회",
            description =
                    "브랜드 ID로 상품그룹 목록을 조회합니다. score DESC 정렬."
                            + " 레거시 /api/v1/product/group/brand/{brandId} 호환.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping("/product/group/brand/{brandId}")
    public ResponseEntity<V1ApiResponse<List<ProductGroupThumbnailV1ApiResponse>>>
            fetchProductGroupWithBrand(
                    @Parameter(description = "브랜드 ID", required = true)
                            @PathVariable(ProductGroupV1Endpoints.PATH_BRAND_ID)
                            Long brandId,
                    @Parameter(description = "조회 크기 (기본값 20)", example = "20")
                            @RequestParam(required = false)
                            Integer size) {

        int pageSize = size != null ? size : 20;
        ProductGroupSliceResult result = fetchByBrandUseCase.execute(brandId, pageSize);
        List<ProductGroupThumbnailV1ApiResponse> response =
                mapper.toThumbnailListResponse(result.content());

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 셀러별 상품그룹 조회 API.
     *
     * <p>GET /api/v1/product/group/seller/{sellerId}
     *
     * <p>레거시 ProductController.fetchProductGroupWithSeller 변환. score DESC 정렬. size 미지정 시 20 기본 적용.
     *
     * @param sellerId 셀러 ID
     * @param size 조회 크기 (기본값 20)
     * @return 상품그룹 썸네일 목록
     */
    @Operation(
            summary = "셀러별 상품그룹 조회",
            description =
                    "셀러 ID로 상품그룹 목록을 조회합니다. score DESC 정렬."
                            + " 레거시 /api/v1/product/group/seller/{sellerId} 호환.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping("/product/group/seller/{sellerId}")
    public ResponseEntity<V1ApiResponse<List<ProductGroupThumbnailV1ApiResponse>>>
            fetchProductGroupWithSeller(
                    @Parameter(description = "셀러 ID", required = true)
                            @PathVariable(ProductGroupV1Endpoints.PATH_SELLER_ID)
                            Long sellerId,
                    @Parameter(description = "조회 크기 (기본값 20)", example = "20")
                            @RequestParam(required = false)
                            Integer size) {

        int pageSize = size != null ? size : 20;
        ProductGroupSliceResult result = fetchBySellerUseCase.execute(sellerId, pageSize);
        List<ProductGroupThumbnailV1ApiResponse> response =
                mapper.toThumbnailListResponse(result.content());

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
