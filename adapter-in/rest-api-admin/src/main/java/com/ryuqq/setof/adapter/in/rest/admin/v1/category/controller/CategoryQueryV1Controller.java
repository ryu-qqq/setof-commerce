package com.ryuqq.setof.adapter.in.rest.admin.v1.category.controller;

import com.ryuqq.setof.adapter.in.rest.admin.v1.category.CategoryAdminV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.query.CategorySearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.ProductCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.mapper.CategoryAdminV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.category.dto.query.CategorySearchParams;
import com.ryuqq.setof.application.category.dto.response.CategoryPageResult;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.application.category.port.in.query.GetAllCategoriesAsTreeUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetChildCategoriesUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetParentCategoriesUseCase;
import com.ryuqq.setof.application.category.port.in.query.SearchCategoryByOffsetUseCase;
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
 * CategoryQueryV1Controller - 카테고리 조회 V1 API.
 *
 * <p>레거시 호환을 위한 V1 카테고리 조회 엔드포인트를 제공합니다.
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
@Tag(name = "카테고리 조회 V1", description = "카테고리 조회 V1 API (레거시 호환)")
@RestController
public class CategoryQueryV1Controller {

    private final GetAllCategoriesAsTreeUseCase getAllCategoriesAsTreeUseCase;
    private final GetChildCategoriesUseCase getChildCategoriesUseCase;
    private final GetParentCategoriesUseCase getParentCategoriesUseCase;
    private final SearchCategoryByOffsetUseCase searchCategoryByOffsetUseCase;
    private final CategoryAdminV1ApiMapper mapper;

    public CategoryQueryV1Controller(
            GetAllCategoriesAsTreeUseCase getAllCategoriesAsTreeUseCase,
            GetChildCategoriesUseCase getChildCategoriesUseCase,
            GetParentCategoriesUseCase getParentCategoriesUseCase,
            SearchCategoryByOffsetUseCase searchCategoryByOffsetUseCase,
            CategoryAdminV1ApiMapper mapper) {
        this.getAllCategoriesAsTreeUseCase = getAllCategoriesAsTreeUseCase;
        this.getChildCategoriesUseCase = getChildCategoriesUseCase;
        this.getParentCategoriesUseCase = getParentCategoriesUseCase;
        this.searchCategoryByOffsetUseCase = searchCategoryByOffsetUseCase;
        this.mapper = mapper;
    }

    /**
     * 전체 카테고리 트리 조회 API.
     *
     * @return 전체 카테고리 트리
     */
    @Operation(summary = "전체 카테고리 트리 조회", description = "전체 카테고리를 트리 구조로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping(CategoryAdminV1Endpoints.CATEGORY)
    public ResponseEntity<V1ApiResponse<List<TreeCategoryV1ApiResponse>>> getAllCategoriesAsTree() {
        List<TreeCategoryResult> results = getAllCategoriesAsTreeUseCase.execute();
        List<TreeCategoryV1ApiResponse> response = mapper.toTreeResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 자식 카테고리 조회 API.
     *
     * @param categoryId 카테고리 ID
     * @return 자식 카테고리 목록
     */
    @Operation(summary = "자식 카테고리 조회", description = "특정 카테고리의 자식 카테고리를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "카테고리를 찾을 수 없음")
    })
    @GetMapping(CategoryAdminV1Endpoints.CATEGORY_CHILDREN)
    public ResponseEntity<V1ApiResponse<List<TreeCategoryV1ApiResponse>>> getChildCategories(
            @Parameter(description = "카테고리 ID", required = true)
                    @PathVariable(CategoryAdminV1Endpoints.PATH_CATEGORY_ID)
                    Long categoryId) {
        List<TreeCategoryResult> results = getChildCategoriesUseCase.execute(categoryId);
        List<TreeCategoryV1ApiResponse> response = mapper.toTreeResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 부모 카테고리 조회 API.
     *
     * @param categoryId 카테고리 ID
     * @return 부모 카테고리 목록
     */
    @Operation(summary = "부모 카테고리 조회", description = "특정 카테고리의 부모 카테고리를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "카테고리를 찾을 수 없음")
    })
    @GetMapping(CategoryAdminV1Endpoints.CATEGORY_PARENT)
    public ResponseEntity<V1ApiResponse<List<TreeCategoryV1ApiResponse>>> getParentCategories(
            @Parameter(description = "카테고리 ID", required = true)
                    @PathVariable(CategoryAdminV1Endpoints.PATH_CATEGORY_ID)
                    Long categoryId) {
        List<TreeCategoryResult> results = getParentCategoriesUseCase.execute(categoryId);
        List<TreeCategoryV1ApiResponse> response = mapper.toTreeResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 카테고리 목록 조회 API (Offset 페이징).
     *
     * @param request 필터 조건
     * @return 카테고리 목록 (페이징)
     */
    @Operation(summary = "카테고리 목록 조회", description = "카테고리 목록을 페이지 기반으로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @GetMapping(CategoryAdminV1Endpoints.CATEGORY_PAGE)
    public ResponseEntity<V1ApiResponse<CustomPageableV1ApiResponse<ProductCategoryV1ApiResponse>>>
            searchCategoriesByOffset(@ModelAttribute @Valid CategorySearchV1ApiRequest request) {
        CategorySearchParams params = mapper.toSearchParams(request);
        CategoryPageResult pageResult = searchCategoryByOffsetUseCase.execute(params);
        CustomPageableV1ApiResponse<ProductCategoryV1ApiResponse> pageResponse =
                mapper.toPageResponse(pageResult);
        return ResponseEntity.ok(V1ApiResponse.success(pageResponse));
    }
}
