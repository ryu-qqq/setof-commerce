package com.ryuqq.setof.adapter.in.rest.v1.category.controller;

import com.ryuqq.setof.adapter.in.rest.v1.category.CategoryV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.category.mapper.CategoryV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryDisplayResult;
import com.ryuqq.setof.application.category.port.in.query.GetCategoriesForDisplayUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CategoryQueryV1Controller - 카테고리 조회 V1 Public API.
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
 * <p>레거시 CategoryController.getCategories 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "카테고리 조회 V1", description = "카테고리 조회 V1 Public API")
@RestController
@RequestMapping(CategoryV1Endpoints.CATEGORY)
public class CategoryQueryV1Controller {

    private final GetCategoriesForDisplayUseCase getCategoriesForDisplayUseCase;
    private final CategoryV1ApiMapper mapper;

    public CategoryQueryV1Controller(
            GetCategoriesForDisplayUseCase getCategoriesForDisplayUseCase,
            CategoryV1ApiMapper mapper) {
        this.getCategoriesForDisplayUseCase = getCategoriesForDisplayUseCase;
        this.mapper = mapper;
    }

    /**
     * 카테고리 트리 조회 API.
     *
     * <p>GET /api/v1/category - 전체 카테고리를 트리 구조로 조회.
     *
     * @return 카테고리 트리 목록
     */
    @Operation(summary = "카테고리 트리 조회", description = "전체 카테고리를 트리 구조로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<V1ApiResponse<List<TreeCategoryV1ApiResponse>>> getCategories() {
        List<CategoryDisplayResult> results = getCategoriesForDisplayUseCase.execute();
        List<TreeCategoryV1ApiResponse> response = mapper.toListResponse(results);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
