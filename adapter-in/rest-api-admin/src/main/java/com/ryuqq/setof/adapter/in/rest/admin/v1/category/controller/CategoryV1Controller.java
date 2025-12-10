package com.ryuqq.setof.adapter.in.rest.admin.v1.category.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.command.CategoryMappingInfoV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.query.CategoryFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.CategoryMappingInfoV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.ProductCategoryContextV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.TreeCategoryContextV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Category Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Category 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Category (Legacy V1)", description = "레거시 Category API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
public class CategoryV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] 하위 카테고리 목록 조회", description = "특정 카테고리의 모든 하위 카테고리를 조회합니다.")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<TreeCategoryContextV1ApiResponse>>>
            fetchAllChildCategories(@PathVariable("categoryId") long categoryId) {

        throw new UnsupportedOperationException("하위 카테고리 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상위 카테고리 목록 조회", description = "특정 카테고리의 모든 상위 카테고리를 조회합니다.")
    @GetMapping("/category/parent/{categoryId}")
    public ResponseEntity<ApiResponse<List<TreeCategoryContextV1ApiResponse>>>
            fetchAllParentCategories(@PathVariable("categoryId") long categoryId) {

        throw new UnsupportedOperationException("상위 카테고리 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 부모 카테고리 목록 조회", description = "여러 카테고리 ID의 부모 카테고리 목록을 조회합니다.")
    @GetMapping("/category/parents")
    public ResponseEntity<ApiResponse<List<TreeCategoryContextV1ApiResponse>>>
            fetchAllParentCategories(@RequestParam List<Long> categoryIds) {

        throw new UnsupportedOperationException("부모 카테고리 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 전체 카테고리 트리 조회", description = "전체 카테고리를 트리 구조로 조회합니다.")
    @GetMapping("/category")
    public ResponseEntity<ApiResponse<List<TreeCategoryContextV1ApiResponse>>>
            fetchAllCategoriesAsTree() {

        throw new UnsupportedOperationException("전체 카테고리 트리 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 카테고리 목록 조회", description = "카테고리 목록을 페이징하여 조회합니다.")
    @GetMapping("/category/page")
    public ResponseEntity<ApiResponse<PageApiResponse<ProductCategoryContextV1ApiResponse>>>
            fetchCategories(@ModelAttribute CategoryFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("카테고리 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 외부 카테고리를 내부 카테고리로 매핑", description = "외부 카테고리를 내부 카테고리로 변환합니다.")
    @PostMapping("/category/external/{siteId}/mapping")
    public ResponseEntity<ApiResponse<List<CategoryMappingInfoV1ApiResponse>>>
            convertExternalCategoryToInternalCategory(
                    @PathVariable("siteId") long siteId,
                    @Valid @RequestBody
                            List<CategoryMappingInfoV1ApiRequest> categoryMappingInfos) {

        throw new UnsupportedOperationException("외부 카테고리 매핑 기능은 아직 지원되지 않습니다.");
    }
}
