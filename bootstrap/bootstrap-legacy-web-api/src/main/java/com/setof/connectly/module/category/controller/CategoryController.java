package com.setof.connectly.module.category.controller;

import com.setof.connectly.module.category.dto.CategoryDisplayDto;
import com.setof.connectly.module.category.service.CategoryFindService;
import com.setof.connectly.module.payload.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional(readOnly = true)
@RequestMapping("/api/v1/category")
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryFindService categoryFindService;

    @Transactional(readOnly = true)
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<CategoryDisplayDto>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoryFindService.getAllCategoriesAsTree()));
    }
}
