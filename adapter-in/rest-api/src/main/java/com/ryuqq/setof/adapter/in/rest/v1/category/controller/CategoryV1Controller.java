package com.ryuqq.setof.adapter.in.rest.v1.category.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.CategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.category.mapper.CategoryV1ApiMapper;
import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryTreeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Legacy Category V1 Controller
 *
 * <p>V2 API로 마이그레이션을 권장합니다. 내부적으로 V2 UseCase를 사용합니다.
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API 사용을 권장합니다 ({@link
 *     com.ryuqq.setof.adapter.in.rest.v2.category.controller.CategoryV2Controller})
 */
@Tag(name = "Category (Legacy V1)", description = "레거시 Category API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class CategoryV1Controller {

    private final GetCategoryTreeUseCase getCategoryTreeUseCase;
    private final CategoryV1ApiMapper categoryV1ApiMapper;

    public CategoryV1Controller(
            GetCategoryTreeUseCase getCategoryTreeUseCase,
            CategoryV1ApiMapper categoryV1ApiMapper) {
        this.getCategoryTreeUseCase = getCategoryTreeUseCase;
        this.categoryV1ApiMapper = categoryV1ApiMapper;
    }

    @Deprecated
    @Operation(summary = "[Legacy] Category 트리 조회", description = "전체 카테고리 트리를 조회합니다.")
    @GetMapping(ApiPaths.Category.LIST)
    public ResponseEntity<ApiResponse<List<CategoryV1ApiResponse>>> getCategories() {
        List<CategoryTreeResponse> treeResponses = getCategoryTreeUseCase.getCategoryTree();
        List<CategoryV1ApiResponse> v1Responses =
                categoryV1ApiMapper.toV1ResponseList(treeResponses);
        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Responses));
    }
}
