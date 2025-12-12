package com.ryuqq.setof.adapter.in.rest.admin.v1.category.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.TreeCategoryContextV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.mapper.CategoryAdminV1ApiMapper;
import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryTreeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Category Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Category 엔드포인트. 내부적으로 V2 UseCase를 사용합니다.
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Category (Legacy V1)", description = "레거시 Category API - V2로 마이그레이션 권장")
@RestController
@RequestMapping(ApiPaths.Category.BASE)
@Validated
@Deprecated
public class CategoryV1Controller {

    private final GetCategoryTreeUseCase getCategoryTreeUseCase;
    private final CategoryAdminV1ApiMapper categoryAdminV1ApiMapper;

    public CategoryV1Controller(
            GetCategoryTreeUseCase getCategoryTreeUseCase,
            CategoryAdminV1ApiMapper categoryAdminV1ApiMapper) {
        this.getCategoryTreeUseCase = getCategoryTreeUseCase;
        this.categoryAdminV1ApiMapper = categoryAdminV1ApiMapper;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 하위 카테고리 목록 조회", description = "특정 카테고리의 모든 하위 카테고리를 조회합니다.")
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<List<TreeCategoryContextV1ApiResponse>>>
            fetchAllChildCategories(@PathVariable("categoryId") long categoryId) {

        List<CategoryTreeResponse> treeResponses = getCategoryTreeUseCase.getCategoryTree();
        List<TreeCategoryContextV1ApiResponse> v1Responses =
                findChildrenById(treeResponses, categoryId);

        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Responses));
    }

    @Deprecated
    @Operation(summary = "[Legacy] 전체 카테고리 트리 조회", description = "전체 카테고리를 트리 구조로 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TreeCategoryContextV1ApiResponse>>>
            fetchAllCategoriesAsTree() {

        List<CategoryTreeResponse> treeResponses = getCategoryTreeUseCase.getCategoryTree();
        List<TreeCategoryContextV1ApiResponse> v1Responses =
                categoryAdminV1ApiMapper.toV1ResponseList(treeResponses);

        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Responses));
    }

    private List<TreeCategoryContextV1ApiResponse> findChildrenById(
            List<CategoryTreeResponse> responses, long categoryId) {
        for (CategoryTreeResponse response : responses) {
            if (response.id().equals(categoryId)) {
                return categoryAdminV1ApiMapper.toV1ResponseList(response.children());
            }
            List<TreeCategoryContextV1ApiResponse> found =
                    findChildrenById(response.children(), categoryId);
            if (!found.isEmpty()) {
                return found;
            }
        }
        return List.of();
    }
}
