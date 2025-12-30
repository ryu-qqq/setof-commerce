package com.connectly.partnerAdmin.module.category.controller;

import com.connectly.partnerAdmin.module.category.core.CategoryMappingInfo;
import com.connectly.partnerAdmin.module.category.core.ProductCategoryContext;
import com.connectly.partnerAdmin.module.category.core.TreeCategoryContext;
import com.connectly.partnerAdmin.module.category.filter.CategoryFilter;
import com.connectly.partnerAdmin.module.category.service.CategoryFetchService;
import com.connectly.partnerAdmin.module.category.service.CategoryMappingManager;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_ANY_AUTHORITY_MASTER_SELLER;

@PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)
@RequestMapping(BASE_END_POINT_V1)
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryFetchService categoryFetchService;
    private final CategoryMappingManager categoryMappingManager;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<TreeCategoryContext>>> fetchAllChildCategories(@PathVariable("categoryId") long categoryId){
        return ResponseEntity.ok(ApiResponse.success(categoryFetchService.fetchAllChildCategories(categoryId)));
    }

    @GetMapping("/category/parent/{categoryId}")
    public ResponseEntity<ApiResponse<List<TreeCategoryContext>>> fetchAllParentCategories(@PathVariable("categoryId") long categoryId){
        return ResponseEntity.ok(ApiResponse.success(categoryFetchService.fetchAllParentCategories(categoryId)));
    }

    @GetMapping("/category/parents")
    public ResponseEntity<ApiResponse<List<TreeCategoryContext>>> fetchAllParentCategories(
            @RequestParam List<Long> categoryIds) {
        return ResponseEntity.ok(ApiResponse.success(categoryFetchService.fetchAllCategories(new HashSet<>(categoryIds))));
    }

    @GetMapping("/category")
    public ResponseEntity<ApiResponse<List<TreeCategoryContext>>> fetchAllCategoriesAsTree(){
        return ResponseEntity.ok(ApiResponse.success(categoryFetchService.fetchAllCategories()));
    }


    @GetMapping("/category/page")
    public ResponseEntity<ApiResponse<CustomPageable<ProductCategoryContext>>> fetchCategories(@ModelAttribute CategoryFilter filter, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(categoryFetchService.fetchCategories(filter, pageable)));
    }


    @PostMapping("/category/external/{siteId}/mapping")
    public ResponseEntity<ApiResponse<List<CategoryMappingInfo>>> convertExternalCategoryToInternalCategory(@PathVariable("siteId") long siteId, @RequestBody List<CategoryMappingInfo> categoryMappingInfos) {
        categoryMappingManager.convertExternalCategoryToInternalCategory(siteId, categoryMappingInfos);
        return ResponseEntity.ok(ApiResponse.success(categoryMappingInfos));
    }

}
