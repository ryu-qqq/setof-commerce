package com.ryuqq.setof.adapter.in.rest.admin.v1.category.controller;

import com.ryuqq.setof.adapter.in.rest.admin.v1.category.CategoryAdminV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.request.SearchCategoriesV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.ProductCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.mapper.CategoryAdminV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.category.dto.query.CategorySearchParams;
import com.ryuqq.setof.application.category.dto.response.CategoryPageResult;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.application.category.port.in.query.GetAllCategoriesAsTreeUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetCategoriesByIdsUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetChildCategoriesUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetParentCategoriesUseCase;
import com.ryuqq.setof.application.category.port.in.query.SearchCategoryByOffsetUseCase;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * CategoryAdminQueryV1Controller - 카테고리 조회 Admin V1 API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>레거시 CategoryController 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "카테고리 조회 Admin V1", description = "카테고리 조회 Admin V1 API")
@RestController
@RequestMapping(CategoryAdminV1Endpoints.CATEGORY)
public class CategoryAdminQueryV1Controller {

    private final GetAllCategoriesAsTreeUseCase getAllCategoriesAsTreeUseCase;
    private final GetChildCategoriesUseCase getChildCategoriesUseCase;
    private final GetParentCategoriesUseCase getParentCategoriesUseCase;
    private final GetCategoriesByIdsUseCase getCategoriesByIdsUseCase;
    private final SearchCategoryByOffsetUseCase searchCategoryByOffsetUseCase;
    private final CategoryAdminV1ApiMapper mapper;

    public CategoryAdminQueryV1Controller(
            GetAllCategoriesAsTreeUseCase getAllCategoriesAsTreeUseCase,
            GetChildCategoriesUseCase getChildCategoriesUseCase,
            GetParentCategoriesUseCase getParentCategoriesUseCase,
            GetCategoriesByIdsUseCase getCategoriesByIdsUseCase,
            SearchCategoryByOffsetUseCase searchCategoryByOffsetUseCase,
            CategoryAdminV1ApiMapper mapper) {
        this.getAllCategoriesAsTreeUseCase = getAllCategoriesAsTreeUseCase;
        this.getChildCategoriesUseCase = getChildCategoriesUseCase;
        this.getParentCategoriesUseCase = getParentCategoriesUseCase;
        this.getCategoriesByIdsUseCase = getCategoriesByIdsUseCase;
        this.searchCategoryByOffsetUseCase = searchCategoryByOffsetUseCase;
        this.mapper = mapper;
    }

    /**
     * 전체 카테고리 트리 조회.
     *
     * <p>GET /api/v1/category
     */
    @Operation(summary = "전체 카테고리 트리 조회", description = "전체 카테고리를 트리 구조로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<V1ApiResponse<List<TreeCategoryV1ApiResponse>>>
            fetchAllCategoriesAsTree() {
        List<TreeCategoryResult> results = getAllCategoriesAsTreeUseCase.execute();
        List<TreeCategoryV1ApiResponse> response = mapper.toTreeListResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 하위 카테고리 조회.
     *
     * <p>GET /api/v1/category/{categoryId}
     */
    @Operation(summary = "하위 카테고리 조회", description = "특정 카테고리의 하위 카테고리를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping(CategoryAdminV1Endpoints.CATEGORY_ID)
    public ResponseEntity<V1ApiResponse<List<TreeCategoryV1ApiResponse>>> fetchAllChildCategories(
            @Parameter(description = "카테고리 ID", required = true)
                    @PathVariable(CategoryAdminV1Endpoints.PATH_CATEGORY_ID)
                    Long categoryId) {
        List<TreeCategoryResult> results = getChildCategoriesUseCase.execute(categoryId);
        List<TreeCategoryV1ApiResponse> response =
                results.stream().map(mapper::toTreeLeafResponse).toList();
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 상위 카테고리 조회.
     *
     * <p>GET /api/v1/category/parent/{categoryId}
     */
    @Operation(summary = "상위 카테고리 조회", description = "특정 카테고리의 모든 상위 카테고리를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping(CategoryAdminV1Endpoints.PARENT)
    public ResponseEntity<V1ApiResponse<List<TreeCategoryV1ApiResponse>>> fetchAllParentCategories(
            @Parameter(description = "카테고리 ID", required = true)
                    @PathVariable(CategoryAdminV1Endpoints.PATH_CATEGORY_ID)
                    Long categoryId) {
        List<TreeCategoryResult> results = getParentCategoriesUseCase.execute(categoryId);
        List<TreeCategoryV1ApiResponse> response =
                results.stream().map(mapper::toTreeLeafResponse).toList();
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 다건 카테고리 조회 (ID 목록).
     *
     * <p>GET /api/v1/category/parents?categoryIds=100,200,300
     *
     * <p>주의: API 경로는 'parents'이나 실제 동작은 요청한 카테고리 ID만 조회 (상위 재귀 탐색 없음).
     */
    @Operation(summary = "다건 카테고리 조회", description = "카테고리 ID 목록으로 카테고리를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping(CategoryAdminV1Endpoints.PARENTS)
    public ResponseEntity<V1ApiResponse<List<TreeCategoryV1ApiResponse>>>
            fetchAllParentCategoriesBulk(
                    @Parameter(description = "조회할 카테고리 ID 목록", required = true) @RequestParam
                            List<Long> categoryIds) {
        List<TreeCategoryResult> results = getCategoriesByIdsUseCase.execute(categoryIds);
        List<TreeCategoryV1ApiResponse> response =
                results.stream().map(mapper::toTreeLeafResponse).toList();
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 카테고리 페이징 조회.
     *
     * <p>GET /api/v1/category/page
     */
    @Operation(summary = "카테고리 페이징 조회", description = "조건에 맞는 카테고리를 페이징하여 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping(CategoryAdminV1Endpoints.PAGE)
    public ResponseEntity<V1ApiResponse<CustomPageableV1ApiResponse<ProductCategoryV1ApiResponse>>>
            fetchCategories(@ParameterObject @Valid SearchCategoriesV1ApiRequest request) {
        CategorySearchParams params = mapper.toSearchParams(request);
        CategoryPageResult pageResult = searchCategoryByOffsetUseCase.execute(params);
        CustomPageableV1ApiResponse<ProductCategoryV1ApiResponse> response =
                mapper.toPageResponse(pageResult);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
